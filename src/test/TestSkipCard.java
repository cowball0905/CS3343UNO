package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;

public class TestSkipCard {
    private UNOController controller;
    private ArrayList<Player> players;
    
    @BeforeEach
    public void setUp() {
        // Get the controller instance
        controller = UNOController.getInstance();
        
        // Get the player list
        players = controller.getPlayerList();
        players.clear();
        
        // Add test players
        for (int i = 0; i < 4; i++) {
            CPUPlayer player = new CPUPlayer("Player" + (i + 1));
            player.setController();
            players.add(player);
        }
        
        // Set the first player as current
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);
        
        // Play a starting card
        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);
    }
    
    @AfterEach
	public void tearDown() {
    	controller.resetInstance();
	}
    
    @Test
	void testSkipCardToString() {
		SkipCard skipCard = new SkipCard(Color.Green, true);
		assertEquals("Green Skip", skipCard.toString());
	}
    
    @Test
    void testSkipCardInitialization() {
        SkipCard skipCard = new SkipCard(Color.Blue, true);
        
        assertNotNull(skipCard, "SkipCard should be initialized");
        assertEquals(Color.Blue, skipCard.getColor(), "Card color should be Blue");
        assertEquals(Type.Skip, skipCard.getType(), "Card type should be Skip");
    }
    
    @Test
    void testSkipCardFunction() {
        // Set current player to the first player
        Player initialPlayer = players.get(0);
        controller.setCurrentPlayer(initialPlayer);
        
        // Create and play the skip card
        SkipCard skipCard = new SkipCard(Color.Red, true);
        skipCard.cardFunction(controller);
        
        // The next player should be the one after the next (skipping one player)
      Player expectedNextPlayer = controller.getPlayerList().get(2);
      Player actualNextPlayer = controller.getCurrentPlayer();
      assertEquals(expectedNextPlayer, actualNextPlayer);
    }
    
    @Test
    void testSkipCardWithWrapAround() {
        // Set current player to the last player in the list
        Player lastPlayer = players.get(players.size() - 1);
        controller.setCurrentPlayer(lastPlayer);
        
        // Create and play the skip card (should wrap around to the beginning)
        SkipCard skipCard = new SkipCard(Color.Red, true);
        skipCard.cardFunction(controller);
        
        // Should skip the first player (index 0) and go to the second player (index 1)
        Player expectedNextPlayer = players.get(1);
        Player actualNextPlayer = controller.getCurrentPlayer();
        
        assertEquals(expectedNextPlayer, actualNextPlayer);
    }
}
