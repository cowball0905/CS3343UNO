package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import model.*;
import controller.UNOController;

public class TestConcreteCardFactory {
    private ConcreteCardFactory factory;
    private ArrayList<String> testDeck;
    
    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        factory = new ConcreteCardFactory();
        testDeck = new ArrayList<>();
        testDeck.add("r0");
        testDeck.add("bSkip");
        testDeck.add("yReverse");
        testDeck.add("gDrawTwo");
        testDeck.add("WildCard");
        testDeck.add("WildDrawFour");
    }
    
    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }
    
    @Test
    public void testCreateWildCardNotNull() {
        ArrayList<String> wildDeck = new ArrayList<>();
        wildDeck.add("WildCard");
        
        Card card = factory.createCard(wildDeck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateWildCardType() {
        ArrayList<String> wildDeck = new ArrayList<>();
        wildDeck.add("WildCard");
        
        Card card = factory.createCard(wildDeck, false, true, "");
        
        assertEquals(Type.Wild, card.getType());
    }
    
    @Test
    public void testCreateNumberCardNotNull() {
        ArrayList<String> numberDeck = new ArrayList<>();
        numberDeck.add("r5");
        
        Card card = factory.createCard(numberDeck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateNumberCardType() {
        ArrayList<String> numberDeck = new ArrayList<>();
        numberDeck.add("r5");
        
        Card card = factory.createCard(numberDeck, false, true, "");
        
        assertEquals(Type.Number, card.getType());
    }
    
    @Test
    public void testCreateNumberCardColor() {
        ArrayList<String> numberDeck = new ArrayList<>();
        numberDeck.add("r5");
        
        Card card = factory.createCard(numberDeck, false, true, "");
        
        assertEquals(Color.Red, card.getColor());
    }
    
    @Test
    public void testCreateSkipCardNotNull() {
        ArrayList<String> skipDeck = new ArrayList<>();
        skipDeck.add("bSkip");
        
        Card card = factory.createCard(skipDeck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateSkipCardType() {
        ArrayList<String> skipDeck = new ArrayList<>();
        skipDeck.add("bSkip");
        
        Card card = factory.createCard(skipDeck, false, true, "");
        
        assertEquals(Type.Skip, card.getType());
    }
    
    @Test
    public void testCreateSkipCardColor() {
        ArrayList<String> skipDeck = new ArrayList<>();
        skipDeck.add("bSkip");
        
        Card card = factory.createCard(skipDeck, false, true, "");
        
        assertEquals(Color.Blue, card.getColor());
    }
    
    @Test
    public void testCreateReverseCardNotNull() {
        ArrayList<String> reverseDeck = new ArrayList<>();
        reverseDeck.add("yReverse");
        
        Card card = factory.createCard(reverseDeck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateReverseCardType() {
        ArrayList<String> reverseDeck = new ArrayList<>();
        reverseDeck.add("yReverse");
        
        Card card = factory.createCard(reverseDeck, false, true, "");
        
        assertEquals(Type.Reverse, card.getType());
    }
    
    @Test
    public void testCreateReverseCardColor() {
        ArrayList<String> reverseDeck = new ArrayList<>();
        reverseDeck.add("yReverse");
        
        Card card = factory.createCard(reverseDeck, false, true, "");
        
        assertEquals(Color.Yellow, card.getColor());
    }
    
    @Test
    public void testCreateDrawTwoCardNotNull() {
        ArrayList<String> drawTwoDeck = new ArrayList<>();
        drawTwoDeck.add("gDrawTwo");
        
        Card card = factory.createCard(drawTwoDeck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateDrawTwoCardType() {
        ArrayList<String> drawTwoDeck = new ArrayList<>();
        drawTwoDeck.add("gDrawTwo");
        
        Card card = factory.createCard(drawTwoDeck, false, true, "");
        
        assertEquals(Type.DrawTwo, card.getType());
    }
    
    @Test
    public void testCreateDrawTwoCardColor() {
        ArrayList<String> drawTwoDeck = new ArrayList<>();
        drawTwoDeck.add("gDrawTwo");
        
        Card card = factory.createCard(drawTwoDeck, false, true, "");
        
        assertEquals(Color.Green, card.getColor());
    }
    
    @Test
    public void testCreateWildDrawFourCardNotNull() {
        ArrayList<String> wildDrawFourDeck = new ArrayList<>();
        wildDrawFourDeck.add("WildDrawFour");
        
        Card card = factory.createCard(wildDrawFourDeck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateWildDrawFourCardType() {
        ArrayList<String> wildDrawFourDeck = new ArrayList<>();
        wildDrawFourDeck.add("WildDrawFour");
        
        Card card = factory.createCard(wildDrawFourDeck, false, true, "");
        
        assertEquals(Type.WildDrawFour, card.getType());
    }
    
    @Test
    public void testCreateSpecificCardNotNull() {
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");
        testDeck.add("bSkip");
        testDeck.add("WildCard");
        
        Card card = factory.createCard(testDeck, false, true, "bSkip");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateSpecificCardType() {
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");
        testDeck.add("bSkip");
        testDeck.add("WildCard");
        
        Card card = factory.createCard(testDeck, false, true, "bSkip");
        
        assertEquals(Type.Skip, card.getType());
    }
    
    @Test
    public void testCreateSpecificCardColor() {
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");
        testDeck.add("bSkip");
        testDeck.add("WildCard");
        
        Card card = factory.createCard(testDeck, false, true, "bSkip");
        
        assertEquals(Color.Blue, card.getColor());
    }
    
    @Test
    public void testCreateSpecificCardRemovesFromDeck() {
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");
        testDeck.add("bSkip");
        testDeck.add("WildCard");
        
        factory.createCard(testDeck, false, true, "bSkip");
        
        assertEquals(2, testDeck.size());
    }
    
    @Test
    public void testCreateSpecificCardNotInDeck() {
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");
        testDeck.add("bSkip");
        testDeck.add("WildCard");
        
        factory.createCard(testDeck, false, true, "bSkip");
        
        assertEquals(false, testDeck.contains("bSkip"));
    }
    
    @Test
    public void testCreateCardWithIsTopNotNull() {
        ArrayList<String> testDeck = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            testDeck.add("r" + (i % 10));
        }
        
        Card card = factory.createCard(testDeck, true, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testCreateCardWithIsTopReducesDeckSize() {
        ArrayList<String> testDeck = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            testDeck.add("r" + (i % 10));
        }
        int initialSize = testDeck.size();
        
        factory.createCard(testDeck, true, true, "");
        
        assertEquals(initialSize - 1, testDeck.size());
    }
    
    @Test
    public void testCreateCardWithIsTopHasType() {
        ArrayList<String> testDeck = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            testDeck.add("r" + (i % 10));
        }
        
        Card card = factory.createCard(testDeck, true, true, "");
        
        assertNotNull(card.getType());
    }
    
    @Test
    public void testGiveCardNotNull() {
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");
        
        Card card = factory.giveCard(testDeck, false, true, "");
        
        assertNotNull(card);
    }
    
    @Test
    public void testGiveCardReducesDeckSize() {
        ArrayList<String> testDeck = new ArrayList<>();
        testDeck.add("r5");
        testDeck.add("b3");
        int initialSize = testDeck.size();
        
        factory.giveCard(testDeck, false, true, "");
        
        assertEquals(initialSize - 1, testDeck.size());
    }
}