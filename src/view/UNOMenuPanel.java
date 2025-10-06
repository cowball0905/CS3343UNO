package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
// import controller.UNOController;

// If UNOController is in another package, update the import accordingly, e.g.:
// import src.controller.UNOController; // Adjust this path to match your actual project structure

// If UNOController is in the default package, use:
// import UNOController; // Uncomment and update the path below if UNOController is in a package

import controller.UNOController;
// import src.controller.UNOController; // Adjust as needed

// If UNOController is in the default package, do not import it; just use it directly.

public class UNOMenuPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 1280;  // Increased width for better display
    private static final int HEIGHT = 720;  // Increased height for better display
    
    private BufferedImage backgroundImage;
    private UNOController controller;
    private JButton startButton;
    
    public UNOMenuPanel(UNOController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        
        // Load the background image
        loadBackgroundImage();
        
        // Create and style the start button
        createStartButton();
    }
    
    private void loadBackgroundImage() {
        String[] possiblePaths = {
            "/asset/title.png",                      // For JAR packaging
            "src/asset/title.png",                   // For development
        };
        
        for (String path : possiblePaths) {
            try {
                // Try to load from classpath first
                java.net.URL imageUrl = getClass().getResource(path);
                if (imageUrl != null) {
                    backgroundImage = ImageIO.read(imageUrl);
                    System.out.println("Successfully loaded image from: " + path);
                    return;
                }
                
                // If not found in classpath, try filesystem
                File file = new File(path);
                if (file.exists()) {
                    backgroundImage = ImageIO.read(file);
                    System.out.println("Successfully loaded image from filesystem: " + file.getAbsolutePath());
                    return;
                }
                
                System.out.println("File not found at: " + file.getAbsolutePath());
                
            } catch (IOException e) {
                System.err.println("Error loading image from " + path + ": " + e.getMessage());
            }
        }
        
        // If we get here, all paths failed
        System.err.println("Failed to load background image. Tried paths: " + String.join(", ", possiblePaths));
        System.err.println("Current working directory: " + System.getProperty("user.dir"));
        
        // Create a default background
        backgroundImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillRect(0, 0, 1280, 720);
        g2d.dispose();
    }
    
    private void createStartButton() {
        startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBounds(WIDTH/2 - 100, HEIGHT - 150, 200, 50);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setForeground(Color.WHITE);
        
        // Add hover effect
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setForeground(new Color(255, 215, 0)); // Gold color on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setForeground(Color.WHITE);
            }
        });
        
        startButton.addActionListener(e -> controller.startGame());
        add(startButton);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        if (backgroundImage != null) {
            // Get current panel size
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            
            // Calculate scaling factors
            double widthRatio = (double) panelWidth / backgroundImage.getWidth();
            double heightRatio = (double) panelHeight / backgroundImage.getHeight();
            
            // Use the smaller ratio to ensure the whole image is visible (letterboxing)
            double scale = Math.min(widthRatio, heightRatio);
            
            // Calculate new dimensions
            int newWidth = (int)(backgroundImage.getWidth() * scale);
            int newHeight = (int)(backgroundImage.getHeight() * scale);
            
            // Calculate position to center the image
            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;
            
            // Fill background with black before drawing the image
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, panelWidth, panelHeight);
            
            // Draw the scaled image
            g2d.drawImage(backgroundImage, x, y, newWidth, newHeight, this);
        } else {
            // Fallback background if no image is loaded
            g2d.setColor(new Color(0, 100, 0));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw version info (small and subtle)
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.drawString("v1.0", 20, HEIGHT - 20);
    }
}
