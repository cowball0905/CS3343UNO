package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import controller.UNOController;
import model.Card;

public class UNOGamePanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    private UNOController controller;
    private JButton menuButton;
    // Card dimensions can be accessed from the Card class if needed
    
    public UNOGamePanel(UNOController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0, 100, 0)); // Slightly darker green for game area
        setLayout(null);
        
        // Initialize menu button
        menuButton = new JButton("Back to Menu");
        menuButton.setBounds(10, 10, 120, 30);
        menuButton.addActionListener(e -> controller.showMenu());
        add(menuButton);
        
        // UI initialization only
        
        // Add mouse listener for card selection
        // addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         handleCardClick(e.getX(), e.getY());
        //     }
        // });
    }
    
    private static final int CARD_OFFSET_X = 25; // 水平方向的卡牌間距
    private static final int CARD_OFFSET_Y = 20; // 垂直方向的卡牌間距
    
    private void updateCardPositions() {
        // Get current game state from controller
        List<Card> playerHand = controller.getPlayedCard();
        List<Card> computer1Hand = controller.getCPUCard(0);
        List<Card> computer2Hand = controller.getCPUCard(1);
        List<Card> computer3Hand = controller.getCPUCard(2);
        Card topCard = controller.getTopCard();
        // Card deckCard = controller.getDeckCard();
        
        // // Set positions for deck and top card
        // if (deckCard != null) {
        //     deckCard.setPosition(50, 200);
        // }
        if (topCard != null) {
            topCard.setPosition(200, 200);
        }
        
        // 玩家手牌 - 底部水平排列（不旋轉）
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            // 不旋轉
            int cardX = (WIDTH / 2) - (playerHand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X);
            int cardY = HEIGHT - 150;
            card.setPosition(cardX, cardY);
        }
        
        // CPU1 - 左側垂直排列
        for (int i = 0; i < computer1Hand.size(); i++) {
            Card card = computer1Hand.get(i);
            card.setRotation(90); // 順時針旋轉90度
            
            // X坐標：固定在左側
            int cardX = 50;
            // Y坐標：垂直居中排列
            int cardY = (HEIGHT / 2) - (computer1Hand.size() * CARD_OFFSET_Y / 2) + (i * CARD_OFFSET_Y);
            
            card.setPosition(cardX, cardY);
        }

        // CPU3 - 右側垂直排列
        for (int i = 0; i < computer3Hand.size(); i++) {
            Card card = computer3Hand.get(i);
            card.setRotation(270); // 逆時針旋轉90度
            
            // X坐標：固定在右側
            int cardX = WIDTH - 50 - card.getWidth();
            // Y坐標：垂直居中排列
            int cardY = (HEIGHT / 2) - (computer3Hand.size() * CARD_OFFSET_Y / 2) + (i * CARD_OFFSET_Y);
            
            card.setPosition(cardX, cardY);
        }

        // CPU2 - 頂部水平排列（不旋轉）
        for (int i = 0; i < computer2Hand.size(); i++) {
            Card card = computer2Hand.get(i);
            // 不旋轉
            int cardX = (WIDTH / 2) - (computer2Hand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X);
            int cardY = 50;
            card.setPosition(cardX, cardY);
        }
    }
    
    // private void handleCardClick(int x, int y) {
    //     // Check if player clicked on a card in their hand
    //     Card clickedCard = controller.getCardAt(x, y);
    //     if (clickedCard != null) {
    //         controller.playCard(clickedCard);
    //         return;
    //     }
        
    //     // Check if player clicked on the deck
    //     if (controller.isDeckClicked(x, y)) {
    //         controller.drawCard();
    //     }
    // }
    
    private void drawCenteredString(Graphics2D g, String text, java.awt.Rectangle rect) {
        // Center text in the given rectangle
        java.awt.FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
    }
    
    // 繪製旋轉卡牌的方法
    private void drawRotatedCard(Graphics2D g2d, Card card) {
        if (card.isRotated()) {
            // 保存當前變換狀態
            AffineTransform oldTransform = g2d.getTransform();
            
            // 獲取圖像的原始尺寸（旋轉前）
            int imageWidth = card.getImage().getWidth();
            int imageHeight = card.getImage().getHeight();
            
            // 計算旋轉中心 - 使用卡牌在螢幕上應該占據的空間
            double centerX = card.getX() + card.getWidth() / 2.0;
            double centerY = card.getY() + card.getHeight() / 2.0;
            
            // 移動到旋轉中心
            g2d.translate(centerX, centerY);
            // 執行旋轉
            g2d.rotate(Math.toRadians(card.getRotationAngle()));
            // 移動到繪製位置（圖像左上角）
            g2d.translate(-imageWidth / 2.0, -imageHeight / 2.0);
            
            // 繪製卡牌 - 使用原始圖像尺寸
            g2d.drawImage(card.getImage(), 0, 0, imageWidth, imageHeight, this);
            
            // 恢復變換狀態
            g2d.setTransform(oldTransform);
        } else {
            // 正常繪製
            g2d.drawImage(card.getImage(), card.getX(), card.getY(), 
                         card.getWidth(), card.getHeight(), this);
        }
    }
    
    public void startGame() {
        // Just update the display
        updateDisplay();
    }
    
    public void updateDisplay() {
        updateCardPositions();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Get current game state from controller
        List<Card> playerHand = controller.getPlayedCard();
        List<Card> computer1Hand = controller.getCPUCard(0);
        List<Card> computer2Hand = controller.getCPUCard(1);
        List<Card> computer3Hand = controller.getCPUCard(2);
        Card topCard = controller.getTopCard();
        
        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Draw background
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw computer's hand (face down)
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "CPU 1 (" + computer1Hand.size() + " cards)", 
                         new java.awt.Rectangle(0, 20, WIDTH, 30));
                         
        // Draw computer's cards (face down)
        for (Card card : computer1Hand) {
            drawRotatedCard(g2d, card);
        }

        // Draw computer's hand (face down)
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "CPU 2 (" + computer2Hand.size() + " cards)", 
                         new java.awt.Rectangle(0, 20, WIDTH, 30));
                         
        // Draw computer's cards (face down)
        for (Card card : computer2Hand) {
            drawRotatedCard(g2d, card);
        }

        // Draw computer's hand (face down)
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "CPU 3 (" + computer3Hand.size() + " cards)", 
                         new java.awt.Rectangle(0, 20, WIDTH, 30));
                         
        // Draw computer's cards (face down)
        for (Card card : computer3Hand) {
            drawRotatedCard(g2d, card);
        }
        
        // // Draw deck (back of cards)
        // if (deckCard != null) {
        //     g2d.drawImage(deckCard.getImage(), 
        //                  deckCard.getX(), deckCard.getY(),
        //                  deckCard.getWidth(), deckCard.getHeight(),
        //                  this);
            
        //     // Draw deck label
        //     g2d.setColor(Color.WHITE);
        //     g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        //     drawCenteredString(g2d, "DECK", 
        //         new java.awt.Rectangle(deckCard.getX(), deckCard.getY() + deckCard.getHeight() + 5, 
        //                              deckCard.getWidth(), 20));
        // }
        
        // Draw discard pile (top card)
        if (topCard != null) {
            g2d.drawImage(topCard.getImage(),
                         topCard.getX(), topCard.getY(),
                         topCard.getWidth(), topCard.getHeight(),
                         this);
            
            // Draw discard pile label
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            drawCenteredString(g2d, "DISCARD", 
                new java.awt.Rectangle(topCard.getX(), topCard.getY() + topCard.getHeight() + 5, 
                                     topCard.getWidth(), 20));
        }
        
        // Draw player's hand (face up)
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "YOU (" + playerHand.size() + " cards)", 
                         new java.awt.Rectangle(0, HEIGHT - 150, WIDTH, 30));
        
        // Draw cards in player's hand
        for (Card card : playerHand) {
            drawRotatedCard(g2d, card);
        }
    }
}