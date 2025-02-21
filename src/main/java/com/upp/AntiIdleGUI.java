package com.upp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * A simple GUI application for simulating anti-idle behavior.
 */
public class AntiIdleGUI {

    private final Robot robot;
    Thread idleThread;

    public AntiIdleGUI() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Failed to initialize Robot", e);
        }

        JFrame frame = new JFrame("AntiIdle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setLayout(new FlowLayout());

        JButton startButton = new JButton("Start Idle");
        startButton.addActionListener(e -> startIdle());

        JButton stopButton = new JButton("Stop Idle");
        stopButton.addActionListener(e -> stopIdle());

        frame.add(startButton);
        frame.add(stopButton);
        frame.setVisible(true);
    }

    void startIdle() {
        if (idleThread == null || !idleThread.isAlive()) {
            idleThread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        robot.mouseMove(0, 0);
                        robot.delay(1000);
                        robot.mouseMove(1, 0);
                        robot.delay(1000);
                        robot.mouseMove(0, 0);

                        robot.keyPress(KeyEvent.VK_SHIFT);
                        robot.delay(1000);
                        robot.keyRelease(KeyEvent.VK_SHIFT);

                        for (int i = 0; i < 300; i++) { // Check for interruption every second
                            if (Thread.currentThread().isInterrupted()) {
                                throw new InterruptedException();
                            }
                            Thread.sleep(1000);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            idleThread.start();
        }
    }

    void stopIdle() {
        if (idleThread != null && idleThread.isAlive()) {
            idleThread.interrupt();
            try {
                idleThread.join(); // Wait for the thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AntiIdleGUI::new);
    }
}