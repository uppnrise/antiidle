# Contributing to AntiIdle

Thank you for your interest in contributing to AntiIdle! This document provides guidelines and instructions for contributing to this project.

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Gradle 8.4 or higher (included via wrapper)
- Git for version control

### Development Environment Setup

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/antiidle.git
   cd antiidle
   ```
3. **Add the upstream repository**:
   ```bash
   git remote add upstream https://github.com/uppnrise/antiidle.git
   ```
4. **Build the project**:
   ```bash
   mvn clean install
   ```

## 📝 How to Contribute

### Reporting Bugs

Before creating bug reports, please check the existing issues to avoid duplicates. When creating a bug report, include:

- **Clear, descriptive title**
- **Steps to reproduce** the behavior
- **Expected behavior** description
- **Actual behavior** observed
- **Environment details**:
  - OS and version
  - Java version (`java -version`)
  - Gradle version (`./gradlew --version`)
- **Screenshots** if applicable
- **Error messages** (complete stack traces)

### Suggesting Enhancements

Enhancement suggestions are welcome! Please provide:

- **Clear, descriptive title**
- **Detailed description** of the proposed enhancement
- **Use case** explaining why this enhancement would be useful
- **Implementation ideas** if you have them

### Pull Requests

1. **Create a feature branch**:
   ```bash
   git checkout -b feature/amazing-feature
   ```

2. **Make your changes**:
   - Follow the coding standards (see below)
   - Add tests for new functionality
   - Update documentation as needed

3. **Test your changes**:
   ```bash
   mvn clean test
   ```

4. **Commit your changes**:
   ```bash
   git commit -m "Add some amazing feature"
   ```

5. **Push to your fork**:
   ```bash
   git push origin feature/amazing-feature
   ```

6. **Create a Pull Request** on GitHub

## 🏗️ Development Guidelines

### Code Style

- **Java Code Style**:
  - Use 4 spaces for indentation
  - Follow Oracle's Java Code Conventions
  - Use meaningful variable and method names
  - Add Javadoc comments for public methods
  - Keep methods focused and small

- **Commit Messages**:
  - Use present tense ("Add feature" not "Added feature")
  - Use imperative mood ("Move cursor to..." not "Moves cursor to...")
  - Limit first line to 72 characters
  - Reference issues and pull requests when applicable

### Testing

- Write unit tests for new functionality
- Ensure all tests pass before submitting PR
- Aim for good test coverage
- Test on multiple platforms when possible

### Documentation

- Update README.md for significant changes
- Add entries to CHANGELOG.md
- Include inline code comments for complex logic
- Update Javadoc for public APIs

## 🎯 Areas for Contribution

We welcome contributions in these areas:

### High Priority
- **System tray integration** - Minimize to system tray
- **Configuration options** - Customizable intervals and behavior
- **Better GUI** - Modern look and feel, better UX
- **Logging system** - Track activity and debugging

### Medium Priority
- **Multiple activity modes** - Different simulation strategies
- **Auto-start functionality** - Start with system boot
- **Settings persistence** - Save user preferences
- **Internationalization** - Multi-language support

### Low Priority
- **Dark mode theme** - Dark UI theme option
- **Statistics tracking** - Usage statistics and reporting
- **Plugin system** - Extensible architecture
- **CLI interface** - Command-line operation mode

## 🔍 Code Review Process

1. **Automated checks** must pass (build, tests, formatting)
2. **Manual review** by maintainers
3. **Testing** on different platforms if needed
4. **Documentation review** for public-facing changes
5. **Approval** and merge by maintainers

## 📜 License

By contributing to AntiIdle, you agree that your contributions will be licensed under the MIT License.

## ❓ Questions?

Feel free to create an issue with the `question` label if you have any questions about contributing!

## 🙏 Recognition

Contributors will be recognized in:
- GitHub contributors list
- CHANGELOG.md for significant contributions
- README.md for major features

Thank you for contributing to AntiIdle! 🎉
