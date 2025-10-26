package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;

public class TestNumberCard {
    private UNOController controller;
    private ArrayList<Player> players;
    
    @BeforeEach
    void setUp() {
        // Get the controller instance
        controller = UNOController.getInstance();
        
        // Get the player list
        players = controller.getPlayerList();
        players.clear();
        
        // Add test players
        for (int i = 0; i < 4; i++) {
            CPUPlayer player = new CPUPlayer("Player" + (i + 1));
            player.setController(controller);
            players.add(player);
        }
        
        // Set the first player as current
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);
        
        // Play a starting card
        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);
    }
    
    @Test
    void testNumberCardInitialization() {
        // Test creating a red 5 card
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertNotNull(card, "NumberCard should be initialized");
        assertEquals(Color.Red, card.getColor(), "Card color should be Red");
        assertEquals(5, card.getValue(), "Card value should be 5");
        assertEquals(Type.Number, card.getType(), "Card type should be Number");
    }
    
    @Test
    void testNumberCardToString() {
        // Test with different numbers and colors
        NumberCard red5 = new NumberCard(Color.Red, 5, true);
        assertEquals("Red 5", red5.toString(), "Should return 'Red 5'");
        
        NumberCard blue0 = new NumberCard(Color.Blue, 0, true);
        assertEquals("Blue 0", blue0.toString(), "Should return 'Blue 0'");
    }
    
    @Test
    void testNumberCardFunction() {
        // Set current player to the first player
        Player initialPlayer = players.get(0);
        controller.setCurrentPlayer(initialPlayer);
        
        // Create and play a number card
        NumberCard numberCard = new NumberCard(Color.Red, 7, true);
        numberCard.cardFunction();
        
        // The next player should be the immediate next player (index 1)
        Player expectedNextPlayer = players.get(1);
        Player actualNextPlayer = controller.getCurrentPlayer();
        
        assertEquals(expectedNextPlayer.getName(), actualNextPlayer.getName(),
            String.format("Expected next player to be %s but was %s",
                expectedNextPlayer.getName(), actualNextPlayer.getName()));
    }
    
    @Test
    void testNumberCardFunctionWithWrapAround() {
        // Set current player to the last player in the list
        Player lastPlayer = players.get(players.size() - 1);
        controller.setCurrentPlayer(lastPlayer);
        
        // Create and play a number card (should wrap around to the beginning)
        NumberCard numberCard = new NumberCard(Color.Red, 3, true);
        numberCard.cardFunction();
        
        // Should wrap around to the first player (index 0)
        Player expectedNextPlayer = players.get(0);
        Player actualNextPlayer = controller.getCurrentPlayer();
        
        assertEquals(expectedNextPlayer.getName(), actualNextPlayer.getName(),
            "Should wrap around to the beginning of the player list");
    }
}
