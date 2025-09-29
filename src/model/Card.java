package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

enum Color{
    Red, Green, Blue, Yellow;
}

enum Type{
    Number, Skip, Reverse, DrawTwo, Wild, WildDrawFour, Deck;
}

public abstract class Card {
    private BufferedImage image;
    private int x, y;
    private static final int CARD_WIDTH = 80;  // Standard width for all cards
    private static final int CARD_HEIGHT = 120; // Standard height for all cards
    private int width = CARD_WIDTH;
    private int height = CARD_HEIGHT;
    private Color color;
    private Type type;

    public Card(Type type, Color color) {
        this.type = type;
        this.color = color;
    }
    
    private void loadImage(String path) {
        try {
            // Try to load from classpath (works in JAR)
            java.net.URL imageUrl = getClass().getResource(path);
            if (imageUrl != null) {
                image = ImageIO.read(imageUrl);
            } else {
                // Fallback to file system (for development)
                image = ImageIO.read(new File("src" + path));
            }
            
            if (image != null) {
                // Scale the image to standard size while maintaining aspect ratio
                BufferedImage scaledImage = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaledImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(image, 0, 0, CARD_WIDTH, CARD_HEIGHT, null);
                g2d.dispose();
                image = scaledImage;
                width = CARD_WIDTH;
                height = CARD_HEIGHT;
            }
        } catch (IOException e) {
            System.err.println("Error loading card image: " + path);
            e.printStackTrace();
        }
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public String getColor() {
        return color.toString();
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean contains(int pointX, int pointY) {
        return pointX >= x && pointX <= x + width &&
               pointY >= y && pointY <= y + height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    abstract public void cardFunction();
}
