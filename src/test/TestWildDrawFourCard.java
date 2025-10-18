package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;
import model.Color;  // Import Color from model package
import java.util.ArrayList;

// Remove java.awt.* import to avoid conflict with model.Color

public class TestWildDrawFourCard {
    private WildDrawFourCard wildDrawFourCard;
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        wildDrawFourCard = new WildDrawFourCard(true);
        controller = UNOController.getInstance();
        // Reset the controller state for each test
        controller.resetGame();
    }
    
    @Test
    void testWildDrawFourCardInitialization() {
        assertNotNull(wildDrawFourCard, "WildDrawFourCard should be initialized");
        assertNull(wildDrawFourCard.getColor(), "WildDrawFourCard color should be null initially");
        assertTrue(wildDrawFourCard.isRevealed(), "WildDrawFourCard should be revealed");
        assertEquals(Type.WildDrawFour, wildDrawFourCard.getType(), 
            "Card type should be WildDrawFour");
    }
    
    @Test
    public void testCardFunctionHumanPlayer() {
        // Set up players
        HumanPlayer humanPlayer = new HumanPlayer("TestPlayer");
        CPUPlayer cpuPlayer = new CPUPlayer("CPU1");
        controller.addPlayer(humanPlayer);
        controller.addPlayer(cpuPlayer);
        controller.setCurrentPlayer(humanPlayer);
        
        // Execute card function
        wildDrawFourCard.cardFunction();
        
        // Verify the wild card viewer is set up for color selection
        assertNotNull(controller.getWildCardViewer().getWildCard(), 
            "WildDrawFourCard should be set in the viewer");
        assertTrue(controller.getTurnTimer().isRunning(), 
            "Turn timer should be running after playing WildDrawFourCard");
    }
    
    @Test
    public void testCardFunctionCPUPlayer() {
        // Set up players
        CPUPlayer currentCPU = new CPUPlayer("CPU1");
        HumanPlayer nextPlayer = new HumanPlayer("Human");
        controller.addPlayer(currentCPU);
        controller.addPlayer(nextPlayer);
        controller.setCurrentPlayer(currentCPU);
        
        // Execute card function
        wildDrawFourCard.cardFunction();
        
        // Verify color is set and it's one of the valid colors
        assertNotNull(wildDrawFourCard.getColor(), 
            "Color should be set after CPU plays WildDrawFourCard");
        assertTrue(wildDrawFourCard.getColor() == Color.RED || 
                  wildDrawFourCard.getColor() == Color.BLUE ||
                  wildDrawFourCard.getColor() == Color.GREEN ||
                  wildDrawFourCard.getColor() == Color.YELLOW,
                  "Color should be one of the valid UNO colors");
    }
    
    @Test
    public void testCardFunctionChallengeScenario() {
        // Set up players - CPU plays, next is human who can challenge
        CPUPlayer cpuPlayer = new CPUPlayer("CPU1");
        HumanPlayer humanPlayer = new HumanPlayer("Human");
        controller.addPlayer(cpuPlayer);
        controller.addPlayer(humanPlayer);
        controller.setCurrentPlayer(cpuPlayer);
        
        // Execute card function
        wildDrawFourCard.cardFunction();
        
        // Verify the challenge viewer is set up for human player
        assertTrue(controller.getChallengeViewer().isVisible(),
            "Challenge viewer should be visible for human player");
    }
    
    @Test
    public void testSetColor() {
        // Test setting different colors
        wildDrawFourCard.setColor(Color.RED);
        assertEquals(Color.RED, wildDrawFourCard.getColor(), 
            "Color should be set to RED");
        
        wildDrawFourCard.setColor(Color.BLUE);
        assertEquals(Color.BLUE, wildDrawFourCard.getColor(), 
            "Color should be set to BLUE");
    }
    
    @Test
    public void testToString() {
        // Test before color is set
        assertEquals("Wild Draw Four Card", wildDrawFourCard.toString(), 
            "toString should return 'Wild Draw Four Card' when no color is set");
            
        // Test after color is set
        wildDrawFourCard.setColor(Color.RED);
        assertEquals("Wild Draw Four Card (RED)", wildDrawFourCard.toString(), 
            "toString should include color when set");
    }
}
