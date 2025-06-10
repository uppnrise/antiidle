package com.upp.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration manager for AntiIdle application.
 * Handles loading and saving of application settings.
 */
public class ConfigurationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);
    private static final String CONFIG_FILE_NAME = "antiidle-config.yml";
    private static final String DEFAULT_CONFIG_RESOURCE = "/default-config.yml";
    
    private final ObjectMapper objectMapper;
    private final Path configPath;
    private AntiIdleConfig config;
    
    public ConfigurationManager() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.configPath = getConfigFilePath();
        this.config = loadConfiguration();
    }
    
    /**
     * Gets the configuration file path in the user's home directory.
     */
    private Path getConfigFilePath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, ".antiidle", CONFIG_FILE_NAME);
    }
    
    /**
     * Loads configuration from file or creates default configuration.
     */
    private AntiIdleConfig loadConfiguration() {
        try {
            if (Files.exists(configPath)) {
                LOGGER.info("Loading configuration from: {}", configPath);
                return objectMapper.readValue(configPath.toFile(), AntiIdleConfig.class);
            } else {
                LOGGER.info("Configuration file not found, creating default configuration");
                return createDefaultConfiguration();
            }
        } catch (IOException e) {
            LOGGER.error("Error loading configuration, using defaults", e);
            return createDefaultConfiguration();
        }
    }
    
    /**
     * Creates and saves default configuration.
     */
    private AntiIdleConfig createDefaultConfiguration() {
        AntiIdleConfig defaultConfig = new AntiIdleConfig();
        saveConfiguration(defaultConfig);
        return defaultConfig;
    }
    
    /**
     * Saves configuration to file.
     * 
     * @param config the configuration to save
     */
    public void saveConfiguration(AntiIdleConfig config) {
        try {
            // Create directory if it doesn't exist
            Path parentDir = configPath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            
            objectMapper.writeValue(configPath.toFile(), config);
            this.config = config;
            LOGGER.info("Configuration saved to: {}", configPath);
        } catch (IOException e) {
            LOGGER.error("Error saving configuration", e);
        }
    }
    
    /**
     * Gets current configuration.
     * 
     * @return a copy of the current configuration
     */
    public AntiIdleConfig getConfig() {
        // Return a defensive copy to prevent external modification
        try {
            String json = objectMapper.writeValueAsString(config);
            return objectMapper.readValue(json, AntiIdleConfig.class);
        } catch (IOException e) {
            LOGGER.warn("Failed to create defensive copy, returning original", e);
            return config;
        }
    }
    
    /**
     * Updates configuration with new values.
     * 
     * @param newConfig the new configuration to apply
     */
    public void updateConfig(AntiIdleConfig newConfig) {
        // Create a defensive copy before storing
        try {
            String json = objectMapper.writeValueAsString(newConfig);
            this.config = objectMapper.readValue(json, AntiIdleConfig.class);
            saveConfiguration(this.config);
        } catch (IOException e) {
            LOGGER.error("Failed to update configuration", e);
            throw new RuntimeException("Configuration update failed", e);
        }
    }
    
    /**
     * Reloads configuration from file.
     */
    public void reloadConfiguration() {
        this.config = loadConfiguration();
    }
    
    /**
     * Configuration data class.
     */
    public static class AntiIdleConfig {
        @JsonProperty("activity")
        private ActivitySettings activity = new ActivitySettings();
        
        @JsonProperty("gui")
        private GuiSettings gui = new GuiSettings();
        
        @JsonProperty("system")
        private SystemSettings system = new SystemSettings();
        
        @JsonProperty("logging")
        private LoggingSettings logging = new LoggingSettings();
        
        // Getters and setters
        public ActivitySettings getActivity() {
            return activity;
        }
        
        public void setActivity(ActivitySettings activity) {
            this.activity = activity;
        }
        
        public GuiSettings getGui() {
            return gui;
        }
        
        public void setGui(GuiSettings gui) {
            this.gui = gui;
        }
        
        public SystemSettings getSystem() {
            return system;
        }
        
        public void setSystem(SystemSettings system) {
            this.system = system;
        }
        
        public LoggingSettings getLogging() {
            return logging;
        }
        
        public void setLogging(LoggingSettings logging) {
            this.logging = logging;
        }
    }
    
    /**
     * Activity simulation settings.
     */
    public static class ActivitySettings {
        @JsonProperty("interval_seconds")
        private int intervalSeconds = 30;
        
        @JsonProperty("mouse_movement_enabled")
        private boolean mouseMovementEnabled = true;
        
        @JsonProperty("mouse_movement_distance")
        private int mouseMovementDistance = 1;
        
        @JsonProperty("keyboard_simulation_enabled")
        private boolean keyboardSimulationEnabled = true;
        
        @JsonProperty("simulation_key")
        private String simulationKey = "SHIFT";
        
        @JsonProperty("key_press_duration_ms")
        private int keyPressDurationMs = 100;
        
        // Getters and setters
        public int getIntervalSeconds() {
            return intervalSeconds;
        }
        
        public void setIntervalSeconds(int intervalSeconds) {
            this.intervalSeconds = intervalSeconds;
        }
        
        public boolean isMouseMovementEnabled() {
            return mouseMovementEnabled;
        }
        
        public void setMouseMovementEnabled(boolean mouseMovementEnabled) {
            this.mouseMovementEnabled = mouseMovementEnabled;
        }
        
        public int getMouseMovementDistance() {
            return mouseMovementDistance;
        }
        
        public void setMouseMovementDistance(int mouseMovementDistance) {
            this.mouseMovementDistance = mouseMovementDistance;
        }
        
        public boolean isKeyboardSimulationEnabled() {
            return keyboardSimulationEnabled;
        }
        
        public void setKeyboardSimulationEnabled(boolean keyboardSimulationEnabled) {
            this.keyboardSimulationEnabled = keyboardSimulationEnabled;
        }
        
        public String getSimulationKey() {
            return simulationKey;
        }
        
        public void setSimulationKey(String simulationKey) {
            this.simulationKey = simulationKey;
        }
        
        public int getKeyPressDurationMs() {
            return keyPressDurationMs;
        }
        
        public void setKeyPressDurationMs(int keyPressDurationMs) {
            this.keyPressDurationMs = keyPressDurationMs;
        }
    }
    
    /**
     * GUI settings.
     */
    public static class GuiSettings {
        @JsonProperty("start_minimized")
        private boolean startMinimized = false;
        
        @JsonProperty("minimize_to_tray")
        private boolean minimizeToTray = true;
        
        @JsonProperty("show_notifications")
        private boolean showNotifications = true;
        
        @JsonProperty("dark_mode")
        private boolean darkMode = false;
        
        @JsonProperty("window_position_x")
        private int windowPositionX = -1;
        
        @JsonProperty("window_position_y")
        private int windowPositionY = -1;
        
        // Getters and setters
        public boolean isStartMinimized() {
            return startMinimized;
        }
        
        public void setStartMinimized(boolean startMinimized) {
            this.startMinimized = startMinimized;
        }
        
        public boolean isMinimizeToTray() {
            return minimizeToTray;
        }
        
        public void setMinimizeToTray(boolean minimizeToTray) {
            this.minimizeToTray = minimizeToTray;
        }
        
        public boolean isShowNotifications() {
            return showNotifications;
        }
        
        public void setShowNotifications(boolean showNotifications) {
            this.showNotifications = showNotifications;
        }
        
        public boolean isDarkMode() {
            return darkMode;
        }
        
        public void setDarkMode(boolean darkMode) {
            this.darkMode = darkMode;
        }
        
        public int getWindowPositionX() {
            return windowPositionX;
        }
        
        public void setWindowPositionX(int windowPositionX) {
            this.windowPositionX = windowPositionX;
        }
        
        public int getWindowPositionY() {
            return windowPositionY;
        }
        
        public void setWindowPositionY(int windowPositionY) {
            this.windowPositionY = windowPositionY;
        }
    }
    
    /**
     * System settings.
     */
    public static class SystemSettings {
        @JsonProperty("auto_start")
        private boolean autoStart = false;
        
        @JsonProperty("start_on_boot")
        private boolean startOnBoot = false;
        
        @JsonProperty("check_for_updates")
        private boolean checkForUpdates = true;
        
        // Getters and setters
        public boolean isAutoStart() {
            return autoStart;
        }
        
        public void setAutoStart(boolean autoStart) {
            this.autoStart = autoStart;
        }
        
        public boolean isStartOnBoot() {
            return startOnBoot;
        }
        
        public void setStartOnBoot(boolean startOnBoot) {
            this.startOnBoot = startOnBoot;
        }
        
        public boolean isCheckForUpdates() {
            return checkForUpdates;
        }
        
        public void setCheckForUpdates(boolean checkForUpdates) {
            this.checkForUpdates = checkForUpdates;
        }
    }
    
    /**
     * Logging settings.
     */
    public static class LoggingSettings {
        @JsonProperty("log_level")
        private String logLevel = "INFO";
        
        @JsonProperty("log_to_file")
        private boolean logToFile = true;
        
        @JsonProperty("max_log_file_size_mb")
        private int maxLogFileSizeMb = 10;
        
        @JsonProperty("keep_log_files")
        private int keepLogFiles = 5;
        
        // Getters and setters
        public String getLogLevel() {
            return logLevel;
        }
        
        public void setLogLevel(String logLevel) {
            this.logLevel = logLevel;
        }
        
        public boolean isLogToFile() {
            return logToFile;
        }
        
        public void setLogToFile(boolean logToFile) {
            this.logToFile = logToFile;
        }
        
        public int getMaxLogFileSizeMb() {
            return maxLogFileSizeMb;
        }
        
        public void setMaxLogFileSizeMb(int maxLogFileSizeMb) {
            this.maxLogFileSizeMb = maxLogFileSizeMb;
        }
        
        public int getKeepLogFiles() {
            return keepLogFiles;
        }
        
        public void setKeepLogFiles(int keepLogFiles) {
            this.keepLogFiles = keepLogFiles;
        }
    }
}
