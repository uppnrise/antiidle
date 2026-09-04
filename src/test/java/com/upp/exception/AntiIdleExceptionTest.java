package com.upp.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for {@link AntiIdleException} and its nested subclasses.
 */
class AntiIdleExceptionTest {

    @Test
    void testMessageOnlyConstructor() {
        AntiIdleException ex = new AntiIdleException("boom");

        assertEquals("boom", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        Throwable cause = new IllegalStateException("root cause");
        AntiIdleException ex = new AntiIdleException("boom", cause);

        assertEquals("boom", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testRobotInitializationException() {
        Throwable cause = new RuntimeException("awt failure");
        AntiIdleException.RobotInitializationException ex =
            new AntiIdleException.RobotInitializationException("robot failed", cause);

        assertEquals("robot failed", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testConfigurationException() {
        Throwable cause = new RuntimeException("config failure");
        AntiIdleException.ConfigurationException ex =
            new AntiIdleException.ConfigurationException("config failed", cause);

        assertEquals("config failed", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testGuiException() {
        Throwable cause = new RuntimeException("gui failure");
        AntiIdleException.GuiException ex =
            new AntiIdleException.GuiException("gui failed", cause);

        assertEquals("gui failed", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void testActivitySimulationException() {
        Throwable cause = new RuntimeException("simulation failure");
        AntiIdleException.ActivitySimulationException ex =
            new AntiIdleException.ActivitySimulationException("simulation failed", cause);

        assertEquals("simulation failed", ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
