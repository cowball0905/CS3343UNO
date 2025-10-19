package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import model.*;

public class TestConcreteCardFactory {
    private ConcreteCardFactory factory;
    private ArrayList<String> testDeck;
    
    @Before
    public void setUp() {
        factory = new ConcreteCardFactory();
        testDeck = new ArrayList<>();
        // Add test cards to the deck
        testDeck.add("r0");
        testDeck.add("bSkip");
        testDeck.add("yReverse");
        testDeck.add("gDrawTwo");
        testDeck.add("WildCard");
        testDeck.add("WildDrawFour");
    }
    
    @Test
    public void testCreateWildCard() {
        ArrayList<String> wildDeck = new ArrayList<>();
        wildDeck.add("WildCard");
        Card card = factory.createCard(wildDeck, false, true, null);
        assertNotNull("Should create a WildCard", card);
        assertEquals("Card should be of type Wild", Type.Wild, card.getType());
    }
    
    @Test
    public void testCreateNumberCard() {
        ArrayList<String> numberDeck = new ArrayList<>();
        numberDeck.add("r5");
        Card card = factory.createCard(numberDeck, false, true, null);
        assertNotNull("Should create a NumberCard", card);
        assertEquals("Card should be of type Number", Type.Number, card.getType());
        assertEquals("Card color should be Red", Color.Red, card.getColor());
    }
    
    @Test
    public void testCreateSkipCard() {
        ArrayList<String> skipDeck = new ArrayList<>();
        skipDeck.add("bSkip");
        Card card = factory.createCard(skipDeck, false, true, null);
        assertNotNull("Should create a SkipCard", card);
        assertEquals("Card should be of type Skip", Type.Skip, card.getType());
        assertEquals("Card color should be Blue", Color.Blue, card.getColor());
    }
    
    @Test
    public void testCreateReverseCard() {
        ArrayList<String> reverseDeck = new ArrayList<>();
        reverseDeck.add("yReverse");
        Card card = factory.createCard(reverseDeck, false, true, null);
        assertNotNull("Should create a ReverseCard", card);
        assertEquals("Card should be of type Reverse", Type.Reverse, card.getType());
        assertEquals("Card color should be Yellow", Color.Yellow, card.getColor());
    }
    
    @Test
    public void testCreateDrawTwoCard() {
        ArrayList<String> drawTwoDeck = new ArrayList<>();
        drawTwoDeck.add("gDrawTwo");
        Card card = factory.createCard(drawTwoDeck, false, true, null);
        assertNotNull("Should create a DrawTwoCard", card);
        assertEquals("Card should be of type DrawTwo", Type.DrawTwo, card.getType());
        assertEquals("Card color should be Green", Color.Green, card.getColor());
    }
    
    @Test
    public void testCreateWildDrawFourCard() {
        ArrayList<String> wildDrawFourDeck = new ArrayList<>();
        wildDrawFourDeck.add("WildDrawFour");
        Card card = factory.createCard(wildDrawFourDeck, false, true, null);
        assertNotNull("Should create a WildDrawFourCard", card);
        assertEquals("Card should be of type WildDrawFour", Type.WildDrawFour, card.getType());
    }
    
    @Test
    public void testCreateSpecificCard() {
        // Setup test deck with specific cards
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");  // Red 5
        testDeck.add("bSkip"); // Blue Skip
        testDeck.add("WildCard");
        
        // Test creating a specific card that exists in the deck
        Card card = factory.createCard(testDeck, false, true, "bSkip");
        
        // Verify the correct card was created
        assertNotNull("Should create a card", card);
        assertEquals("Card should be of type Skip", Type.Skip, card.getType());
        assertEquals("Card color should be Blue", Color.Blue, card.getColor());
        
        // Verify the card was removed from the deck
        assertEquals("Deck size should be reduced by 1", 2, testDeck.size());
        assertFalse("Card should be removed from deck", testDeck.contains("bSkip"));
    }
    
@Test
public void testCreateCardWithIsTop() {
    // Setup test deck with enough cards
    ArrayList<String> testDeck = new ArrayList<>();
    // Add enough cards to test the isTop condition
    for (int i = 0; i < 60; i++) {
        testDeck.add("r" + (i % 10)); // Add cards with values 0-9
    }
    
    // Get the initial deck size
    int initialSize = testDeck.size();
    
    // Create a card with isTop = true
    Card card = factory.createCard(testDeck, true, true, null);
    
    // Verify the card was created
    assertNotNull("Should create a card", card);
    
    // Verify the deck size is reduced by 1
    assertEquals("Deck size should be reduced by 1", initialSize - 1, testDeck.size());
    
    // Verify the card is of a valid type
    assertNotNull("Card type should not be null", card.getType());
}

}