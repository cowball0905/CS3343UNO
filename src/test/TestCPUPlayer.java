package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;
import java.util.ArrayList;

public class TestCPUPlayer {
    private CPUPlayer cpuPlayer;
    private HumanPlayer humanPlayer;
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        controller = UNOController.getInstance();
        cpuPlayer = new CPUPlayer("CPU1");
        cpuPlayer.setController(controller);
        humanPlayer = new HumanPlayer("Player1");
        humanPlayer.setController(controller);
    }
    
    @Test
    public void testGetName() {
        assertEquals("CPU1", cpuPlayer.getName());
    }
    
    @Test
    public void testDrawCard() {
        int initialSize = cpuPlayer.getHand().size();
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        assertEquals(initialSize + 1, cpuPlayer.getHand().size());
    }
    
    @Test
    public void testShoutUno() {
        // Setup
        cpuPlayer.getHand().clear();
        cpuPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        
        // Test case 1: Shout UNO with one card
        cpuPlayer.setIsShout(false);
        String result = cpuPlayer.shoutUno();
        assertNull(result, "shoutUno() should return null for CPU players");
        assertTrue(cpuPlayer.getIsShout(), "isShout should be true after shouting UNO");
        
        // Test case 2: Already shouted UNO
        result = cpuPlayer.shoutUno();
        assertNull(result, "shoutUno() should still return null when already shouted");
        
        // Test case 3: More than one card
        cpuPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        cpuPlayer.setIsShout(false);
        result = cpuPlayer.shoutUno();
        assertNull(result, "shoutUno() should return null even with more than one card");
    }

    @Test
    public void testPlayCard() {
        // Setup
        cpuPlayer.getHand().clear();
        Card card = new NumberCard(Color.Red, 5, true);
        cpuPlayer.getHand().add(card);
        
        // Test playing a card
        cpuPlayer.playCard(card);
        assertFalse(cpuPlayer.getHand().contains(card), "Card should be removed from hand after playing");
        
        // Test UNO shout check
        Card secondCard = new NumberCard(Color.Blue, 7, true);
        cpuPlayer.getHand().add(secondCard);
        cpuPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        cpuPlayer.playCard(secondCard); // Now has 1 card left
        assertFalse(cpuPlayer.getIsShout(), "Should not automatically shout UNO, should be handled by timer");
    }
    
    @Test
    public void testCatchForgotShout() {
        // Setup
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.setIsShout(false);
        
        // Test successful catch
        cpuPlayer.catchForgotShout(targetPlayer);
        // Can't verify exact card count since it depends on the controller's deck state
        assertTrue(targetPlayer.getHand().size() > 1, "Target should have received penalty cards");
        
        // Test failed catch (player already shouted)
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Blue, 7, true));
        targetPlayer.setIsShout(true);
        int initialHandSize = targetPlayer.getHand().size();
        cpuPlayer.catchForgotShout(targetPlayer);
        assertEquals(initialHandSize, targetPlayer.getHand().size(), 
                   "Should not add penalty cards if player already shouted");
    }
    
    @Test
    public void testChallengeDrawFour() {
        // Setup
        Player targetPlayer = new CPUPlayer("Target");
        
        // Test successful challenge (target has playable card)
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        
        // Save initial state
        int initialTargetHandSize = targetPlayer.getHand().size();
        int initialHumanHandSize = humanPlayer.getHand().size();
        
        humanPlayer.challengeDrawFour(targetPlayer);
        
        // Verify the challenge outcome
        // Either target gets 4 cards or human gets 6 cards, but can't predict which due to random behavior
        boolean validOutcome = 
            (targetPlayer.getHand().size() == initialTargetHandSize + 4) ||
            (humanPlayer.getHand().size() == initialHumanHandSize + 6);
        
        assertTrue(validOutcome, "Challenge should result in either target getting 4 cards or human getting 6 cards");
    }
    
    @Test
    public void testChooseColor() {
        // Setup
        cpuPlayer.getHand().clear();
        cpuPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        cpuPlayer.getHand().add(new NumberCard(Color.Red, 7, true));
        cpuPlayer.getHand().add(new NumberCard(Color.Blue, 2, true));
        
        // Should choose Red as it's the most common color
        Color chosenColor = cpuPlayer.chooseColor();
        assertEquals(Color.Red, chosenColor, "Should choose the most common color in hand");
    }
}