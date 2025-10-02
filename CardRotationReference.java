/**
 * 卡牌旋转功能参考代码
 * Card Rotation Reference Code
 * 
 * 这个文件包含了实现卡牌旋转功能所需的所有代码示例
 * This file contains all the code examples needed to implement card rotation functionality
 * 
 * 使用说明 / Usage Instructions:
 * 1. 将相关方法复制到对应的类中
 * 2. 根据需要修改和调整代码
 * 3. 添加必要的导入语句
 */

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class CardRotationReference {
    
    // ================================
    // 1. Card.java 中需要添加的代码
    // Code to add to Card.java
    // ================================
    
    /**
     * 在 Card.java 类中添加这些属性和方法
     * Add these properties and methods to Card.java class
     */
    /*
    // 新增属性 / New Properties
    protected boolean isRotated = false;  // 是否旋转 / Whether rotated
    protected double rotationAngle = 0.0; // 旋转角度 / Rotation angle
    
    // 设置旋转角度 / Set rotation angle
    public void setRotation(double angle) {
        this.rotationAngle = angle;
        this.isRotated = (angle != 0);
        // 如果旋转90度或270度，交换宽高 / Swap width/height for 90/270 degree rotation
        if (angle == 90 || angle == 270) {
            int temp = width;
            width = height;
            height = temp;
        }
    }
    
    // 获取是否旋转 / Get rotation status
    public boolean isRotated() {
        return isRotated;
    }
    
    // 获取旋转角度 / Get rotation angle
    public double getRotationAngle() {
        return rotationAngle;
    }
    
    // 旋转图片的方法 / Method to rotate image
    public BufferedImage rotateImage(BufferedImage image, double angle) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // 计算旋转后的新尺寸 / Calculate new dimensions after rotation
        BufferedImage rotatedImage = new BufferedImage(height, width, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        
        // 设置高质量渲染 / Set high quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // 移动到旋转中心，旋转，然后绘制 / Move to rotation center, rotate, then draw
        g2d.translate(height / 2, width / 2);
        g2d.rotate(Math.toRadians(angle));
        g2d.translate(-width / 2, -height / 2);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        
        return rotatedImage;
    }
    */
    
    // ================================
    // 2. UNOGamePanel.java 中需要添加的代码
    // Code to add to UNOGamePanel.java
    // ================================
    
    /**
     * 在 UNOGamePanel.java 中添加这些常量和方法
     * Add these constants and methods to UNOGamePanel.java
     */
    
    // 新增常量 / New Constants
    private static final int CARD_OFFSET_Y = 20; // 垂直方向的卡牌间距 / Vertical card spacing
    
    /**
     * 绘制旋转的卡牌方法 / Method to draw rotated cards
     * 将此方法添加到 UNOGamePanel.java 中 / Add this method to UNOGamePanel.java
     */
    private void drawRotatedCard(Graphics2D g2d, Object card) {
        // 注意：这里的 card 参数类型应该是你的 Card 类
        // Note: The card parameter type should be your Card class
        
        /*
        if (card.isRotated()) {
            // 保存当前变换状态 / Save current transform state
            AffineTransform oldTransform = g2d.getTransform();
            
            // 移动到卡牌中心，旋转，再移回来 / Move to card center, rotate, move back
            g2d.translate(card.getX() + card.getWidth()/2, card.getY() + card.getHeight()/2);
            g2d.rotate(Math.toRadians(card.getRotationAngle()));
            g2d.translate(-card.getWidth()/2, -card.getHeight()/2);
            
            // 绘制卡牌 / Draw card
            g2d.drawImage(card.getImage(), 0, 0, card.getWidth(), card.getHeight(), this);
            
            // 恢复变换状态 / Restore transform state
            g2d.setTransform(oldTransform);
        } else {
            // 正常绘制 / Normal drawing
            g2d.drawImage(card.getImage(), card.getX(), card.getY(), 
                         card.getWidth(), card.getHeight(), this);
        }
        */
    }
    
    /**
     * updateCardPositions() 方法的修改示例
     * Example modifications for updateCardPositions() method
     */
    private void updateCardPositionsExample() {
        // 这些是示例代码，显示如何修改位置计算
        // These are example codes showing how to modify position calculations
        
        /*
        // CPU1 - 左侧垂直排列 / CPU1 - Left side vertical layout
        for (int i = 0; i < computer1Hand.size(); i++) {
            Card card = computer1Hand.get(i);
            card.setRotation(90); // 顺时针旋转90度 / Rotate 90 degrees clockwise
            
            // X坐标：固定在左侧 / X coordinate: fixed on left side
            int cardX = 50; 
            
            // Y坐标：垂直居中排列 / Y coordinate: vertical center alignment
            int cardY = (HEIGHT / 2) - (computer1Hand.size() * CARD_OFFSET_Y / 2) + (i * CARD_OFFSET_Y);
            
            card.setPosition(cardX, cardY);
        }
        
        // CPU2 - 顶部水平排列（保持原样）/ CPU2 - Top horizontal layout (keep original)
        for (int i = 0; i < computer2Hand.size(); i++) {
            Card card = computer2Hand.get(i);
            // 不旋转 / No rotation
            int cardX = (WIDTH / 2) - (computer2Hand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X);
            int cardY = 50;
            card.setPosition(cardX, cardY);
        }
        
        // CPU3 - 右侧垂直排列 / CPU3 - Right side vertical layout
        for (int i = 0; i < computer3Hand.size(); i++) {
            Card card = computer3Hand.get(i);
            card.setRotation(270); // 逆时针旋转90度 / Rotate 270 degrees (or -90)
            
            // X坐标：固定在右侧 / X coordinate: fixed on right side
            int cardX = WIDTH - 50 - card.getWidth(); // 右侧边距50像素 / 50px margin from right
            
            // Y坐标：垂直居中排列 / Y coordinate: vertical center alignment
            int cardY = (HEIGHT / 2) - (computer3Hand.size() * CARD_OFFSET_Y / 2) + (i * CARD_OFFSET_Y);
            
            card.setPosition(cardX, cardY);
        }
        
        // 玩家手牌（保持原样）/ Player hand (keep original)
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            // 不旋转 / No rotation
            int cardX = (WIDTH / 2) - (playerHand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X);
            int cardY = HEIGHT - 150;
            card.setPosition(cardX, cardY);
        }
        */
    }
    
    /**
     * paintComponent() 方法的修改示例
     * Example modifications for paintComponent() method
     */
    private void paintComponentExample() {
        // 将所有的卡牌绘制改为使用 drawRotatedCard 方法
        // Change all card drawing to use drawRotatedCard method
        
        /*
        // 原来的代码 / Original code:
        // g2d.drawImage(card.getImage(), card.getX(), card.getY(), card.getWidth(), card.getHeight(), this);
        
        // 新的代码 / New code:
        // drawRotatedCard(g2d, card);
        
        // 示例：绘制所有CPU1卡牌 / Example: Draw all CPU1 cards
        for (Card card : computer1Hand) {
            drawRotatedCard(g2d, card);
        }
        
        // 示例：绘制所有CPU2卡牌 / Example: Draw all CPU2 cards
        for (Card card : computer2Hand) {
            drawRotatedCard(g2d, card);
        }
        
        // 示例：绘制所有CPU3卡牌 / Example: Draw all CPU3 cards
        for (Card card : computer3Hand) {
            drawRotatedCard(g2d, card);
        }
        
        // 示例：绘制所有玩家卡牌 / Example: Draw all player cards
        for (Card card : playerHand) {
            drawRotatedCard(g2d, card);
        }
        */
    }
    
    // ================================
    // 3. 需要添加的导入语句
    // Import statements to add
    // ================================
    
    /**
     * 在相关的Java文件顶部添加这些导入语句
     * Add these import statements at the top of relevant Java files
     */
    /*
    // 在 UNOGamePanel.java 中添加 / Add to UNOGamePanel.java:
    import java.awt.geom.AffineTransform;
    
    // 在 Card.java 中添加 / Add to Card.java:
    import java.awt.Graphics2D;
    import java.awt.RenderingHints;
    import java.awt.image.BufferedImage;
    */
    
    // ================================
    // 4. 布局效果说明
    // Layout Effect Description
    // ================================
    
    /**
     * 最终的布局效果 / Final Layout Effect:
     * 
     *           CPU2 (顶部水平) / CPU2 (Top Horizontal)
     *           [卡][卡][卡][卡][卡]
     * 
     * CPU1        中央区域         CPU3
     * (左侧)      牌堆 顶牌       (右侧)
     * [卡]        [卡] [卡]      [卡]
     * [卡]                       [卡]  
     * [卡]                       [卡]
     * [卡]                       [卡]
     * [卡]                       [卡]
     * 
     *           玩家 (底部水平) / Player (Bottom Horizontal)
     *           [卡][卡][卡][卡][卡]
     */
    
    // ================================
    // 5. 实施步骤
    // Implementation Steps
    // ================================
    
    /**
     * 实施顺序 / Implementation Order:
     * 
     * 1. 修改 Card.java:
     *    - 添加 isRotated, rotationAngle 属性
     *    - 添加 setRotation(), isRotated(), getRotationAngle() 方法
     *    - 添加 rotateImage() 方法（可选）
     * 
     * 2. 修改 UNOGamePanel.java:
     *    - 添加 CARD_OFFSET_Y 常量
     *    - 添加 drawRotatedCard() 方法
     *    - 修改 updateCardPositions() 方法中的位置计算
     *    - 修改 paintComponent() 方法中的绘制调用
     * 
     * 3. 测试:
     *    - 编译并运行程序
     *    - 检查卡牌是否正确旋转和定位
     *    - 调整位置和角度参数
     */
}