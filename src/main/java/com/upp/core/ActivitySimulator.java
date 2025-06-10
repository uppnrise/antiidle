package com.upp.core;

import com.upp.config.ConfigurationManager;
import com.upp.exception.AntiIdleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Core activity simulation engine for AntiIdle.
 * Handles mouse movement and keyboard simulation based on configuration.
 */
public class ActivitySimulator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitySimulator.class);
    
    private final Robot robot;
    private final ConfigurationManager configManager;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread simulationThread;
    
    /**
     * Creates a new ActivitySimulator with the given configuration manager.
     * 
     * @param configManager the configuration manager to use
     * @throws AntiIdleException.RobotInitializationException if Robot initialization fails
     */
    public ActivitySimulator(ConfigurationManager configManager) throws AntiIdleException.RobotInitializationException {
        this.configManager = configManager;
        try {
            this.robot = new Robot();
            robot.setAutoWaitForIdle(false);
            LOGGER.info("Robot initialized successfully");
        } catch (AWTException e) {
            throw new AntiIdleException.RobotInitializationException(
                "Failed to initialize Robot for input simulation", e);
        }
    }
    
    /**
     * Starts the activity simulation.
     */
    public synchronized void startSimulation() throws AntiIdleException.ActivitySimulationException {
        if (running.get()) {
            LOGGER.warn("Activity simulation is already running");
            return;
        }
        
        running.set(true);
        simulationThread = new Thread(this::runSimulation, "ActivitySimulator");
        simulationThread.setDaemon(true);
        simulationThread.start();
        
        LOGGER.info("Activity simulation started");
    }
    
    /**
     * Stops the activity simulation.
     */
    public synchronized void stopSimulation() {
        if (!running.get()) {
            LOGGER.warn("Activity simulation is not running");
            return;
        }
        
        running.set(false);
        
        if (simulationThread != null) {
            simulationThread.interrupt();
            try {
                simulationThread.join(2000); // Wait up to 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warn("Interrupted while waiting for simulation thread to stop");
            }
        }
        
        LOGGER.info("Activity simulation stopped");
    }
    
    /**
     * Checks if simulation is currently running.
     * 
     * @return true if simulation is running, false otherwise
     */
    public boolean isRunning() {
        return running.get();
    }
    
    /**
     * Main simulation loop.
     */
    private void runSimulation() {
        LOGGER.info("Starting activity simulation loop");
        
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            try {
                performActivity();
                waitForNextInterval();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.info("Simulation thread interrupted, stopping");
                break;
            } catch (Exception e) {
                LOGGER.error("Error during activity simulation", e);
                // Continue running despite errors
            }
        }
        
        running.set(false);
        LOGGER.info("Activity simulation loop ended");
    }
    
    /**
     * Performs a single activity cycle.
     */
    private void performActivity() throws AntiIdleException.ActivitySimulationException {
        ConfigurationManager.ActivitySettings settings = configManager.getConfig().getActivity();
        
        try {
            if (settings.isMouseMovementEnabled()) {
                simulateMouseMovement(settings);
            }
            
            if (settings.isKeyboardSimulationEnabled()) {
                simulateKeyPress(settings);
            }
            
            LOGGER.debug("Activity simulation cycle completed");
        } catch (Exception e) {
            throw new AntiIdleException.ActivitySimulationException("Failed to perform activity simulation", e);
        }
    }
    
    /**
     * Simulates mouse movement.
     */
    private void simulateMouseMovement(ConfigurationManager.ActivitySettings settings) {
        try {
            Point currentPos = MouseInfo.getPointerInfo().getLocation();
            int distance = settings.getMouseMovementDistance();
            
            // Move mouse slightly
            robot.mouseMove(currentPos.x + distance, currentPos.y);
            robot.delay(250);
            
            // Move back to original position
            robot.mouseMove(currentPos.x, currentPos.y);
            robot.delay(250);
            
            LOGGER.debug("Mouse movement simulated at position ({}, {})", currentPos.x, currentPos.y);
        } catch (Exception e) {
            LOGGER.warn("Failed to simulate mouse movement", e);
        }
    }
    
    /**
     * Simulates key press.
     */
    private void simulateKeyPress(ConfigurationManager.ActivitySettings settings) {
        try {
            int keyCode = getKeyCode(settings.getSimulationKey());
            int duration = settings.getKeyPressDurationMs();
            
            robot.keyPress(keyCode);
            robot.delay(duration);
            robot.keyRelease(keyCode);
            
            LOGGER.debug("Key press simulated: {} (duration: {}ms)", settings.getSimulationKey(), duration);
        } catch (Exception e) {
            LOGGER.warn("Failed to simulate key press", e);
        }
    }
    
    /**
     * Gets the key code for a key name.
     */
    private int getKeyCode(String keyName) {
        try {
            Field field = KeyEvent.class.getField("VK_" + keyName.toUpperCase());
            return field.getInt(null);
        } catch (Exception e) {
            LOGGER.warn("Unknown key name: {}, using SHIFT", keyName);
            return KeyEvent.VK_SHIFT;
        }
    }
    
    /**
     * Waits for the next simulation interval.
     */
    private void waitForNextInterval() throws InterruptedException {
        int intervalSeconds = configManager.getConfig().getActivity().getIntervalSeconds();
        
        // Sleep in 1-second chunks to allow for interruption
        for (int i = 0; i < intervalSeconds; i++) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            Thread.sleep(1000);
        }
    }
    
    /**
     * Gets current activity statistics.
     * 
     * @return activity statistics containing current state
     */
    public ActivityStats getStats() {
        // This could be expanded to track more detailed statistics
        return new ActivityStats(running.get());
    }
    
    /**
     * Activity statistics data class.
     */
    public static class ActivityStats {
        private final boolean running;
        private final long startTime;
        
        public ActivityStats(boolean running) {
            this.running = running;
            this.startTime = System.currentTimeMillis();
        }
        
        public boolean isRunning() {
            return running;
        }
        
        public long getStartTime() {
            return startTime;
        }
    }
}
