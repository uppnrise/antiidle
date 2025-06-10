package com.upp.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for ConfigurationManager.
 */
class ConfigurationManagerTest {

    @TempDir
    private Path tempDir;
    
    private ConfigurationManager configManager;

    @BeforeEach
    void setUp() {
        // Override the config path to use temp directory for testing
        System.setProperty("user.home", tempDir.toString());
        configManager = new ConfigurationManager();
    }

    @Test
    void testDefaultConfiguration() {
        ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
        
        assertNotNull(config);
        assertNotNull(config.getActivity());
        assertNotNull(config.getGui());
        assertNotNull(config.getSystem());
        assertNotNull(config.getLogging());
        
        // Test default values
        assertEquals(30, config.getActivity().getIntervalSeconds());
        assertTrue(config.getActivity().isMouseMovementEnabled());
        assertTrue(config.getActivity().isKeyboardSimulationEnabled());
        assertEquals("SHIFT", config.getActivity().getSimulationKey());
        assertEquals(100, config.getActivity().getKeyPressDurationMs());
    }

    @Test
    void testConfigurationPersistence() throws IOException {
        // Modify configuration
        ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
        config.getActivity().setIntervalSeconds(60);
        config.getActivity().setSimulationKey("CTRL");
        config.getGui().setStartMinimized(true);
        
        // Save configuration
        configManager.updateConfig(config);
        
        // Create new configuration manager to test loading
        ConfigurationManager newConfigManager = new ConfigurationManager();
        ConfigurationManager.AntiIdleConfig loadedConfig = newConfigManager.getConfig();
        
        // Verify loaded configuration
        assertEquals(60, loadedConfig.getActivity().getIntervalSeconds());
        assertEquals("CTRL", loadedConfig.getActivity().getSimulationKey());
        assertTrue(loadedConfig.getGui().isStartMinimized());
    }

    @Test
    void testConfigurationFileCreation() {
        Path configFile = tempDir.resolve(".antiidle").resolve("antiidle-config.yml");
        assertTrue(Files.exists(configFile));
    }

    @Test
    void testReloadConfiguration() {
        // Modify and save configuration
        ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
        config.getActivity().setIntervalSeconds(45);
        configManager.updateConfig(config);
        
        // Modify in-memory config without saving
        config.getActivity().setIntervalSeconds(90);
        
        // Reload should restore saved value
        configManager.reloadConfiguration();
        assertEquals(45, configManager.getConfig().getActivity().getIntervalSeconds());
    }
}
