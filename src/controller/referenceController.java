// controller/UNOController.java
package controller;

import model.*;
import view.UNOGamePanel;
import view.UNOMenuPanel;
// Add these imports at the top
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class UNOController {
    private static UNOController instance;
    private GameState gameState;
    private UNOGamePanel gamePanel;
    private UNOMenuPanel menuPanel;
    private JFrame mainFrame;
    private List<Player> players;
    private Player currentPlayer;
    private Stack<Card> discardPile;

    private UNOController() {
        players = new ArrayList<>();
        discardPile = new Stack<>();
        initializeGame();
    }

    public static UNOController getInstance() {
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
        // Initialize players
        players.add(new HumanPlayer("Player 1"));
        players.add(new CPUPlayer("CPU 1"));
        currentPlayer = players.get(0); // Human starts first

        // Initialize game state
        gameState = new GameState();

        // Deal initial cards
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.addCard(gameState.drawCard());
            }
        }

        // Initialize discard pile with a non-special card
        Card firstCard;
        do {
            firstCard = gameState.drawCard();
        } while (firstCard.getType() != Type.Number);

        discardPile.add(firstCard);
    }

    public void setGamePanel(UNOGamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // In UNOController.java
    public void playCard(Card card) {
        if (isPlayerTurn() && currentPlayer.getHand().contains(card) && 
            isValidMove(card, getTopCard())) {
            
            currentPlayer.getHand().remove(card);
            discardPile.push(card);
            
            // Handle special card effects
            handleCardEffect(card);
            
            // Check for win condition
            if (currentPlayer.getHand().isEmpty()) {
                handleWin();
                return;
            }
            
            // End turn if not a Draw Two or Wild Draw Four that requires drawing
            if (!(card.getType() == Type.DrawTwo || card.getType() == Type.WildDrawFour)) {
                endTurn();
            }
            
            updateView();
        }
    }
    public void drawCard() {
        if (isPlayerTurn()) {
            Card drawnCard = gameState.drawCard();
            if (drawnCard != null) {
                currentPlayer.addCard(drawnCard);
                // Player must play the drawn card if possible
                if (!isValidMove(drawnCard, getTopCard())) {
                    endTurn();
                }
                updateView();
            }
        }
    }
    // Getters for view
    public Card getTopCard() {
        return gameState.getTopCard();
    }

    public boolean isPlayerTurn() {
        return currentPlayer instanceof HumanPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Color getCurrentColor() {
        return gameState.getCurrentColor();
    }

    public List<Card> getPlayerHand() {
        return new ArrayList<>(currentPlayer.getHand());
    }

    public List<Card> getComputerHand() {
        return new ArrayList<>(players.get(1).getHand());
    }

    // In UNOController.java
    public void endTurn() {
        // Switch to next player
        int currentIndex = players.indexOf(currentPlayer);
        currentIndex = (currentIndex + 1) % players.size();
        currentPlayer = players.get(currentIndex);

        // Update UI
        if (gamePanel != null) {
            gamePanel.updateGameState();
        }

        // Handle CPU turn if needed
        if (!isPlayerTurn()) {
            handleCPUTurn();
        }
    }

    private void handleCPUTurn() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CPUPlayer cpu = (CPUPlayer) currentPlayer;
                Card topCard = getTopCard();
                
                // CPU makes decision
                Card cardToPlay = cpu.chooseCardToPlay(topCard);
                
                if (cardToPlay != null) {
                    // Play the card
                    playCard(cardToPlay);
                    
                    // Handle special cards
                    if (cardToPlay.getType() == Type.Wild || 
                        cardToPlay.getType() == Type.WildDrawFour) {
                        Color chosenColor = cpu.chooseWildCardColor();
                        setCurrentColor(chosenColor);
                    }
                    
                    // Check for UNO
                    if (cpu.getHand().size() == 1) {
                        cpu.shoutUno();
                    }
                } else {
                    // Draw a card if no playable card
                    drawCard();
                }
                
                // Update UI
                updateView();
                
                // End turn (this will check if it's still CPU's turn)
                endTurn();
            }
        });
        
        timer.setRepeats(false);
        timer.start();
    }
    
    
    public boolean isValidMove(Card card, Card topCard) {
        if (topCard == null) return true; // First card
        if (card.getType() == Type.Wild || card.getType() == Type.WildDrawFour) {
            return true;
        }
        return card.getColor() == topCard.getColor() || 
               card.getType() == topCard.getType() ||
               (card instanceof NumberCard && topCard instanceof NumberCard && 
                ((NumberCard)card).getValue() == ((NumberCard)topCard).getValue());
    }
    private void handleCardEffect(Card card) {
        // Get the card's function and execute it with the controller as context
        card.cardFunction(this);
        
        // Additional common logic can go here if needed
        updateView();
    }
    private void handleWin() {
        String winner = currentPlayer.getName() + " wins!";
        JOptionPane.showMessageDialog(mainFrame, winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // Reset game or return to menu
    }

    private void updateView() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }

    private void setCurrentColor(Color color) {
        if (gameState != null) {
            gameState.setCurrentColor(color);
        }
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