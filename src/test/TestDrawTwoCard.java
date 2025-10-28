package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;
import java.util.ArrayList;

public class TestDrawTwoCard {
    private DrawTwoCard drawTwoCard;
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
        
        controller.setIsFreezed(true);
        
        // Play a starting card
        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);
    }
    
    @AfterEach
    public void tearsDown() {
    	controller.resetInstance();
    }
    
    @Test
	void testToString() {
		drawTwoCard = new DrawTwoCard(Color.Blue, true);
		// DrawTwoCard toString should return correct format
		assertEquals("Blue Draw Two", drawTwoCard.toString());
	}
    
    @Test
    void testDrawTwoCardInitialization() {
        drawTwoCard = new DrawTwoCard(Color.Red, true);
        //DrawTwoCard color should be set
        assertEquals(Color.Red, drawTwoCard.getColor());
    }

    @Test
    void testDrawTwoCardRevealed() {
        drawTwoCard = new DrawTwoCard(Color.Red, true);
        //DrawTwoCard should be revealed
        assertTrue(drawTwoCard.isRevealed());
    }

    @Test
    void testDrawTwoCardType() {
        drawTwoCard = new DrawTwoCard(Color.Red, true);
        //"Card type should be DrawTwo"
        assertEquals(Type.DrawTwo, drawTwoCard.getType());
    }
    
    @Test
	void testDrawTwoForPlayerSize() {
      controller.setIsFreezed(true);
      Player cpuPlayer = players.get(1);
      Player targetPlayer = players.get(2);
      targetPlayer.getHand().clear();
      targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
      int initialHandSize = targetPlayer.getHand().size();
      
      DrawTwoCard drawTwoCard = new DrawTwoCard(Color.Red, true);
      controller.setCurrentPlayer(cpuPlayer);
      
      drawTwoCard.cardFunction(controller);
      
      assertEquals(initialHandSize + 2, targetPlayer.getHand().size());
	}
    
    @Test
	void testDrawTwoForPlayerTurn() {
      controller.setIsFreezed(true);
      Player cpuPlayer = players.get(1);
      Player targetPlayer = players.get(2);
      targetPlayer.getHand().clear();
      
      DrawTwoCard drawTwoCard = new DrawTwoCard(Color.Red, true);
      controller.setCurrentPlayer(cpuPlayer);
      
      drawTwoCard.cardFunction(controller);
      
      assertEquals(controller.getCurrentPlayer(), players.get(3));
	}
}