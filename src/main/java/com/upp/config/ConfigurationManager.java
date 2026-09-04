package com.upp.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLMapper;

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
    
    /**
     * Constructs a new ConfigurationManager and loads the configuration.
     */
    public ConfigurationManager() {
        this.objectMapper = new YAMLMapper();
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
        } catch (JacksonException e) {
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
        } catch (IOException | JacksonException e) {
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
        } catch (JacksonException e) {
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
        } catch (JacksonException e) {
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
        
        /**
         * Constructs a new AntiIdleConfig with default settings.
         */
        public AntiIdleConfig() {
            // Default constructor with default field initialization
        }
        
        /**
         * Gets the activity settings.
         * 
         * @return the activity settings
         */
        public ActivitySettings getActivity() {
            return activity;
        }
        
        /**
         * Sets the activity settings.
         * 
         * @param activity the activity settings to set
         */
        public void setActivity(ActivitySettings activity) {
            this.activity = activity;
        }
        
        /**
         * Gets the GUI settings.
         * 
         * @return the GUI settings
         */
        public GuiSettings getGui() {
            return gui;
        }
        
        /**
         * Sets the GUI settings.
         * 
         * @param gui the GUI settings to set
         */
        public void setGui(GuiSettings gui) {
            this.gui = gui;
        }
        
        /**
         * Gets the system settings.
         * 
         * @return the system settings
         */
        public SystemSettings getSystem() {
            return system;
        }
        
        /**
         * Sets the system settings.
         * 
         * @param system the system settings to set
         */
        public void setSystem(SystemSettings system) {
            this.system = system;
        }
        
        /**
         * Gets the logging settings.
         * 
         * @return the logging settings
         */
        public LoggingSettings getLogging() {
            return logging;
        }
        
        /**
         * Sets the logging settings.
         * 
         * @param logging the logging settings to set
         */
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
        
        /**
         * Constructs a new ActivitySettings with default values.
         */
        public ActivitySettings() {
            // Default constructor with default field initialization
        }
        
        /**
         * Gets the interval between simulations in seconds.
         * 
         * @return the interval in seconds
         */
        public int getIntervalSeconds() {
            return intervalSeconds;
        }
        
        /**
         * Sets the interval between simulations in seconds.
         * 
         * @param intervalSeconds the interval in seconds
         */
        public void setIntervalSeconds(int intervalSeconds) {
            this.intervalSeconds = intervalSeconds;
        }
        
        /**
         * Checks if mouse movement is enabled.
         * 
         * @return true if enabled, false otherwise
         */
        public boolean isMouseMovementEnabled() {
            return mouseMovementEnabled;
        }
        
        /**
         * Sets whether mouse movement is enabled.
         * 
         * @param mouseMovementEnabled true to enable, false to disable
         */
        public void setMouseMovementEnabled(boolean mouseMovementEnabled) {
            this.mouseMovementEnabled = mouseMovementEnabled;
        }
        
        /**
         * Gets the mouse movement distance in pixels.
         * 
         * @return the distance in pixels
         */
        public int getMouseMovementDistance() {
            return mouseMovementDistance;
        }
        
        /**
         * Sets the mouse movement distance in pixels.
         * 
         * @param mouseMovementDistance the distance in pixels
         */
        public void setMouseMovementDistance(int mouseMovementDistance) {
            this.mouseMovementDistance = mouseMovementDistance;
        }
        
        /**
         * Checks if keyboard simulation is enabled.
         * 
         * @return true if enabled, false otherwise
         */
        public boolean isKeyboardSimulationEnabled() {
            return keyboardSimulationEnabled;
        }
        
        /**
         * Sets whether keyboard simulation is enabled.
         * 
         * @param keyboardSimulationEnabled true to enable, false to disable
         */
        public void setKeyboardSimulationEnabled(boolean keyboardSimulationEnabled) {
            this.keyboardSimulationEnabled = keyboardSimulationEnabled;
        }
        
        /**
         * Gets the simulation key name.
         * 
         * @return the key name
         */
        public String getSimulationKey() {
            return simulationKey;
        }
        
        /**
         * Sets the simulation key name.
         * 
         * @param simulationKey the key name
         */
        public void setSimulationKey(String simulationKey) {
            this.simulationKey = simulationKey;
        }
        
        /**
         * Gets the key press duration in milliseconds.
         * 
         * @return the duration in milliseconds
         */
        public int getKeyPressDurationMs() {
            return keyPressDurationMs;
        }
        
        /**
         * Sets the key press duration in milliseconds.
         * 
         * @param keyPressDurationMs the duration in milliseconds
         */
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
        
        @JsonProperty("language")
        private String language = "en";
        
        @JsonProperty("theme")
        private String theme = "system";
        
        /**
         * Constructs a new GuiSettings with default values.
         */
        public GuiSettings() {
            // Default constructor with default field initialization
        }
        
        /**
         * Checks if the application should start minimized.
         * 
         * @return true if should start minimized, false otherwise
         */
        public boolean isStartMinimized() {
            return startMinimized;
        }
        
        /**
         * Sets whether the application should start minimized.
         * 
         * @param startMinimized true to start minimized, false otherwise
         */
        public void setStartMinimized(boolean startMinimized) {
            this.startMinimized = startMinimized;
        }
        
        /**
         * Checks if the application should minimize to tray.
         * 
         * @return true if should minimize to tray, false otherwise
         */
        public boolean isMinimizeToTray() {
            return minimizeToTray;
        }
        
        /**
         * Sets whether the application should minimize to tray.
         * 
         * @param minimizeToTray true to minimize to tray, false otherwise
         */
        public void setMinimizeToTray(boolean minimizeToTray) {
            this.minimizeToTray = minimizeToTray;
        }
        
        /**
         * Checks if notifications should be shown.
         * 
         * @return true if notifications should be shown, false otherwise
         */
        public boolean isShowNotifications() {
            return showNotifications;
        }
        
        /**
         * Sets whether notifications should be shown.
         * 
         * @param showNotifications true to show notifications, false otherwise
         */
        public void setShowNotifications(boolean showNotifications) {
            this.showNotifications = showNotifications;
        }
        
        /**
         * Checks if dark mode is enabled.
         * 
         * @return true if dark mode is enabled, false otherwise
         */
        public boolean isDarkMode() {
            return darkMode;
        }
        
        /**
         * Sets whether dark mode is enabled.
         * 
         * @param darkMode true to enable dark mode, false otherwise
         */
        public void setDarkMode(boolean darkMode) {
            this.darkMode = darkMode;
        }
        
        /**
         * Gets the window X position.
         * 
         * @return the X position
         */
        public int getWindowPositionX() {
            return windowPositionX;
        }
        
        /**
         * Sets the window X position.
         * 
         * @param windowPositionX the X position
         */
        public void setWindowPositionX(int windowPositionX) {
            this.windowPositionX = windowPositionX;
        }
        
        /**
         * Gets the window Y position.
         * 
         * @return the Y position
         */
        public int getWindowPositionY() {
            return windowPositionY;
        }
        
        /**
         * Sets the window Y position.
         * 
         * @param windowPositionY the Y position
         */
        public void setWindowPositionY(int windowPositionY) {
            this.windowPositionY = windowPositionY;
        }
        
        /**
         * Gets the application language.
         * 
         * @return the language code
         */
        public String getLanguage() {
            return language;
        }
        
        /**
         * Sets the application language.
         * 
         * @param language the language code
         */
        public void setLanguage(String language) {
            this.language = language;
        }
        
        /**
         * Gets the application theme.
         * 
         * @return the theme name
         */
        public String getTheme() {
            return theme;
        }
        
        /**
         * Sets the application theme.
         * 
         * @param theme the theme name
         */
        public void setTheme(String theme) {
            this.theme = theme;
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
        
        /**
         * Constructs a new SystemSettings with default values.
         */
        public SystemSettings() {
            // Default constructor with default field initialization
        }
        
        /**
         * Checks if auto-start is enabled.
         * 
         * @return true if auto-start is enabled, false otherwise
         */
        public boolean isAutoStart() {
            return autoStart;
        }
        
        /**
         * Sets whether auto-start is enabled.
         * 
         * @param autoStart true to enable auto-start, false otherwise
         */
        public void setAutoStart(boolean autoStart) {
            this.autoStart = autoStart;
        }
        
        /**
         * Checks if start on boot is enabled.
         * 
         * @return true if start on boot is enabled, false otherwise
         */
        public boolean isStartOnBoot() {
            return startOnBoot;
        }
        
        /**
         * Sets whether start on boot is enabled.
         * 
         * @param startOnBoot true to enable start on boot, false otherwise
         */
        public void setStartOnBoot(boolean startOnBoot) {
            this.startOnBoot = startOnBoot;
        }
        
        /**
         * Checks if update checking is enabled.
         * 
         * @return true if update checking is enabled, false otherwise
         */
        public boolean isCheckForUpdates() {
            return checkForUpdates;
        }
        
        /**
         * Sets whether update checking is enabled.
         * 
         * @param checkForUpdates true to enable update checking, false otherwise
         */
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
        
        /**
         * Constructs a new LoggingSettings with default values.
         */
        public LoggingSettings() {
            // Default constructor with default field initialization
        }
        
        /**
         * Gets the log level.
         * 
         * @return the log level
         */
        public String getLogLevel() {
            return logLevel;
        }
        
        /**
         * Sets the log level.
         * 
         * @param logLevel the log level
         */
        public void setLogLevel(String logLevel) {
            this.logLevel = logLevel;
        }
        
        /**
         * Checks if logging to file is enabled.
         * 
         * @return true if logging to file is enabled, false otherwise
         */
        public boolean isLogToFile() {
            return logToFile;
        }
        
        /**
         * Sets whether logging to file is enabled.
         * 
         * @param logToFile true to enable logging to file, false otherwise
         */
        public void setLogToFile(boolean logToFile) {
            this.logToFile = logToFile;
        }
        
        /**
         * Gets the maximum log file size in megabytes.
         * 
         * @return the maximum log file size in megabytes
         */
        public int getMaxLogFileSizeMb() {
            return maxLogFileSizeMb;
        }
        
        /**
         * Sets the maximum log file size in megabytes.
         * 
         * @param maxLogFileSizeMb the maximum log file size in megabytes
         */
        public void setMaxLogFileSizeMb(int maxLogFileSizeMb) {
            this.maxLogFileSizeMb = maxLogFileSizeMb;
        }
        
        /**
         * Gets the number of log files to keep.
         * 
         * @return the number of log files to keep
         */
        public int getKeepLogFiles() {
            return keepLogFiles;
        }
        
        /**
         * Sets the number of log files to keep.
         * 
         * @param keepLogFiles the number of log files to keep
         */
        public void setKeepLogFiles(int keepLogFiles) {
            this.keepLogFiles = keepLogFiles;
        }
    }
}
