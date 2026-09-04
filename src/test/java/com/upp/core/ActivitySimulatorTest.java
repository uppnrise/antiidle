package com.upp.core;

import com.upp.config.ConfigurationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ActivitySimulator.
 *
 * <p>These tests use Mockito's {@link MockedConstruction} to intercept {@code new Robot()},
 * so the simulator can be created and exercised deterministically without depending on a
 * real (or non-headless) display environment. Simulation-cycle behavior is verified by
 * invoking the private {@code performActivity()} method synchronously via reflection rather
 * than starting the background thread and polling with a timeout; this keeps the tests fast
 * and immune to thread-scheduling jitter on shared/constrained machines.</p>
 */
class ActivitySimulatorTest {

    private ConfigurationManager configManager;
    private ConfigurationManager.AntiIdleConfig config;
    private MockedConstruction<Robot> mockedRobot;
    private ActivitySimulator activitySimulator;

    @BeforeEach
    void setUp() {
        configManager = mock(ConfigurationManager.class);
        config = new ConfigurationManager.AntiIdleConfig();
        config.getActivity().setIntervalSeconds(1);
        when(configManager.getConfig()).thenReturn(config);

        mockedRobot = mockConstruction(Robot.class);
    }

    @AfterEach
    void tearDown() {
        if (activitySimulator != null && activitySimulator.isRunning()) {
            activitySimulator.stopSimulation();
        }
        if (mockedRobot != null) {
            mockedRobot.close();
        }
    }

    /**
     * Invokes the private {@code performActivity()} method synchronously on the calling
     * thread, unwrapping any exception it throws so callers can assert on it directly.
     */
    private void invokePerformActivity(ActivitySimulator simulator) throws Exception {
        Method method = ActivitySimulator.class.getDeclaredMethod("performActivity");
        method.setAccessible(true);
        try {
            method.invoke(simulator);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            }
            throw e;
        }
    }

    @Test
    void testSimulatorInitializesRobot() throws Exception {
        activitySimulator = new ActivitySimulator(configManager);

        assertNotNull(activitySimulator);
        assertEquals(1, mockedRobot.constructed().size());
        verify(mockedRobot.constructed().get(0)).setAutoWaitForIdle(false);
    }

    @Test
    void testStartStopSimulation() throws Exception {
        activitySimulator = new ActivitySimulator(configManager);

        assertFalse(activitySimulator.isRunning());
        activitySimulator.startSimulation();
        assertTrue(activitySimulator.isRunning());

        activitySimulator.stopSimulation();
        assertFalse(activitySimulator.isRunning());
    }

    @Test
    void testMultipleStartCallsAreIgnored() throws Exception {
        activitySimulator = new ActivitySimulator(configManager);

        activitySimulator.startSimulation();
        assertTrue(activitySimulator.isRunning());

        activitySimulator.startSimulation(); // second call should just log a warning and return
        assertTrue(activitySimulator.isRunning());

        activitySimulator.stopSimulation();
    }

    @Test
    void testStopWithoutStartIsSafe() throws Exception {
        activitySimulator = new ActivitySimulator(configManager);

        assertFalse(activitySimulator.isRunning());
        activitySimulator.stopSimulation(); // should just log a warning and return
        assertFalse(activitySimulator.isRunning());
    }

    @Test
    void testGetStatsReflectsRunningState() throws Exception {
        activitySimulator = new ActivitySimulator(configManager);

        ActivitySimulator.ActivityStats stats = activitySimulator.getStats();
        assertNotNull(stats);
        assertFalse(stats.isRunning());
        assertTrue(stats.getStartTime() > 0);

        activitySimulator.startSimulation();
        stats = activitySimulator.getStats();
        assertTrue(stats.isRunning());

        activitySimulator.stopSimulation();
    }

    @Test
    void testPerformActivityInvokesKeyboardSimulation() throws Exception {
        config.getActivity().setMouseMovementEnabled(false);
        config.getActivity().setKeyboardSimulationEnabled(true);
        config.getActivity().setSimulationKey("SHIFT");
        config.getActivity().setKeyPressDurationMs(10);

        activitySimulator = new ActivitySimulator(configManager);
        invokePerformActivity(activitySimulator);

        Robot mockRobotInstance = mockedRobot.constructed().get(0);
        verify(mockRobotInstance).keyPress(KeyEvent.VK_SHIFT);
        verify(mockRobotInstance).delay(10);
        verify(mockRobotInstance).keyRelease(KeyEvent.VK_SHIFT);
    }

    @Test
    void testPerformActivityFallsBackToShiftForUnknownKeyName() throws Exception {
        config.getActivity().setMouseMovementEnabled(false);
        config.getActivity().setKeyboardSimulationEnabled(true);
        config.getActivity().setSimulationKey("NOT_A_REAL_KEY_NAME");

        activitySimulator = new ActivitySimulator(configManager);
        invokePerformActivity(activitySimulator);

        verify(mockedRobotConstructedInstance()).keyPress(KeyEvent.VK_SHIFT);
    }

    @Test
    void testPerformActivitySkipsDisabledFeatures() throws Exception {
        config.getActivity().setMouseMovementEnabled(false);
        config.getActivity().setKeyboardSimulationEnabled(false);

        activitySimulator = new ActivitySimulator(configManager);
        invokePerformActivity(activitySimulator);

        Robot mockRobotInstance = mockedRobotConstructedInstance();
        verify(mockRobotInstance, never()).keyPress(anyInt());
        verify(mockRobotInstance, never()).mouseMove(anyInt(), anyInt());
    }

    @Test
    void testPerformActivityAttemptsMouseMovementWithoutThrowing() throws Exception {
        config.getActivity().setMouseMovementEnabled(true);
        config.getActivity().setKeyboardSimulationEnabled(false);

        activitySimulator = new ActivitySimulator(configManager);

        // Mouse movement may fail with HeadlessException in a headless test environment,
        // but simulateMouseMovement() must catch it internally so performActivity() never
        // throws either way.
        assertDoesNotThrow(() -> invokePerformActivity(activitySimulator));
    }

    private Robot mockedRobotConstructedInstance() {
        return mockedRobot.constructed().get(0);
    }
}

