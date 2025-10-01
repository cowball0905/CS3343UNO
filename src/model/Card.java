package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Graphics;

public abstract class Card {
    protected BufferedImage image;
    protected int x, y;
    protected int width = 80;
    protected int height = 120;
    protected Color color;
    protected Type type;

    public Card(Type type, Color color) {
        this.type = type;
        this.color = color;
        loadImage();

    }

    // In Card.java, update the loadImage method
    protected void loadImage() {
        try {
            String imageName;
            if (type == Type.Wild || type == Type.WildDrawFour) {
                // Handle wild cards
                imageName = (type == Type.Wild) ? 
                    "Wild_Card_Change_Colour.png" : "Wild_Card_Draw_4.png";
            } else {
                String colorName = color != null ? color.name() : "Wild";
                String typeName;
                
                if (this instanceof NumberCard) {
                    // For NumberCard, use the actual number value
                    typeName = String.valueOf(((NumberCard)this).getValue());
                } else {
                    // For action cards
                    switch (type) {
                        case Skip: typeName = "Skip"; break;
                        case Reverse: typeName = "Reverse"; break;
                        case DrawTwo: typeName = "Draw_2"; break;
                        default: typeName = type.toString();
                    }
                }
                
                imageName = colorName + "_" + typeName + ".png";
            }
    
            System.out.println("Trying to load image: " + imageName);
            
            // Try multiple possible locations
            String[] possiblePaths = {
                "/asset/uno-card-images-master/" + imageName,                      // For JAR packaging
                "src/asset/uno-card-images-master/" + imageName,                   // For development
            };
            
            for (String path : possiblePaths) {
                try {
                    // Try as file first
                    File file = new File(path);
                    if (file.exists() && !file.isDirectory()) {
                        System.out.println("Found at: " + file.getAbsolutePath());
                        image = ImageIO.read(file);
                        if (image != null) break;
                    }
                    
                    // Try as resource
                    java.net.URL imageUrl = getClass().getClassLoader().getResource(path);
                    if (imageUrl == null) {
                        // Try with leading slash
                        imageUrl = getClass().getResource("/" + path);
                    }
                    if (imageUrl != null) {
                        System.out.println("Found as resource: " + imageUrl);
                        image = ImageIO.read(imageUrl);
                        if (image != null) break;
                    }
                } catch (Exception e) {
                    System.err.println("Error loading from " + path + ": " + e.getMessage());
                }
            }
            
            if (image == null) {
                throw new IOException("Could not find image: " + imageName);
            }
    
            // Scale the image
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                               RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, width, height, null);
            g2d.dispose();
            image = scaledImage;
                
        } catch (Exception e) {
            System.err.println("Error loading card image: " + e.getMessage());
            
        }
    }
    public BufferedImage getImage() {
        return image;
    }

    public Color getColor() {
        return color;
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

    public Type getType() {
        return type;
    }

    public void draw(Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    abstract public void cardFunction();
}
