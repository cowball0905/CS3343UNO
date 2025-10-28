package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.DisplayName;

import model.*;
import controller.UNOController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JPanel;

public class TestCard {
    private Card testCard;
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        // Using NumberCard as a concrete implementation for testing common Card methods
        testCard = new NumberCard(Color.Red, 5, true);
        ArrayList<Player> players = new ArrayList<>();
        
        // Set up controller with test players
        controller = UNOController.getInstance();
        controller.setViewers();
        controller.setPlayers();
        // Setup controller with test players
        for (int i = 0; i < 4; i++) {
            players.add(new CPUPlayer("Player" + (i + 1)));
        }
        controller.getPlayerList().clear();
        controller.getPlayerList().addAll(players);
        controller.setCurrentPlayer(players.get(0));
    }
    
    @AfterEach
	public void tearDown() {
		UNOController.resetInstance();
	}
    
    @Test
    public void testSetAndGetPosition() {
        testCard.setPosition(100, 200);
        assertEquals(100, testCard.getX(), "X position should be set correctly");
        assertEquals(200, testCard.getY(), "Y position should be set correctly");
    }
    
    @Test
    public void testSetAndGetSize() {
        testCard.setSize(100, 150);
        assertEquals(100, testCard.getWidth(), "Width should be set correctly");
        assertEquals(150, testCard.getHeight(), "Height should be set correctly");
    }
    
    @Test
    public void testCardSelection() {
        testCard.setCardSelected(true);
        assertTrue(testCard.isCardSelected(), "Card should be selected after setCardSelected(true)");
        
        // Verify Y position changed to 410 when selected
        assertEquals(410, testCard.getY(), "Y position should be 410 when selected");
        
        // Deselect the card
        testCard.setCardSelected(false);
        assertFalse(testCard.isCardSelected(), "Card should not be selected after deselecting");
        
        // Verify Y position changed back to 450 when deselected
        assertEquals(450, testCard.getY(), "Y position should be 450 when not selected");
    }
    
	@Test
	public void testSetRotation() {
	    // Test setting rotation to 90 degrees
	    testCard.setRotation(90);
	    assertEquals(90, testCard.getRotationAngle());
	    
	    // Test setting rotation to 180 degrees
	    testCard.setRotation(180);
	    assertEquals(180, testCard.getRotationAngle());
	    
	    // Test setting rotation to 270 degrees
	    testCard.setRotation(270);
	    assertEquals(270, testCard.getRotationAngle());
	    
	}

	@Test
	public void testRotationBoundaries() {
	    // Test minimum boundary
	    testCard.setRotation(0);
	    assertEquals(0, testCard.getRotationAngle(), 0.01, "Rotation should handle 0 degrees");
	    
	}

	@Test
	public void testRotationPersistence() {
	    // Test rotation to 90 degrees
	    testCard.setRotation(90);
	    assertEquals(90, testCard.getRotationAngle(), 0.01, "Rotation should be set to 90 degrees");
	    assertTrue(testCard.isRotated(), "Card should be marked as rotated at 90 degrees");
	
	    // Test rotation to 180 degrees
	    testCard.setRotation(180);
	    assertEquals(180, testCard.getRotationAngle(), 0.01, "Rotation should be set to 180 degrees");
	    assertTrue(testCard.isRotated(), "Card should not be marked as rotated at 180 degrees");
	
	    // Test rotation to 270 degrees
	    testCard.setRotation(270);
	    assertEquals(270, testCard.getRotationAngle(), 0.01, "Rotation should be set to 270 degrees");
	    assertTrue(testCard.isRotated(), "Card should be marked as rotated at 270 degrees");
	
	    // Test rotation to 0 degrees
	    testCard.setRotation(0);
	    assertEquals(0, testCard.getRotationAngle(), 0.01, "Rotation should be set to 0 degrees");
	    assertFalse(testCard.isRotated(), "Card should not be marked as rotated at 0 degrees");
	}
	
    @Test
    public void testRevealedState() {
        // Initial state (set to revealed in constructor)
        assertTrue(testCard.isRevealed(), "Card should be revealed initially");
        
        // Set to not revealed
        testCard.setRevealed(false);
        assertFalse(testCard.isRevealed(), "Card should not be revealed after setting to false");
    }
    
    @Test
    public void testGetImage() {
        // The image might be loaded in the constructor or when first accessed
        // So we can't assume it's null
        // Just verify the method doesn't throw an exception
        assertDoesNotThrow(() -> testCard.getImage(), "getImage() should not throw an exception");
    }
    

    @Test
    public void testGetType() {
        assertEquals(Type.Number, testCard.getType(), "Card type should be Number");
    }
    
    @Test
    @DisplayName("Test card image loading through constructor")
    public void testCardImageLoading() {
        // Test that the card loads its image in the constructor
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        // The card should have loaded its image in the constructor
        assertNotNull(card.getImage(), "Card image should be loaded through constructor");
        
        // Verify the image has the expected dimensions after scaling
        assertEquals(80, card.getImage().getWidth(), "Image width should be scaled to 80");
        assertEquals(120, card.getImage().getHeight(), "Image height should be scaled to 120");
    }
    
    @Test
    @DisplayName("Test card with revealed state false")
    public void testCardWithHiddenState() {
        // Create a card with isRevealed = false
        NumberCard card = new NumberCard(Color.Red, 5, false);
        
        // The card should still have an image (the back of the card)
        assertNotNull(card.getImage(), "Card should have a back image when not revealed");
    }
    
    @Test
    @DisplayName("Test card image changes with reveal state")
    public void testCardImageChangesWithRevealState() {
        // Create a card with isRevealed = false
        NumberCard card = new NumberCard(Color.Red, 5, false);
        BufferedImage backImage = card.getImage();
        
        // Change to revealed state
        card.setRevealed(true);
        
        // The image should now be different (front of the card)
        assertNotEquals(backImage, card.getImage(), "Card image should change when revealed state changes");
    }
    
    @Test
    @DisplayName("Test drawing a card at specific position")
    public void testDrawCardAtPosition() {
        // Create a test card
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        // Create a test image to draw on
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = testImage.createGraphics();
        
        // Draw the card at position (50, 50)
        int x = 50;
        int y = 50;
        card.draw(g2d, x, y);
        
        // Verify the card's position was updated
        assertEquals(x, card.getX(), "Card's x position should be updated");
        assertEquals(y, card.getY(), "Card's y position should be updated");
        
        // Test drawing with negative coordinates (should still work)
        card.draw(g2d, -10, -20);
        assertEquals(-10, card.getX(), "Card's x position should handle negative values");
        assertEquals(-20, card.getY(), "Card's y position should handle negative values");
        
        g2d.dispose();
    }
    
    @Test
    @DisplayName("Test drawing with null graphics throws NullPointerException")
    public void testDrawWithNullGraphics() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        assertThrows(NullPointerException.class, 
            () -> card.draw(null, 0, 0), 
            "Drawing with null graphics should throw NullPointerException");
    }
    
    @Test
    @DisplayName("Test card revealed state and image loading")
    public void testCardRevealedState() {
        // Create a face-down card
        NumberCard card = new NumberCard(Color.Red, 5, false);
        
        // Initially, the card should show the back image
        assertFalse(card.isRevealed(), "Card should be face down initially");
        
        // Reveal the card
        card.setRevealed(true);
        assertTrue(card.isRevealed(), "Card should be revealed after setRevealed(true)");
        
        // The card should now show the front image
        assertNotNull(card.getImage(), "Card should have an image after being revealed");
        
        // Hide the card again
        card.setRevealed(false);
        assertFalse(card.isRevealed(), "Card should be hidden after setRevealed(false)");
    }
    
    @Test
    @DisplayName("Test image loading with different reveal states")
    public void testImageLoadingWithRevealStates() {
        // Test with revealed card
        NumberCard revealedCard = new NumberCard(Color.Blue, 3, true);
        assertNotNull(revealedCard.getImage(), "Revealed card should have an image");
        
        // Test with hidden card
        NumberCard hiddenCard = new NumberCard(Color.Blue, 3, false);
        assertNotNull(hiddenCard.getImage(), "Hidden card should have a back image");
        
        // The images should be different
        assertNotEquals(revealedCard.getImage(), hiddenCard.getImage(),
                       "Revealed and hidden cards should have different images");
    }
    
    @Test
    void testCardFunctionalityWithController() {
        // This is a basic test - you might need to mock the controller
        // or set up a test environment for more complex scenarios
        UNOController controller = UNOController.getInstance();
        
        // Test with a WildCard
        WildCard wildCard = new WildCard(true);
        assertDoesNotThrow(() -> wildCard.cardFunction(controller),
            "Wild card function should execute without throwing exceptions");
            
        // Test with a WildDrawFourCard
        WildDrawFourCard wildDrawFourCard = new WildDrawFourCard(true);
        assertDoesNotThrow(() -> wildDrawFourCard.cardFunction(controller),
            "Wild Draw Four card function should execute without throwing exceptions");
    }
}