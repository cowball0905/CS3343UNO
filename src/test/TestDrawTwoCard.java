package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import controller.UNOController;

public class TestDrawTwoCard {
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        controller = UNOController.getInstance();
        controller.setPlayers();
        controller.setViewers();
        controller.startGame();
        controller.setIsFreezed(true);
    }
    
    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }
    
    @Test
    public void testDrawTwoCardToString() {
        DrawTwoCard card = new DrawTwoCard(Color.Blue, true);
        
        assertEquals("Blue Draw Two", card.toString());
    }
    
    @Test
    public void testDrawTwoCardColor() {
        DrawTwoCard card = new DrawTwoCard(Color.Red, true);
        
        assertEquals(Color.Red, card.getColor());
    }
    
    @Test
    public void testDrawTwoCardRevealed() {
        DrawTwoCard card = new DrawTwoCard(Color.Red, true);
        
        assertEquals(true, card.isRevealed());
    }
    
    @Test
    public void testDrawTwoCardType() {
        DrawTwoCard card = new DrawTwoCard(Color.Red, true);
        
        assertEquals(Type.DrawTwo, card.getType());
    }
    
    @Test
    public void testDrawTwoCardValue() {
        DrawTwoCard card = new DrawTwoCard(Color.Red, true);
        
        assertEquals(20, card.getValue());
    }
    
    @Test
    public void testDrawTwoFunctionAddsCards() {
        Player cpuPlayer = controller.getPlayerList().get(1);
        Player targetPlayer = controller.getPlayerList().get(2);
        targetPlayer.getHand().clear();
        targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        int initialHandSize = targetPlayer.getHand().size();
        
        DrawTwoCard drawTwoCard = new DrawTwoCard(Color.Red, true);
        controller.setCurrentPlayer(cpuPlayer);
        drawTwoCard.cardFunction(controller);
        
        assertEquals(initialHandSize + 2, targetPlayer.getHand().size());
    }
    
    @Test
    public void testDrawTwoFunctionSkipsPlayer() {
        Player cpuPlayer = controller.getPlayerList().get(1);
        
        DrawTwoCard drawTwoCard = new DrawTwoCard(Color.Red, true);
        controller.setCurrentPlayer(cpuPlayer);
        drawTwoCard.cardFunction(controller);
        
        assertEquals(controller.getPlayerList().get(3), controller.getCurrentPlayer());
    }
}
