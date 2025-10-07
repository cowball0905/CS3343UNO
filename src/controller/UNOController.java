package controller;

import model.*;
import view.UNOGamePanel;
import view.UNOMenuPanel;
import view.WildCardViewer;

import java.util.*;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class UNOController {
    private static UNOController instance;
    private UNOGamePanel gamePanel;
    private UNOMenuPanel menuPanel;
    private JFrame mainFrame;
    private ArrayList<String> Deck;
    private CardFactory cardFactory = new ConcreteCardFactory();
    private ArrayList<Card> PlayedCard = new ArrayList<>();
    private Player currentPlayer;
    private ArrayList<Player> players;
    private int playDirection;
    private CountDownTimer turnTimer;
    private boolean isAction = false;
    private WildCardViewer wildCardViewer;


    private UNOController() {
        mainFrame = new JFrame("UNO Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        
        players = new ArrayList<>();
        for(int i=0;i<4;i++){
            if(i==0){
                players.add(new HumanPlayer("Player"));
            }else{
                players.add(new CPUPlayer("CPU"+(i)));
            }
        }
        
        menuPanel = new UNOMenuPanel(this);
        gamePanel = new UNOGamePanel(this);
        wildCardViewer = new WildCardViewer();


        turnTimer = new CountDownTimer(gamePanel, new CountDownTimer.TimerCallback() {
            @Override
            public void onTimerComplete() {
                if (wildCardViewer.isHavingWild()) {
                    wildCardViewer.autoSelectColor();
                    wildCardViewer.setHavingWild(false);
                    wildCardViewer.removeButtons();
                    passNextPlayer(false);
                    eachRound();
                } else {
                    getCardFromDeck();
                }
            }
        });
        
        wildCardViewer.setTimer(turnTimer);
        wildCardViewer.setController(this);
        wildCardViewer.setPanel(gamePanel);
        
        initializeGame();
        
        // Show the window
        showMenu();
        mainFrame.setVisible(true);
        
        System.out.println("UNO Game window created and should be visible!");
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
Deck = new ArrayList<String>(Arrays.asList(
            "r0","r1","r2","r3","r4","r5","r6","r7","r8","r9",
            "r1","r2","r3","r4","r5","r6","r7","r8","r9",
            "g0","g1","g2","g3","g4","g5","g6","g7","g8","g9",
            "g1","g2","g3","g4","g5","g6","g7","g8","g9",
            "b0","b1","b2","b3","b4","b5","b6","b7","b8","b9",
            "b1","b2","b3","b4","b5","b6","b7","b8","b9",
            "y0","y1","y2","y3","y4","y5","y6","y7","y8","y9",
            "y1","y2","y3","y4","y5","y6","y7","y8","y9",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard",
            "WildCard", "WildCard", "WildCard", "WildCard",
            "WildDrawFour", "WildDrawFour", "WildDrawFour", "WildDrawFour"
        ));

        PlayedCard.add(cardFactory.giveCard(Deck,true, true)); // 已打出的牌顯示
        
        for (int i = 0; i < 7; i++) {
            players.get(0).drawCard(cardFactory.giveCard(Deck,false, true)); // 玩家的牌顯示
            for(int j=1;j<4;j++){
                players.get(j).drawCard(cardFactory.giveCard(Deck,false, false)); // CPU的牌隱藏
            }
        }
        currentPlayer = players.get(0);
        playDirection = 1;
        eachRound();
    }

    public void getCardFromDeck(){
        if(!isAction){
            isAction = true;
            currentPlayer.drawCard(cardFactory.giveCard(Deck,false, players.indexOf(currentPlayer)==0 ? true:false));
            isAction = false;
            passNextPlayer(false);
            eachRound();
        }
    }

    public ArrayList<Card> getHumanPlayedCard() {
        return players.get(0).getHand();
    }

    public ArrayList<Card> getCPUCard(int index) {
        return players.get(index+1).getHand();
    }

    public Card getTopCard() {
        return PlayedCard.get(PlayedCard.size() - 1);
    }

    public Player getCurrentPlayer() {
        return players.get(players.indexOf(currentPlayer));
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
        gamePanel.updateDisplay();
        if (currentPlayer.getHand().size() == 0) {
            return; // Game ends
        }
        if (currentPlayer == players.get(0)){
            turnTimer.startTimer(30); 
        } else {
            turnTimer.stopTimer();
            CPUPlayer cpuPlayer = (CPUPlayer) currentPlayer;
            
            Timer cpuTimer = new Timer(2000, e -> cpuPlayer.chooseCard());
            cpuTimer.setRepeats(false);  
            cpuTimer.start();
        }
    }

    public void playCard(Card card){
        isAction = true;
        card.setRotation(0);
        PlayedCard.add(card);
        currentPlayer.playCard(card);
        gamePanel.updateDisplay();
        
        if ((card.getType() == Type.Wild) && players.indexOf(currentPlayer) == 0) {
            card.cardFunction();
        } else {
            passNextPlayer(true);
            isAction = false;
            eachRound();
        }
    }

    public void passNextPlayer(boolean playCard){
        if(playCard){
            Card topCard = getTopCard();
            int nextPlayer = topCard.cardFunction();
            int currentIndex = players.indexOf(currentPlayer);
            int nextIndex = (currentIndex + nextPlayer * playDirection + players.size()) % players.size();
            currentPlayer = players.get(nextIndex);
        }else{
            int currentIndex = players.indexOf(currentPlayer);
            int nextIndex = (currentIndex + 1 * playDirection + players.size()) % players.size();
            currentPlayer = players.get(nextIndex);
        }
        gamePanel.updateDisplay();
    }
    
    public boolean canPlayCard(Card playedCard) {
        Card topCard = getTopCard();
        switch (playedCard.getType()) {
            case Wild:
            case WildDrawFour:
                return true;
            case Skip:
            case Reverse:
            case DrawTwo:
                if (playedCard.getColor() == topCard.getColor() || playedCard.getType() == topCard.getType()) {
                    return true;
                }
                return false;
            case Number:
                if(playedCard.getColor() == topCard.getColor()){
                    return true;
                }else if(topCard.getType()==Type.Number && ((NumberCard) playedCard).getValue() == ((NumberCard)topCard).getValue()){
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    public void startGame() {
        if (mainFrame == null && menuPanel != null) {
            mainFrame = (JFrame) SwingUtilities.getWindowAncestor(menuPanel);
        }
        
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll();
            
            if (gamePanel == null) {
                gamePanel = new UNOGamePanel(this);
            }
            
            mainFrame.add(gamePanel);
            mainFrame.revalidate();
            gamePanel.requestFocusInWindow();
            gamePanel.startGame(); 
            this.eachRound();
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
    
    public CountDownTimer getTurnTimer() {
        return this.turnTimer;
    }

    public WildCardViewer getWildCardViewer(){
        return wildCardViewer;
    }
}
