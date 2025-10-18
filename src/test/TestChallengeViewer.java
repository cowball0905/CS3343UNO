package test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.UNOController;
import model.CPUPlayer;
import model.CountDownTimer;
import model.Player;
import view.ChallengeViewer;

class TestChallengeViewer {
    private ChallengeViewer challengeViewer;
    private UNOController controller;
    private JPanel panel;

    @BeforeEach
    void setUp() {
        challengeViewer = new ChallengeViewer();
        controller = UNOController.getInstance();
        panel = new JPanel();
        challengeViewer.setController(controller);
        challengeViewer.setPanel(panel);
        
        // Initialize timer
        CountDownTimer timer = new CountDownTimer(panel, () -> {});
        challengeViewer.setTimer(timer);
    }

    @Test
    void testInitialState() {
        assertFalse(challengeViewer.getIsChallenging(), 
            "Initially should not be in challenging state");
    }

    @Test
    void testSetChallenge() {
        challengeViewer.setChallenge();
        assertTrue(challengeViewer.getIsChallenging(), 
            "Should be in challenging state after setChallenge");
    }

    @Test
    void testButtonActions() {
        // Setup test data
        Player currentPlayer = new CPUPlayer("TestPlayer1");
        Player nextPlayer = new CPUPlayer("TestPlayer2");
        ArrayList<Player> playerList = new ArrayList<>(Arrays.asList(currentPlayer, nextPlayer));
        
        // Setup controller
        controller.getPlayerList().clear();
        controller.getPlayerList().addAll(playerList);
        controller.setCurrentPlayer(currentPlayer);
        
        // Test red button
        challengeViewer.setChallenge();
        JButton redButton = findButtonByText("Get 4 Cards");
        assertNotNull(redButton, "Red button should exist");
        redButton.doClick();
        
        // Test green button
        challengeViewer.setChallenge();
        JButton greenButton = findButtonByText("Challenge");
        assertNotNull(greenButton, "Green button should exist");
        greenButton.doClick();
    }

    @Test
    void testNullController() {
        // Create a new instance without setting the controller
        challengeViewer = new ChallengeViewer();
        challengeViewer.setPanel(panel);
        
        // Create a test timer
        CountDownTimer testTimer = new CountDownTimer(panel, () -> {});
        challengeViewer.setTimer(testTimer);
        
        // Set up the challenge and draw the window to initialize buttons
        challengeViewer.setChallenge();
        challengeViewer.drawWindow(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics());
        
        // Find the red button
        JButton redButton = findButtonByText("Get 4 Cards");
        assertNotNull(redButton, "Red button should exist");
        
        // Click the button - should not throw NullPointerException
        assertDoesNotThrow(() -> redButton.doClick(), 
            "Should handle null controller gracefully");
        
        // Verify the challenge state was reset
        assertFalse(challengeViewer.getIsChallenging(), 
            "Challenge state should be reset after button click");
        
        // Verify buttons were removed
        assertNull(findButtonByText("Get 4 Cards"), 
            "Buttons should be removed after click");
    }

    @Test
    void testTimerDisplay() {
        // Create a test timer with fixed time
        CountDownTimer testTimer = new CountDownTimer(panel, () -> {}) {
            @Override
            public int getRemainingSeconds() {
                return 10; // Fixed value for testing
            }
        };
        
        challengeViewer.setTimer(testTimer);
        challengeViewer.setChallenge();
        
        // Test drawing
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        assertDoesNotThrow(() -> challengeViewer.drawWindow(g), 
            "Should draw without exceptions");
        g.dispose();
    }

    @Test
    void testRemoveButtons() {
        // Add buttons first
        challengeViewer.setChallenge();
        challengeViewer.drawWindow(new BufferedImage(100, 100, 
            BufferedImage.TYPE_INT_ARGB).getGraphics());
        
        // Verify buttons exist
        assertNotNull(findButtonByText("Get 4 Cards"), 
            "Red button should exist after draw");
        assertNotNull(findButtonByText("Challenge"), 
            "Green button should exist after draw");
        
        // Remove buttons
        challengeViewer.removeButtons();
        
        // Verify buttons are gone
        assertNull(findButtonByText("Get 4 Cards"), 
            "Red button should be removed");
        assertNull(findButtonByText("Challenge"), 
            "Green button should be removed");
    }

    @Test
    void testEdgeCases() {
        // Test with null panel
        challengeViewer.setPanel(null);
        assertDoesNotThrow(() -> challengeViewer.drawWindow(null),
            "Should handle null panel gracefully");
        
        // Test with null timer
        challengeViewer.setTimer(null);
        assertDoesNotThrow(() -> challengeViewer.drawWindow(null),
            "Should handle null timer gracefully");
    }

    // Helper method to find buttons
    private JButton findButtonByText(String text) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (text.equals(button.getText())) {
                    return button;
                }
            }
        }
        return null;
    }
}