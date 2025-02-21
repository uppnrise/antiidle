package com.upp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import java.util.logging.Level;

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
                while (!Thread.currentThread().isInterrupted()) {
                    robot.mouseMove(0, 0);
                    robot.delay(1000);
                    robot.mouseMove(1, 0);
                    robot.delay(1000);
                    robot.mouseMove(0, 0);

                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.delay(1000);
                    robot.keyRelease(KeyEvent.VK_SHIFT);

                    try {
                        Thread.sleep(300000); // 5 minutes
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            idleThread.start();
        }
    }

    void stopIdle() {
        if (idleThread != null && idleThread.isAlive()) {
            idleThread.interrupt();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AntiIdleGUI::new);
    }
}