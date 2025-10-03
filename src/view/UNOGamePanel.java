package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
            topCard.setPosition(450, 240);
        }
        
        // 玩家手牌 - 底部水平排列（不旋轉）
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            // 不旋轉
            int cardX = (WIDTH / 2) - (playerHand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X) + 60;
            int cardY = HEIGHT - 150;
            card.setPosition(cardX, cardY);
        }
        
        // CPU1 - 左側垂直排列
        for (int i = 0; i < computer1Hand.size(); i++) {
            Card card = computer1Hand.get(i);
            card.setRotation(90); // 順時針旋轉90度
            
            // X坐標：固定在左側
            int cardX = 50 + 60;
            // Y坐標：垂直居中排列
            int cardY = (HEIGHT / 2) - (computer1Hand.size() * CARD_OFFSET_Y / 2) + (i * CARD_OFFSET_Y);
            
            card.setPosition(cardX, cardY);
        }

        // CPU3 - 右側垂直排列
        for (int i = 0; i < computer3Hand.size(); i++) {
            Card card = computer3Hand.get(i);
            card.setRotation(270); // 逆時針旋轉90度
            
            // X坐標：固定在右側
            int cardX = WIDTH - 50 - card.getWidth() + 120;
            // Y坐標：垂直居中排列
            int cardY = (HEIGHT / 2) - (computer3Hand.size() * CARD_OFFSET_Y / 2) + (i * CARD_OFFSET_Y);
            
            card.setPosition(cardX, cardY);
        }

        // CPU2 - 頂部水平排列（不旋轉）
        for (int i = 0; i < computer2Hand.size(); i++) {
            Card card = computer2Hand.get(i);
            // 不旋轉
            int cardX = (WIDTH / 2) - (computer2Hand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X) + 60;
            int cardY = 50;
            card.setPosition(cardX, cardY);
        }
    }
    
    // private void handleCardClick(int x, int y) {
    //     // Check if player clicked on a card in their hand
    //     Card clickedCard = controller.getCardAt(x, y);
    //     if (clickedCard != null) {
    //         controller.playCard(clickedCard);
    //         updateDisplay();
    //         return;
    //     }
        
    //     // Check if player clicked on the deck
    //     if (controller.isDeckClicked(x, y)) {
    //         controller.drawCard();
    //         updateDisplay();
    //     }
    // }
    

    
    // 繪製旋轉卡牌的方法
    private void drawRotatedCard(Graphics2D g2d, Card card) {
        // 檢查卡牌和圖像是否存在
        if (card == null || card.getImage() == null) {
            System.err.println("Warning: Card or image " + card.getType().toString() + " " + card.getColor().toString() + " is null, skipping draw");
            return;
        }
        
        if (card.isRotated()) {
            // 保存當前變換狀態
            //保存當前的圖形變換狀態，以便後續恢復
            AffineTransform oldTransform = g2d.getTransform();
            
            // 獲取圖像的原始尺寸（旋轉前）
            int imageWidth = card.getImage().getWidth();
            int imageHeight = card.getImage().getHeight();
            
            // 計算旋轉中心 - 使用卡牌在螢幕上應該占據的空間
            /*計算公式分解：

            WIDTH / 2: 螢幕寬度的中心點 (400)
            playerHand.size() * CARD_OFFSET_X: 所有卡牌總寬度
            (playerHand.size() * CARD_OFFSET_X / 2): 所有卡牌總寬度的一半
            i * CARD_OFFSET_X: 第 i 張卡牌的偏移量
            實際計算範例： 假設玩家有 5 張牌，CARD_OFFSET_X = 25：

            第1張(i=0): cardX = 400 - (5×25/2) + (0×25) = 400 - 62.5 + 0 = 337.5
            第2張(i=1): cardX = 400 - 62.5 + 25 = 362.5
            第3張(i=2): cardX = 400 - 62.5 + 50 = 387.5 (中心)
            第4張(i=3): cardX = 400 - 62.5 + 75 = 412.5
            第5張(i=4): cardX = 400 - 62.5 + 100 = 437.5 
            */
            double centerX = card.getX() + card.getWidth() / 2.0;
            double centerY = card.getY() + card.getHeight() / 2.0;
            
            // 移動到旋轉中心
            // 平移
            g2d.translate(centerX, centerY);
            // 執行旋轉
            g2d.rotate(Math.toRadians(card.getRotationAngle()));
            // 移動到繪製位置（圖像左上角）
            g2d.translate(-imageWidth / 2.0, -imageHeight / 2.0);
            
            // 繪製卡牌 - 使用原始圖像尺寸
            g2d.drawImage(card.getImage(), 0, 0, imageWidth, imageHeight, this);
            
            // 恢復變換狀態
            // 如果不恢復變換，第二張卡牌也會被旋轉90度！
            g2d.setTransform(oldTransform);
        } else {
            // 正常繪製
            g2d.drawImage(card.getImage(), card.getX(), card.getY(), 
                         card.getWidth(), card.getHeight(), this);
        }
    }
    
    public void startGame() {
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
        
        // Null safety checks
        if (playerHand == null) playerHand = new java.util.ArrayList<>();
        if (computer1Hand == null) computer1Hand = new java.util.ArrayList<>();
        if (computer2Hand == null) computer2Hand = new java.util.ArrayList<>();
        if (computer3Hand == null) computer3Hand = new java.util.ArrayList<>();
        
        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Draw background
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // === 第一層：繪製所有卡牌 (底層) ===
        // Draw CPU1 cards
        for (Card card : computer1Hand) {
            drawRotatedCard(g2d, card);
        }
        
        // Draw CPU2 cards
        for (Card card : computer2Hand) {
            drawRotatedCard(g2d, card);
        }
        
        // Draw CPU3 cards
        for (Card card : computer3Hand) {
            drawRotatedCard(g2d, card);
        }
        
        // === 第二層：繪製所有文字標籤 (上層，不會被卡牌覆蓋) ===
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        
        // CPU1 標籤 (左側)
        g2d.drawString("CPU 1 (" + computer1Hand.size() + " cards)", 105, 220);
        
        // CPU2 標籤 (頂部居中)
        g2d.drawString("CPU 2 (" + computer2Hand.size() + " cards)", 
                         420, 40);
        
        // CPU3 標籤 (右側)
        g2d.drawString("CPU 3 (" + computer3Hand.size() + " cards)", 745, 220);
        
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
        
        // === 繼續第一層：繪製其他卡牌 ===
        // Draw player's cards (底層)
        for (Card card : playerHand) {
            drawRotatedCard(g2d, card);
        }
        
        // Draw discard pile (top card)
        if (topCard != null) {
            g2d.drawImage(topCard.getImage(),
                         topCard.getX(), topCard.getY(),
                         topCard.getWidth(), topCard.getHeight(),
                         this);
        }
        
        // === 第二層：繼續繪製文字標籤 (上層) ===
        // Player 標籤
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        g2d.drawString("YOU (" + playerHand.size() + " cards)", 420, 600);
        
        // Discard pile label
        if (topCard != null) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("DISCARD", 
                topCard.getX() + (topCard.getWidth() / 2) - 30, 
                topCard.getY() + topCard.getHeight() + 20);
        }
    }
}