package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import model.*;

public class TestConcreteCardFactory {
    private ConcreteCardFactory factory;
    private HashMap<String, Integer> deck;
    
    @BeforeEach
    public void setUp() {
        factory = new ConcreteCardFactory();
        deck = new HashMap<>();
    }
    
    @AfterEach
    public void tearDown() {
        deck.clear();
    }
    
    // Test creating each card type
    @Test
    public void testCreateNumberCard() {
        deck.put("r5", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertNotNull(card);
        assertEquals(Type.Number, card.getType());
        assertEquals(Color.Red, card.getColor());
        assertEquals(5, card.getValue());
    }
    
    @Test
    public void testCreateSkipCard() {
        deck.put("bSkip", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertNotNull(card);
        assertEquals(Type.Skip, card.getType());
        assertEquals(Color.Blue, card.getColor());
    }
    
    @Test
    public void testCreateReverseCard() {
        deck.put("yReverse", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertNotNull(card);
        assertEquals(Type.Reverse, card.getType());
        assertEquals(Color.Yellow, card.getColor());
    }
    
    @Test
    public void testCreateDrawTwoCard() {
        deck.put("gDrawTwo", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertNotNull(card);
        assertEquals(Type.DrawTwo, card.getType());
        assertEquals(Color.Green, card.getColor());
    }
    
    @Test
    public void testCreateWildCard() {
        deck.put("WildCard", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertNotNull(card);
        assertEquals(Type.Wild, card.getType());
    }
    
    @Test
    public void testCreateWildDrawFourCard() {
        deck.put("WildDrawFour", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertNotNull(card);
        assertEquals(Type.WildDrawFour, card.getType());
    }
    
    // Test isTop parameter (only number cards)
    @Test
    public void testIsTopSelectsNumberCard() {
        deck.put("r5", 1);
        deck.put("bSkip", 1);
        deck.put("WildCard", 1);
        
        Card card = factory.createCard(deck, true, true, "");
        
        assertNotNull(card);
        assertEquals(Type.Number, card.getType());
    }
    
    @Test
    public void testIsTopWithOnlyNumberCards() {
        deck.put("r0", 1);
        deck.put("b3", 1);
        deck.put("y7", 1);
        
        Card card = factory.createCard(deck, true, true, "");
        
        assertNotNull(card);
        assertEquals(Type.Number, card.getType());
    }
    
    // Test revealed parameter
    @Test
    public void testRevealedTrue() {
        deck.put("r5", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertTrue(card.isRevealed());
    }
    
    @Test
    public void testRevealedFalse() {
        deck.put("r5", 1);
        Card card = factory.createCard(deck, false, false, "");
        
        assertFalse(card.isRevealed());
    }
    
    // Test deck modification
    @Test
    public void testCreateCardReducesDeckSize() {
        deck.put("r5", 3);
        
        factory.createCard(deck, false, true, "");
        
        assertEquals(2, deck.get("r5"));
    }
    
    @Test
    public void testCreateCardRemovesLastCard() {
        deck.put("r5", 1);
        
        factory.createCard(deck, false, true, "");
        
        assertFalse(deck.containsKey("r5"));
    }
    
    @Test
    public void testCreateMultipleCards() {
        deck.put("r5", 3);
        
        Card card1 = factory.createCard(deck, false, true, "");
        Card card2 = factory.createCard(deck, false, true, "");
        
        assertNotNull(card1);
        assertNotNull(card2);
        assertEquals(1, deck.get("r5"));
    }
    
    // Test empty deck
    @Test
    public void testCreateCardFromEmptyDeck() {
        Card card = factory.createCard(deck, false, true, "");
        
        assertNull(card);
    }
    
    @Test
    public void testIsTopWithEmptyDeck() {
        Card card = factory.createCard(deck, true, true, "");
        
        assertNull(card);
    }
    
    @Test
    public void testIsTopWithNoNumberCards() {
        deck.put("bSkip", 1);
        deck.put("WildCard", 1);
        
        Card card = factory.createCard(deck, true, true, "");
        
        assertNull(card);
    }
    
    // Test all colors
    @Test
    public void testCreateRedCard() {
        deck.put("r3", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertEquals(Color.Red, card.getColor());
    }
    
    @Test
    public void testCreateBlueCard() {
        deck.put("b7", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertEquals(Color.Blue, card.getColor());
    }
    
    @Test
    public void testCreateYellowCard() {
        deck.put("y2", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertEquals(Color.Yellow, card.getColor());
    }
    
    @Test
    public void testCreateGreenCard() {
        deck.put("g9", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertEquals(Color.Green, card.getColor());
    }
    
    // Test all number values
    @Test
    public void testCreateNumber0() {
        deck.put("r0", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertEquals(0, card.getValue());
    }
    
    @Test
    public void testCreateNumber9() {
        deck.put("b9", 1);
        Card card = factory.createCard(deck, false, true, "");
        
        assertEquals(9, card.getValue());
    }
    
    // Test giveCard method
    @Test
    public void testGiveCardNotNull() {
        deck.put("r5", 1);
        Card card = factory.giveCard(deck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testGiveCardReducesDeck() {
        deck.put("r5", 2);
        
        factory.giveCard(deck, false, true, "");
        
        assertEquals(1, deck.get("r5"));
    }
    
    @Test
    public void testGiveCardFromEmptyDeck() {
        Card card = factory.giveCard(deck, false, true, "");
        
        assertNull(card);
    }
}
