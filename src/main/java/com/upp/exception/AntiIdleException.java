package com.upp.exception;

/**
 * Custom exception for AntiIdle application errors.
 */
public class AntiIdleException extends Exception {
    
    /**
     * Constructs a new AntiIdleException with the specified detail message.
     * 
     * @param message the detail message
     */
    public AntiIdleException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new AntiIdleException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public AntiIdleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Exception thrown when Robot initialization fails.
     */
    public static class RobotInitializationException extends AntiIdleException {
        /**
         * Constructs a new RobotInitializationException.
         * 
         * @param message the detail message
         * @param cause the cause of this exception
         */
        public RobotInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Exception thrown when configuration operations fail.
     */
    public static class ConfigurationException extends AntiIdleException {
        /**
         * Constructs a new ConfigurationException.
         * 
         * @param message the detail message
         * @param cause the cause of this exception
         */
        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Exception thrown when GUI operations fail.
     */
    public static class GuiException extends AntiIdleException {
        /**
         * Constructs a new GuiException.
         * 
         * @param message the detail message
         * @param cause the cause of this exception
         */
        public GuiException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Exception thrown when activity simulation fails.
     */
    public static class ActivitySimulationException extends AntiIdleException {
        /**
         * Constructs a new ActivitySimulationException.
         * 
         * @param message the detail message
         * @param cause the cause of this exception
         */
        public ActivitySimulationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
