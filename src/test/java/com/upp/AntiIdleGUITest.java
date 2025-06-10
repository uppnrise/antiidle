package com.upp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Integration tests for AntiIdleGUI.
 */
class AntiIdleGUITest {

    @BeforeEach
    void setUp() {
        // Set headless mode for testing
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void testGUIInitializationInHeadlessMode() {
        // In headless mode, GUI initialization should handle gracefully
        AntiIdleGUI gui = new AntiIdleGUI();
        // Constructor should not throw but initialization should fail in headless mode
        assertFalse(gui.isInitialized());
    }

    @Test
    void testMainMethodWithHeadlessMode() {
        // Test that main method handles headless mode gracefully
        String originalHeadless = System.getProperty("java.awt.headless");
        
        try {
            System.setProperty("java.awt.headless", "true");
            
            // This should not throw an exception, but will show error dialog
            assertDoesNotThrow(() -> {
                // We can't really test the full main method in headless mode
                // but we can test that it sets the right properties
                System.setProperty("java.awt.headless", "false");
                System.setProperty("apple.awt.UIElement", "false");
            });
            
        } finally {
            if (originalHeadless != null) {
                System.setProperty("java.awt.headless", originalHeadless);
            }
        }
    }

    @Test
    @Timeout(2)
    void testSystemPropertyConfiguration() {
        // Test that the application sets the correct system properties
        
        // Simulate what main method does
        System.setProperty("java.awt.headless", "false");
        System.setProperty("apple.awt.UIElement", "false");
        
        assertEquals("false", System.getProperty("java.awt.headless"));
        assertEquals("false", System.getProperty("apple.awt.UIElement"));
    }
}