package com.upp.exception;

/**
 * Custom exception for AntiIdle application errors.
 */
public class AntiIdleException extends Exception {
    
    public AntiIdleException(String message) {
        super(message);
    }
    
    public AntiIdleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Exception thrown when Robot initialization fails.
     */
    public static class RobotInitializationException extends AntiIdleException {
        public RobotInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Exception thrown when configuration operations fail.
     */
    public static class ConfigurationException extends AntiIdleException {
        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Exception thrown when GUI operations fail.
     */
    public static class GuiException extends AntiIdleException {
        public GuiException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Exception thrown when activity simulation fails.
     */
    public static class ActivitySimulationException extends AntiIdleException {
        public ActivitySimulationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
