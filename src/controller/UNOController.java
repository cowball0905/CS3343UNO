package controller;


import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import view.UNOGamePanel;
import view.UNOMenuPanel;
import model.Card;
import model.CardFactory;

public class UNOController extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private static UNOController instance;
    private UNOGamePanel gamePanel;
    private UNOMenuPanel menuPanel;
    
    // Game state
    private List<Card> playerHand;
    private List<Card> computerHand;
    private Card topCard;
    private Card deckCard;
    
    public static UNOController getInstance() {
        if (instance == null) {
            instance = new UNOController();
        }
        return instance;
    }
    
    private UNOController() {
        super("UNO Game");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // Initialize and show menu panel first
        showMenu();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void showMenu() {
        if (menuPanel == null) {
            menuPanel = new UNOMenuPanel(this);
        }
        if (gamePanel != null) {
            remove(gamePanel);
        }
        add(menuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void startGame() {
        if (gamePanel == null) {
            gamePanel = new UNOGamePanel(this);
        }
        remove(menuPanel);
        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        
        // Initialize game logic
        initializeGameLogic();
        gamePanel.startGame();
    }
    
    private void initializeGameLogic() {
        // Initialize game state
        playerHand = new ArrayList<>();
        computerHand = new ArrayList<>();
        
        // Create deck and top card
        deckCard = CardFactory.createDeckCard();
        topCard = CardFactory.createNumberCard();
        
        // Deal cards to players
        for (int i = 0; i < 5; i++) {
            playerHand.add(CardFactory.createRandomCard());
            computerHand.add(CardFactory.createDeckCard()); // Face-down cards for computer
        }
    }
    
    public boolean playCard(Card card) {
        if (playerHand.contains(card)) {
            // Move card from player hand to discard pile
            playerHand.remove(card);
            topCard = card;
            gamePanel.updateDisplay();
            return true;
        }
        return false;
    }
    
    public boolean drawCard() {
        if (playerHand.size() < 10) { // Limit hand size
            Card newCard = CardFactory.createRandomCard();
            playerHand.add(newCard);
            gamePanel.updateDisplay();
            return true;
        }
        return false;
    }
    
    public Card getCardAt(int x, int y) {
        // Check player hand
        for (Card card : playerHand) {
            if (card.contains(x, y)) {
                return card;
            }
        }
        return null;
    }
    
    public boolean isDeckClicked(int x, int y) {
        return deckCard != null && deckCard.contains(x, y);
    }
    
    // Getters for view
    public List<Card> getPlayerHand() {
        return new ArrayList<>(playerHand);
    }
    
    public List<Card> getComputerHand() {
        return new ArrayList<>(computerHand);
    }
    
    public Card getTopCard() {
        return topCard;
    }
    
    public Card getDeckCard() {
        return deckCard;
    }
}
