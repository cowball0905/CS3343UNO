package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import controller.UNOController;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class TestCard {
    private Card testCard;
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        testCard = new NumberCard(Color.Red, 5, true);
        controller = UNOController.getInstance();
        controller.setPlayers();
        controller.setViewers();
    }
    
    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }
    
    @Test
    public void testSetPositionX() {
        testCard.setPosition(100, 200);
        
        assertEquals(100, testCard.getX());
    }
    
    @Test
    public void testSetPositionY() {
        testCard.setPosition(100, 200);
        
        assertEquals(200, testCard.getY());
    }
    
    @Test
    public void testSetSizeWidth() {
        testCard.setSize(100, 150);
        
        assertEquals(100, testCard.getWidth());
    }
    
    @Test
    public void testSetSizeHeight() {
        testCard.setSize(100, 150);
        
        assertEquals(150, testCard.getHeight());
    }
    
    @Test
    public void testSetCardSelectedTrue() {
        testCard.setCardSelected(true);
        
        assertEquals(true, testCard.isCardSelected());
    }
    
    @Test
    public void testSetCardSelectedTrueChangesY() {
        testCard.setCardSelected(true);
        
        assertEquals(410, testCard.getY());
    }
    
    @Test
    public void testSetCardSelectedFalse() {
        testCard.setCardSelected(true);
        testCard.setCardSelected(false);
        
        assertEquals(false, testCard.isCardSelected());
    }
    
    @Test
    public void testSetCardSelectedFalseChangesY() {
        testCard.setCardSelected(true);
        testCard.setCardSelected(false);
        
        assertEquals(450, testCard.getY());
    }
    
    @Test
    public void testSetRotation90() {
        testCard.setRotation(90);
        
        assertEquals(90.0, testCard.getRotationAngle());
    }
    
    @Test
    public void testSetRotation180() {
        testCard.setRotation(180);
        
        assertEquals(180.0, testCard.getRotationAngle());
    }
    
    @Test
    public void testSetRotation270() {
        testCard.setRotation(270);
        
        assertEquals(270.0, testCard.getRotationAngle());
    }

    @Test
    public void testSetRotation0() {
        testCard.setRotation(0);
        
        assertEquals(0.0, testCard.getRotationAngle());
    }

    @Test
    public void testRotation90IsRotated() {
        testCard.setRotation(90);
        
        assertEquals(true, testCard.isRotated());
    }
    
    @Test
    public void testRotation180IsRotated() {
        testCard.setRotation(180);
        
        assertEquals(true, testCard.isRotated());
    }
    
    @Test
    public void testRotation270IsRotated() {
        testCard.setRotation(270);
        
        assertEquals(true, testCard.isRotated());
    }
    
    @Test
    public void testRotation0NotRotated() {
        testCard.setRotation(90);
        testCard.setRotation(0);
        
        assertEquals(false, testCard.isRotated());
    }
	
    @Test
    public void testInitialRevealedState() {
        assertEquals(true, testCard.isRevealed());
    }
    
    @Test
    public void testSetRevealedFalse() {
        testCard.setRevealed(false);
        
        assertEquals(false, testCard.isRevealed());
    }
    
    @Test
    public void testGetImageNotNull() {
        assertNotNull(testCard.getImage());
    }

    @Test
    public void testGetType() {
        assertEquals(Type.Number, testCard.getType());
    }
    
    @Test
    public void testCardImageNotNull() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertNotNull(card.getImage());
    }
    
    @Test
    public void testCardImageWidth() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertEquals(80, card.getImage().getWidth());
    }
    
    @Test
    public void testCardImageHeight() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertEquals(120, card.getImage().getHeight());
    }
    
    @Test
    public void testHiddenCardHasImage() {
        NumberCard card = new NumberCard(Color.Red, 5, false);
        
        assertNotNull(card.getImage());
    }
    
    @Test
    public void testCardImageChangesWhenRevealed() {
        NumberCard card = new NumberCard(Color.Red, 5, false);
        BufferedImage backImage = card.getImage();
        
        card.setRevealed(true);
        
        assertNotEquals(backImage, card.getImage());
    }
    
    @Test
    public void testDrawUpdatesX() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = testImage.createGraphics();
        
        card.draw(g2d, 50, 50);
        
        assertEquals(50, card.getX());
        g2d.dispose();
    }
    
    @Test
    public void testDrawUpdatesY() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = testImage.createGraphics();
        
        card.draw(g2d, 50, 50);
        
        assertEquals(50, card.getY());
        g2d.dispose();
    }
    
    @Test
    public void testDrawWithNegativeX() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = testImage.createGraphics();
        
        card.draw(g2d, -10, -20);
        
        assertEquals(-10, card.getX());
        g2d.dispose();
    }
    
    @Test
    public void testDrawWithNegativeY() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        BufferedImage testImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = testImage.createGraphics();
        
        card.draw(g2d, -10, -20);
        
        assertEquals(-20, card.getY());
        g2d.dispose();
    }
    
    @Test
    public void testDrawWithNullGraphics() {
        NumberCard card = new NumberCard(Color.Red, 5, true);
        
        assertThrows(NullPointerException.class, () -> card.draw(null, 0, 0));
    }
    
    @Test
    public void testCardInitiallyFaceDown() {
        NumberCard card = new NumberCard(Color.Red, 5, false);
        
        assertEquals(false, card.isRevealed());
    }
    
    @Test
    public void testSetRevealedTrue() {
        NumberCard card = new NumberCard(Color.Red, 5, false);
        
        card.setRevealed(true);
        
        assertEquals(true, card.isRevealed());
    }
    
    @Test
    public void testRevealedCardHasImage() {
        NumberCard card = new NumberCard(Color.Red, 5, false);
        
        card.setRevealed(true);
        
        assertNotNull(card.getImage());
    }
    
    @Test
    public void testSetRevealedFalseAfterTrue() {
        NumberCard card = new NumberCard(Color.Red, 5, false);
        card.setRevealed(true);
        
        card.setRevealed(false);
        
        assertEquals(false, card.isRevealed());
    }
    
    @Test
    public void testRevealedCardHasImageLoaded() {
        NumberCard revealedCard = new NumberCard(Color.Blue, 3, true);
        
        assertNotNull(revealedCard.getImage());
    }
    
    @Test
    public void testHiddenCardHasBackImage() {
        NumberCard hiddenCard = new NumberCard(Color.Blue, 3, false);
        
        assertNotNull(hiddenCard.getImage());
    }
    
    @Test
    public void testRevealedAndHiddenCardsDifferentImages() {
        NumberCard revealedCard = new NumberCard(Color.Blue, 3, true);
        NumberCard hiddenCard = new NumberCard(Color.Blue, 3, false);
        
        assertNotEquals(revealedCard.getImage(), hiddenCard.getImage());
    }
    
    @Test
    public void testWildCardFunctionNotNull() {
        controller.startGame();
        controller.setIsFreezed(true);
        WildCard wildCard = new WildCard(true);
        
        wildCard.cardFunction(controller);
        
        assertNotNull(wildCard);
    }
    
    @Test
    public void testWildDrawFourCardFunctionNotNull() {
        controller.startGame();
        controller.setIsFreezed(true);
        WildDrawFourCard wildDrawFourCard = new WildDrawFourCard(true);
        
        wildDrawFourCard.cardFunction(controller);
        
        assertNotNull(wildDrawFourCard);
    }
}