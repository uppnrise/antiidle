package com.upp.ui;

import com.upp.config.ConfigurationManager;
import com.upp.i18n.I18nManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * Modern settings dialog with tabbed interface.
 */
public class SettingsDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private final ConfigurationManager configManager;
    private final I18nManager i18n;
    private ConfigurationManager.AntiIdleConfig config;
    private boolean settingsChanged = false;
    
    // Activity settings
    private JSpinner intervalSpinner;
    private JCheckBox mouseEnabledCheck;
    private JSpinner mouseDistanceSpinner;
    private JCheckBox keyboardEnabledCheck;
    private JTextField simulationKeyField;
    private JSpinner keyDurationSpinner;
    
    // GUI settings
    private JComboBox<String> languageCombo;
    private JComboBox<String> themeCombo;
    private JCheckBox darkModeCheck;
    private JCheckBox minimizeToTrayCheck;
    private JCheckBox showNotificationsCheck;
    
    public SettingsDialog(JFrame parent, ConfigurationManager configManager) {
        super(parent, true);
        this.configManager = configManager;
        this.i18n = I18nManager.getInstance();
        this.config = configManager.getConfig();
        
        initComponents();
        loadSettings();
    }
    
    private void initComponents() {
        setTitle(i18n.getMessage("settings.title"));
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Activity tab
        tabbedPane.addTab(i18n.getMessage("settings.activity"), createActivityPanel());
        
        // GUI tab
        tabbedPane.addTab(i18n.getMessage("settings.gui"), createGuiPanel());
        
        // System tab
        tabbedPane.addTab(i18n.getMessage("settings.system"), createSystemPanel());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton(i18n.getMessage("button.ok"));
        JButton cancelButton = new JButton(i18n.getMessage("button.cancel"));
        JButton applyButton = new JButton(i18n.getMessage("button.apply"));
        
        okButton.addActionListener(e -> {
            saveSettings();
            dispose();
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        applyButton.addActionListener(e -> saveSettings());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Interval
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("activity.interval")), gbc);
        gbc.gridx = 1;
        intervalSpinner = new JSpinner(new SpinnerNumberModel(30, 5, 300, 5));
        intervalSpinner.setToolTipText(i18n.getMessage("tooltip.interval"));
        panel.add(intervalSpinner, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel(i18n.getMessage("activity.interval.seconds")), gbc);
        
        row++;
        
        // Mouse movement
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("activity.mouse")), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mouseEnabledCheck = new JCheckBox(i18n.getMessage("activity.enabled"));
        mouseEnabledCheck.setToolTipText(i18n.getMessage("tooltip.mouse.movement"));
        panel.add(mouseEnabledCheck, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        // Mouse distance
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("activity.distance")), gbc);
        gbc.gridx = 1;
        mouseDistanceSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        panel.add(mouseDistanceSpinner, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("pixels"), gbc);
        
        row++;
        
        // Keyboard simulation
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("activity.keyboard")), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        keyboardEnabledCheck = new JCheckBox(i18n.getMessage("activity.enabled"));
        keyboardEnabledCheck.setToolTipText(i18n.getMessage("tooltip.keyboard.simulation"));
        panel.add(keyboardEnabledCheck, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        // Simulation key
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("activity.key")), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        simulationKeyField = new JTextField(10);
        panel.add(simulationKeyField, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        // Key duration
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("activity.duration")), gbc);
        gbc.gridx = 1;
        keyDurationSpinner = new JSpinner(new SpinnerNumberModel(100, 10, 1000, 10));
        panel.add(keyDurationSpinner, gbc);
        gbc.gridx = 2;
        panel.add(new JLabel(i18n.getMessage("activity.milliseconds")), gbc);
        
        return panel;
    }
    
    private JPanel createGuiPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Language
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("settings.language")), gbc);
        gbc.gridx = 1;
        String[] languages = {i18n.getMessage("language.english"), i18n.getMessage("language.german")};
        languageCombo = new JComboBox<>(languages);
        panel.add(languageCombo, gbc);
        
        row++;
        
        // Theme
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(i18n.getMessage("gui.theme")), gbc);
        gbc.gridx = 1;
        String[] themes = {
            i18n.getMessage("gui.system.default"),
            i18n.getMessage("gui.light.mode"),
            i18n.getMessage("gui.dark.mode")
        };
        themeCombo = new JComboBox<>(themes);
        themeCombo.setToolTipText(i18n.getMessage("tooltip.dark.mode"));
        panel.add(themeCombo, gbc);
        
        row++;
        
        // Dark mode (legacy)
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        darkModeCheck = new JCheckBox(i18n.getMessage("gui.dark.mode"));
        panel.add(darkModeCheck, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        // Minimize to tray
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        minimizeToTrayCheck = new JCheckBox(i18n.getMessage("gui.minimize.tray"));
        panel.add(minimizeToTrayCheck, gbc);
        gbc.gridwidth = 1;
        
        row++;
        
        // Show notifications
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        showNotificationsCheck = new JCheckBox(i18n.getMessage("gui.show.notifications"));
        panel.add(showNotificationsCheck, gbc);
        
        return panel;
    }
    
    private JPanel createSystemPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(new JCheckBox(i18n.getMessage("system.auto.start")), gbc);
        
        row++;
        gbc.gridy = row;
        panel.add(new JCheckBox(i18n.getMessage("system.start.boot")), gbc);
        
        row++;
        gbc.gridy = row;
        panel.add(new JCheckBox(i18n.getMessage("system.check.updates")), gbc);
        
        return panel;
    }
    
    private void loadSettings() {
        ConfigurationManager.ActivitySettings activity = config.getActivity();
        intervalSpinner.setValue(activity.getIntervalSeconds());
        mouseEnabledCheck.setSelected(activity.isMouseMovementEnabled());
        mouseDistanceSpinner.setValue(activity.getMouseMovementDistance());
        keyboardEnabledCheck.setSelected(activity.isKeyboardSimulationEnabled());
        simulationKeyField.setText(activity.getSimulationKey());
        keyDurationSpinner.setValue(activity.getKeyPressDurationMs());
        
        ConfigurationManager.GuiSettings gui = config.getGui();
        String lang = gui.getLanguage();
        languageCombo.setSelectedIndex("de".equals(lang) ? 1 : 0);
        
        String theme = gui.getTheme();
        if ("light".equals(theme)) {
            themeCombo.setSelectedIndex(1);
        } else if ("dark".equals(theme)) {
            themeCombo.setSelectedIndex(2);
        } else {
            themeCombo.setSelectedIndex(0);
        }
        
        darkModeCheck.setSelected(gui.isDarkMode());
        minimizeToTrayCheck.setSelected(gui.isMinimizeToTray());
        showNotificationsCheck.setSelected(gui.isShowNotifications());
    }
    
    private void saveSettings() {
        ConfigurationManager.ActivitySettings activity = config.getActivity();
        activity.setIntervalSeconds((Integer) intervalSpinner.getValue());
        activity.setMouseMovementEnabled(mouseEnabledCheck.isSelected());
        activity.setMouseMovementDistance((Integer) mouseDistanceSpinner.getValue());
        activity.setKeyboardSimulationEnabled(keyboardEnabledCheck.isSelected());
        activity.setSimulationKey(simulationKeyField.getText());
        activity.setKeyPressDurationMs((Integer) keyDurationSpinner.getValue());
        
        ConfigurationManager.GuiSettings gui = config.getGui();
        gui.setLanguage(languageCombo.getSelectedIndex() == 1 ? "de" : "en");
        
        int themeIndex = themeCombo.getSelectedIndex();
        if (themeIndex == 1) {
            gui.setTheme("light");
        } else if (themeIndex == 2) {
            gui.setTheme("dark");
        } else {
            gui.setTheme("system");
        }
        
        gui.setDarkMode(darkModeCheck.isSelected());
        gui.setMinimizeToTray(minimizeToTrayCheck.isSelected());
        gui.setShowNotifications(showNotificationsCheck.isSelected());
        
        configManager.saveConfiguration(config);
        settingsChanged = True;
        
        JOptionPane.showMessageDialog(this,
            "Settings saved successfully!",
            i18n.getMessage("settings.title"),
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public boolean isSettingsChanged() {
        return settingsChanged;
    }
}
