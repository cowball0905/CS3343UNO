// controller/UNOController.java
package controller;

import model.*;
import view.UNOGamePanel;
import view.UNOMenuPanel;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class UNOController {
    private static UNOController instance;
    private GameState gameState;
    private UNOGamePanel gamePanel;
    private UNOMenuPanel menuPanel;
    private JFrame mainFrame;
    private List<Card> playerHand;
    private List<Card> computerHand;

    private UNOController() {
        this.gameState = new GameState();
        this.playerHand = new ArrayList<>();
        this.computerHand = new ArrayList<>();
        initializeGame();
    }

    public static synchronized UNOController getInstance() {
        if (instance == null) {
            instance = new UNOController();
        }
        return instance;
    }

        public void setMenuPanel(UNOMenuPanel menuPanel) {
        this.menuPanel = menuPanel;
        if (menuPanel != null && menuPanel.getParent() instanceof JFrame) {
            this.mainFrame = (JFrame) menuPanel.getParent();
        }
    }

    private void initializeGame() {
        // Deal initial cards
        for (int i = 0; i < 7; i++) {
            playerHand.add(gameState.drawCard());
            computerHand.add(gameState.drawCard());
        }
        
        // First card on discard pile
        Card firstCard;
        do {
            firstCard = gameState.drawCard();
        } while (firstCard.getType() != Type.Number);
        
        gameState.getDiscardPile().push(firstCard);
        gameState.setCurrentColor(firstCard.getColor());
    }

    public void setGamePanel(UNOGamePanel panel) {
        this.gamePanel = panel;
        if (gamePanel != null) {
            gamePanel.initialize(this);
        }
    }

    public boolean playCard(Card card) {
        if (gameState.isPlayerTurn() && playerHand.contains(card) && gameState.isValidMove(card)) {
            playerHand.remove(card);
            gameState.playCard(card);
            if (gamePanel != null) {
                gamePanel.updateGameState();
            }
            return true;
        }
        return false;
    }

    public Card drawCard() {
        if (gameState.isPlayerTurn()) {
            Card card = gameState.drawCard();
            if (card != null) {
                playerHand.add(card);
                if (gamePanel != null) {
                    gamePanel.updateGameState();
                }
                return card;
            }
        }
        return null;
    }

    // Getters for view
    public Card getTopCard() {
        return gameState.getTopCard();
    }


    public boolean isPlayerTurn() {
        return gameState.isPlayerTurn();
    }

    public Color getCurrentColor() {
        return gameState.getCurrentColor();
    }

    public List<Card> getPlayerHand() {
        return new ArrayList<>(playerHand);
    }

    public List<Card> getComputerHand() {
        return new ArrayList<>(computerHand);
    }

    // In UNOController.java
    public void startGame() {
        if (mainFrame == null && menuPanel != null) {
            mainFrame = (JFrame) SwingUtilities.getWindowAncestor(menuPanel);
        }
        
        if (mainFrame != null) {
            // Remove current panel
            mainFrame.getContentPane().removeAll();
            
            // Create and add game panel if not exists
            if (gamePanel == null) {
                gamePanel = new UNOGamePanel(this);
            }
            
            mainFrame.add(gamePanel);
            mainFrame.revalidate();
            gamePanel.requestFocusInWindow();
        }
    }

    public void showMenu() {
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll();
            if (menuPanel != null) {
                mainFrame.add(menuPanel);
            }
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }

}