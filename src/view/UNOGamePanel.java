package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCardClick(e.getX(), e.getY());
            }
        });
    }
    
    private static final int CARD_OFFSET_X = 25; // Horizontal overlap between cards in hand
    private static final int CARD_OVERLAP = 15;   // How much cards overlap in hand
    
    private void updateCardPositions() {
        // Get current game state from controller
        List<Card> playerHand = controller.getPlayerHand();
        List<Card> computerHand = controller.getComputerHand();
        Card topCard = controller.getTopCard();
        Card deckCard = controller.getDeckCard();
        
        // Set positions for deck and top card
        if (deckCard != null) {
            deckCard.setPosition(50, 200);
        }
        if (topCard != null) {
            topCard.setPosition(200, 200);
        }
        
        // Update positions of all cards in player's hand
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            int cardX = (WIDTH / 2) - (playerHand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X);
            int cardY = HEIGHT - 150; // Position near bottom of screen
            card.setPosition(cardX, cardY);
        }
        
        // Update positions of all cards in computer's hand
        for (int i = 0; i < computerHand.size(); i++) {
            Card card = computerHand.get(i);
            int cardX = (WIDTH / 2) - (computerHand.size() * CARD_OFFSET_X / 2) + (i * CARD_OFFSET_X);
            int cardY = 50; // Position near top of screen
            card.setPosition(cardX, cardY);
        }
    }
    
    private void handleCardClick(int x, int y) {
        // Check if player clicked on a card in their hand
        Card clickedCard = controller.getCardAt(x, y);
        if (clickedCard != null) {
            controller.playCard(clickedCard);
            return;
        }
        
        // Check if player clicked on the deck
        if (controller.isDeckClicked(x, y)) {
            controller.drawCard();
        }
    }
    
    private void drawCenteredString(Graphics2D g, String text, java.awt.Rectangle rect) {
        // Center text in the given rectangle
        java.awt.FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
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
        List<Card> playerHand = controller.getPlayerHand();
        List<Card> computerHand = controller.getComputerHand();
        Card topCard = controller.getTopCard();
        Card deckCard = controller.getDeckCard();
        
        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Draw background
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw computer's hand (face down)
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "COMPUTER'S HAND (" + computerHand.size() + " cards)", 
                         new java.awt.Rectangle(0, 20, WIDTH, 30));
                         
        // Draw computer's cards (face down)
        for (Card card : computerHand) {
            g2d.drawImage(card.getImage(), 
                         card.getX(), card.getY(),
                         card.getWidth(), card.getHeight(),
                         this);
        }
        
        // Draw deck (back of cards)
        if (deckCard != null) {
            g2d.drawImage(deckCard.getImage(), 
                         deckCard.getX(), deckCard.getY(),
                         deckCard.getWidth(), deckCard.getHeight(),
                         this);
            
            // Draw deck label
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            drawCenteredString(g2d, "DECK", 
                new java.awt.Rectangle(deckCard.getX(), deckCard.getY() + deckCard.getHeight() + 5, 
                                     deckCard.getWidth(), 20));
        }
        
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
        drawCenteredString(g2d, "YOUR HAND (" + playerHand.size() + " cards)", 
                         new java.awt.Rectangle(0, HEIGHT - 150, WIDTH, 30));
        
        // Draw cards in player's hand
        for (Card card : playerHand) {
            g2d.drawImage(card.getImage(),
                         card.getX(), card.getY(),
                         card.getWidth(), card.getHeight(),
                         this);
        }
    }
    }
