package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import controller.UNOController;
import model.Card;
import java.awt.image.BufferedImage;

public class UNOGamePanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private List <JButton> cardButtons = new ArrayList<JButton>();
    private int currentSelectedCardIndex = -1;
    private String errorMessage = null;
    private long errorMessageTimer = 0;
    
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
    }
    
    private static final int CARD_OFFSET_X = 25; // 水平方向的卡牌間距
    private static final int CARD_OFFSET_Y = 20; // 垂直方向的卡牌間距

    
    private JButton updateDeck(){
        JButton button = new JButton();
        button.setBounds(140, 30, 80, 120);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            controller.getCardFromDeck();
            updateDisplay();
        });
        
        return button;
    }
    
    private void updateCardPositions() {
        // Get current game state from controller
        List<Card> playerHand = controller.getPlayedCard();
        List<Card> computer1Hand = controller.getCPUCard(0);
        List<Card> computer2Hand = controller.getCPUCard(1);
        List<Card> computer3Hand = controller.getCPUCard(2);
        Card topCard = controller.getTopCard();

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

    public JButton createCardButton(Card card, int index, List<Card> Cards) {
        JButton button = new JButton();
        button.setBounds(card.getX(), card.getY(), card.getWidth(), card.getHeight());
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            selectedCard(index);
        });
        
        return button;
    }

public void selectedCard(int index) {
        List<Card> playerHand = controller.getPlayedCard();
        if (currentSelectedCardIndex == -1){ //Click unselected card
            currentSelectedCardIndex = index;
            playerHand.get(index).setCardSelected(true);
        } else if (index == currentSelectedCardIndex){ //Click selected card
            Card selectedCard = playerHand.get(index);
            boolean isPlayed = controller.canPlayCard(selectedCard);
            if (isPlayed){
                controller.playCard(selectedCard);
                errorMessage = "Card played!";
                errorMessageTimer = System.currentTimeMillis();

                currentSelectedCardIndex = -1;
                selectedCard.setCardSelected(false);

                selectedCard.cardFunction(); //Using polymorphism to handle special card effects

                updateDisplay();
            } else {
                errorMessage = "Can't play this card!";
                errorMessageTimer = System.currentTimeMillis();
            }
        } else { // Click another card
            playerHand.get(currentSelectedCardIndex).setCardSelected(false);
            currentSelectedCardIndex = index;
            playerHand.get(index).setCardSelected(true);
        }
        updateCardButtons();
        updateDeck();
        repaint();
    }
    
    public void updateCardButtons() {
        for (JButton btn : cardButtons) {
            remove(btn);
        }
        cardButtons.clear();
        List<Card> playerHand = controller.getPlayedCard();
        
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            JButton button = createCardButton(card, i, playerHand);
            cardButtons.add(button);
            add(button);
        }
        for (int i = 0; i < playerHand.size(); i++) {
            JButton button = cardButtons.get(i);
            setComponentZOrder(button, playerHand.size() - 1 - i);
        }

        JButton button = updateDeck();
        add(button);
        
        revalidate();
    }

    public void startGame() {
        updateDisplay();
    }
    
    public void updateDisplay() {
        updateCardPositions();
        updateCardButtons();
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

        ImageIcon icon = new ImageIcon(getClass().getResource("/asset/uno-card-images-master/Back.jpg"));
        if (icon.getImage() == null) {
            System.err.println("Failed to load card back image");
        } else {
            g2d.drawImage(icon.getImage(), 140, 30, 80, 120, null);
        }
        g2d.drawString("DECK", 160, 165);
        
        // Draw error message if it exists and hasn't timed out
        if (errorMessage != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - errorMessageTimer < 1000) { // Show message for 2 seconds
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                // Draw the error message in the center of the screen
                g2d.drawString(errorMessage, 630, 580);
            } else {
                errorMessage = null; // Clear the message after timeout
            }
        }
    }
}