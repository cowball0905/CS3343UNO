package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import controller.UNOController;
import model.Card;
import model.Player;

public class UNOGamePanel extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int card_gap_x = 25; 
    private static final int card_gap_y = 20; 
    private List <JButton> cardButtons = new ArrayList<JButton>();
    private int currentSelectedCardIndex = -1;
    private String errorMessage = null;
    private long errorMessageTimer = 0;
    
    private UNOController controller;
    private JButton UnoButton;
    private JButton catchcpu1Button;
    private JButton catchcpu2Button;
    private JButton catchcpu3Button;
    private boolean isGameEnd = false;
    
    public UNOGamePanel() {
        this.controller = UNOController.getInstance();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(255, 255, 255)); 
        setLayout(null);
    }

    public void setIsGameEnd(boolean isGameEnd) {
        this.isGameEnd = isGameEnd;
    }

    
    private JButton updateDeck(){
        JButton button = new JButton();
        button.setBounds(140, 30, 80, 120);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            if (isGameEnd) {
                return;
            }
            if(controller.getPlayerList().indexOf(controller.getCurrentPlayer()) !=0){
                errorMessage = "It's not your turn!";
                errorMessageTimer = System.currentTimeMillis();
                repaint();
                return;
            }
            controller.getCardFromDeck();
            updateDisplay();
        });
        
        return button;
    }
    
    private void updateCardPositions() {
        // Get current game state from controller
        List<Card> playerHand = controller.getHumanPlayedCard();
        List<Card> computer1Hand = controller.getCPUCard(0);
        List<Card> computer2Hand = controller.getCPUCard(1);
        List<Card> computer3Hand = controller.getCPUCard(2);
        Card topCard = controller.getTopCard(1);

        if (topCard != null) {
            topCard.setRotation(0);
            topCard.setPosition(450, 240);
        }
        
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            int cardX = (WIDTH / 2) - (playerHand.size() * card_gap_x / 2) + (i * card_gap_x) + 60;
            int cardY = HEIGHT - 150;
            card.setPosition(cardX, cardY);
        }
        
        // CPU1 - 左側垂直排列
        for (int i = 0; i < computer1Hand.size(); i++) {
            Card card = computer1Hand.get(i);
            card.setRotation(90); 
            
            int cardX = 50 + 60;
            int cardY = (HEIGHT / 2) - (computer1Hand.size() * card_gap_y / 2) + (i * card_gap_y);
            
            card.setPosition(cardX, cardY);
        }

        for (int i = 0; i < computer3Hand.size(); i++) {
            Card card = computer3Hand.get(i);
            card.setRotation(270); // 逆時針旋轉90度
            
            // X坐標：固定在右側
            int cardX = WIDTH - 50 - card.getWidth() + 120;
            // Y坐標：垂直居中排列
            int cardY = (HEIGHT / 2) - (computer3Hand.size() * card_gap_y / 2) + (i * card_gap_y);
            
            card.setPosition(cardX, cardY);
        }

        // CPU2 - 頂部水平排列（不旋轉）
        for (int i = 0; i < computer2Hand.size(); i++) {
            Card card = computer2Hand.get(i);
            int cardX = (WIDTH / 2) - (computer2Hand.size() * card_gap_x / 2) + (i * card_gap_x) + 60;
            int cardY = 50;
            card.setPosition(cardX, cardY);
        }
    }
    
    // 繪製旋轉卡牌的方法
    private void drawRotatedCard(Graphics2D graphic2D, Card card) {
        // 檢查卡牌和圖像是否存在
        if (card == null || card.getImage() == null) {
            System.err.println("Warning: Card or image " + card.getType().toString() + " " + card.getColor().toString() + " is null, skipping draw");
            return;
        }
        
        if (card.isRotated()) {
            AffineTransform oldTransform = graphic2D.getTransform();
            
            // 獲取圖像的原始尺寸（旋轉前）
            int imageWidth = card.getImage().getWidth();
            int imageHeight = card.getImage().getHeight();
            
            // 計算旋轉中心 - 使用卡牌在螢幕上應該占據的空間
            double centerX = card.getX() + card.getWidth() / 2.0;
            double centerY = card.getY() + card.getHeight() / 2.0;
            
            // 移動到旋轉中心
            // 平移
            graphic2D.translate(centerX, centerY);
            // 執行旋轉
            graphic2D.rotate(Math.toRadians(card.getRotationAngle()));
            // 移動到繪製位置（圖像左上角）
            graphic2D.translate(-imageWidth / 2.0, -imageHeight / 2.0);
            
            // 繪製卡牌 - 使用原始圖像尺寸
            graphic2D.drawImage(card.getImage(), 0, 0, imageWidth, imageHeight, this);
            
            // 恢復變換狀態
            // 如果不恢復變換，第二張卡牌也會被旋轉90度！
            graphic2D.setTransform(oldTransform);
        } else {
            // 正常繪製
            graphic2D.drawImage(card.getImage(), card.getX(), card.getY(), 
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
        if (isGameEnd) {
            return;
        }
        if (controller.getCurrentPlayer() != controller.getPlayerList().get(0)) {
            errorMessage = "It's not your turn!";
            errorMessageTimer = System.currentTimeMillis();
            repaint();
            return;
        }
        List<Card> playerHand = controller.getHumanPlayedCard();
        if (currentSelectedCardIndex == -1){ //Click unselected card
            currentSelectedCardIndex = index;
            playerHand.get(index).setCardSelected(true);
        } else if (index == currentSelectedCardIndex){ //Click selected card
            Card selectedCard = playerHand.get(index);
            boolean isPlayed = controller.canPlayCard(selectedCard,controller.getTopCard(1));
            if (isPlayed){
                controller.playCard(selectedCard);
                errorMessage = "Card played!";
                errorMessageTimer = System.currentTimeMillis();
                currentSelectedCardIndex = -1;
                selectedCard.setCardSelected(false);
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

    public void shoutUno() {
        if (isGameEnd) {
            return;
        }
        errorMessage = controller.getPlayerList().get(0).shoutUno();
        errorMessageTimer = System.currentTimeMillis();
        repaint();
    }
    
    public void updateCardButtons() {
        for (JButton btn : cardButtons) {
            remove(btn);
        }
        cardButtons.clear();
        List<Card> playerHand = controller.getHumanPlayedCard();
        
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
        Graphics2D graphic2D = (Graphics2D) g;
        
        // Get current game state from controller with error handling
        List<Card> playerHand;
        List<Card> computer1Hand;
        List<Card> computer2Hand;
        List<Card> computer3Hand;
        Card topCard;
        
        try {
            playerHand = new ArrayList<>(controller.getHumanPlayedCard());
            computer1Hand = new ArrayList<>(controller.getCPUCard(0));
            computer2Hand = new ArrayList<>(controller.getCPUCard(1));
            computer3Hand = new ArrayList<>(controller.getCPUCard(2));
            topCard = controller.getTopCard(1);
        } catch (Exception e) {
            // 如果获取数据时出错，使用空列表
            playerHand = new ArrayList<>();
            computer1Hand = new ArrayList<>();
            computer2Hand = new ArrayList<>();
            computer3Hand = new ArrayList<>();
            topCard = null;
        }
        
        // Null safety checks
        if (playerHand == null) playerHand = new ArrayList<>();
        if (computer1Hand == null) computer1Hand = new ArrayList<>();
        if (computer2Hand == null) computer2Hand = new ArrayList<>();
        if (computer3Hand == null) computer3Hand = new ArrayList<>();
        
        // Draw background
        graphic2D.setColor(new Color(255,203,142));
        graphic2D.fillRect(0, 0, getWidth(), getHeight());
        
        // === 第一層：繪製所有卡牌 (底層) ===
        // Draw CPU1 cards
        for (Card card : computer1Hand) {
            drawRotatedCard(graphic2D, card);
        }
        
        // Draw CPU2 cards
        for (Card card : computer2Hand) {
            drawRotatedCard(graphic2D, card);
        }
        
        // Draw CPU3 cards
        for (Card card : computer3Hand) {
            drawRotatedCard(graphic2D, card);
        }
        
        // === 第二層：繪製所有文字標籤 (上層，不會被卡牌覆蓋) ===
        graphic2D.setFont(new Font("Arial", Font.BOLD, 18));
        graphic2D.setColor(Color.WHITE);
        
        // CPU1 標籤 (左側)
        graphic2D.drawString("CPU 1 (" + computer1Hand.size() + " cards)" + (controller.getPlayerList().get(1).getIsShout() ? " *UNO*" : ""), 105, 220);
        catchcpu1Button = new JButton();
        catchcpu1Button.setBounds(105, 200, 250, 30); // Positioned where the old text was
        catchcpu1Button.setBorderPainted(false);
        catchcpu1Button.setContentAreaFilled(false);
        catchcpu1Button.setFocusPainted(false);
        catchcpu1Button.addActionListener(e -> {
            controller.getPlayerList().get(0).catchForgotShout(controller.getPlayerList().get(1));
        });
        add(catchcpu1Button);
        // CPU2 標籤 (頂部居中)
        graphic2D.drawString("CPU 2 (" + computer2Hand.size() + " cards)" + (controller.getPlayerList().get(2).getIsShout() ? " *UNO*" : ""),420, 40);
        catchcpu2Button = new JButton();
        catchcpu2Button.setBounds(420, 20, 250, 30); // Positioned where the old text was
        catchcpu2Button.setBorderPainted(false);
        catchcpu2Button.setContentAreaFilled(false);
        catchcpu2Button.setFocusPainted(false);
        catchcpu2Button.addActionListener(e -> {
            controller.getPlayerList().get(0).catchForgotShout(controller.getPlayerList().get(2));
        });
        add(catchcpu2Button);
        // CPU3 標籤 (右側)
        graphic2D.drawString("CPU 3 (" + computer3Hand.size() + " cards)" + (controller.getPlayerList().get(3).getIsShout() ? " *UNO*" : ""), 745, 220);
        catchcpu3Button = new JButton();
        catchcpu3Button.setBounds(745, 200, 250, 30); // Positioned where the old text was
        catchcpu3Button.setBorderPainted(false);
        catchcpu3Button.setContentAreaFilled(false);
        catchcpu3Button.setFocusPainted(false);
        catchcpu3Button.addActionListener(e -> {
            controller.getPlayerList().get(0).catchForgotShout(controller.getPlayerList().get(3));
        });
        add(catchcpu3Button);
        // === 繼續第一層：繪製其他卡牌 ===
        // Draw player's cards (底層)
        for (Card card : playerHand) {
            drawRotatedCard(graphic2D, card);
        }
        
        // Draw discard pile (top card)
        if (topCard != null) {
            graphic2D.drawImage(topCard.getImage(),
                         topCard.getX(), topCard.getY(),
                         topCard.getWidth(), topCard.getHeight(),
                         this);
        }
        
        // === 第二層：繼續繪製文字標籤 (上層) ===
        // Player 標籤
        graphic2D.setFont(new Font("Arial", Font.BOLD, 18));
        graphic2D.setColor(Color.WHITE);
        graphic2D.drawString("YOU (" + playerHand.size() + " cards)" + (controller.getPlayerList().get(0).getIsShout() ? " *UNO*" : ""), 420, 600);

        // Discard pile label
        if (topCard != null) {
            graphic2D.setColor(Color.WHITE);
            graphic2D.setFont(new Font("Arial", Font.PLAIN, 14));
            graphic2D.drawString("DISCARD", 
                topCard.getX() + (topCard.getWidth() / 2) - 30, 
                topCard.getY() + topCard.getHeight() + 20);
        }

        ImageIcon cardBackIcon = new ImageIcon(getClass().getResource("/asset/uno-card-images-master/Back.jpg"));
        if (cardBackIcon.getImage() == null) {
            System.err.println("Failed to load card back image");
        } else {
            graphic2D.drawImage(cardBackIcon.getImage(), 140, 30, 80, 120, null);
        }
        graphic2D.drawString("DECK", 160, 165);

        // Uno button
        ImageIcon unoButtonIcon = new ImageIcon(getClass().getResource("/asset/Uno_button.png"));
        if (unoButtonIcon.getImage() == null) {
            System.err.println("Failed to load Uno button image");
        } else {
            graphic2D.drawImage(unoButtonIcon.getImage(), 110, 460, 120, 106, null);
        }
        graphic2D.setFont(new Font("Arial", Font.BOLD, 24));
        graphic2D.setColor(Color.BLUE);
        graphic2D.drawString("UNO!", 140, 520);
        
        UnoButton = new JButton();
        UnoButton.setBounds(110, 460, 120, 106);
        UnoButton.setBorderPainted(false);
        UnoButton.setContentAreaFilled(false);
        UnoButton.setFocusPainted(false);
        UnoButton.addActionListener(e -> {
            shoutUno();
        });
        add(UnoButton);

        // Draw error message if it exists and hasn't timed out
        if (errorMessage != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - errorMessageTimer < 1000) { // Show message for 2 seconds
                graphic2D.setColor(Color.RED);
                graphic2D.setFont(new Font("Arial", Font.BOLD, 24));
                // Draw the error message in the center of the screen
                graphic2D.drawString(errorMessage, 630, 580);
            } else {
                errorMessage = null; // Clear the message after timeout
            }
        }
        
        if (controller.getTurnTimer() != null) {
            controller.getTurnTimer().drawTimer(g);
        }
        if(controller.getWildCardViewer().isHavingWild()){
            graphic2D.drawRect(300, 200, 360, 230);
            graphic2D.setFont(new Font("Arial", 1, 28));
            graphic2D.drawString("Choose a new color", 350, 230);
            controller.getWildCardViewer().drawWindow(g);
        }
        if(controller.getChallengeViewer().getIsChallenging()){
            graphic2D.drawRect(300, 200, 360, 230);
            graphic2D.setFont(new Font("Arial", 1, 28));
            graphic2D.drawString("Choose to Challenge?", 350, 230);
            controller.getChallengeViewer().drawWindow(g);   
        }
        if(isGameEnd) {
            graphic2D.setColor(Color.BLUE);
            graphic2D.setFont(new Font("Arial", 1, 100));
            graphic2D.drawString(controller.getCurrentPlayer().getName() + " Win !!!", 150, 330);
        }
    }
}