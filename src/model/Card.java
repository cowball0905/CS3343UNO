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
    
    // 旋轉相關屬性
    protected boolean isRotated = false;  // 是否旋轉
    protected double rotationAngle = 0.0; // 旋轉角度（度數）

    public Card(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    // In Card.java, update the loadImage method
    protected void loadImage(String path) {
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
                BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaledImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(image, 0, 0, width, height, null);
                g2d.dispose();
                image = scaledImage;
            }
        } catch (IOException e) {
            System.err.println("Error loading card image: " + path);
            e.printStackTrace();
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

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
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
    
    // 設定旋轉角度
    public void setRotation(double angle) {
        this.rotationAngle = angle;
        this.isRotated = (angle != 0);
        // 如果旋轉90度或270度，交換寬高
        if (angle == 90 || angle == 270) {
            int temp = width;
            width = height;
            height = temp;
        }
    }
    
    // 獲取是否旋轉
    public boolean isRotated() {
        return isRotated;
    }
    
    // 獲取旋轉角度
    public double getRotationAngle() {
        return rotationAngle;
    }
    
    // 旋轉圖片的方法
    public BufferedImage rotateImage(BufferedImage image, double angle) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // 計算旋轉後的新尺寸
        BufferedImage rotatedImage = new BufferedImage(height, width, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        
        // 設定高質量渲染
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // 移動到旋轉中心，旋轉，然後繪製
        g2d.translate(height / 2, width / 2);
        g2d.rotate(Math.toRadians(angle));
        g2d.translate(-width / 2, -height / 2);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        return rotatedImage;
    }

    abstract public void cardFunction();
}
