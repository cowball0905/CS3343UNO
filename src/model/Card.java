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
    protected String cardImagePath;  // 保存卡牌真實圖像路徑
    
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
        System.out.println("setSize called: " + width + "x" + height);
        this.width = width;
        this.height = height;
        System.out.println("After setting: " + this.width + "x" + this.height);
    }
    
    public boolean isCardSelected(){
        return isSelected;
    }

    public void setCardSelected(boolean selected){
        this.isSelected = selected;
        if (selected){
            y = y - 40;
        } else {
            y = y + 40;
        }
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
        if (angle == 90 || angle == 270) {
            int tempWidth = this.width;
            int tempHeight = this.height;
            this.width = tempHeight;
            this.height = tempWidth;
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
    
    // 獲取是否已翻牌
    public boolean isRevealed() {
        return isRevealed;
    }
    
    // 設置翻牌狀態
    public void setRevealed(boolean revealed) {
        this.isRevealed = revealed;
        if (cardImagePath != null) {
            loadImage(cardImagePath);  // 重新載入對應圖像
        }
    }

    abstract public int cardFunction();
}
