package com.upp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class AntiIdleGUITest {

    private AntiIdleGUI antiIdleGUI;

    @BeforeEach
    void setUp() {
        antiIdleGUI = new AntiIdleGUI();
    }

    @Test
    void startIdleSimulation() {
        antiIdleGUI.startIdle();
        assertNotNull(antiIdleGUI.idleThread);
        assertTrue(antiIdleGUI.idleThread.isAlive());
    }

    @Test
    void stopIdleSimulation() {
        antiIdleGUI.startIdle();
        antiIdleGUI.stopIdle();
        assertFalse(antiIdleGUI.idleThread.isAlive());
    }

    @Test
    void startButtonActionPerformed() {
        JButton startButton = new JButton("Start Idle");
        startButton.addActionListener(e -> antiIdleGUI.startIdle());
        startButton.doClick();
        assertNotNull(antiIdleGUI.idleThread);
        assertTrue(antiIdleGUI.idleThread.isAlive());
    }

    @Test
    void stopButtonActionPerformed() {
        JButton stopButton = new JButton("Stop Idle");
        stopButton.addActionListener(e -> antiIdleGUI.stopIdle());
        antiIdleGUI.startIdle();
        stopButton.doClick();
        assertFalse(antiIdleGUI.idleThread.isAlive());
    }
}