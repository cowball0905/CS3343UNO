package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import controller.UNOController;

public class TestNumberCard {
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
    public void testNumberCardNotNull() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertNotNull(card);
    }
    
    @Test
    public void testNumberCardColor() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertEquals(Color.Red, card.getColor());
    }
    
    @Test
    public void testNumberCardValue() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertEquals(5, card.getValue());
    }
    
    @Test
    public void testNumberCardType() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertEquals(Type.Number, card.getType());
    }
    
    @Test
    public void testNumberCardToStringRed5() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertEquals("Red 5", card.toString());
    }
    
    @Test
    public void testNumberCardFunctionPassesNextPlayer() {
        controller.startGame();
        controller.setIsFreezed(true);
        Player initialPlayer = controller.getPlayerList().get(0);
        controller.setCurrentPlayer(initialPlayer);
        
        NumberCard numberCard = new NumberCard(Color.Red, 7, true);
        numberCard.cardFunction(controller);
        
        assertEquals(controller.getPlayerList().get(1), controller.getCurrentPlayer());
    }
    
    @Test
    public void testNumberCardFunctionWrapsAround() {
        controller.startGame();
        controller.setIsFreezed(true);
        Player lastPlayer = controller.getPlayerList().get(3);
        controller.setCurrentPlayer(lastPlayer);
        
        NumberCard numberCard = new NumberCard(Color.Red, 3, true);
        numberCard.cardFunction(controller);
        
        assertEquals(controller.getPlayerList().get(0), controller.getCurrentPlayer());
    }

    @Test
    public void testCheckCardSameColor() {
        NumberCard topCard = new NumberCard(Color.Red, 5, true);
        NumberCard playedCard = new NumberCard(Color.Red, 3, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardSameNumber() {
        NumberCard topCard = new NumberCard(Color.Red, 5, true);
        NumberCard playedCard = new NumberCard(Color.Blue, 5, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardWildCard() {
        NumberCard topCard = new NumberCard(Color.Red, 5, true);
        WildCard playedCard = new WildCard(true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardWildDrawFour() {
        NumberCard topCard = new NumberCard(Color.Red, 5, true);
        WildDrawFourCard playedCard = new WildDrawFourCard(true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardDifferentColorAndNumber() {
        NumberCard topCard = new NumberCard(Color.Red, 5, true);
        NumberCard playedCard = new NumberCard(Color.Blue, 3, true);
        
        assertEquals(false, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardSkipDifferentColor() {
        NumberCard topCard = new NumberCard(Color.Red, 5, true);
        SkipCard playedCard = new SkipCard(Color.Blue, true);
        
        assertEquals(false, topCard.checkCard(playedCard));
    }
}