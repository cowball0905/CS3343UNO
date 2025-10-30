package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import controller.UNOController;

public class TestWildCard {
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        controller = UNOController.getInstance();
        controller.setPlayers();
        controller.setViewers();
    }
    
    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }
    
    @Test
    public void testWildCardNotNull() {
        WildCard card = new WildCard(true);
        
        assertNotNull(card);
    }
    
    @Test
    public void testWildCardColorNull() {
        WildCard card = new WildCard(true);
        
        assertEquals(null, card.getColor());
    }
    
    @Test
    public void testWildCardRevealed() {
        WildCard card = new WildCard(true);
        
        assertEquals(true, card.isRevealed());
    }
    
    @Test
    public void testWildCardType() {
        WildCard card = new WildCard(true);
        
        assertEquals(Type.Wild, card.getType());
    }
    
    @Test
    public void testWildCardValue() {
        WildCard card = new WildCard(true);
        
        assertEquals(50, card.getValue());
    }
    
    @Test
    public void testWildCardToString() {
        WildCard card = new WildCard(true);
        
        assertEquals("Wild Card", card.toString());
    }
    
    @Test
    public void testWildCardSetColor() {
        WildCard card = new WildCard(true);
        
        card.setColor(Color.Red);
        
        assertEquals(Color.Red, card.getColor());
    }
    
    @Test
    public void testWildCardSetColorBlue() {
        WildCard card = new WildCard(true);
        
        card.setColor(Color.Blue);
        
        assertEquals(Color.Blue, card.getColor());
    }
    
    @Test
    public void testWildCardFunctionWithCPU() {
        controller.startGame();
        controller.setIsFreezed(true);
        controller.setCurrentPlayer(controller.getPlayerList().get(1));
        
        WildCard card = new WildCard(true);
        card.cardFunction(controller);
        
        assertNotNull(card.getColor());
    }
    
    @Test
    public void testWildCardFunctionWithHuman() {
        controller.startGame();
        controller.setIsFreezed(true);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        
        WildCard card = new WildCard(true);
        card.cardFunction(controller);
        
        assertEquals(card, controller.getWildCardViewer().getCard());
    }

    @Test
    public void testCheckCardNumberCard() {
        WildCard topCard = new WildCard(true);
        NumberCard playedCard = new NumberCard(Color.Red, 5, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardSkipCard() {
        WildCard topCard = new WildCard(true);
        SkipCard playedCard = new SkipCard(Color.Blue, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardReverseCard() {
        WildCard topCard = new WildCard(true);
        ReverseCard playedCard = new ReverseCard(Color.Green, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardDrawTwoCard() {
        WildCard topCard = new WildCard(true);
        DrawTwoCard playedCard = new DrawTwoCard(Color.Yellow, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardWildCard() {
        WildCard topCard = new WildCard(true);
        WildCard playedCard = new WildCard(true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardWildDrawFour() {
        WildCard topCard = new WildCard(true);
        WildDrawFourCard playedCard = new WildDrawFourCard(true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }
}