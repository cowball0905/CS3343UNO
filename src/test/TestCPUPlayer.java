package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;
import java.util.ArrayList;

public class TestCPUPlayer {
    private CPUPlayer cpuPlayer;
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        controller = UNOController.getInstance();
        cpuPlayer = new CPUPlayer("CPU1");
        cpuPlayer.setController(controller);
    }
    
    @Test
    public void testShoutUno() {
        // Setup
        cpuPlayer.getHand().clear();
        Card card = new NumberCard(Color.Red, 5, true);
        cpuPlayer.getHand().add(card);
        
        // Test case 1: Current player with one card
        controller.setCurrentPlayer(cpuPlayer);
        assertNull(cpuPlayer.shoutUno(), "Current player with one card should return null");
        assertTrue(cpuPlayer.getIsShout(), "isShout should be true after shouting UNO");
        
        // Test case 2: Not current player with one card
        Player otherPlayer = new CPUPlayer("CPU2");
        controller.setCurrentPlayer(otherPlayer);
        cpuPlayer.getHand().clear();
        cpuPlayer.getHand().add(new NumberCard(Color.Blue, 7, true));
        cpuPlayer.setIsShout(false);  // Use this instead of cpuPlayer.isShout = false
        
        String result = cpuPlayer.shoutUno();
        assertNull(result, "Non-current player with one card should return null");
        assertTrue(cpuPlayer.getIsShout(), "isShout should be true after shouting UNO");
        
        // Test case 3: Already shouted UNO
        result = cpuPlayer.shoutUno();
        assertEquals("You shouted UNO already!", result, "Should return error message when already shouted");
        
        // Test case 4: More than 2 cards
        cpuPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        cpuPlayer.getHand().add(new NumberCard(Color.Yellow, 3, true));
        cpuPlayer.setIsShout(false);  // Use this instead of cpuPlayer.isShout = false
        result = cpuPlayer.shoutUno();
        assertEquals("You have more than 2 card!", result, "Should return error when more than 2 cards");
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
        // Should have scheduled a check for UNO shout
    }
    
    @Test
    public void testCatchForgotShout() {
        // Setup
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        cpuPlayer.setIsShout(false);  // Use this instead of cpuPlayer.isShout = false
        
        // Test successful catch
        cpuPlayer.catchForgotShout(targetPlayer);
        assertEquals(3, targetPlayer.getHand().size(), "Should have 3 cards after penalty (1 original + 2 penalty)");
        
        // Test failed catch (player already shouted)
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Blue, 7, true));
        targetPlayer.setIsShout(false);  // Use this instead of cpuPlayer.isShout = false
        cpuPlayer.catchForgotShout(targetPlayer);
        assertEquals(1, targetPlayer.getHand().size(), "Should not add penalty cards if player already shouted");
    }
    
    @Test
    public void testChallengeDrawFour() {
        // Setup
        Player targetPlayer = new CPUPlayer("Target");
        UNOController mockController = UNOController.getInstance();
        cpuPlayer.setController(mockController);
        
        // Test successful challenge
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true)); // Has playable card
        cpuPlayer.challengeDrawFour(targetPlayer);
        assertEquals(4, targetPlayer.getHand().size(), "Target should get 4 penalty cards for failed challenge");
        
        // Test failed challenge
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new WildCard(true)); // Only has wild cards
        int initialHandSize = cpuPlayer.getHand().size();
        cpuPlayer.challengeDrawFour(targetPlayer);
        assertEquals(initialHandSize + 6, cpuPlayer.getHand().size(), 
                   "Challenger should get 6 penalty cards for failed challenge");
    }
}