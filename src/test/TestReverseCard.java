package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;

public class TestReverseCard {
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
    public void testReverseCardNotNull() {
        ReverseCard card = new ReverseCard(Color.Yellow, true);
        
        assertNotNull(card);
    }
    
    @Test
    public void testReverseCardColor() {
        ReverseCard card = new ReverseCard(Color.Yellow, true);
        
        assertEquals(Color.Yellow, card.getColor());
    }
    
    @Test
    public void testReverseCardType() {
        ReverseCard card = new ReverseCard(Color.Yellow, true);
        
        assertEquals(Type.Reverse, card.getType());
    }
    
    @Test
    public void testReverseCardValue() {
        ReverseCard card = new ReverseCard(Color.Yellow, true);
        
        assertEquals(20, card.getValue());
    }
    
    @Test
    public void testReverseCardToString() {
        ReverseCard card = new ReverseCard(Color.Green, true);
        
        assertEquals("Green Reverse", card.toString());
    }
    
    @Test
    public void testReverseCardFunctionReversesDirection() {
        controller.startGame();
        controller.setIsFreezed(true);
        controller.setPlayDirection(1);
        
        ReverseCard card = new ReverseCard(Color.Red, true);
        card.cardFunction(controller);
        
        assertEquals(-1, controller.getPlayDirection());
    }
    
    @Test
    public void testReverseCardFunctionReversesFromNegative() {
        controller.startGame();
        controller.setIsFreezed(true);
        controller.setPlayDirection(-1);
        
        ReverseCard card = new ReverseCard(Color.Red, true);
        card.cardFunction(controller);
        
        assertEquals(1, controller.getPlayDirection());
    }
}
