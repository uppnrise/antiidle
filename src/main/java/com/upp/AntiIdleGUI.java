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

    private static final Logger LOGGER = Logger.getLogger(AntiIdleGUI.class.getName());

    private static final Integer FRAME_WIDTH = 300;
    private static final Integer FRAME_HEIGHT = 100;
    Robot robot;
    Thread idleThread;

    JFrame frame;
    JButton startButton;
    JButton stopButton;

    /**
     * Constructs an instance of AntiIdleGUI.
     */
    public AntiIdleGUI() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize Robot", e);
        }

        frame = new JFrame("AntiIdle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new FlowLayout());

        startButton = new JButton("Start Idle");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startIdle();
            }
        });

        stopButton = new JButton("Stop Idle");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopIdle();
            }
        });

        frame.add(startButton);
        frame.add(stopButton);

        frame.setVisible(true);
    }

    /**
     * Starts the idle simulation thread.
     */
    void startIdle() {
        if (idleThread == null || !idleThread.isAlive()) {
            idleThread = new Thread(new Runnable() {
                @Override
                public void run() {
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
                }
            });
            idleThread.start();
        }
    }

    /**
     * Stops the idle simulation thread.
     */
    void stopIdle() {
        if (idleThread != null && idleThread.isAlive()) {
            idleThread.interrupt();
        }
    }

    /**
     * Entry point for the AntiIdleGUI application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new AntiIdleGUI();
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred during GUI initialization", e);
        }
    }
}