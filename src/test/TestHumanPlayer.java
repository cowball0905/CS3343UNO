package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
        
        // Test drawing a valid card
        Card card = new NumberCard(Color.Red, 5, true);
        humanPlayer.drawCard(card);
        assertEquals(initialSize + 1, humanPlayer.getHand().size());
        assertTrue(humanPlayer.getHand().contains(card));
        
        // Test drawing null card - should be added to hand without throwing
        int sizeBeforeNull = humanPlayer.getHand().size();
        humanPlayer.drawCard(null);
        assertEquals(sizeBeforeNull + 1, humanPlayer.getHand().size(), 
            "Should add null to hand when null card is drawn");
        assertNull(humanPlayer.getHand().get(humanPlayer.getHand().size() - 1), 
            "Last card in hand should be null");
            
        // Test isShout is reset when drawing a card
        humanPlayer.setIsShout(true);
        humanPlayer.drawCard(new NumberCard(Color.Blue, 2, true));
        assertFalse(humanPlayer.getIsShout(), "isShout should be reset to false when drawing a card");
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
        
        // Clear the player's hand and add a playable card
        humanPlayer.getHand().clear();
        
        // Set the top card to a known value (Red 5)
        Card topCard = new NumberCard(Color.Red, 5, true);
        // Clear and set the played cards pile using reflection since PlayedCard is private
        try {
            java.lang.reflect.Field playedCardField = controller.getClass().getDeclaredField("PlayedCard");
            playedCardField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.List<Card> playedCards = (java.util.List<Card>) playedCardField.get(controller);
            playedCards.clear();
            playedCards.add(topCard);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test: " + e.getMessage(), e);
        }
        
        // Add a playable card to human's hand (matching the top card color)
        Card playableCard = new NumberCard(Color.Red, 7, true);
        humanPlayer.getHand().add(playableCard);
        
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
        // Set a different top card color using reflection
        try {
            // Get the played cards list using reflection
            java.lang.reflect.Field playedCardField = controller.getClass().getDeclaredField("PlayedCard");
            playedCardField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.List<Card> playedCards = (java.util.List<Card>) playedCardField.get(controller);
            
            // Create a new top card with a different color
            Card newTopCard = new NumberCard(Color.Blue, 3, true);
            playedCards.clear();
            playedCards.add(newTopCard);
            
            // Add a card to player's hand that doesn't match the top card
            humanPlayer.getHand().clear();
            humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
            
            result = humanPlayer.shoutUno();
            assertEquals("You have no playable card!", result, 
                "Should return error when no playable cards");
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test: " + e.getMessage(), e);
        }
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
        
        // Test UNO shout check when playing down to 1 card
        Card secondCard = new NumberCard(Color.Blue, 7, true);
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(secondCard);
        humanPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        humanPlayer.playCard(secondCard);
        
        // Verify UNO shout check was scheduled
        assertFalse(humanPlayer.getIsShout(), "Should not automatically set isShout to true");
        
        // Test playing a card not in hand
        humanPlayer.getHand().clear();
        Card notInHandCard = new NumberCard(Color.Yellow, 3, true);
        humanPlayer.playCard(notInHandCard);
        assertTrue(humanPlayer.getHand().isEmpty(), "Hand should remain unchanged when playing card not in hand");
    }
    
    @Test
    public void testCatchForgotShout() {
        // Setup
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.setController(controller);
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.setIsShout(false);
        
        // Test successful catch
        humanPlayer.catchForgotShout(targetPlayer);
        //Should have 3 cards after penalty (1 original + 2 penalty)
        assertEquals(3, targetPlayer.getHand().size());
        
        // Test failed catch (player already shouted)
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Blue, 7, true));
        targetPlayer.setIsShout(true);
        humanPlayer.catchForgotShout(targetPlayer);
        //Should not add penalty cards if player already shouted
        assertEquals(1, targetPlayer.getHand().size());
        
        // Test with target having more than 1 card (should not be penalized)
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.getHand().add(new NumberCard(Color.Green, 3, true));
        targetPlayer.setIsShout(false);
        humanPlayer.catchForgotShout(targetPlayer);
        //Should not add penalty cards if target has more than 1 card
        assertEquals(2, targetPlayer.getHand().size());
        
        // Test with null target player should throw NullPointerException
        assertThrows(NullPointerException.class, 
            () -> humanPlayer.catchForgotShout(null),
            "Should throw NullPointerException when target player is null");
    }
    
    @Test
    public void testChallengeDrawFour() {
        // Setup
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.setController(controller);
        
        // Test successful challenge (target has playable card)
        targetPlayer.getHand().clear();
        // Add a card that matches the current top card
        Card topCard = controller.getTopCard();
        Card matchingCard = new NumberCard(topCard.getColor(), 5, true);
        targetPlayer.getHand().add(matchingCard);
        
        // Save initial state
        int initialTargetHandSize = targetPlayer.getHand().size();
        
        // Perform the challenge
        humanPlayer.challengeDrawFour(targetPlayer);
        
        // Verify the target player got 4 penalty cards
        assertEquals(initialTargetHandSize + 4, targetPlayer.getHand().size(), 
            "Target should get 4 penalty cards when challenge succeeds");
        
        // Reset for next test
        targetPlayer.getHand().clear();
        targetPlayer.setController(controller);
        humanPlayer.getHand().clear();
        
        // Test failed challenge (target only has wild cards)
        targetPlayer.getHand().add(new WildCard(true));
        int initialHandSize = humanPlayer.getHand().size();
        
        // Perform the challenge
        humanPlayer.challengeDrawFour(targetPlayer);
        
        // Verify the challenger (human) got 6 penalty cards
        assertEquals(initialHandSize + 6, humanPlayer.getHand().size(), 
            "Human should get 6 penalty cards when challenge fails");
            
        // Test with null target player should throw NullPointerException
        assertThrows(NullPointerException.class, 
            () -> humanPlayer.challengeDrawFour(null),
            "Should throw NullPointerException when target player is null");
    }
    
    @Test
    public void testShoutUnoEdgeCases() {
        // Test with empty hand
        humanPlayer.getHand().clear();
        String result = humanPlayer.shoutUno();
        assertEquals("You have no playable card!", result, 
            "Should return error when hand is empty");
            
        // Test with 2 cards but no playable cards
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        humanPlayer.getHand().add(new NumberCard(Color.Red, 6, true));
        controller.getTopCard().setColor(Color.Blue); // Make sure cards don't match
        result = humanPlayer.shoutUno();
        assertEquals("You have more than 1 card!", result,
            "Should return error when more than 1 card in hand");
    }
}