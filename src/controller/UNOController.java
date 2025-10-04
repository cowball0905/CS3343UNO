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
    private UNOGamePanel gamePanel;
    private UNOMenuPanel menuPanel;
    private JFrame mainFrame;
    private ArrayList<String> Deck;
    private Player player;
    private ArrayList<Player> CPU;
    private CardFactory cardFactory = new ConcreteCardFactory();
    private ArrayList<Card> PlayedCard = new ArrayList<>();
    private Color currentColor;
    private Player currentPlayer;
    private ArrayList<Player> players;
    private int playDirection;

    private UNOController() {
        // Create the main frame
        mainFrame = new JFrame("UNO Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        
        // Initialize game components
        this.player = new HumanPlayer("Player");
        this.CPU = new ArrayList<>();
        for(int i=0;i<3;i++){
            CPU.add(new CPUPlayer("CPU"+(i+1)));
        }
        
        // Create panels
        menuPanel = new UNOMenuPanel(this);
        gamePanel = new UNOGamePanel(this);
        
        initializeGame();
        
        // Show the window
        showMenu();
        mainFrame.setVisible(true);
        
        System.out.println("UNO Game window created and should be visible!");
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
        Deck = new ArrayList<String>(Arrays.asList(
            "r0","r1","r2","r3","r4","r5","r6","r7","r8","r9",
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
            "WildDrawFour", "WildDrawFour", "WildDrawFour", "WildDrawFour"
        ));

        PlayedCard.add(cardFactory.giveCard(Deck,true, true)); // 已打出的牌顯示
        
        for (int i = 0; i < 7; i++) {
            player.drawCard(cardFactory.giveCard(Deck,false, true)); // 玩家的牌顯示
            for(Player cpu:CPU){
                cpu.drawCard(cardFactory.giveCard(Deck,false, false)); // CPU的牌隱藏
            }
        }
        currentColor = PlayedCard.get(0).getColor();
        players = new ArrayList<>();
        players.add(player);
        players.addAll(CPU);
        currentPlayer = players.get(0);
        playDirection = 1;
    }

    public void getCardFromDeck(){
        currentPlayer.drawCard(cardFactory.giveCard(Deck,false, players.indexOf(currentPlayer)==0 ? true:false));
    }

    public ArrayList<Card> getPlayedCard() {
        return players.get(0).getHand();
    }

    public ArrayList<Card> getCPUCard(int index) {
        return players.get(index+1).getHand();
    }

    public Card getTopCard() {
        return PlayedCard.get(PlayedCard.size() - 1);
    }

    public Player getCurrentPlayer() {
        return player;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public ArrayList<Player> getPlayerList(){
        return players;
    }

    public int getPlayDirection() {
        return playDirection;
    }

    public void setPlayDirection(int direction) {
        this.playDirection = direction;
    } 

    public void eachRound(){
        Scanner scanner = new Scanner(System.in);
        

        
    }

    public boolean playCard(Card playedCard) {
        if (currentPlayer != player) {
            System.out.println("It's not the player's turn!");
            return false;
        }
        Card topCard = getTopCard();
        switch (playedCard.getType()) {
            case Wild:
            case WildDrawFour:
                return true;
            case Skip:
            case Reverse:
            case DrawTwo:
                if (playedCard.getColor() == currentColor) {
                    PlayedCard.add(playedCard);
                    player.playCard(playedCard);
                    currentColor = playedCard.getColor();
                    return true;
                }
                return false;
            case Number:
            if(playedCard.getColor()==topCard.getColor() || ((NumberCard) playedCard).getValue() == ((NumberCard)topCard).getValue()){
                    PlayedCard.add(playedCard);
                    player.playCard(playedCard);
                    currentColor = playedCard.getColor();
                    return true;
                }
                return false;
            default:
                return false;
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
            gamePanel.startGame(); 
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

    public CardFactory getCardFactory() {
        return this.cardFactory;
    }

    public ArrayList<String> getDeck() {
        return this.Deck;
    }
}
