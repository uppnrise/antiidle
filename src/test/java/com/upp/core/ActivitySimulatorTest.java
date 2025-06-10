package com.upp.core;

import com.upp.config.ConfigurationManager;
import com.upp.exception.AntiIdleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import java.awt.GraphicsEnvironment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ActivitySimulator.
 */
class ActivitySimulatorTest {

    private ConfigurationManager configManager;
    private ActivitySimulator activitySimulator;

    @BeforeEach
    void setUp() throws Exception {
        // Create configuration manager with test settings
        configManager = mock(ConfigurationManager.class);
        ConfigurationManager.AntiIdleConfig config = new ConfigurationManager.AntiIdleConfig();
        config.getActivity().setIntervalSeconds(1); // Short interval for testing
        when(configManager.getConfig()).thenReturn(config);
    }

    @Test
    void testSimulatorInitialization() {
        // Test that simulator can be created without Robot in headless mode
        assertDoesNotThrow(() -> {
            try {
                activitySimulator = new ActivitySimulator(configManager);
                // If we get here without exception, Robot was created successfully
                assertNotNull(activitySimulator);
            } catch (AntiIdleException.RobotInitializationException e) {
                // Expected in headless mode or environments without display
                assertTrue(e.getMessage().contains("Robot"));
            }
        });
    }

    @Test
    void testStartStopSimulation() throws Exception {
        // Skip test in headless environment
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), 
            "Skipping Robot tests in headless environment");
            
        try {
            activitySimulator = new ActivitySimulator(configManager);
            
            // Test starting simulation
            assertFalse(activitySimulator.isRunning());
            activitySimulator.startSimulation();
            assertTrue(activitySimulator.isRunning());
            
            // Test stopping simulation
            activitySimulator.stopSimulation();
            
            // Wait a moment for the thread to stop
            Thread.sleep(100);
            assertFalse(activitySimulator.isRunning());
        } catch (AntiIdleException.RobotInitializationException e) {
            // Skip test if Robot cannot be initialized
            Assumptions.assumeTrue(false, "Robot initialization failed: " + e.getMessage());
        }
    }

    @Test
    void testMultipleStartCalls() throws Exception {
        // Skip test in headless environment
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), 
            "Skipping Robot tests in headless environment");
            
        try {
            activitySimulator = new ActivitySimulator(configManager);
            
            // Multiple start calls should not cause issues
            activitySimulator.startSimulation();
            assertTrue(activitySimulator.isRunning());
            
            activitySimulator.startSimulation(); // Second call should be ignored
            assertTrue(activitySimulator.isRunning());
            
            activitySimulator.stopSimulation();
        } catch (AntiIdleException.RobotInitializationException e) {
            // Skip test if Robot cannot be initialized
            Assumptions.assumeTrue(false, "Robot initialization failed: " + e.getMessage());
        }
    }

    @Test
    void testStatistics() throws Exception {
        // Skip test in headless environment
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), 
            "Skipping Robot tests in headless environment");
            
        try {
            activitySimulator = new ActivitySimulator(configManager);
            
            ActivitySimulator.ActivityStats stats = activitySimulator.getStats();
            assertNotNull(stats);
            assertFalse(stats.isRunning());
            
            activitySimulator.startSimulation();
            stats = activitySimulator.getStats();
            assertTrue(stats.isRunning());
            
            activitySimulator.stopSimulation();
        } catch (AntiIdleException.RobotInitializationException e) {
            // Skip test if Robot cannot be initialized
            Assumptions.assumeTrue(false, "Robot initialization failed: " + e.getMessage());
        }
    }

    @Test
    void testConfigurationChanges() throws Exception {
        // Skip test in headless environment
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), 
            "Skipping Robot tests in headless environment");
            
        try {
            activitySimulator = new ActivitySimulator(configManager);
            
            // Test with mouse movement disabled
            ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
            config.getActivity().setMouseMovementEnabled(false);
            config.getActivity().setKeyboardSimulationEnabled(false);
            
            activitySimulator.startSimulation();
            assertTrue(activitySimulator.isRunning());
            
            // Brief wait then stop
            Thread.sleep(100);
            activitySimulator.stopSimulation();
        } catch (AntiIdleException.RobotInitializationException e) {
            // Skip test if Robot cannot be initialized
            Assumptions.assumeTrue(false, "Robot initialization failed: " + e.getMessage());
        }
    }
}
