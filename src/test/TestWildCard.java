package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;

public class TestWildCard {
    private WildCard wildCard;
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
    void testWildCardInitialization() {
        WildCard wildCard = new WildCard(true);
        assertNotNull(wildCard, "WildCard should be initialized");
        assertNull(wildCard.getColor(), "WildCard color should be null initially");
        assertTrue(wildCard.isRevealed(), "WildCard should be revealed");
        assertEquals(Type.Wild, wildCard.getType(), "Card type should be Wild");
    }
    
  @Test
  void testWildCardFunction() {
      WildCard wildCard = new WildCard(true);
      
      // Test color setting
      wildCard.setColor(Color.Red);
      assertEquals(Color.Red, wildCard.getColor(), 
          "Wild card color should be settable");
      

  }
  
@Test
void testWildCardFunctionWithPlayerIndex0() {
    // Set current player to index 0 (human player)
    controller.setCurrentPlayer(controller.getPlayerList().get(0));
    
    // Create the wild card
    WildCard wildCard = new WildCard(true);
    
    // When played by human (index 0), it should open WildCardViewer
    wildCard.cardFunction();
    
    // Since it's a human player, the color should be set through the UI
    // We'll manually set a color for testing purposes
    wildCard.setColor(Color.Red);
    
    // Verify the color was set
    assertNotNull(wildCard.getColor(), 
        "Wild card should have a color after being set");
        
    // Now test with CPU player (index 1)
    controller.setCurrentPlayer(controller.getPlayerList().get(1));
    
    // Create a new wild card for CPU
    WildCard cpuWildCard = new WildCard(true);
    
    wildCard.setColor(Color.Red);
    
    // Play the card
    cpuWildCard.cardFunction();
    
    // Verify the color was set by CPU
    assertNotNull(cpuWildCard.getColor(), 
        "CPU should set a color when playing wild card");
        
    // Verify the next player is set correctly (should be player at index 2)
    assertEquals(controller.getPlayerList().get(2), controller.getCurrentPlayer(),
        "Current player should be the next player after CPU plays wild card");
}

}
