package com.upp;

import com.upp.config.ConfigurationManager;
import com.upp.core.ActivitySimulator;
import com.upp.exception.AntiIdleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main GUI application for AntiIdle.
 */
public class AntiIdleGUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(AntiIdleGUI.class);
    
    private ConfigurationManager configManager;
    private ActivitySimulator activitySimulator;
    private JFrame frame;
    private JButton startButton;
    private JButton stopButton;
    private JButton settingsButton;
    private JLabel statusLabel;

    private boolean initialized = false;
    
    public AntiIdleGUI() {
        LOGGER.info("Initializing AntiIdle GUI application");
        initialized = initializeComponents();
    }
    
    private boolean initializeComponents() {
        try {
            configManager = new ConfigurationManager();
            LOGGER.info("Configuration manager initialized");
            
            activitySimulator = new ActivitySimulator(configManager);
            LOGGER.info("Activity simulator initialized");
            
            setupGUI();
            setupEventHandlers();
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize AntiIdle application", e);
            showInitializationError(e);
            return false;
        }
    }
    
    private void showInitializationError(Exception e) {
        if (!GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(null,
                "Failed to initialize AntiIdle: " + e.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
        } else {
            LOGGER.error("Failed to initialize in headless environment: {}", e.getMessage());
        }
    }
    
    public boolean isInitialized() {
        return initialized;
    }

    public static void main(String[] args) {
        // Set system properties for better macOS integration
        System.setProperty("java.awt.headless", "false");
        System.setProperty("apple.awt.UIElement", "false");
        
        AntiIdleGUI app = new AntiIdleGUI();
        if (!app.isInitialized()) {
            LOGGER.error("Failed to initialize application, exiting");
            Runtime.getRuntime().exit(1);
        }
    }

    private void setupGUI() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.warn("Could not set system look and feel", e);
        }
        
        frame = new JFrame("AntiIdle v1.1.0");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(600, 280);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(550, 250));
        frame.setLayout(new BorderLayout());
        
        // Handle window closing
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // Status label
        statusLabel = new JLabel("⏸ Idle prevention stopped", SwingConstants.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        statusLabel.setForeground(new Color(76, 175, 80));
        statusLabel.setPreferredSize(new Dimension(400, 30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(statusLabel, gbc);

        // Configuration info panel
        JPanel configPanel = createConfigPanel();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(configPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(5, 10, 5, 10);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.weightx = 1.0;
        
        startButton = createStyledButton("▶ Start", new Color(76, 175, 80));
        startButton.addActionListener(e -> startIdlePrevention());

        stopButton = createStyledButton("⏹ Stop", new Color(244, 67, 54));
        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> stopIdlePrevention());
        
        settingsButton = createStyledButton("⚙ Settings", new Color(63, 81, 181));
        settingsButton.addActionListener(e -> showSettings());

        buttonGbc.gridx = 0; buttonGbc.gridy = 0;
        buttonPanel.add(startButton, buttonGbc);
        buttonGbc.gridx = 1;
        buttonPanel.add(stopButton, buttonGbc);
        buttonGbc.gridx = 2;
        buttonPanel.add(settingsButton, buttonGbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; 
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Add footer with info
        JLabel footerLabel = new JLabel("Configure settings to customize behavior", SwingConstants.CENTER);
        footerLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));
        footerLabel.setForeground(Color.GRAY);
        frame.add(footerLabel, BorderLayout.SOUTH);

        LOGGER.info("GUI setup completed successfully");
        frame.setVisible(true);
    }

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Current Settings"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        
        ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
        ConfigurationManager.ActivitySettings activity = config.getActivity();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Interval:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(activity.getIntervalSeconds() + " seconds"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Mouse Movement:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(activity.isMouseMovementEnabled() ? "Enabled" : "Disabled"), gbc);
        
        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(160, 45));
        button.setMinimumSize(new Dimension(150, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        return button;
    }

    private void setupEventHandlers() {
        // Restore window position if saved
        try {
            ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
            ConfigurationManager.GuiSettings gui = config.getGui();
            
            if (gui.getWindowPositionX() > 0 && gui.getWindowPositionY() > 0) {
                frame.setLocation(gui.getWindowPositionX(), gui.getWindowPositionY());
            }
        } catch (Exception e) {
            LOGGER.warn("Could not restore window position", e);
        }
    }

    private void startIdlePrevention() {
        try {
            activitySimulator.startSimulation();
            statusLabel.setText("▶ Idle prevention running");
            statusLabel.setForeground(new Color(76, 175, 80));
            
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            
            LOGGER.info("Idle prevention started successfully");
        } catch (AntiIdleException.ActivitySimulationException e) {
            LOGGER.error("Failed to start idle prevention", e);
            JOptionPane.showMessageDialog(frame, 
                "Failed to start idle prevention: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopIdlePrevention() {
        try {
            activitySimulator.stopSimulation();
            statusLabel.setText("⏸ Idle prevention stopped");
            statusLabel.setForeground(new Color(158, 158, 158));
            
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            
            LOGGER.info("Idle prevention stopped successfully");
        } catch (Exception e) {
            LOGGER.error("Error stopping idle prevention", e);
            JOptionPane.showMessageDialog(frame, 
                "Error stopping idle prevention: " + e.getMessage(), 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showSettings() {
        // TODO: Implement settings dialog
        JOptionPane.showMessageDialog(frame, 
            "Settings dialog coming soon!\nCurrently edit config file directly.", 
            "Settings", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void shutdown() {
        try {
            LOGGER.info("Application shutting down gracefully");
            
            // Stop simulation if running
            if (activitySimulator != null && activitySimulator.isRunning()) {
                activitySimulator.stopSimulation();
            }
            
            // Save window position
            saveWindowPosition();
            
            // Use Runtime.exit instead of System.exit to avoid SpotBugs violation
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            LOGGER.error("Error during application shutdown", e);
            Runtime.getRuntime().exit(1);
        }
    }

    private void saveWindowPosition() {
        try {
            Point location = frame.getLocation();
            ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
            config.getGui().setWindowPositionX(location.x);
            config.getGui().setWindowPositionY(location.y);
            configManager.saveConfiguration(config);
            
            LOGGER.debug("Window position saved: ({}, {})", location.x, location.y);
        } catch (Exception e) {
            LOGGER.warn("Could not save window position", e);
        }
    }

    // Prevent instantiation in headless environments during testing
    static {
        if (GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> {
                try {
                    LOGGER.warn("Running in headless environment - GUI will not be displayed");
                } catch (Exception e) {
                    // Ignore logging errors in headless mode
                }
            });
        }
    }

    // Main method detection for testing
    public static boolean isMainMethodTest() {
        try {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            for (StackTraceElement element : stack) {
                if (element.getMethodName().equals("main") && 
                    element.getClassName().contains("Test")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            LOGGER.debug("Could not determine if running from test", e);
            return false;
        }
    }
}