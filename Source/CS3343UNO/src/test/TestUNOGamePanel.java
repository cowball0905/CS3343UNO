package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

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
    public void testUnoButtonExists() {
        System.out.println("Before repaint - hasUnoButton(): " + hasUnoButton());
        
        // Force a repaint to create the button
        gamePanel.repaint();
        
        // Force the repaint to complete
        try {
            Thread.sleep(100); // Give time for the repaint to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("After repaint - hasUnoButton(): " + hasUnoButton());
        
        // Also try direct painting
        Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
        gamePanel.paint(g);
        g.dispose();
        
        System.out.println("After paint - hasUnoButton(): " + hasUnoButton());
        
        // Check the components
        System.out.println("Components in gamePanel: " + gamePanel.getComponents().length);
        for (Component comp : gamePanel.getComponents()) {
            System.out.println("Component: " + comp.getClass().getName() + 
                             ", visible: " + comp.isVisible() + 
                             ", bounds: " + comp.getBounds());
        }
        
        assertTrue(hasUnoButton());
    }
    
    @Test
    public void testUnoButtonAction() {
        // Create a test panel
        UNOGamePanel panel = new UNOGamePanel();
        
        // Create a flag to check if shoutUno was called
        final boolean[] shoutUnoCalled = {false};
        
        // Override the shoutUno method
        UNOGamePanel panelSpy = new UNOGamePanel() {
            @Override
            public void shoutUno() {
                shoutUnoCalled[0] = true;
            }
        };
        
        // Get the button using reflection
        JButton unoButton = null;
        try {
            Field field = UNOGamePanel.class.getDeclaredField("UnoButton");
            field.setAccessible(true);
            unoButton = (JButton) field.get(panelSpy);
        } catch (Exception e) {
            fail("Failed to get UnoButton: " + e.getMessage());
        }
        
        // Simulate button click
        for (ActionListener al : unoButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(unoButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify shoutUno was called
        assertTrue("shoutUno should be called when UNO button is clicked", shoutUnoCalled[0]);
    }

    

@Test
public void testDeckButtonWhenGameEnded() {
    try {
        // Set up the game
        controller.startGame();
        gamePanel.startGame();
        
        // Get the deck button using reflection to access the private method
        Method updateDeckMethod = UNOGamePanel.class.getDeclaredMethod("updateDeck");
        updateDeckMethod.setAccessible(true);
        JButton deckButton = (JButton) updateDeckMethod.invoke(gamePanel);
        
        // Set game end state
        gamePanel.setIsGameEnd(true);
        
        // Get initial deck size
        int initialDeckSize = controller.getDeck().size();
        
        // Simulate button click
        for (ActionListener al : deckButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(deckButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify deck size didn't change
        assertEquals(initialDeckSize, controller.getDeck().size(), 
                   "Deck size should not change when game has ended");
        
    } catch (Exception e) {
        fail("Exception during test: " + e.getMessage());
    }
}

@Test
public void testDeckButtonWhenNotPlayersTurn() {
    try {
        // Set up the game
        controller.startGame();
        gamePanel.startGame();
        
        // Set current player to someone else (not player 0)
        if (controller.getPlayerList().size() > 1) {
            controller.setCurrentPlayer(controller.getPlayerList().get(1));
        }
        
        // Get the deck button using reflection
        Method updateDeckMethod = UNOGamePanel.class.getDeclaredMethod("updateDeck");
        updateDeckMethod.setAccessible(true);
        JButton deckButton = (JButton) updateDeckMethod.invoke(gamePanel);
        
        // Simulate button click
        for (ActionListener al : deckButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(deckButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify error message was set
        Field errorField = UNOGamePanel.class.getDeclaredField("errorMessage");
        errorField.setAccessible(true);
        String errorMessage = (String) errorField.get(gamePanel);
        assertEquals("It's not your turn!", errorMessage, 
                   "Error message should be set when it's not player's turn");
        
    } catch (Exception e) {
        fail("Exception during test: " + e.getMessage());
    }
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
            System.out.println(button);
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
}
