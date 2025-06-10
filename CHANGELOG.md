# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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

### Planned Features
- System tray integration
- Configurable activity intervals
- Multiple activity simulation modes
- Logging and status reporting
- Auto-start with system
- Configuration file support
- Dark mode theme
