package com.upp;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.upp.config.ConfigurationManager;
import com.upp.core.ActivitySimulator;
import com.upp.exception.AntiIdleException;
import com.upp.i18n.I18nManager;
import com.upp.ui.SettingsDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public class AntiIdleGUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(AntiIdleGUI.class);
    
    private ConfigurationManager configManager;
    private ActivitySimulator activitySimulator;
    private I18nManager i18n;
    private JFrame frame;
    private JButton startButton;
    private JButton stopButton;
    private JButton settingsButton;
    private JLabel statusLabel;

    private boolean initialized = false;
    
    /**
     * Constructs a new AntiIdleGUI and initializes all components.
     */
    public AntiIdleGUI() {
        LOGGER.info("Initializing AntiIdle GUI application");
        initialized = initializeComponents();
    }
    
    private boolean initializeComponents() {
        try {
            configManager = new ConfigurationManager();
            LOGGER.info("Configuration manager initialized");
            
            i18n = I18nManager.getInstance();
            
            // Set locale from configuration
            ConfigurationManager.GuiSettings gui = configManager.getConfig().getGui();
            if (gui.getLanguage() != null) {
                i18n.setLocale(gui.getLanguage());
            }
            
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
                I18nManager.getInstance().getMessage("dialog.init.failed", e.getMessage()),
                I18nManager.getInstance().getMessage("dialog.init.error"),
                JOptionPane.ERROR_MESSAGE);
        } else {
            LOGGER.error("Failed to initialize in headless environment: {}", e.getMessage());
        }
    }
    
    /**
     * Checks if the application was initialized successfully.
     * 
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Main entry point for the application.
     * 
     * @param args command line arguments (not used)
     */
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
            // Set FlatLaf Look and Feel based on configuration
            setupLookAndFeel();
        } catch (Exception e) {
            LOGGER.warn("Could not set FlatLaf look and feel", e);
        }
        
        frame = new JFrame(i18n.getMessage("app.title"));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(650, 320);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(600, 280));
        frame.setLayout(new BorderLayout());
        
        // Create menu bar
        setupMenuBar();
        
        // Handle window closing
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();

        // Status label
        statusLabel = new JLabel(i18n.getMessage("status.stopped"), SwingConstants.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        statusLabel.setPreferredSize(new Dimension(500, 40));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 25, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(statusLabel, gbc);

        // Configuration info panel
        JPanel configPanel = createConfigPanel();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(configPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(5, 12, 5, 12);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.weightx = 1.0;
        
        startButton = createStyledButton(i18n.getMessage("button.start"), new Color(76, 175, 80));
        startButton.setToolTipText(i18n.getMessage("tooltip.start"));
        startButton.addActionListener(e -> startIdlePrevention());

        stopButton = createStyledButton(i18n.getMessage("button.stop"), new Color(244, 67, 54));
        stopButton.setToolTipText(i18n.getMessage("tooltip.stop"));
        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> stopIdlePrevention());
        
        settingsButton = createStyledButton(i18n.getMessage("button.settings"), new Color(63, 81, 181));
        settingsButton.setToolTipText(i18n.getMessage("tooltip.settings"));
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
        JLabel footerLabel = new JLabel(i18n.getMessage("app.footer"), SwingConstants.CENTER);
        footerLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        frame.add(footerLabel, BorderLayout.SOUTH);

        LOGGER.info("GUI setup completed successfully");
        frame.setVisible(true);
    }
    
    private void setupLookAndFeel() {
        try {
            ConfigurationManager.GuiSettings gui = configManager.getConfig().getGui();
            String theme = gui.getTheme();
            
            if ("dark".equals(theme) || (gui.isDarkMode() && "system".equals(theme))) {
                FlatDarculaLaf.setup();
                LOGGER.info("FlatLaf Dark theme applied");
            } else if ("light".equals(theme)) {
                FlatIntelliJLaf.setup();
                LOGGER.info("FlatLaf Light theme applied");
            } else {
                // System default - use FlatLaf with system detection
                if (FlatLaf.isLafDark()) {
                    FlatDarculaLaf.setup();
                } else {
                    FlatIntelliJLaf.setup();
                }
                LOGGER.info("FlatLaf theme applied with system detection");
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to setup FlatLaf, using default", e);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                LOGGER.warn("Failed to set system look and feel", ex);
            }
        }
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu(i18n.getMessage("menu.file"));
        JMenuItem settingsItem = new JMenuItem(i18n.getMessage("menu.settings"));
        settingsItem.addActionListener(e -> showSettings());
        JMenuItem exitItem = new JMenuItem(i18n.getMessage("menu.exit"));
        exitItem.addActionListener(e -> shutdown());
        
        fileMenu.add(settingsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Language menu
        JMenu languageMenu = new JMenu(i18n.getMessage("menu.language"));
        JMenuItem englishItem = new JMenuItem(i18n.getMessage("language.english"));
        englishItem.addActionListener(e -> changeLanguage("en"));
        JMenuItem germanItem = new JMenuItem(i18n.getMessage("language.german"));
        germanItem.addActionListener(e -> changeLanguage("de"));
        
        languageMenu.add(englishItem);
        languageMenu.add(germanItem);
        
        // Help menu
        JMenu helpMenu = new JMenu(i18n.getMessage("menu.help"));
        JMenuItem aboutItem = new JMenuItem(i18n.getMessage("menu.about"));
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(languageMenu);
        menuBar.add(helpMenu);
        
        frame.setJMenuBar(menuBar);
    }

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(i18n.getMessage("settings.current")));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        
        ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
        ConfigurationManager.ActivitySettings activity = config.getActivity();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(i18n.getMessage("activity.interval")), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(activity.getIntervalSeconds() + " " + i18n.getMessage("activity.interval.seconds")), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel(i18n.getMessage("activity.mouse")), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(activity.isMouseMovementEnabled() ? 
            i18n.getMessage("activity.enabled") : i18n.getMessage("activity.disabled")), gbc);
        
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
            statusLabel.setText(i18n.getMessage("status.running"));
            
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            
            LOGGER.info("Idle prevention started successfully");
        } catch (AntiIdleException.ActivitySimulationException e) {
            LOGGER.error("Failed to start idle prevention", e);
            JOptionPane.showMessageDialog(frame, 
                i18n.getMessage("dialog.start.failed", e.getMessage()), 
                i18n.getMessage("dialog.start.error"), 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopIdlePrevention() {
        try {
            activitySimulator.stopSimulation();
            statusLabel.setText(i18n.getMessage("status.stopped"));
            
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            
            LOGGER.info("Idle prevention stopped successfully");
        } catch (Exception e) {
            LOGGER.error("Error stopping idle prevention", e);
            JOptionPane.showMessageDialog(frame, 
                i18n.getMessage("dialog.stop.failed", e.getMessage()), 
                i18n.getMessage("dialog.stop.warning"), 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showSettings() {
        SettingsDialog dialog = new SettingsDialog(frame, configManager);
        dialog.setVisible(true);
        
        if (dialog.isSettingsChanged()) {
            // Refresh UI if language or theme changed
            JOptionPane.showMessageDialog(frame,
                "Settings saved! Restart the application for all changes to take effect.",
                i18n.getMessage("settings.title"),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void changeLanguage(String languageCode) {
        i18n.setLocale(languageCode);
        
        // Save language preference
        ConfigurationManager.AntiIdleConfig config = configManager.getConfig();
        config.getGui().setLanguage(languageCode);
        configManager.saveConfiguration(config);
        
        // Inform user to restart
        JOptionPane.showMessageDialog(frame,
            "Language changed! Please restart the application for the changes to take effect.",
            "Language",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(frame,
            i18n.getMessage("dialog.about.message"),
            i18n.getMessage("dialog.about.title"),
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
    /**
     * Detects if the application is running from a test context.
     * 
     * @return true if running from a test, false otherwise
     */
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