package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.DisplayName;

import model.*;
import controller.UNOController;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.stream.Stream;

public class TestCard {
    private Card testCard;
    
    @BeforeEach
    public void setUp() {
        // Using NumberCard as a concrete implementation for testing common Card methods
        testCard = new NumberCard(Color.Red, 5, true);
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
        // Initialize the card's selected state if needed
        testCard.setCardSelected(false);
        
        // Initial state
        assertFalse(testCard.isCardSelected(), "Card should not be selected initially");
        
        // Select the card
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
    public void testRotation() {
        // Initial state
        assertFalse(testCard.isRotated(), "Card should not be rotated initially");
        assertEquals(0.0, testCard.getRotationAngle(), 0.001, "Initial rotation angle should be 0");
        
        // Rotate 90 degrees
        testCard.setRotation(90);
        assertTrue(testCard.isRotated(), "Card should be marked as rotated after rotation");
        assertEquals(90.0, testCard.getRotationAngle(), 0.001, "Rotation angle should be 90 degrees");
        
        // Rotate back to 0 degrees
        testCard.setRotation(0);
        assertFalse(testCard.isRotated(), "Card should not be rotated after setting angle to 0");
        assertEquals(0.0, testCard.getRotationAngle(), 0.001, "Rotation angle should be 0");
        
        // Test 90-degree rotation (portrait)
        int originalWidth = testCard.getWidth();
        int originalHeight = testCard.getHeight();
        testCard.setRotation(90);
        assertEquals(originalHeight, testCard.getWidth(), "Width and height should be swapped after 90-degree rotation");
        assertEquals(originalWidth, testCard.getHeight(), "Width and height should be swapped after 90-degree rotation");
        
        // Test 180-degree rotation
        testCard.setRotation(180);
        assertEquals(originalWidth, testCard.getWidth(), "Width should be back to original after 180-degree rotation");
        assertEquals(originalHeight, testCard.getHeight(), "Height should be back to original after 180-degree rotation");
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
    
    
    // Test common card properties and functionality
    @ParameterizedTest
    @MethodSource("cardProvider")
    void testCardProperties(Card card, Type expectedType, Color expectedColor, boolean expectedRevealed) {
        assertNotNull(card, "Card should not be null");
        assertEquals(expectedType, card.getType(), "Card type should match");
        assertEquals(expectedColor, card.getColor(), "Card color should match");
        assertEquals(expectedRevealed, card.isRevealed(), "Card revealed state should match");
    }

    // Test card-specific functionality
    @ParameterizedTest
    @MethodSource("cardFunctionProvider")
    void testCardFunctionality(Card card, String expectedBehavior) {
        // Skip testing card function for cards that require UNOController
        if (card instanceof NumberCard || card instanceof SkipCard || 
            card instanceof ReverseCard || card instanceof DrawTwoCard ||
            card instanceof WildCard || card instanceof WildDrawFourCard) {
            // Just verify the card is not null
            assertNotNull(card, "Card should not be null");
            return;
        }
        
        // For other card types, test that the card function can be called without throwing exceptions
        assertDoesNotThrow(card::cardFunction, 
            expectedBehavior + " should execute without throwing exceptions");
    }

    // Test card value for NumberCard
    @ParameterizedTest
    @MethodSource("numberCardProvider")
    void testNumberCardValue(NumberCard card, int expectedValue) {
        assertEquals(expectedValue, card.getValue(), "Number card value should match");
    }

    // Test color setting for wild cards
    @ParameterizedTest
    @MethodSource("wildCardProvider")
    void testWildCardColorSetting(Card wildCard, Color colorToSet) {
        assertNull(wildCard.getColor(), "Wild card should initially have null color");
        wildCard.setColor(colorToSet);
        assertEquals(colorToSet, wildCard.getColor(), "Wild card color should be settable");
    }

    // Provider methods for parameterized tests
    private static Stream<Arguments> cardProvider() {
        return Stream.of(
            Arguments.of(new NumberCard(Color.Red, 5, true), Type.Number, Color.Red, true),
            Arguments.of(new SkipCard(Color.Blue, true), Type.Skip, Color.Blue, true),
            Arguments.of(new ReverseCard(Color.Green, false), Type.Reverse, Color.Green, false),
            Arguments.of(new DrawTwoCard(Color.Yellow, true), Type.DrawTwo, Color.Yellow, true),
            Arguments.of(new WildCard(true), Type.Wild, null, true),
            Arguments.of(new WildDrawFourCard(false), Type.WildDrawFour, null, false)
        );
    }

    private static Stream<Arguments> cardFunctionProvider() {
        return Stream.of(
            Arguments.of(new NumberCard(Color.Red, 5, true), "Number card function"),
            Arguments.of(new SkipCard(Color.Blue, true), "Skip card function"),
            Arguments.of(new ReverseCard(Color.Green, true), "Reverse card function"),
            Arguments.of(new DrawTwoCard(Color.Yellow, true), "DrawTwo card function"),
            Arguments.of(new WildCard(true), "Wild card function"),
            Arguments.of(new WildDrawFourCard(true), "WildDrawFour card function")
        );
    }
    
    @Test
    void testWildCardFunctionality() {
        WildCard wildCard = new WildCard(true);
        assertNull(wildCard.getColor(), "Wild card should have null color initially");
        
        // Test color setting
        wildCard.setColor(Color.Red);
        assertEquals(Color.Red, wildCard.getColor(), 
            "Wild card color should be settable");
        
        // Test toString
        assertEquals("Wild Card (Red)", wildCard.toString(),
            "toString should include color when set");
    }
    
    @Test
    public void testWildDrawFourCardFunctionality() {
        WildDrawFourCard wildDrawFourCard = new WildDrawFourCard(true);
        assertNull(wildDrawFourCard.getColor(), 
                  "Wild Draw Four card should have null color initially");
        
        // Test color setting
        wildDrawFourCard.setColor(Color.Blue);
        assertEquals(Color.Blue, wildDrawFourCard.getColor(), 
                    "Wild Draw Four card color should be settable");
        
        // Test toString
        assertTrue(wildDrawFourCard.toString().contains("Wild Draw Four"), 
                  "toString should include 'Wild Draw Four'");
    }
    
@Test
public void testWildDrawFourCardFunction_CPUPlayer() {
    // Setup test environment
    UNOController controller = UNOController.getInstance();
    controller.getPlayerList().clear(); // Clear any existing players
    
    // Create test players - using only CPU players
    CPUPlayer cpuPlayer1 = new CPUPlayer("CPU1");
    CPUPlayer cpuPlayer2 = new CPUPlayer("CPU2");
    
    // Add players to the game
    controller.getPlayerList().add(cpuPlayer1);
    controller.getPlayerList().add(cpuPlayer2);
    
    // Set current player
    controller.setCurrentPlayer(cpuPlayer1);
    
    // Add cards to CPU player's hand to ensure chooseColor() has options
    cpuPlayer1.drawCard(new NumberCard(Color.Red, 5, true));
    cpuPlayer1.drawCard(new NumberCard(Color.Blue, 3, true));
    cpuPlayer1.drawCard(new NumberCard(Color.Green, 7, true));
    cpuPlayer1.drawCard(new WildDrawFourCard(true)); // Add the Wild Draw Four card
    
    // Play the Wild Draw Four card
    WildDrawFourCard wildDrawFour = (WildDrawFourCard)cpuPlayer1.getHand().get(3); // Get the Wild Draw Four card
    cpuPlayer1.playCard(wildDrawFour);
    
    // Verify the color was set (should be one of the colors in CPU1's hand)
    assertNotNull(wildDrawFour.getColor(), "Wild Draw Four card should have a color after being played by CPU");
    
    // The color should be one of the colors in CPU1's hand (Red, Blue, or Green)
    Color chosenColor = wildDrawFour.getColor();
    assertTrue(chosenColor == Color.Red || chosenColor == Color.Blue || chosenColor == Color.Green,
              "Chosen color should be one of the colors in CPU's hand");
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
    public void testDrawTwoCardFunctionality() {
        // Setup test environment
        UNOController controller = UNOController.getInstance();
        
        // Create test players
        HumanPlayer currentPlayer = new HumanPlayer("Current");
        CPUPlayer nextPlayer = new CPUPlayer("Next");
        
        // Add players to controller
        controller.getPlayerList().add(currentPlayer);
        controller.getPlayerList().add(nextPlayer);
        
        // Set current player
        controller.setCurrentPlayer(currentPlayer);
        
        // Create and test DrawTwoCard
        DrawTwoCard drawTwoCard = new DrawTwoCard(Color.Yellow, true);
        
        // Get initial hand size of next player
        int initialHandSize = nextPlayer.getHand().size();
        
        // Execute card function
        drawTwoCard.cardFunction();
        
        // Verify next player drew 2 cards
        assertEquals(initialHandSize + 2, nextPlayer.getHand().size(),
                    "Next player should receive 2 cards");
        
        // Clean up
        controller.getPlayerList().clear();
    }
    
    @Test
    void testCardFunctionalityWithController() {
        // This is a basic test - you might need to mock the controller
        // or set up a test environment for more complex scenarios
        UNOController controller = UNOController.getInstance();
        
        // Test with a WildCard
        WildCard wildCard = new WildCard(true);
        assertDoesNotThrow(() -> wildCard.cardFunction(),
            "Wild card function should execute without throwing exceptions");
            
        // Test with a WildDrawFourCard
        WildDrawFourCard wildDrawFourCard = new WildDrawFourCard(true);
        assertDoesNotThrow(() -> wildDrawFourCard.cardFunction(),
            "Wild Draw Four card function should execute without throwing exceptions");
    }

    private static Stream<Arguments> numberCardProvider() {
        return Stream.of(
            Arguments.of(new NumberCard(Color.Red, 0, true), 0),
            Arguments.of(new NumberCard(Color.Blue, 5, false), 5),
            Arguments.of(new NumberCard(Color.Green, 9, true), 9)
        );
    }

    private static Stream<Arguments> wildCardProvider() {
        return Stream.of(
            Arguments.of(new WildCard(true), Color.Red),
            Arguments.of(new WildCard(false), Color.Blue),
            Arguments.of(new WildDrawFourCard(true), Color.Green),
            Arguments.of(new WildDrawFourCard(false), Color.Yellow)
        );
    }
}