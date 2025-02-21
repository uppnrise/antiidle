# AntiIdle

The **AntiIdle** project is a simple Java application that simulates user activity to prevent system idle sleep. It includes a graphical user interface (GUI) with buttons to start and stop the idle simulation.

![AntiIdle Screenshot from Mac](antiidle-mac.png)

## Features

- Moves the mouse cursor periodically to prevent system idle sleep.
- Simulates key presses to maintain system activity.
- Provides a user-friendly GUI interface for controlling the idle simulation.

## Requirements

- Java Development Kit (JDK) 17 or higher.
- A system with AWT and Swing support.

## Usage

1. Clone the repository:
    ```sh
    git clone https://github.com/uppnrise/antiidle.git
    cd antiidle
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    java -jar target/antiidle-1.0.0.0.jar
    ```

4. The GUI will appear. Click the **Start Idle** button to begin the idle simulation.

5. To stop the simulation, click the **Stop Idle** button.

## Code Explanation

### Main Class: `AntiIdleGUI`

The `AntiIdleGUI` class is the main class of the application. It sets up the GUI and handles the start and stop actions for the idle simulation.

### Algorithm Explanation

1. **Initialization**:
    - The `AntiIdleGUI` constructor initializes the `Robot` instance and sets up the GUI with `JFrame` and `JButton` components.

2. **Start Idle Simulation**:
    - The `startIdle` method creates and starts a new thread if no thread is currently running.
    - The thread moves the mouse cursor and simulates key presses in a loop to prevent the system from going idle.
    - The loop includes a delay and a sleep period to simulate user activity at regular intervals.

3. **Stop Idle Simulation**:
    - The `stopIdle` method interrupts the running thread, stopping the idle simulation.

## Notes

- Ensure that your system supports the `Robot` class functionalities used for simulating input events.
- This application is intended for educational purposes to demonstrate basic GUI programming and threading in Java.

## Contributing

Contributions are welcome! If you have suggestions, improvements, or bug fixes, please feel free to submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.