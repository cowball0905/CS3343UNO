package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    protected Boolean isRevealed;
    protected Boolean isSelected;
    protected String cardImagePath;
    
    // 旋轉相關屬性
    protected boolean isRotated = false;  // 是否旋轉
    protected double rotationAngle = 0.0; // 旋轉角度（度數）

    public Card(Type type, Color color, boolean isRevealed) {
        this.type = type;
        this.color = color;
        this.isRevealed = isRevealed;
    }

    // In Card.java, update the loadImage method
    protected void loadImage(String path) {
        this.cardImagePath = path;  // 保存真實卡牌路徑
        
        // 根據isRevealed決定載入哪個圖像
        String actualPath = isRevealed ? path : "/asset/uno-card-images-master/Back.jpg";
        
        try {
            // Try to load from classpath (works in JAR)
            java.net.URL imageUrl = getClass().getResource(actualPath);
            if (imageUrl != null) {
                image = ImageIO.read(imageUrl);
            } else {
                // Fallback to file system (for development)
                image = ImageIO.read(new File("src" + actualPath));
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

    public void setColor(Color red) {
        this.color = red;
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
    
    public boolean isCardSelected(){
        return isSelected;
    }

    public void setCardSelected(boolean selected){
        this.isSelected = selected;
        if (selected){
            y = 410;
        } else {
            y = 450;
        }
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
        g.drawImage(image, x, y, null);
    }
    
    public void setRotation(double angle) {
        boolean isRotated1 = false; 
        if(this.rotationAngle == 90 || this.rotationAngle == 270){
            isRotated1 = true;
        }
        boolean isRotated2 = false; 
        if(angle==90||angle==270){
            isRotated2 = true;
        }
        if (isRotated1 != isRotated2) {
            int tempWidth = this.width;
            int tempHeight = this.height;
            this.width = tempHeight;
            this.height = tempWidth;
        }
        this.rotationAngle = angle;
        if(angle!=0){
            this.isRotated = true;
        }
    }
    
    public boolean isRotated() {
        return isRotated;
    }
    
    public double getRotationAngle() {
        return rotationAngle;
    }

    public boolean isRevealed() {
        return isRevealed;
    }
    
    public void setRevealed(boolean revealed) {
        this.isRevealed = revealed;
        if (cardImagePath != null) {
            loadImage(cardImagePath);
        }
    }

    abstract public void cardFunction();
}
