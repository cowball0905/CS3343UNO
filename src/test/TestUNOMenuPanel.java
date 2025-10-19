package test;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.UNOController;
import view.UNOMenuPanel;

public class TestUNOMenuPanel {
    private UNOController controller;
    private UNOMenuPanel menuPanel;
    
    @BeforeEach
    void setUp() {
        // Reset the singleton instance for testing
        try {
            // Use reflection to reset the singleton instance
            java.lang.reflect.Field instance = UNOController.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            fail("Failed to reset UNOController singleton: " + e.getMessage());
        }
        
        controller = UNOController.getInstance();
        menuPanel = new UNOMenuPanel(controller);
    }
    
    @Test
    void testInitialState() {
        assertNotNull(menuPanel, "Menu panel should be created");
        assertEquals(new Dimension(1280, 720), menuPanel.getPreferredSize(), 
            "Menu panel should have correct preferred size");
        assertNull(menuPanel.getLayout(), "Menu panel should use null layout");
    }
    
    @Test
    void testStartButtonPresence() {
        boolean startButtonFound = false;
        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof JButton && "START GAME".equals(((JButton) comp).getText())) {
                startButtonFound = true;
                break;
            }
        }
        assertTrue(startButtonFound, "Start button should be present");
    }
    
    @Test
    void testStartButtonAction() {
        JButton startButton = null;
        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof JButton && "START GAME".equals(((JButton) comp).getText())) {
                startButton = (JButton) comp;
                break;
            }
        }
        
        assertNotNull(startButton, "Start button should exist");
        
        // Store the initial game state
        int initialPlayerCount = controller.getPlayerList().size();
        
        // Simulate button click
        startButton.doClick();
        
        // Verify the game state changed (e.g., players were added)
        assertTrue(controller.getPlayerList().size() > initialPlayerCount, 
            "Start game should initialize players");
    }
    
    @Test
    void testButtonHoverEffect() {
        JButton startButton = null;
        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof JButton && "START GAME".equals(((JButton) comp).getText())) {
                startButton = (JButton) comp;
                break;
            }
        }
        
        assertNotNull(startButton, "Start button should exist");
        
        // Get initial color (should be white)
        assertEquals(Color.WHITE, startButton.getForeground(), 
            "Initial button color should be white");
        
        // Create a proper mouse event
        java.awt.event.MouseEvent enterEvent = new java.awt.event.MouseEvent(
            startButton, 
            java.awt.event.MouseEvent.MOUSE_ENTERED,
            System.currentTimeMillis(),
            0, 0, 0, 0, false
        );
        
        // Simulate mouse enter
        for (var listener : startButton.getMouseListeners()) {
            listener.mouseEntered(enterEvent);
        }
        
        // Verify color changed to gold (255, 215, 0)
        Color gold = new Color(255, 215, 0);
        assertEquals(gold, startButton.getForeground(), 
            "Button color should change to gold on hover");
        
        // Create exit event
        java.awt.event.MouseEvent exitEvent = new java.awt.event.MouseEvent(
            startButton, 
            java.awt.event.MouseEvent.MOUSE_EXITED,
            System.currentTimeMillis(),
            0, 0, 0, 0, false
        );
        
        // Simulate mouse exit
        for (var listener : startButton.getMouseListeners()) {
            if (listener instanceof java.awt.event.MouseAdapter) {
                listener.mouseExited(exitEvent);
            }
        }
        
        // Verify color changed back to white
        assertEquals(Color.WHITE, startButton.getForeground(), 
            "Button color should revert to white on mouse exit");
    }
    
    @Test
    void testPaint() {
        // Create a test graphics context
        BufferedImage buffer = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        
        // This should not throw an exception
        assertDoesNotThrow(() -> menuPanel.paint(g2d), 
            "paint should not throw exceptions");
        
        g2d.dispose();
    }
    
    @Test
    void testBackgroundImageLoading() {
        // Test that the panel can be created without throwing exceptions
        assertDoesNotThrow(() -> {
            UNOMenuPanel panel = new UNOMenuPanel(controller);
            // Trigger a paint to ensure the background is painted
            panel.paint(new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB).createGraphics());
        }, "Panel should be created and painted without exceptions");
    }
    
    @Test
    void testFallbackBackground() throws Exception {
        // 1. Create a test panel
        UNOController testController = UNOController.getInstance();
        UNOMenuPanel panel = new UNOMenuPanel(testController);
        
        // 2. Use reflection to set backgroundImage to null
        Field backgroundImageField = UNOMenuPanel.class.getDeclaredField("backgroundImage");
        backgroundImageField.setAccessible(true);
        backgroundImageField.set(panel, null);
        
        // 3. Create a buffer to paint on
        BufferedImage buffer = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = buffer.createGraphics();
        
        try {
            // 4. Paint the panel - should use fallback
            panel.paint(g2d);
            
            // 5. Check for dark green pixels (the fallback color)
            boolean hasDarkGreen = false;
            for (int x = 0; x < 100 && !hasDarkGreen; x += 10) {
                for (int y = 0; y < 100 && !hasDarkGreen; y += 10) {
                    Color pixel = new Color(buffer.getRGB(x, y));
                    if (pixel.getRed() == 0 && pixel.getGreen() == 100 && pixel.getBlue() == 0) {
                        hasDarkGreen = true;
                    }
                }
            }
            
            assertTrue(hasDarkGreen, "Panel should have used fallback dark green background");
            
        } finally {
            g2d.dispose();
            // Reset accessibility
            backgroundImageField.setAccessible(false);
        }
    }
    
}