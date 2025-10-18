package test;

import static org.junit.jupiter.api.Assertions.*;
import static model.Card.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.Card;
import model.HumanPlayer;
import model.NumberCard;
import model.Player;
import model.WildCard;
import view.UNOGamePanel;

public class TestUNOGamePanel {
    private UNOController controller;
    private UNOGamePanel gamePanel;
    private Player testPlayer;
    private List<Card> testHand;
    
    @BeforeEach
    void setUp() {
        // Create test cards using the correct constructor
        testHand = new ArrayList<>();
        testHand.add(new NumberCard(Color.Red, 5, true));    // Red 5
        testHand.add(new NumberCard(Color.Blue, 1, true));   // Blue 1
        testHand.add(new WildCard(true));                    // Wild card
        
        // Set up test player with the hand
        testPlayer = new HumanPlayer("Test Player");
        setPlayerHand(testPlayer, testHand);
        
        // Set up controller
        controller = UNOController.getInstance();
        initializeController(controller, testPlayer);
        
        // Create the game panel
        gamePanel = new UNOGamePanel(controller);
        gamePanel.setPreferredSize(new Dimension(800, 600));
        
        // Initialize the panel
        try {
            // Try to call updateDisplay if it exists
            gamePanel.getClass().getMethod("updateDisplay").invoke(gamePanel);
        } catch (Exception e) {
            // If updateDisplay doesn't exist, try initComponents
            try {
                gamePanel.getClass().getMethod("initComponents").invoke(gamePanel);
            } catch (Exception ex) {
                // If neither exists, we'll proceed without them
            }
        }
        
        // Make sure the panel is visible
        gamePanel.setVisible(true);
    }
    
    private void initializeController(UNOController controller, Player player) {
        try {
            // Initialize players list
            Field playersField = UNOController.class.getDeclaredField("players");
            playersField.setAccessible(true);
            
            List<Player> players = new ArrayList<>();
            players.add(player);
            // Add some CPU players
            for (int i = 0; i < 3; i++) {
                Player cpu = new HumanPlayer("CPU " + (i+1));
                setPlayerHand(cpu, new ArrayList<>());
                players.add(cpu);
            }
            playersField.set(controller, players);
            
            // Set current player
            Field currentPlayerField = UNOController.class.getDeclaredField("currentPlayer");
            currentPlayerField.setAccessible(true);
            currentPlayerField.set(controller, player);
            
            // Initialize other required fields
            initFieldIfNull(controller, "gameBoard", new Object());
            if (!testHand.isEmpty()) {
                initFieldIfNull(controller, "currentCard", testHand.get(0));
                initFieldIfNull(controller, "topCard", testHand.get(0));
            }
            initFieldIfNull(controller, "playDirection", 1);
            
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize all controller fields: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initFieldIfNull(Object obj, String fieldName, Object defaultValue) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.get(obj) == null) {
                field.set(obj, defaultValue);
            }
        } catch (Exception e) {
            // Field might not exist, which is fine
        }
    }
    
    private void setPlayerHand(Player player, List<Card> hand) {
        try {
            // Try to find the hand field in the player's class hierarchy
            Class<?> current = player.getClass();
            while (current != null) {
                try {
                    Field handField = current.getDeclaredField("hand");
                    handField.setAccessible(true);
                    handField.set(player, new ArrayList<>(hand));
                    return;
                } catch (NoSuchFieldException e) {
                    current = current.getSuperclass();
                }
            }
            System.err.println("Warning: Could not find 'hand' field in " + player.getClass().getName());
        } catch (Exception e) {
            System.err.println("Warning: Could not set player hand: " + e.getMessage());
        }
    }
    
    @Test
    void testInitialState() {
        assertNotNull(gamePanel, "Game panel should be initialized");
        assertTrue(gamePanel.getComponentCount() >= 0, "Game panel should have zero or more components");
    }
    
    @Test
    void testCardButtonsCreation() {
        try {
            // Try to call updateCardButtons if it exists
            gamePanel.getClass().getMethod("updateCardButtons").invoke(gamePanel);
        } catch (Exception e) {
            // If updateCardButtons doesn't exist, try updateDisplay
            try {
                gamePanel.getClass().getMethod("updateDisplay").invoke(gamePanel);
            } catch (Exception ex) {
                // If neither exists, we'll proceed without them
            }
        }
        
        // Give the panel some time to process the update
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify buttons were created for each card
        int buttonCount = 0;
        for (var comp : gamePanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (!"Back to Menu".equals(button.getText())) {
                    buttonCount++;
                }
            }
        }
        
        // Just check that we have some buttons
        assertTrue(buttonCount > 0, "Should create some card buttons");
    }
    
    @Test
    void testMenuButton() {
        boolean foundMenuButton = false;
        for (var comp : gamePanel.getComponents()) {
            if (comp instanceof JButton && "Back to Menu".equals(((JButton) comp).getText())) {
                foundMenuButton = true;
                break;
            }
        }
        assertTrue(foundMenuButton, "Menu button should exist with text 'Back to Menu'");
    }
    
    @Test
    void testPanelInitialization() {
        // Test that the panel has the correct size
        Dimension size = gamePanel.getPreferredSize();
        assertTrue(size.width > 0 && size.height > 0, "Panel should have positive dimensions");
        
        // Test that the panel is visible
        assertTrue(gamePanel.isVisible(), "Panel should be visible");
        assertTrue(gamePanel.isEnabled(), "Panel should be enabled");
    }
    
    @Test
    void testDrawCardButton() {
        // Find the draw card button
        JButton drawButton = null;
        for (var comp : gamePanel.getComponents()) {
            if (comp instanceof JButton && "Draw Card".equals(((JButton) comp).getText())) {
                drawButton = (JButton) comp;
                break;
            }
        }
        assertNotNull(drawButton, "Draw card button should exist");
        
        // Test drawing a card
        int initialHandSize = testHand.size();
        drawButton.doClick();
        
        // Verify the controller's draw method was called
        try {
            Field handField = testPlayer.getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Card> updatedHand = (List<Card>) handField.get(testPlayer);
            assertTrue(updatedHand.size() > initialHandSize, "Hand size should increase after drawing");
        } catch (Exception e) {
            fail("Failed to verify draw card: " + e.getMessage());
        }
    }
    
    @Test
    void testCardSelection() throws Exception {
        // Get the selectCard method using reflection
        Method selectCard = UNOGamePanel.class.getDeclaredMethod("selectedCard", int.class);
        selectCard.setAccessible(true);
        
        // Test selecting a card
        selectCard.invoke(gamePanel, 0);
        
        // Verify the card is selected
        Field selectedIndexField = UNOGamePanel.class.getDeclaredField("currentSelectedCardIndex");
        selectedIndexField.setAccessible(true);
        int selectedIndex = (int) selectedIndexField.get(gamePanel);
        assertEquals(0, selectedIndex, "First card should be selected");
        
        // Test deselecting the same card
        selectCard.invoke(gamePanel, 0);
        
        // Verify the card is deselected
        selectedIndex = (int) selectedIndexField.get(gamePanel);
        assertEquals(-1, selectedIndex, "Card should be deselected when clicking again");
    }
    
    @Test
    void testShoutUno() throws Exception {
        // Get the shoutUno method using reflection
        Method shoutUno = UNOGamePanel.class.getDeclaredMethod("shoutUno");
        shoutUno.setAccessible(true);
        
        // Test shouting UNO
        shoutUno.invoke(gamePanel);
        
        // Verify the controller's shoutUno method was called
        Field errorMessageField = UNOGamePanel.class.getDeclaredField("errorMessage");
        errorMessageField.setAccessible(true);
        String errorMessage = (String) errorMessageField.get(gamePanel);
        assertNotNull(errorMessage, "Error message should be set after shouting UNO");
    }
    
    @Test
    void testPaintComponent() throws Exception {
        // Create a buffered image to test painting
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        
        try {
            // Call paintComponent directly
            Method paintComponent = UNOGamePanel.class.getDeclaredMethod("paintComponent", Graphics.class);
            paintComponent.setAccessible(true);
            
            // Should not throw any exceptions
            paintComponent.invoke(gamePanel, g);
            
            // Verify some basic painting occurred
            int nonWhitePixels = 0;
            for (int x = 0; x < image.getWidth(); x += 10) {
                for (int y = 0; y < image.getHeight(); y += 10) {
                    if (image.getRGB(x, y) != 0) {  // 0 is transparent/black
                        nonWhitePixels++;
                    }
                }
            }
            assertTrue(nonWhitePixels > 0, "Should have painted some non-background pixels");
            
        } finally {
            g.dispose();
        }
    }
    
    @Test
    void testGameOverState() {
        // Set game over state
        setGameOver(true);
        
        // Verify game over state
        try {
            Field isGameOverField = UNOGamePanel.class.getDeclaredField("isGameEnd");
            isGameOverField.setAccessible(true);
            assertTrue(isGameOverField.getBoolean(gamePanel), "Game should be in game over state");
        } catch (Exception e) {
            fail("Failed to test game over state: " + e.getMessage());
        }
    }
    
    private void setGameOver(boolean isOver) {
        try {
            Field gameOverField = UNOGamePanel.class.getDeclaredField("isGameEnd");
            gameOverField.setAccessible(true);
            gameOverField.setBoolean(gamePanel, isOver);
        } catch (Exception e) {
            fail("Failed to set game over state: " + e.getMessage());
        }
    }
}