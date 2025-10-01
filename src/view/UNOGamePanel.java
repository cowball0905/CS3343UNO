// view/UNOGamePanel.java
package view;

import controller.UNOController;
import model.CPUPlayer;
import model.Card;
import model.DeckCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UNOGamePanel extends JPanel {
    private UNOController controller;
    private BufferedImage cardBackImage;

    public UNOGamePanel(UNOController controller) {
        this.controller = controller;
        setupUI();
        setupEventListeners();
    }

    private void setupUI() {
        setBackground(new Color(0, 100, 0)); // Dark green background
        setPreferredSize(new Dimension(800, 600));
        loadCardImages();
    }

// In UNOGamePanel.java
private void setupEventListeners() {
    addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (controller.isPlayerTurn()) {
                // Check if deck was clicked
                Rectangle deckBounds = getDeckBounds();
                if (deckBounds.contains(e.getPoint())) {
                    controller.drawCard();
                    return;
                }
                
                // Check if a card was clicked
                List<Card> playerHand = controller.getPlayerHand();
                for (int i = 0; i < playerHand.size(); i++) {
                    Rectangle cardBounds = getCardBounds(i, playerHand.size());
                    if (cardBounds.contains(e.getPoint())) {
                        Card clickedCard = playerHand.get(i);
                        if (controller.isValidMove(clickedCard, controller.getTopCard())) {
                            controller.playCard(clickedCard);
                        }
                        return;
                    }
                }
            }
        }
    });
}

private Rectangle getDeckBounds() {
    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;
    return new Rectangle(centerX - 175, centerY - 75, 100, 150);
}

private Rectangle getCardBounds(int index, int totalCards) {
    int cardWidth = 71;
    int cardHeight = 96;
    int overlap = 30;
    int startX = (getWidth() - (cardWidth + (totalCards - 1) * overlap)) / 2;
    int y = getHeight() - cardHeight - 20;
    
    return new Rectangle(
        startX + index * overlap,
        y,
        cardWidth,
        cardHeight
    );
}
    private void handlePlayerClick(int x, int y) {
        // Implement player card selection logic
        // This is a placeholder for now
    }

    private void loadCardImages() {
        try {
            // Try to load the card back image
            String[] possiblePaths = {
                    "/asset/uno-card-images-master/Wild_Deck.png",
                    "src/asset/uno-card-images-master/Wild_Deck.png"
            };

            for (String path : possiblePaths) {
                File file = new File(path);
                if (file.exists()) {
                    cardBackImage = ImageIO.read(file);
                    break;
                }
            }

            // If still not found, create a placeholder
            if (cardBackImage == null) {
                cardBackImage = new BufferedImage(71, 96, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = cardBackImage.createGraphics();
                g2d.setColor(Color.RED);
                g2d.fillRect(0, 0, 71, 96);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                g2d.drawString("UNO", 25, 50);
                g2d.dispose();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw computer's hand
        drawComputerHand(g2d);
        
        // Draw play area (deck and discard pile)
        drawPlayArea(g2d);
        
        // Draw player's hand
        drawPlayerHand(g2d);
        
        // Draw current player indicator
        drawCurrentPlayerIndicator(g2d);
    }
    

    private void drawComputerHand(Graphics2D g2d) {
        List<Card> computerHand = controller.getComputerHand();
        if (computerHand == null || computerHand.isEmpty())
            return;

        int cardWidth = 60;
        int cardHeight = 96;
        int overlap = 20;
        int startX = (getWidth() - (cardWidth + (computerHand.size() - 1) * overlap)) / 2;

        // Draw computer's cards as card backs
        for (int i = 0; i < computerHand.size(); i++) {
            int x = startX + i * overlap;
            int y = 20;

            if (cardBackImage != null) {
                g2d.drawImage(cardBackImage, x, y, cardWidth, cardHeight, null);
            } else {
                // Fallback if card back image fails to load
                g2d.setColor(Color.RED);
                g2d.fillRoundRect(x, y, cardWidth, cardHeight, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawRoundRect(x, y, cardWidth, cardHeight, 10, 10);
            }
        }

        // Draw computer's label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String label = "Computer - " + computerHand.size() + " cards";
        int labelWidth = g2d.getFontMetrics().stringWidth(label);
        g2d.drawString(label, (getWidth() - labelWidth) / 2, 15);
    }

    private void drawPlayArea(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Draw discard pile
        Card topCard = controller.getTopCard();
        if (topCard != null) {
            BufferedImage cardImage = topCard.getImage();
            if (cardImage != null) {
                g2d.drawImage(cardImage, centerX - 50, centerY - 75, 100, 150, null);
            }
        }

        // Draw deck
        if (cardBackImage != null) {
            g2d.drawImage(cardBackImage, centerX - 150, centerY - 75, 100, 150, null);
        }

        // Draw "DECK" text under the deck
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String deckText = "DECK";
        int textWidth = g2d.getFontMetrics().stringWidth(deckText);
        g2d.drawString(deckText, centerX - 150 + (100 - textWidth) / 2, centerY + 90);
    }

    private void drawPlayerHand(Graphics2D g2d) {
        List<Card> playerHand = controller.getPlayerHand();
        if (playerHand == null || playerHand.isEmpty())
            return;

        int cardWidth = 71;
        int cardHeight = 96;
        int overlap = 30;
        int startX = (getWidth() - (cardWidth + (playerHand.size() - 1) * overlap)) / 2;
        int y = getHeight() - cardHeight - 20;

        // Draw player's cards
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            int x = startX + i * overlap;

            BufferedImage cardImage = card.getImage();
            if (cardImage != null) {
                g2d.drawImage(cardImage, x, y, cardWidth, cardHeight, null);
            }
        }
    }

    private void drawCurrentPlayerIndicator(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        String text;
        if (controller.isPlayerTurn()) {
            text = "Your turn";
        } else {
            text = "Computer's turn";
        }

        int textWidth = g2d.getFontMetrics().stringWidth(text);
        g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
    }


    // Call this method after any player action
    public void updateGameState() {
        repaint();
    }
}