package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;

public class TestReverseCard {
    private UNOController controller;
    private ArrayList<Player> players;
	
    @BeforeEach
    void setUp() {
        // Get the controller instance
        controller = UNOController.getInstance();
        controller.setViewers();
        controller.setPlayers();
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
    void tearsDown() {
    	controller.resetInstance();
    }
    
    @Test
    void testReverseCardInitialization() {
        ReverseCard reverseCard = new ReverseCard(Color.Yellow, true);
        
        assertNotNull(reverseCard, "ReverseCard should be initialized");
        assertEquals(Color.Yellow, reverseCard.getColor());
        assertEquals(Type.Reverse, reverseCard.getType());
    }
    
	@Test
	void testReverseCardFunction() {
	    // Set initial play direction (1 = clockwise)
	    controller.setPlayDirection(1);
	      
	    // Create and test the card
	    ReverseCard card = new ReverseCard(Color.Red, true);
	    card.cardFunction(controller);
	      
	    // Verify the play direction is reversed
	    assertEquals(-1, controller.getPlayDirection());
	}
}
