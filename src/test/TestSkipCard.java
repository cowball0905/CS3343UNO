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
        controller = UNOController.getInstance();

        controller.setViewers();
        controller.setPlayers();
        
        players = controller.getPlayerList();
        players.clear();
        
        for (int i = 0; i < 4; i++) {
            CPUPlayer player = new CPUPlayer("Player" + (i + 1));
            player.setController();
            players.add(player);
        }
        
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);
        
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
    void testSkipCardInitializationType() {
        SkipCard skipCard = new SkipCard(Color.Blue, true);
        
        assertEquals(Type.Skip, skipCard.getType(), "Card type should be Skip");
    }
    
    @Test
    void testSkipCardInitializationColour() {
        SkipCard skipCard = new SkipCard(Color.Blue, true);
        
        assertEquals(Color.Blue, skipCard.getColor(), "Card color should be Blue");
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
        Player lastPlayer = players.get(players.size() - 1);
        controller.setCurrentPlayer(lastPlayer);
        
        SkipCard skipCard = new SkipCard(Color.Red, true);
        skipCard.cardFunction(controller);
        
        Player expectedNextPlayer = players.get(1);
        Player actualNextPlayer = controller.getCurrentPlayer();
        
        assertEquals(expectedNextPlayer, actualNextPlayer);
    }
}
