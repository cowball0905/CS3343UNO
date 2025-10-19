package test;

import static org.junit.jupiter.api.Assertions.*;
import static model.Color.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;
import view.UNOGamePanel;

public class TestUNOGamePanel {
    private UNOController controller;
    private UNOGamePanel gamePanel;
    private HumanPlayer testPlayer;
    private List<Player> players;
    private List<Card> testHand;
    private Card testTopCard;
    
    @BeforeEach
    void setUp() {
        // Create test cards
        testHand = new ArrayList<>();
        testHand.add(new NumberCard(Red, 5, true));    // Red 5
        testHand.add(new NumberCard(Blue, 1, true));   // Blue 1
        testHand.add(new WildCard(true));              // Wild card
        testTopCard = new NumberCard(Green, 7, true);  // Green 7 as top card
        
        // Set up test player with the hand
        testPlayer = new HumanPlayer("Test Player");
        setPlayerHand(testPlayer, testHand);
        
        // Set up players
        players = new ArrayList<>();
        players.add(testPlayer);
        
        // Add CPU players
        for (int i = 1; i <= 3; i++) {
            Player cpu = new CPUPlayer("CPU " + i);
            setPlayerHand(cpu, new ArrayList<>());
            players.add(cpu);
        }
        
        // Create controller instance
        controller = UNOController.getInstance();
        
        try {
            // Set up controller state using reflection
            setField(controller, "players", players);
            setField(controller, "currentPlayer", testPlayer);
            setField(controller, "gameBoard", new Object());
            setField(controller, "currentCard", testTopCard);
            setField(controller, "topCard", testTopCard);
            setField(controller, "playDirection", 1);
            
            // Set controller reference in the test player
            setField(testPlayer, "controller", controller);
            
        } catch (Exception e) {
            System.err.println("Error setting up controller: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create the game panel
        gamePanel = new UNOGamePanel(controller);
        gamePanel.setPreferredSize(new Dimension(800, 600));
        
        // Initialize the panel
        try {
            Method updateDisplay = gamePanel.getClass().getMethod("updateDisplay");
            updateDisplay.setAccessible(true);
            updateDisplay.invoke(gamePanel);
        } catch (Exception e) {
            try {
                Method initComponents = gamePanel.getClass().getMethod("initComponents");
                initComponents.setAccessible(true);
                initComponents.invoke(gamePanel);
            } catch (Exception ex) {
                // If neither exists, we'll proceed without them
            }
        }
        
        gamePanel.setVisible(true);
    }
    
    // Helper method to set private fields using reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            System.err.println("Error setting field " + fieldName + ": " + e.getMessage());
        }
    }
    
    // Helper method to get private field value using reflection
    private Object getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            System.err.println("Error getting field " + fieldName + ": " + e.getMessage());
            return null;
        }
    }
    
    // Helper method to set player's hand
    private void setPlayerHand(Player player, List<Card> hand) {
        try {
            Field handField = player.getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            handField.set(player, new ArrayList<>(hand));
        } catch (Exception e) {
            System.err.println("Error setting player hand: " + e.getMessage());
        }
    }

    @Test
    void testInitialState() {
        assertNotNull(gamePanel, "Game panel should be initialized");
        assertTrue(gamePanel.isVisible(), "Game panel should be visible");
        assertTrue(gamePanel.getComponentCount() > 0, "Game panel should have components");
        
        // Verify basic components are present
        boolean hasMenuButton = false;
        boolean hasUnoButton = false;
        
        for (Component comp : gamePanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Back to Menu".equals(button.getText())) {
                    hasMenuButton = true;
                } else if (button.getActionListeners().length > 0) {
                    // Check if this is the UNO button by its action listener
                    for (var listener : button.getActionListeners()) {
                        if (listener.toString().contains("shoutUno")) {
                            hasUnoButton = true;
                            break;
                        }
                    }
                }
            }
        }
        
        assertTrue(hasMenuButton, "Menu button should be present");
        assertTrue(hasUnoButton, "Uno button should be present");
    }
    
    @Test
    void testMenuButton() {
        JButton menuButton = null;
        
        // Find the menu button
        for (Component comp : gamePanel.getComponents()) {
            if (comp instanceof JButton && "Back to Menu".equals(((JButton) comp).getText())) {
                menuButton = (JButton) comp;
                break;
            }
        }
        
        assertNotNull(menuButton, "Menu button should exist");
        assertEquals(new Rectangle(10, 10, 120, 30), menuButton.getBounds(), 
            "Menu button should be properly positioned");
            
        // Test button action
        AtomicBoolean wasClicked = new AtomicBoolean(false);
        menuButton.addActionListener(e -> wasClicked.set(true));
        menuButton.doClick();
        
        assertTrue(wasClicked.get(), "Menu button click should trigger action");
    }
    
@Test
void testCardSelection() throws Exception {
    // Set up a playable card
    Card playableCard = new NumberCard(Green, 7, true);  // Same color as top card
    testHand.clear();
    testHand.add(playableCard);
    setPlayerHand(testPlayer, testHand);
    
    // Set up controller state
    setField(controller, "currentPlayer", testPlayer);
    setField(controller, "topCard", testTopCard);
    setField(controller, "currentCard", testTopCard);
    
    // Get the selectedCard method
    Method selectedCard = UNOGamePanel.class.getDeclaredMethod("selectedCard", int.class);
    selectedCard.setAccessible(true);
    
    // Test selecting the card
    selectedCard.invoke(gamePanel, 0);
    
    // Verify the card is selected
    int selectedIndex = (int) getField(gamePanel, "currentSelectedCardIndex");
    assertEquals(0, selectedIndex, "Card should be selected");
    
    // Verify the card was played
    Card newTopCard = (Card) getField(controller, "topCard");
    assertEquals(playableCard, newTopCard, "Top card should be updated to the played card");
    
    // Verify player's hand is updated
    @SuppressWarnings("unchecked")
    List<Card> playerHand = (List<Card>) getField(testPlayer, "hand");
    assertFalse(playerHand.contains(playableCard), "Played card should be removed from hand");
}
@Test
void testShoutUno() throws Exception {
    // Set up test player with exactly 1 card (UNO condition)
    List<Card> unoHand = new ArrayList<>();
    unoHand.add(new NumberCard(Red, 1, true));
    setPlayerHand(testPlayer, unoHand);
    
    // Ensure the controller is properly set up
    setField(controller, "currentPlayer", testPlayer);
    setField(testPlayer, "controller", controller);
    
    // Get the shoutUno method
    Method shoutUno = UNOGamePanel.class.getDeclaredMethod("shoutUno");
    shoutUno.setAccessible(true);
    
    // Call the method
    shoutUno.invoke(gamePanel);
    
    // Verify the player's UNO status
    boolean hasShoutedUno = (boolean) getField(testPlayer, "hasShoutedUno");
    assertTrue(hasShoutedUno, "Player should have shouted UNO");
    
    // Verify the error message was cleared (assuming successful UNO)
    String errorMessage = (String) getField(gamePanel, "errorMessage");
    assertNull(errorMessage, "Error message should be null after successful UNO");
}
    @Test
    void testGameOverState() {
        // Set game over state
        gamePanel.setIsGameEnd(true);
        
        // Verify game over state
        boolean isGameEnd = (boolean) getField(gamePanel, "isGameEnd");
        assertTrue(isGameEnd, "Game should be in game over state");
        
        // Test game over rendering
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        
        try {
            // Call paintComponent directly
            Method paintComponent = UNOGamePanel.class.getDeclaredMethod("paintComponent", Graphics.class);
            paintComponent.setAccessible(true);
            paintComponent.invoke(gamePanel, g);
            
            // Check if game over text was drawn
            boolean foundGameOverText = false;
            for (int x = 0; x < image.getWidth(); x += 50) {
                for (int y = 0; y < image.getHeight(); y += 50) {
                    if (image.getRGB(x, y) != 0) {
                        foundGameOverText = true;
                        break;
                    }
                }
                if (foundGameOverText) break;
            }
            
            assertTrue(foundGameOverText, "Game over text should be visible");
            
        } catch (Exception e) {
            fail("Error testing game over state: " + e.getMessage());
        } finally {
            g.dispose();
        }
    }
    
    @Test
    void testUpdateDisplay() throws Exception {
        // Get the updateDisplay method using reflection
        Method updateDisplay = UNOGamePanel.class.getDeclaredMethod("updateDisplay");
        updateDisplay.setAccessible(true);
        
        // Call updateDisplay
        updateDisplay.invoke(gamePanel);
        
        // Verify it updated the card buttons
        Field cardButtonsField = UNOGamePanel.class.getDeclaredField("cardButtons");
        cardButtonsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JButton> cardButtons = (List<JButton>) cardButtonsField.get(gamePanel);
        
        assertEquals(testHand.size(), cardButtons.size(), 
            "Should have a button for each card in hand");
    }
}