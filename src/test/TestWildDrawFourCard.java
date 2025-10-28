package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Import Color from model package
import model.*;
import controller.UNOController;

import java.util.ArrayList;
import java.util.Arrays;

// Remove java.awt.* import to avoid conflict with model.Color

public class TestWildDrawFourCard {
    private WildDrawFourCard wildDrawFourCard;
    private UNOController controller;
    private ArrayList<Player> players;
    
    @BeforeEach
    public void setUp() {
        // Get the controller instance
        controller = UNOController.getInstance();
        
        // Clear any existing players first
        controller.getPlayerList().clear();
        
        // Add test players
        for (int i = 0; i < 4; i++) {
            CPUPlayer player = new CPUPlayer("Player" + (i + 1));
            player.setController(controller);
            controller.getPlayerList().add(player);
        }
        
        // Now initialize the game with the players
        controller.startGame();
        
        // Get the player list
        players = controller.getPlayerList();
        
        // Set the first player as current
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);
        
        // Play a starting card
        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);
    }
    
    @AfterEach
	public void tearsDown() {
		controller.resetInstance();
	}
    
    @Test
	void testWildDrawFourCardToString() {
		wildDrawFourCard = new WildDrawFourCard(true);
		assertEquals("Wild Draw Four", wildDrawFourCard.toString());
	}
    
    
    @Test
    void testWildDrawFourCardInitialization() {
        wildDrawFourCard = new WildDrawFourCard(true);
        assertNotNull(wildDrawFourCard, "WildDrawFourCard should be initialized");
        assertNull(wildDrawFourCard.getColor(), "WildDrawFourCard color should be null initially");
        assertTrue(wildDrawFourCard.isRevealed(), "WildDrawFourCard should be revealed");
        assertEquals(Type.WildDrawFour, wildDrawFourCard.getType(), 
            "Card type should be WildDrawFour");
    }

    
	@Test
	public void testWildDrawFourCardFunction_challenge() {
	    // Set up players with index 0 as CPU to test challenge flow
	
	    
	    // Set current player to cpuPlayer2 (index 1)
	    controller.setCurrentPlayer(players.get(2));
	
	    WildDrawFourCard wildDrawFourCard = new WildDrawFourCard(true);
	    players.get(2).drawCard(wildDrawFourCard);
	    
	    // Play the Wild Draw Four card
	    wildDrawFourCard.cardFunction(controller);
	    
	    // Verify color was set (CPU should choose a color)
	    assertNotNull(wildDrawFourCard.getColor(), 
	        "Wild Draw Four card should have a color after being played by CPU");
	    
	    // The next player (index 0) should be challenged
	    // The challenge will be handled by the UI in the actual game
	    assertTrue(controller.getChallengeViewer() != null || 
	              controller.getCurrentPlayer() == players.get(1),
	        "Next player should be challenged or set as current player");
	}
	
	
	@Test
	public void testWildDrawFourCardFunction_noChallenge() {
	    // Reset controller state
	    controller.getPlayerList().clear();
	    
	    // Create test players - using only CPU players
	    CPUPlayer cpuPlayer1 = new CPUPlayer("CPU1");
	    CPUPlayer cpuPlayer2 = new CPUPlayer("CPU2");
	    
	    // Add players to the game
	    controller.getPlayerList().add(cpuPlayer1);
	    controller.getPlayerList().add(cpuPlayer2);
	    
	    // Set current player to cpuPlayer2 (index 1) to trigger the else branch
	    controller.setCurrentPlayer(cpuPlayer2);
	    
	    // Add cards to CPU player's hand to ensure chooseColor() has options
	    cpuPlayer2.drawCard(new NumberCard(Color.Red, 5, true));
	    cpuPlayer2.drawCard(new NumberCard(Color.Blue, 3, true));
	    cpuPlayer2.drawCard(new NumberCard(Color.Green, 7, true));
	    WildDrawFourCard wildDrawFourCard = new WildDrawFourCard(true);
	    cpuPlayer2.drawCard(wildDrawFourCard);
	    
	    // Play the Wild Draw Four card
	    wildDrawFourCard.cardFunction(controller);
	    
	    // Verify the color was set (should be one of the colors in CPU2's hand)
	    assertNotNull(wildDrawFourCard.getColor(), 
	                "Wild Draw Four card should have a color after being played by CPU");
	    
	    // The color should be one of the colors in CPU2's hand (Red, Blue, or Green)
	    Color chosenColor = wildDrawFourCard.getColor();
	    assertEquals(Color.Red,chosenColor,
	              "Chosen color should be one of the colors in CPU's hand");
	    
	    // Verify the next player (cpuPlayer1) was challenged
	    // You might want to add a method in CPUPlayer to track if challengeDrawFour was called
	    // or verify the game state after the challenge
	}



	@Test
	public void testWildDrawFourCardFunctionWithPlayerIndex0() {
	    // Set up players
	    controller.getPlayerList().clear();
	    HumanPlayer humanPlayer = new HumanPlayer("Human");
	    CPUPlayer cpuPlayer1 = new CPUPlayer("CPU1");
	    controller.getPlayerList().add(humanPlayer);
	    controller.getPlayerList().add(cpuPlayer1);
	    
	    // Set current player to human (index 0)
	    controller.setCurrentPlayer(humanPlayer);
	    
	    // Create and add Wild Draw Four card to human's hand
	    WildDrawFourCard wildDrawFourCard = new WildDrawFourCard(true);
	    humanPlayer.drawCard(wildDrawFourCard);
	    
	    // Play the card
	    wildDrawFourCard.cardFunction(controller);
	    
	    // Verify the card is set in the controller's wild card viewer
	    assertSame(wildDrawFourCard, controller.getWildCardViewer().getCard(),
	        "Wild card should be set in the viewer");
	    
	    // Verify no color is set yet (should be set by UI)
	    assertNull(wildDrawFourCard.getColor(), 
	        "Color should not be set until player chooses");
	    
	    // Simulate color selection through the UI
	    wildDrawFourCard.setColor(Color.Red);
	    assertEquals(Color.Red, wildDrawFourCard.getColor(), 
	        "Color should be set after player selection");
	}


}
