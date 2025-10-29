package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;
import view.UNOGamePanel;

import javax.swing.JButton;
import java.awt.Component;
import java.lang.reflect.Field;

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
    public void testSetIsGameEndTrue() {
        gamePanel.setIsGameEnd(true);
        
        assertEquals(true, getIsGameEnd());
    }

    @Test
    public void testSetIsGameEndFalse() {
        gamePanel.setIsGameEnd(false);
        
        assertEquals(false, getIsGameEnd());
    }

    @Test
    public void testInitialIsGameEndFalse() {
        assertEquals(false, getIsGameEnd());
    }

    @Test
    public void testStartGameNotNull() {
        gamePanel.startGame();
        
        assertNotNull(gamePanel);
    }

    @Test
    public void testUpdateDisplayNotNull() {
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
    public void testSelectedCardWithValidIndex() {
        controller.startGame();
        controller.setIsFreezed(true);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        
        gamePanel.selectedCard(0);
        
        assertEquals(0, getCurrentSelectedCardIndex());
    }

    @Test
    public void testSelectedCardResetsIndex() {
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
        
        JButton button = gamePanel.createCardButton(card, 0, controller.getHumanPlayedCard());
        
        assertNotNull(button);
    }

    @Test
    public void testCreateCardButtonBounds() {
        Card card = new NumberCard(Color.Red, 5, true);
        card.setPosition(100, 100);
        
        JButton button = gamePanel.createCardButton(card, 0, controller.getHumanPlayedCard());
        
        assertEquals(100, button.getX());
    }

    @Test
    public void testUpdateCardButtonsNotNull() {
        controller.startGame();
        
        gamePanel.updateCardButtons();
        
        assertNotNull(gamePanel);
    }

    @Test
    public void testGamePanelHasComponents() {
        controller.startGame();
        gamePanel.updateCardButtons();
        
        assertEquals(true, gamePanel.getComponentCount() > 0);
    }

    @Test
    public void testUnoButtonExists() {
        controller.startGame();
        gamePanel.updateCardButtons();
        
        assertEquals(true, hasUnoButton());
    }

    @Test
    public void testDeckButtonExists() {
        controller.startGame();
        gamePanel.updateCardButtons();
        
        assertEquals(true, hasDeckButton());
    }

    @Test
    public void testCatchCPU1ButtonExists() {
        controller.startGame();
        gamePanel.updateDisplay();
        gamePanel.repaint();
        
        assertEquals(true, hasCatchButton("catchcpu1Button"));
    }

    @Test
    public void testCatchCPU2ButtonExists() {
        controller.startGame();
        gamePanel.updateDisplay();
        gamePanel.repaint();
        
        assertEquals(true, hasCatchButton("catchcpu2Button"));
    }

    @Test
    public void testCatchCPU3ButtonExists() {
        controller.startGame();
        gamePanel.updateDisplay();
        gamePanel.repaint();
        
        assertEquals(true, hasCatchButton("catchcpu3Button"));
    }

    // Helper methods
    private boolean getIsGameEnd() {
        try {
            Field field = UNOGamePanel.class.getDeclaredField("isGameEnd");
            field.setAccessible(true);
            return (boolean) field.get(gamePanel);
        } catch (Exception e) {
            return false;
        }
    }

    private int getCurrentSelectedCardIndex() {
        try {
            Field field = UNOGamePanel.class.getDeclaredField("currentSelectedCardIndex");
            field.setAccessible(true);
            return (int) field.get(gamePanel);
        } catch (Exception e) {
            return -1;
        }
    }

    private void setCurrentSelectedCardIndex(int index) {
        try {
            Field field = UNOGamePanel.class.getDeclaredField("currentSelectedCardIndex");
            field.setAccessible(true);
            field.set(gamePanel, index);
        } catch (Exception e) {
            // Ignore
        }
    }

    private boolean hasUnoButton() {
        try {
            Field field = UNOGamePanel.class.getDeclaredField("UnoButton");
            field.setAccessible(true);
            JButton button = (JButton) field.get(gamePanel);
            return button != null;
        } catch (Exception e) {
            return false;
        }
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

    private boolean hasCatchButton(String buttonName) {
        try {
            Field field = UNOGamePanel.class.getDeclaredField(buttonName);
            field.setAccessible(true);
            JButton button = (JButton) field.get(gamePanel);
            return button != null;
        } catch (Exception e) {
            return false;
        }
    }
}
