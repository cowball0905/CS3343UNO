package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;
import java.util.ArrayList;

public class TestHumanPlayer {
    private HumanPlayer humanPlayer;
    private UNOController controller;
    private CPUPlayer cpuPlayer;

    
    @BeforeEach
    public void setUp() {
        controller = UNOController.getInstance();
        humanPlayer = new HumanPlayer("Player1");
        humanPlayer.setController(controller);
        cpuPlayer = new CPUPlayer("CPU1");
        cpuPlayer.setController(controller);
    }
    
    @Test
    public void testGetName() {
        assertEquals("Player1", humanPlayer.getName());
    }
    
    @Test
    public void testDrawCard() {
        int initialSize = humanPlayer.getHand().size();
        humanPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        assertEquals(initialSize + 1, humanPlayer.getHand().size());
    }
    
    @Test
    public void testShoutUno() {
        // Get the controller instance
        UNOController controller = UNOController.getInstance();
        
        // Get the first player (should be HumanPlayer)
        HumanPlayer humanPlayer = (HumanPlayer) controller.getPlayerList().get(0);
        // Get second player (should be CPU)
        CPUPlayer cpuPlayer = (CPUPlayer) controller.getPlayerList().get(1);
        
        // Clear the played cards and add a starting card
        controller.getTopCard(); // This will initialize the first card
        
        // Clear and set up the hand
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        humanPlayer.setIsShout(false);
        
        // Set current player to CPU (human is not current player)
        controller.setCurrentPlayer(cpuPlayer);
        
        // Test case 1: Non-current player with one card
        String result = humanPlayer.shoutUno();
        assertNull(result, "Should return null for non-current player with one card");
        assertTrue(humanPlayer.getIsShout(), "isShout should be true after shouting UNO");
        
        // Test case 2: Non-current player with more than one card
        humanPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        humanPlayer.setIsShout(false);
        result = humanPlayer.shoutUno();
        assertEquals("You have more than 1 card!", result, 
            "Should return error when more than one card");
        
        // Reset state
        humanPlayer.getHand().clear();
        humanPlayer.setIsShout(false);
        
        // Set human as current player
        controller.setCurrentPlayer(humanPlayer);
        
        // Add a playable card to human's hand (matching the top card color)
        humanPlayer.getHand().add(new NumberCard(Color.Red, 7, true));
        
        // Test case 3: Current player with one playable card
        result = humanPlayer.shoutUno();
        assertNull(result, "Should return null for current player with one playable card");
        assertTrue(humanPlayer.getIsShout(), "isShout should be true after shouting UNO");
        
        // Test case 4: Already shouted UNO
        result = humanPlayer.shoutUno();
        assertEquals("You shouted UNO already!", result, 
            "Should return error message when already shouted");
        
        // Reset state
        humanPlayer.setIsShout(false);
        humanPlayer.getHand().clear();
        
        // Test case 5: Current player with one unplayable card
        // Set a different top card color
        controller.getTopCard().setColor(Color.Blue);
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        result = humanPlayer.shoutUno();
        assertEquals("You have no playable card!", result, 
            "Should return error when no playable cards");
    }

    @Test
    public void testPlayCard() {
        // Setup
        humanPlayer.getHand().clear();
        Card card = new NumberCard(Color.Red, 5, true);
        humanPlayer.getHand().add(card);
        
        // Test playing a card
        humanPlayer.playCard(card);
        assertFalse(humanPlayer.getHand().contains(card), "Card should be removed from hand after playing");
        
        // Test UNO shout check
        Card secondCard = new NumberCard(Color.Blue, 7, true);
        humanPlayer.getHand().add(secondCard);
        humanPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        humanPlayer.playCard(secondCard); // Now has 1 card left
        // Should have scheduled a check for UNO shout
    }
    
    @Test
    public void testCatchForgotShout() {
        // Setup
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.setIsShout(false);
        
        // Test successful catch
        humanPlayer.catchForgotShout(targetPlayer);
        assertEquals(3, targetPlayer.getHand().size(), "Should have 3 cards after penalty (1 original + 2 penalty)");
        
        // Test failed catch (player already shouted)
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Blue, 7, true));
        targetPlayer.setIsShout(true);
        humanPlayer.catchForgotShout(targetPlayer);
        assertEquals(1, targetPlayer.getHand().size(), "Should not add penalty cards if player already shouted");
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
    int initialCpuHandSize = cpuPlayer.getHand().size();
    
    // We'll test the random behavior by running multiple attempts
    boolean challengeSucceeded = false;
    boolean challengeFailed = false;
    
    // Try multiple times to account for random behavior
    for (int i = 0; i < 10; i++) {
        // Reset target player's hand
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        
        cpuPlayer.challengeDrawFour(targetPlayer);
        
        if (targetPlayer.getHand().size() > initialTargetHandSize) {
            // Challenge succeeded - target got penalty cards
            assertEquals(initialTargetHandSize + 4, targetPlayer.getHand().size(), 
                "Target should get 4 penalty cards for failed challenge");
            challengeSucceeded = true;
            break;
        } else if (cpuPlayer.getHand().size() > initialCpuHandSize) {
            // Challenge failed - CPU got penalty cards
            assertEquals(initialCpuHandSize + 6, cpuPlayer.getHand().size(), 
                "CPU should get 6 penalty cards for failed challenge");
            challengeFailed = true;
            break;
        }
    }
    
    // Verify that at least one of the outcomes occurred
    assertTrue(challengeSucceeded || challengeFailed, 
        "Challenge should have either succeeded or failed after multiple attempts");
    
    // Test failed challenge (target only has wild cards)
    targetPlayer.getHand().clear();
    targetPlayer.getHand().add(new WildCard(true)); // Only has wild cards
    initialCpuHandSize = cpuPlayer.getHand().size();
    
    cpuPlayer.challengeDrawFour(targetPlayer);
    
    // In this case, the CPU should always get 6 penalty cards
    assertEquals(initialCpuHandSize + 6, cpuPlayer.getHand().size(), 
        "CPU should get 6 penalty cards when challenging with only wild cards");
}

}