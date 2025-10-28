package test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.UNOController;
import model.CPUPlayer;
import model.Card;
import model.CardFactory;
import model.ConcreteCardFactory;
import model.CountDownTimer;
import model.Player;
import view.ChallengeViewer;

class TestChallengeViewer {
    private ChallengeViewer challengeViewer;
    private UNOController controller;
    private JPanel panel;
    private CountDownTimer timer;

    @BeforeEach
    void setUp() {
        challengeViewer = new ChallengeViewer();
        controller = UNOController.getInstance();
        panel = new JPanel();
        timer = new CountDownTimer(panel, () -> {});
        
        // Setup controller with test players
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new CPUPlayer("Player" + (i + 1)));
        }
        controller.getPlayerList().clear();
        controller.getPlayerList().addAll(players);
        controller.setCurrentPlayer(players.get(0));
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
    void testRedButtonAction() {
        // Setup
        challengeViewer.setController();
        challengeViewer.setPanel(panel);
        challengeViewer.setTimer(timer);
        challengeViewer.setChallenge();
        
        // Draw buttons
        challengeViewer.drawWindow(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics());
        
        // Get red button using reflection
        JButton redButton = getButtonFromViewer(challengeViewer, "redButton");
        assertNotNull(redButton, "Red button should exist");
        
        // Store initial hand sizes
        int initialHandSize = controller.getPlayerList().get(1).getHand().size();
        
        assertDoesNotThrow(() -> 
        challengeViewer.drawWindow(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics()),
        "Should handle redraw after button click safely"
    );
        // Click button
        assertDoesNotThrow(() -> redButton.doClick(), 
            "Button click should not throw exceptions");
        
        // Verify state
        assertFalse(challengeViewer.getIsChallenging(), 
            "Challenge state should be reset after button click");
        assertNull(getButtonFromViewer(challengeViewer, "redButton"), 
            "Red button should be removed after click");
        
        // Verify cards were drawn
        int newHandSize = controller.getPlayerList().get(1).getHand().size();
        assertEquals(4, newHandSize - initialHandSize, 
            "Next player should receive 4 cards");
        

    }
    
    @Test
    void testRedButtonAction_Player0() {
        // Setup
        challengeViewer.setController();
        challengeViewer.setPanel(panel);
        challengeViewer.setTimer(timer);
        
        // Get the player list
        List<Player> players = new ArrayList<>(controller.getPlayerList());
        
        // Set the current player to the last player
        Player lastPlayer = players.get(players.size() - 1);
        controller.setCurrentPlayer(lastPlayer);
        
        // Store initial hand size of player 0
        Player player0 = players.get(0);
        int initialHandSize = player0.getHand().size();
        
        // Start challenge
        challengeViewer.setChallenge();
        
        // Draw buttons
        challengeViewer.drawWindow(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics());
        
        // Get red button
        JButton redButton = getButtonFromViewer(challengeViewer, "redButton");
        assertNotNull(redButton, "Red button should exist");
        
        // Click button
        redButton.doClick();
        
        // Verify state
        assertFalse(challengeViewer.getIsChallenging(), 
            "Challenge state should be reset after button click");
        assertNull(getButtonFromViewer(challengeViewer, "redButton"), 
            "Red button should be removed after click");
        
        // Verify cards were drawn to player 0
        int newHandSize = player0.getHand().size();
        assertEquals(4, newHandSize - initialHandSize, 
            "Player 0 should receive 4 cards when they are next");
    }


    

    @Test
    void testGreenButtonAction() {
        // Setup
        challengeViewer.setController();
        challengeViewer.setPanel(panel);
        challengeViewer.setTimer(timer);
        challengeViewer.setChallenge();
        
        // Draw buttons
        challengeViewer.drawWindow(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics());
        
        // Get green button using reflection
        JButton greenButton = getButtonFromViewer(challengeViewer, "greenButton");
        assertNotNull(greenButton, "Green button should exist");
        
        // Click button
        assertDoesNotThrow(() -> greenButton.doClick(), 
            "Button click should not throw exceptions");
        
        // Verify state
        assertFalse(challengeViewer.getIsChallenging(), 
            "Challenge state should be reset after button click");
        assertNull(getButtonFromViewer(challengeViewer, "greenButton"), 
            "Green button should be removed after click");
    }
    




    @Test
    void testDrawWindowWhenNotChallenging() {
        // Setup
        challengeViewer.setController();
        challengeViewer.setPanel(panel);
        
        // Don't call setChallenge()
        challengeViewer.drawWindow(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics());
        
        // Verify no buttons were added
        assertNull(getButtonFromViewer(challengeViewer, "redButton"), 
            "No buttons should be added when not challenging");
    }

    // Helper method to get button from ChallengeViewer using reflection
    private JButton getButtonFromViewer(ChallengeViewer viewer, String fieldName) {
        try {
            Field field = ChallengeViewer.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (JButton) field.get(viewer);
        } catch (Exception e) {
            return null;
        }
    }
}