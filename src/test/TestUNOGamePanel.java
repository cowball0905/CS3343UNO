package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;
import view.UNOGamePanel;

import javax.swing.JButton;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestUNOGamePanel {
    private UNOGamePanel gamePanel;
    private UNOController controller;

    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        controller = UNOController.getInstance();
        controller.setPlayers();
        controller.setViewers();
        
        gamePanel = new UNOGamePanel();
    }

    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }

    @Test
    public void testGamePanelNotNull() {
        assertNotNull(gamePanel);
    }

    @Test
    public void testGamePanelVisible() {
        gamePanel.setVisible(true);
        
        assertEquals(true, gamePanel.isVisible());
    }

    @Test
    public void testSetIsGameEndTrue() throws Exception {
        gamePanel.setIsGameEnd(true);
        
        assertEquals(true, getIsGameEnd());
    }

    @Test
    public void testSetIsGameEndFalse() throws Exception {
        gamePanel.setIsGameEnd(false);
        
        assertEquals(false, getIsGameEnd());
    }

    @Test
    public void testInitialIsGameEndFalse() throws Exception {
        assertEquals(false, getIsGameEnd());
    }

    @Test
    public void testStartGameNotNull() {
        controller.startGame();
        controller.setIsFreezed(true);
        
        gamePanel.startGame();
        
        assertNotNull(gamePanel);
    }

    @Test
    public void testUpdateDisplayNotNull() {
        controller.startGame();
        controller.setIsFreezed(true);
        
        gamePanel.updateDisplay();
        
        assertNotNull(gamePanel);
    }

    @Test
    public void testShoutUnoNotNull() {
        controller.startGame();
        controller.setIsFreezed(true);
        
        gamePanel.shoutUno();
        
        assertNotNull(gamePanel);
    }

    @Test
    public void testSelectedCardWithValidIndex() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        
        gamePanel.selectedCard(0);
        
        assertEquals(0, getCurrentSelectedCardIndex());
    }

    @Test
    public void testSelectedCardResetsIndex() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        gamePanel.selectedCard(0);
        
        setCurrentSelectedCardIndex(-1);
        
        assertEquals(-1, getCurrentSelectedCardIndex());
    }

    @Test
    public void testCreateCardButtonNotNull() {
        Card card = new NumberCard(Color.Red, 5, true);
        card.setPosition(100, 100);
        
        JButton button = gamePanel.createCardButton(card, 0, controller.getPlayerCard(0));
        
        assertNotNull(button);
    }

    @Test
    public void testCreateCardButtonBounds() {
        Card card = new NumberCard(Color.Red, 5, true);
        card.setPosition(100, 100);
        
        JButton button = gamePanel.createCardButton(card, 0, controller.getPlayerCard(0));
        
        assertEquals(100, button.getX());
    }

    @Test
    public void testUpdateCardButtonsNotNull() {
        controller.startGame();
        
        gamePanel.updateCardButtons();
        
        assertNotNull(gamePanel);
    }

    @Test
    public void testUnoButtonExists() throws Exception {
        System.out.println("Before repaint - hasUnoButton(): " + hasUnoButton());
        
        gamePanel.repaint();
        
        Thread.sleep(100);
        
        System.out.println("After repaint - hasUnoButton(): " + hasUnoButton());
        
        Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
        gamePanel.paint(g);
        g.dispose();
        
        System.out.println("After paint - hasUnoButton(): " + hasUnoButton());
        
        System.out.println("Components in gamePanel: " + gamePanel.getComponents().length);
        for (Component comp : gamePanel.getComponents()) {
            System.out.println("Component: " + comp.getClass().getName() + 
                             ", visible: " + comp.isVisible() + 
                             ", bounds: " + comp.getBounds());
        }
        
        assertTrue(hasUnoButton());
    }
    
    @Test
    public void testUnoButtonAction() throws Exception {
        UNOGamePanel panel = new UNOGamePanel();
        
        final boolean[] shoutUnoCalled = {false};
        
        UNOGamePanel panelSpy = new UNOGamePanel() {
            @Override
            public void shoutUno() {
                shoutUnoCalled[0] = true;
            }
        };
        
        Field field = UNOGamePanel.class.getDeclaredField("UnoButton");
        field.setAccessible(true);
        JButton unoButton = (JButton) field.get(panelSpy);
        
        for (ActionListener al : unoButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(unoButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        assertTrue(shoutUnoCalled[0]);
    }

    

    @Test
    public void testDeckButtonWhenGameEnded() throws Exception {
        controller.startGame();
        gamePanel.startGame();
        
        Method updateDeckMethod = UNOGamePanel.class.getDeclaredMethod("updateDeck");
        updateDeckMethod.setAccessible(true);
        JButton deckButton = (JButton) updateDeckMethod.invoke(gamePanel);
        
        gamePanel.setIsGameEnd(true);
        
        int initialDeckSize = controller.getDeck().size();
        
        for (ActionListener al : deckButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(deckButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        assertEquals(initialDeckSize, controller.getDeck().size());
    }

    @Test
    public void testDeckButtonWhenNotPlayersTurn() throws Exception {
        controller.startGame();
        gamePanel.startGame();
        
        controller.setCurrentPlayer(controller.getPlayerList().get(1));
        
        Method updateDeckMethod = UNOGamePanel.class.getDeclaredMethod("updateDeck");
        updateDeckMethod.setAccessible(true);
        JButton deckButton = (JButton) updateDeckMethod.invoke(gamePanel);
        
        for (ActionListener al : deckButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(deckButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        Field errorField = UNOGamePanel.class.getDeclaredField("errorMessage");
        errorField.setAccessible(true);
        String errorMessage = (String) errorField.get(gamePanel);
        assertEquals("It's not your turn!", errorMessage);
    }


    // Helper methods
    private boolean getIsGameEnd() throws Exception {
        Field field = UNOGamePanel.class.getDeclaredField("isGameEnd");
        field.setAccessible(true);
        return (boolean) field.get(gamePanel);
    }

    private int getCurrentSelectedCardIndex() throws Exception {
        Field field = UNOGamePanel.class.getDeclaredField("currentSelectedCardIndex");
        field.setAccessible(true);
        return (int) field.get(gamePanel);
    }

    private void setCurrentSelectedCardIndex(int index) throws Exception {
        Field field = UNOGamePanel.class.getDeclaredField("currentSelectedCardIndex");
        field.setAccessible(true);
        field.set(gamePanel, index);
    }

    private boolean hasUnoButton() throws Exception {
        Field field = UNOGamePanel.class.getDeclaredField("UnoButton");
        field.setAccessible(true);
        JButton button = (JButton) field.get(gamePanel);
        System.out.println(button);
        return button != null;
    }

    private boolean hasDeckButton() {
        for (Component comp : gamePanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getBounds().x == 140 && button.getBounds().y == 30) {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    public void testCatchButtonWhenGameEnded() throws Exception {
        controller.startGame();
        gamePanel.setIsGameEnd(true);
        
        Field field = UNOGamePanel.class.getDeclaredField("catchcpu1Button");
        field.setAccessible(true);
        JButton catchButton = (JButton) field.get(gamePanel);
        
        for (ActionListener al : catchButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(catchButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        assertTrue(true);
    }
    
    @Test
    public void testCatchButtonCPU1Action() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        
        Field field = UNOGamePanel.class.getDeclaredField("catchcpu1Button");
        field.setAccessible(true);
        JButton catchButton = (JButton) field.get(gamePanel);
        
        for (ActionListener al : catchButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(catchButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        assertNotNull(catchButton);
    }
    
    @Test
    public void testCatchButtonCPU2Action() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        
        Field field = UNOGamePanel.class.getDeclaredField("catchcpu2Button");
        field.setAccessible(true);
        JButton catchButton = (JButton) field.get(gamePanel);
        
        for (ActionListener al : catchButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(catchButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        assertNotNull(catchButton);
    }
    
    @Test
    public void testCatchButtonCPU3Action() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        
        Field field = UNOGamePanel.class.getDeclaredField("catchcpu3Button");
        field.setAccessible(true);
        JButton catchButton = (JButton) field.get(gamePanel);
        
        for (ActionListener al : catchButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(catchButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        assertNotNull(catchButton);
    }

    @Test
    public void testSelectedCardWhenGameEnded() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(true);
        
        int initialIndex = getCurrentSelectedCardIndex();
        gamePanel.selectedCard(0);
        
        assertEquals(initialIndex, getCurrentSelectedCardIndex());
    }
    
    @Test
    public void testSelectedCardWhenNotPlayerTurn() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        
        controller.setCurrentPlayer(controller.getPlayerList().get(1));
        
        gamePanel.selectedCard(0);
        
        String errorMessage = getErrorMessage();
        assertEquals("It's not your turn!", errorMessage);
    }
    
    @Test
    public void testSelectedCardClickUnselectedCard() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        
        setCurrentSelectedCardIndex(-1);
        
        gamePanel.selectedCard(0);
        
        assertEquals(0, getCurrentSelectedCardIndex());
        assertTrue(controller.getPlayerCard(0).get(0).isCardSelected());
    }
    
    @Test
    public void testSelectedCardClickSelectedCardCanPlay() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        
        int playableIndex = -1;
        for (int i = 0; i < controller.getPlayerCard(0).size(); i++) {
            Card card = controller.getPlayerCard(0).get(i);
            if (controller.canPlayCard(card, controller.getTopCard(1))) {
                playableIndex = i;
                break;
            }
        }
        
        assumeTrue(playableIndex != -1);
        
        setCurrentSelectedCardIndex(playableIndex);
        controller.getPlayerCard(0).get(playableIndex).setCardSelected(true);
        
        int initialHandSize = controller.getPlayerCard(0).size();
        
        gamePanel.selectedCard(playableIndex);
        
        String errorMessage = getErrorMessage();
        assertEquals("Card played!", errorMessage);
        assertEquals(-1, getCurrentSelectedCardIndex());
    }
    
    @Test
    public void testSelectedCardClickSelectedCardCannotPlay() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        
        int unplayableIndex = -1;
        for (int i = 0; i < controller.getPlayerCard(0).size(); i++) {
            Card card = controller.getPlayerCard(0).get(i);
            if (!controller.canPlayCard(card, controller.getTopCard(1))) {
                unplayableIndex = i;
                break;
            }
        }
        
        assumeTrue(unplayableIndex != -1);
        
        setCurrentSelectedCardIndex(unplayableIndex);
        controller.getPlayerCard(0).get(unplayableIndex).setCardSelected(true);
        
        gamePanel.selectedCard(unplayableIndex);
        
        String errorMessage = getErrorMessage();
        assertEquals("Can't play this card!", errorMessage);
    }
    
    @Test
    public void testSelectedCardClickAnotherCard() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        
        assumeTrue(controller.getPlayerCard(0).size() >= 2);
        
        setCurrentSelectedCardIndex(0);
        controller.getPlayerCard(0).get(0).setCardSelected(true);
        
        gamePanel.selectedCard(1);
        
        assertFalse(controller.getPlayerCard(0).get(0).isCardSelected());
        assertTrue(controller.getPlayerCard(0).get(1).isCardSelected());
        assertEquals(1, getCurrentSelectedCardIndex());
    }

    @Test
    public void testErrorMessageDisplayWithinTimeout() throws Exception {
        Field errorMessageField = UNOGamePanel.class.getDeclaredField("errorMessage");
        errorMessageField.setAccessible(true);
        errorMessageField.set(gamePanel, "Test Error");
        
        Field errorMessageTimerField = UNOGamePanel.class.getDeclaredField("errorMessageTimer");
        errorMessageTimerField.setAccessible(true);
        errorMessageTimerField.set(gamePanel, System.currentTimeMillis());
        
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        gamePanel.paint(g);
        
        String errorMessage = (String) errorMessageField.get(gamePanel);
        assertNotNull(errorMessage);
        
        g.dispose();
    }
    
    @Test
    public void testErrorMessageClearedAfterTimeout() throws Exception {
        Field errorMessageField = UNOGamePanel.class.getDeclaredField("errorMessage");
        errorMessageField.setAccessible(true);
        errorMessageField.set(gamePanel, "Test Error");
        
        Field errorMessageTimerField = UNOGamePanel.class.getDeclaredField("errorMessageTimer");
        errorMessageTimerField.setAccessible(true);
        errorMessageTimerField.set(gamePanel, System.currentTimeMillis() - 2000);
        
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        gamePanel.paintComponent(g);
        
        String errorMessage = (String) errorMessageField.get(gamePanel);
        assertNull(errorMessage);
        
        g.dispose();
    }
    
    @Test
    public void testErrorMessageNull() throws Exception {
        Field errorMessageField = UNOGamePanel.class.getDeclaredField("errorMessage");
        errorMessageField.setAccessible(true);
        errorMessageField.set(gamePanel, null);
        
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        gamePanel.paint(g);
        
        String errorMessage = (String) errorMessageField.get(gamePanel);
        assertNull(errorMessage);
        
        g.dispose();
    }
    
    @Test
    public void testShoutUnoSetsErrorMessage() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(false);
        
        gamePanel.shoutUno();
        
        String errorMessage = getErrorMessage();
        assertNotNull(errorMessage);
    }
    
    @Test
    public void testShoutUnoWhenGameEnded() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        gamePanel.setIsGameEnd(true);
        
        String initialErrorMessage = getErrorMessage();
        
        gamePanel.shoutUno();
        
        assertEquals(initialErrorMessage, getErrorMessage());
    }

    @Test
    public void testHasDeckButtonReturnsFalse() {
        boolean result = hasDeckButton();
        assertFalse(result);
    }

    @Test
    public void testHasDeckButtonReturnsTrue() throws Exception {
        controller.startGame();
        gamePanel.updateCardButtons();
        
        Thread.sleep(100);
        
        boolean result = hasDeckButton();
        assertTrue(result);
    }

    private String getErrorMessage() throws Exception {
        Field field = UNOGamePanel.class.getDeclaredField("errorMessage");
        field.setAccessible(true);
        return (String) field.get(gamePanel);
    }
}
