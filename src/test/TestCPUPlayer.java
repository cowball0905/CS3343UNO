package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.List;
import model.*;
import controller.UNOController;
import java.util.ArrayList;
import java.util.Arrays;

public class TestCPUPlayer {
    private CPUPlayer cpuPlayer;
    private UNOController controller;
    private ArrayList<Player> players;

    @BeforeEach
    public void setUp() {
        // Get the controller instance
        controller = UNOController.getInstance();
        
        // Initialize the game properly
        controller.startGame();  // This will call initializeGame() and set up the deck
        
        // Get the player list
        players = controller.getPlayerList();
        
        // Set the first player as current
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);
        
        // Play a starting card
        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);
        
        // Get the CPU player (should be at index 1 since index 0 is human)
        cpuPlayer = (CPUPlayer) players.get(1);
    }

    @Test
    public void testDrawCard() {
        int initialHandSize = cpuPlayer.getHand().size();
        Card card = new NumberCard(Color.Red, 5, true);
        
        cpuPlayer.drawCard(card);
        
        assertEquals(initialHandSize + 1, cpuPlayer.getHand().size());
        assertTrue(cpuPlayer.getHand().contains(card));
        assertFalse(cpuPlayer.getIsShout());
    }

    @Test
    public void testPlayCard() {
        // Add a card to CPU's hand
        Card card = new NumberCard(Color.Red, 5, true);
        cpuPlayer.drawCard(card);
        
        // Set up game state to allow playing the card
        controller.playCard(new NumberCard(Color.Red, 3, true)); // Set current color to Red
        
        cpuPlayer.playCard(card);
        assertFalse(cpuPlayer.getHand().contains(card));
        
        // Test else part of playCard (when hand size becomes 1 after playing a card)
        // Add two cards, play one to trigger the else condition
        cpuPlayer.getHand().clear();
        Card card1 = new NumberCard(Color.Red, 5, true);
        Card card2 = new NumberCard(Color.Blue, 3, true);
        cpuPlayer.drawCard(card1);
        cpuPlayer.drawCard(card2);
        
        // Play one card, leaving one in hand
        cpuPlayer.playCard(card1);
        
        // Verify UNO shout was handled (either shouted or not, but hand size should be 1)
        assertEquals(1, cpuPlayer.getHand().size());
    }

    @Test
    public void testShoutUno() {
        // Test successful UNO shout
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.shoutUno();
        assertTrue(cpuPlayer.getIsShout());
        
        // Test failed UNO shout (too many cards)
        cpuPlayer.drawCard(new NumberCard(Color.Blue, 3, true));
        cpuPlayer.setIsShout(false);
        cpuPlayer.shoutUno();
        assertFalse(cpuPlayer.getIsShout());
    }

    @Test
    public void testCatchForgotShout() {
        Player targetPlayer = players.get(2); // Another CPU player
        targetPlayer.getHand().clear();
        targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        int initialHandSize = targetPlayer.getHand().size();
        
        cpuPlayer.catchForgotShout(targetPlayer);
        assertEquals(initialHandSize + 2, targetPlayer.getHand().size());
    }

    @Test
    public void testChallengeDrawFour() {
        Player targetPlayer = players.get(0); // Human player
        targetPlayer.getHand().clear();
        
        // Test if part (when challenge is successful)
        // Add a playable card to target's hand
        targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        
        // Mock the random to always return true for the challenge
        try {
            // Use reflection to set the random to a predictable value
            Field randomField = CPUPlayer.class.getDeclaredField("random");
            randomField.setAccessible(true);
            java.util.Random mockRandom = new java.util.Random() {
                @Override
                public double nextDouble() {
                    return 0.3; // Always < 0.5 to trigger challenge
                }
            };
            randomField.set(cpuPlayer, mockRandom);
            
            // Set up current color to make the card playable
            controller.playCard(new NumberCard(Color.Red, 3, true));
            
            // Test the challenge
            int initialTargetHandSize = targetPlayer.getHand().size();
            cpuPlayer.challengeDrawFour(targetPlayer);
            
            // Verify challenge was successful (target should have 4 more cards)
            assertTrue(targetPlayer.getHand().size() >= initialTargetHandSize + 4, 
                "Target should have at least 4 more cards after failed challenge");
            
            // Test else part (when challenge is not made)
            // Reset random to not challenge
            mockRandom = new java.util.Random() {
                @Override
                public double nextDouble() {
                    return 0.6; // Always > 0.5 to skip challenge
                }
            };
            randomField.set(cpuPlayer, mockRandom);
            
            int initialCpuHandSize = cpuPlayer.getHand().size();
            cpuPlayer.challengeDrawFour(targetPlayer);
            
            // Verify no challenge was made (CPU should have 4 more cards)
            assertEquals(initialCpuHandSize + 4, cpuPlayer.getHand().size());
            
        } catch (Exception e) {
            fail("Test failed due to reflection error: " + e.getMessage());
        }
    }

    @Test
    public void testChooseCard() {
        // Add playable and non-playable cards
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.drawCard(new NumberCard(Color.Blue, 3, true));
        
        // Set current color to Red to make Red/5 playable
        controller.playCard(new NumberCard(Color.Red, 3, true));
        
        int initialHandSize = cpuPlayer.getHand().size();
        
        // This should choose a valid card to play
        cpuPlayer.chooseCard();
        
        // Verify a card was played (hand size decreased by 1)
        assertEquals(initialHandSize - 1, cpuPlayer.getHand().size());
        
        // Verify the played card is no longer in hand
        boolean redFiveStillInHand = cpuPlayer.getHand().stream()
            .anyMatch(card -> card.getColor() == Color.Red && 
                             card instanceof NumberCard && 
                             ((NumberCard) card).getValue() == 5);
        
        // Either the red 5 was played, or it's still in hand and blue 3 was played
        if (redFiveStillInHand) {
            // Blue 3 should have been played
            assertFalse(cpuPlayer.getHand().stream()
                .anyMatch(card -> card.getColor() == Color.Blue && 
                                 card instanceof NumberCard && 
                                 ((NumberCard) card).getValue() == 3));
        }
    }

    @Test
    public void testChooseColor() {
        // Add cards of different colors
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.drawCard(new NumberCard(Color.Red, 3, true));
        cpuPlayer.drawCard(new NumberCard(Color.Blue, 2, true));
        
        // Should choose Red as it's the most common
        Color chosenColor = cpuPlayer.chooseColor();
        assertEquals(Color.Red, chosenColor);
    }


    @Test
    public void testPlayCard_UnoShoutProbability() {
        // Test UNO shout probability when playing down to one card
        CPUPlayer testPlayer = (CPUPlayer) players.get(2); // Use a different player
        testPlayer.getHand().clear();
        testPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        testPlayer.drawCard(new NumberCard(Color.Blue, 3, true));
        
        // Play one card, leaving one in hand
        testPlayer.playCard(testPlayer.getHand().get(0));
        
        // Either the player should have shouted UNO or not
        // We can't predict the random outcome, but we can verify hand size
        assertEquals(1, testPlayer.getHand().size());
    }
    
    
    
}