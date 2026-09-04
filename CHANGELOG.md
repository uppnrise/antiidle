# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.1.0] - 2026-09-05

### Changed
- **Dependency Updates** (all to latest stable versions, including major upgrades):
  - Jackson: 2.18.9 → 3.2.2 (new `tools.jackson` groupId; migrated `ConfigurationManager` to the Jackson 3 API)
  - JUnit: 5.11.4 → 6.1.3 (Jupiter + Platform Launcher)
  - Checkstyle: 10.21.1 → 14.1.0
  - SpotBugs plugin: 6.0.27 → 6.5.11
  - SpotBugs annotations: 4.9.0 → 4.10.4
  - SLF4J: 2.0.17 → 2.0.19
  - Logback: 1.5.36 → 1.6.3
  - FlatLaf + IntelliJ themes: 3.5.4 → 3.7.2
  - Mockito: 5.14.2 → 5.23.0
  - Awaitility: 4.2.2 → 4.3.0
  - JaCoCo: 0.8.12 → 0.8.15
- Added scoped SpotBugs exclusions for new detectors introduced by the SpotBugs engine bump (`CT_CONSTRUCTOR_THROW`, `USO_UNSAFE_*_SYNCHRONIZATION`) — verified as newly-surfaced detectors on pre-existing patterns, not regressions.

### Added
- New unit tests for `AntiIdleException` and its subclasses (100% coverage of `com.upp.exception`)
- New unit tests for `I18nManager` covering message lookup, locale switching, and fallback behavior (92.3% coverage of `com.upp.i18n`)
- Rewrote `ActivitySimulatorTest` using `Mockito.mockConstruction(Robot.class)` for deterministic, display-independent testing (76.8% coverage of `com.upp.core`)
- Overall test coverage raised from 18% to 28.1%

### Fixed
- SpotBugs findings introduced by the tooling upgrade, addressed via targeted exclusions or code fixes rather than suppressed broadly

## [2.0.0] - 2025-11-12

### Added
- **Modern FlatLaf UI Framework**:
  - Beautiful, professional FlatLaf Look and Feel (v3.5.4)
  - Support for Light, Dark, and System Default themes
  - Automatic theme detection based on system preferences
  - Smooth, modern appearance across all platforms
- **Comprehensive Internationalization (i18n)**:
  - Full multi-language support framework using Java ResourceBundle
  - Complete English (en) translation
  - Complete German (de) translation
  - Runtime language switching capability
  - Persistent language preference storage
  - Extensible architecture for adding more languages
- **Advanced Settings Dialog**:
  - Tabbed interface with Activity, GUI, and System tabs
  - Live configuration editing with immediate save
  - Intuitive controls with spinners, checkboxes, and combo boxes
  - Activity settings: interval, mouse distance, keyboard simulation
  - GUI settings: language, theme, tray, notifications
  - System settings: auto-start, boot integration, update checking
- **Enhanced Menu System**:
  - File menu: Settings access and application exit
  - Language menu: Quick language switching (English/German)
  - Help menu: About dialog with application information
  - Keyboard shortcuts and mnemonics
- **Improved User Experience**:
  - Tooltips on all interactive elements
  - Better visual feedback and status indicators
  - Larger, more readable fonts
  - Improved spacing and layout
  - Persistent window position
  - About dialog with version and licensing information

### Changed
- **Dependency Updates** (all to latest stable versions):
  - Jackson: 2.15.2 → 2.18.2
  - SLF4J: 2.0.7 → 2.0.16
  - Logback: 1.4.8 → 1.5.15
  - JUnit: 5.10.0 → 5.11.4
  - Mockito: 5.4.0 → 5.14.2
  - SpotBugs: 6.0.7 → 6.0.27
  - Checkstyle: 10.12.1 → 10.21.1
  - JaCoCo: 0.8.10 → 0.8.12
- **Architecture Improvements**:
  - New I18nManager singleton for centralized localization
  - Separate SettingsDialog class for better code organization
  - Enhanced ConfigurationManager with language and theme support
  - Improved separation of concerns in GUI code
- **Version**: Bumped to 2.0.0 to reflect major modernization
- **Documentation**: Comprehensive README update with new features and configuration guide

### Fixed
- All Checkstyle violations resolved
- All SpotBugs warnings addressed
- Code quality improvements across all classes
- Better error handling for UI initialization
- Improved exception messages with i18n support

### Security
- No vulnerabilities found in updated dependencies (verified via GitHub Advisory Database)
- SpotBugs suppression annotations properly documented
- Enhanced input validation in SettingsDialog

## [1.1.0] - 2025-06-10

### Added
- **GitHub Actions CI/CD Pipeline**: Complete automated testing and build pipeline
  - Multi-platform testing (Ubuntu, Windows, macOS)
  - Multi-Java version support (17, 21)
  - Code quality analysis with SpotBugs and Checkstyle
  - Test coverage reporting with JaCoCo
  - Automated release artifact generation
- **Configuration Management System**:
  - YAML-based configuration file (`~/.antiidle/antiidle-config.yml`)
  - Configurable activity intervals and behavior
  - GUI preferences persistence (window position, themes)
  - System settings (auto-start, update checking)
  - Logging configuration
- **Enhanced Error Handling**:
  - Custom exception hierarchy for different error types
  - Graceful error recovery and user notification
  - Comprehensive logging with SLF4J and Logback
  - Better error messages and troubleshooting information
- **Gradle Build System**:
  - Migrated from Maven to Gradle for better build performance
  - Enhanced build plugins and code quality tools
  - Multi-format distribution packages (ZIP, TAR)
  - Runtime image generation support
- **Activity Simulation Engine**:
  - Separated core logic from GUI for better architecture
  - Thread-safe activity simulation with proper lifecycle management
  - Configurable mouse movement distance and keyboard simulation
  - Support for different simulation keys and intervals
- **Enhanced GUI**:
  - Settings button for future configuration dialog
  - Real-time activity interval display
  - Better visual feedback and status indicators
  - Improved window management and positioning
  - Graceful application shutdown handling
- **Comprehensive Testing**:
  - Unit tests for all major components
  - Integration tests for GUI components
  - Mock-based testing for Robot interactions
  - Headless mode support for CI/CD environments
- **Code Quality Tools**:
  - Checkstyle configuration for code style enforcement
  - SpotBugs configuration for bug detection
  - JaCoCo for test coverage analysis
  - Automated quality gates in CI pipeline

### Changed
- **Project Structure**: Reorganized code into logical packages (`config`, `core`, `exception`)
- **Build System**: Completely migrated from Maven to Gradle
- **Architecture**: Separated concerns with dedicated classes for configuration and activity simulation
- **Version**: Bumped to 1.1.0 to reflect major feature additions
- **Documentation**: Enhanced README with comprehensive installation and usage instructions

### Fixed
- **Thread Safety**: Improved thread management and interruption handling
- **Resource Management**: Better cleanup and resource disposal
- **Configuration Persistence**: Reliable saving and loading of user preferences
- **Error Recovery**: More robust error handling and recovery mechanisms

### Security
- **Input Validation**: Added validation for configuration values
- **Safe Defaults**: Implemented secure default configurations
- **Permission Handling**: Better handling of system permissions for input simulation

## [1.0.0.0] - 2025-06-10

### Added
- Initial release of AntiIdle application
- Simple GUI with Start/Stop buttons
- Mouse movement simulation to prevent idle sleep
- Keyboard simulation (Shift key press)
- Threading for background execution
- Cross-platform support (Windows, macOS, Linux)
- Maven build configuration
- Launch4j plugin for Windows executable generation
- Basic unit tests with JUnit 5
- MIT License

### Fixed
- Improved mouse movement algorithm to use current position instead of hardcoded coordinates
- Fixed threading issue by replacing `wait()` with `Thread.sleep()`
- Reduced activity interval from 5 minutes to 30 seconds for better responsiveness
- Added proper thread interruption handling

### Security
- Uses harmless Shift key press instead of potentially disruptive keys
- Smart mouse movement that doesn't interfere with user activity

## [Unreleased]

### Future Enhancements
- System tray integration with minimization support
- Auto-update functionality
- Additional language translations (French, Spanish, etc.)
- Custom notification sounds
- Activity logging and statistics
- Scheduler for timed idle prevention
- Multiple simulation profiles
