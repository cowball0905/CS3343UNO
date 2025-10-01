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
    private String[] Deck;
    private Player player;
    private ArrayList<Player> CPU;
    private CardFactory cardFactory = new ConcreteCardFactory();
    private Card topCard;
    private Color currentColor;

    private UNOController() {
        this.player = new HumanPlayer("Player");
        this.CPU = new ArrayList<>();
        for(int i=0;i<3;i++){
            CPU.add(new CPUPlayer("CPU"+(i+1)));
        }
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
        Deck = new String[]{"r0","r1","r2","r3","r4","r5","r6","r7","r8","r9",
                            "r1","r2","r3","r4","r5","r6","r7","r8","r9",
                            "g0","g1","g2","g3","g4","g5","g6","g7","g8","g9",
                            "g1","g2","g3","g4","g5","g6","g7","g8","g9",
                            "b0","b1","b2","b3","b4","b5","b6","b7","b8","b9",
                            "b1","b2","b3","b4","b5","b6","b7","b8","b9",
                            "y0","y1","y2","y3","y4","y5","y6","y7","y8","y9",
                            "y1","y2","y3","y4","y5","y6","y7","y8","y9",
                            "rSkip","rReverse","rDrawTwo",
                            "rSkip","rReverse","rDrawTwo",
                            "gSkip","gReverse","gDrawTwo",
                            "gSkip","gReverse","gDrawTwo",
                            "bSkip","bReverse","bDrawTwo",
                            "bSkip","bReverse","bDrawTwo",
                            "ySkip","yReverse","yDrawTwo",
                            "ySkip","yReverse","yDrawTwo",
                            "WildCard", "WildCard", "WildCard", "WildCard",
                            "WildDrawFour", "WildDrawFour", "WildDrawFour", "WildDrawFour"};

        topCard = cardFactory.giveCard(new ArrayList<>(Arrays.asList(Deck)),true);
        
        for (int i = 0; i < 7; i++) {
            player.getCard(cardFactory.giveCard(new ArrayList<>(Arrays.asList(Deck)),false));
            for(Player cpu:CPU){
                cpu.getCard(cardFactory.giveCard(new ArrayList<>(Arrays.asList(Deck)),false));
            }
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

        return deckCard;
    }
}