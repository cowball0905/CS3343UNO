package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;
import view.WildCardViewer;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

public class TestWildCardViewer {
    private WildCardViewer wildCardViewer;
    private UNOController controller;
    private JPanel testPanel;

    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        controller = UNOController.getInstance();
        controller.setPlayers();
        controller.setViewers();
        
        wildCardViewer = new WildCardViewer();
        testPanel = new JPanel();
        wildCardViewer.setPanel(testPanel);
        wildCardViewer.setController();
        wildCardViewer.setTimer(controller.getTurnTimer());
    }

    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }

    @Test
    public void testNoWildCardState() {
        assertEquals(false, wildCardViewer.isHavingWild());
    }

    @Test
    public void testSetWildCardSetsFlag() {
        Card wildCard = new WildCard(true);
        
        wildCardViewer.setWildCard(wildCard);
        
        assertEquals(true, wildCardViewer.isHavingWild());
    }

    @Test
    public void testSetWildCardStoresCard() {
        Card wildCard = new WildCard(true);
        
        wildCardViewer.setWildCard(wildCard);
        
        assertEquals(wildCard, wildCardViewer.getCard());
        assertTrue(wildCardViewer.isHavingWild());
    }

    @Test
    public void testGetCardReturnsNull() {
        assertEquals(null, wildCardViewer.getCard());
    }

    @Test
    public void testSetHavingWildTrue() {
        wildCardViewer.setHavingWild(true);
        
        assertEquals(true, wildCardViewer.isHavingWild());
    }

    @Test
    public void testSetHavingWildFalse() {
        wildCardViewer.setHavingWild(false);
        
        assertEquals(false, wildCardViewer.isHavingWild());
    }

    
    //Need to change the autoSelectColor function to also change color of placed card to red
    @Test
    public void testEndTimeAutoSelectColorSetsRed() {
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);
        
        wildCardViewer.autoSelectColor();
        
        assertEquals(Color.Red, wildCard.getColor());
    }

    @Test
    public void testSetControllerNotNull() {
        wildCardViewer.setController();
        
        assertNotNull(controller);
    }

    @Test
    public void testSetTimerNotNull() {
        CountDownTimer timer = controller.getTurnTimer();
        
        wildCardViewer.setTimer(timer);
        
        assertNotNull(timer);
    }

    @Test
    public void testSetPanelNotNull() {
        JPanel panel = new JPanel();
        
        wildCardViewer.setPanel(panel);
        
        assertNotNull(panel);
    }

    @Test
    public void testDrawWindowWhenNotHavingWild() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        
        wildCardViewer.drawWindow(g);
        Field redButtonField = WildCardViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(wildCardViewer);
        
        assertEquals(null, redButton);
        g.dispose();
    }

    @Test
    public void testDrawWindowWhenHavingWild() throws Exception {
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        
        wildCardViewer.drawWindow(g);
        Field redButtonField = WildCardViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(wildCardViewer);
        
        assertNotNull(redButton);
        g.dispose();
    }

    @Test
    public void testDrawButtonsCreatesRedButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        
        wildCardViewer.drawButtons(g);
        Field redButtonField = WildCardViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(wildCardViewer);
        
        assertNotNull(redButton);
        g.dispose();
    }

    @Test
    public void testDrawButtonsCreatesBlueButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        
        wildCardViewer.drawButtons(g);
        Field blueButtonField = WildCardViewer.class.getDeclaredField("blueButton");
        blueButtonField.setAccessible(true);
        JButton blueButton = (JButton) blueButtonField.get(wildCardViewer);
        
        assertNotNull(blueButton);
        g.dispose();
    }

    @Test
    public void testDrawButtonsCreatesYellowButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        
        wildCardViewer.drawButtons(g);
        Field yellowButtonField = WildCardViewer.class.getDeclaredField("yellowButton");
        yellowButtonField.setAccessible(true);
        JButton yellowButton = (JButton) yellowButtonField.get(wildCardViewer);
        
        assertNotNull(yellowButton);
        g.dispose();
    }

    @Test
    public void testDrawButtonsCreatesGreenButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        
        wildCardViewer.drawButtons(g);
        Field greenButtonField = WildCardViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(wildCardViewer);
        
        assertNotNull(greenButton);
        g.dispose();
    }

    
    //Testing wildCardViewer.removeButtons() for all four colors
    @Test
    public void testRemoveButtonsClearsRedButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        wildCardViewer.removeButtons();
        Field redButtonField = WildCardViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(wildCardViewer);
        
        assertEquals(null, redButton);
        g.dispose();
    }

    @Test
    public void testRemoveButtonsClearsBlueButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        wildCardViewer.removeButtons();
        Field blueButtonField = WildCardViewer.class.getDeclaredField("blueButton");
        blueButtonField.setAccessible(true);
        JButton blueButton = (JButton) blueButtonField.get(wildCardViewer);
        
        assertEquals(null, blueButton);
        g.dispose();
    }

    @Test
    public void testRemoveButtonsClearsYellowButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        wildCardViewer.removeButtons();
        Field yellowButtonField = WildCardViewer.class.getDeclaredField("yellowButton");
        yellowButtonField.setAccessible(true);
        JButton yellowButton = (JButton) yellowButtonField.get(wildCardViewer);
        
        assertEquals(null, yellowButton);
        g.dispose();
    }

    @Test
    public void testRemoveButtonsClearsGreenButton() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        wildCardViewer.removeButtons();
        Field greenButtonField = WildCardViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(wildCardViewer);
        
        assertEquals(null, greenButton);
        g.dispose();
    }

    //Check that clicking button sets correct button
    //1st Assert: Wild Card detected color
    //2nd Assert: UNOController detected color
    @Test
    public void testRedButtonClickSetsRedColor() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);
        
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        Field redButtonField = WildCardViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(wildCardViewer);
        redButton.doClick();
        
        assertEquals(Color.Red, wildCard.getColor());
        
        controller.playCard(wildCard);
        
        assertEquals(Color.Red, controller.getTopCard(1).getColor());
        g.dispose();
    }

    @Test
    public void testBlueButtonClickSetsBlueColor() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);
        
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        Field blueButtonField = WildCardViewer.class.getDeclaredField("blueButton");
        blueButtonField.setAccessible(true);
        JButton blueButton = (JButton) blueButtonField.get(wildCardViewer);
        blueButton.doClick();
        
        assertEquals(Color.Blue, wildCard.getColor());
        
        controller.playCard(wildCard);
        
        assertEquals(Color.Blue, controller.getTopCard(1).getColor());
        g.dispose();
    }

    @Test
    public void testYellowButtonClickSetsYellowColor() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);
        
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        Field yellowButtonField = WildCardViewer.class.getDeclaredField("yellowButton");
        yellowButtonField.setAccessible(true);
        JButton yellowButton = (JButton) yellowButtonField.get(wildCardViewer);
        yellowButton.doClick();
        
        assertEquals(Color.Yellow, wildCard.getColor());
        
        controller.playCard(wildCard);
        
        assertEquals(Color.Yellow, controller.getTopCard(1).getColor());
        g.dispose();
    }

    @Test
    public void testGreenButtonClickSetsGreenColor() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);
        
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        Field greenButtonField = WildCardViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(wildCardViewer);
        greenButton.doClick();
        
        assertEquals(Color.Green, wildCard.getColor());
        
        controller.playCard(wildCard);
        
        assertEquals(Color.Green, controller.getTopCard(1).getColor());
        g.dispose();
    }

    //Check that clicking button switches wildCardViewer state
    @Test
    public void testRedButtonClickResetsHavingWild() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);
        
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);
        
        Field redButtonField = WildCardViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(wildCardViewer);
        redButton.doClick();
        
        assertEquals(false, wildCardViewer.isHavingWild());
        g.dispose();
    }
    
    @Test
    public void testBlueButtonClickResetsHavingWild() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);

        Field blueButtonField = WildCardViewer.class.getDeclaredField("blueButton");
        blueButtonField.setAccessible(true);
        JButton blueButton = (JButton) blueButtonField.get(wildCardViewer);
        blueButton.doClick();

        assertEquals(false, wildCardViewer.isHavingWild());
        g.dispose();
    }

    @Test
    public void testYellowButtonClickResetsHavingWild() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);

        Field yellowButtonField = WildCardViewer.class.getDeclaredField("yellowButton");
        yellowButtonField.setAccessible(true);
        JButton yellowButton = (JButton) yellowButtonField.get(wildCardViewer);
        yellowButton.doClick();

        assertEquals(false, wildCardViewer.isHavingWild());
        g.dispose();
    }

    @Test
    public void testGreenButtonClickResetsHavingWild() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Card wildCard = new WildCard(true);
        wildCardViewer.setWildCard(wildCard);

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        wildCardViewer.drawButtons(g);

        Field greenButtonField = WildCardViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(wildCardViewer);
        greenButton.doClick();

        assertEquals(false, wildCardViewer.isHavingWild());
        g.dispose();
    }
}
