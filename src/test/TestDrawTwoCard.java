package test;

import static org.junit.jupiter.api.Assertions.*;
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
        
        // Play a starting card
        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);
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
    void testDrawTwoCardFunction() {
        // Test with player at index 0
        testDrawTwoForPlayer(0);
        
        // Test with player at index 3 (last player)
        testDrawTwoForPlayer(3);
    }
    
  private void testDrawTwoForPlayer(int playerIndex) {
  // Reset to known state
  Player currentPlayer = controller.getPlayerList().get(playerIndex);
  controller.setCurrentPlayer(currentPlayer);
  controller.setPlayDirection(1);
  
  // Get the next player who should draw cards (one after current)
  int drawPlayerIndex = (playerIndex + 1) % controller.getPlayerList().size();
  Player drawPlayer = controller.getPlayerList().get(drawPlayerIndex);
  
  // Get the player who should be next after the draw (two after current)
  int nextPlayerIndex = (playerIndex + 2) % controller.getPlayerList().size();
  Player expectedNextPlayer = controller.getPlayerList().get(nextPlayerIndex);
  // Clear the draw player's hand to ensure a known state
  drawPlayer.getHand().clear();
  
  // Create and play the draw two card
  DrawTwoCard drawTwoCard = new DrawTwoCard(Color.Red, true);
  drawTwoCard.cardFunction();
  
  // Verify the current player is now the expected next player
  String expectedPlayerName = expectedNextPlayer.getName();
  String actualPlayerName = controller.getCurrentPlayer().getName();
  assertEquals(expectedPlayerName, actualPlayerName, 
      String.format("Current player should be %s after player %d plays DrawTwo, but was %s", 
          expectedPlayerName, playerIndex, actualPlayerName));
      
  // Verify the correct player had to draw 2 cards
  int expectedCards = 2;
  int actualCards = drawPlayer.getHand().size();
  assertEquals(expectedCards, actualCards, 
      String.format("Player %s should have drawn %d cards, but had %d", 
          drawPlayer.getName(),
          expectedCards, 
          actualCards));
}
}