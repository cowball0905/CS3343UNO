package controller;

import model.*;
import view.ChallengeViewer;
import view.UNOGamePanel;
import view.WildCardViewer;

import java.util.*;
import javax.swing.Timer;

import javax.swing.JFrame;

public class UNOController {
    private static UNOController instance;
    private UNOGamePanel gamePanel;
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
    private ChallengeViewer challengeViewer;
    private final int INITCARDSIZE = 7;

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
        for (Player p : players) {
            p.setController(this);
        }
        
        gamePanel = new UNOGamePanel(this);
        wildCardViewer = new WildCardViewer();
        challengeViewer = new ChallengeViewer();

        turnTimer = new CountDownTimer(gamePanel, new CountDownTimer.TimerCallback() {
            @Override
            public void onTimerComplete() {
                if (wildCardViewer.isHavingWild()) {
                    wildCardViewer.autoSelectColor();
                    wildCardViewer.setHavingWild(false);
                    wildCardViewer.removeButtons();
                    if(wildCardViewer.getCard().getType()==Type.WildDrawFour){
                        wildCardViewer.callChallenge();
                    }else{  
                        passNextPlayer(1);
                    }
                    eachRound();
                }else if(challengeViewer.getIsChallenging()){
                    int currentIndex = checkCurrentPlayer();
                    int nextIndex = (currentIndex + (1 * playDirection) + players.size()) % players.size();
                    Player nextPlayer = players.get(nextIndex);
                    for(int i=0;i<4;i++){
                        nextPlayer.drawCard(getCardFactory().giveCard(Deck, false, checkCurrentPlayer()==0? true: false, ""));
                    }
                }else {
                    getCardFromDeck();
                }
            }
        });
        
        wildCardViewer.setTimer(turnTimer);
        wildCardViewer.setController(this);
        wildCardViewer.setPanel(gamePanel);

        challengeViewer.setTimer(turnTimer);
        challengeViewer.setController(this);
        challengeViewer.setPanel(gamePanel);
        
        //initializeGame();
        
        mainFrame.setVisible(true);
        
        System.out.println("UNO Game window created and should be visible!");
    }

    public static UNOController getInstance() {
        if (instance == null) {
            instance = new UNOController();
        }
        return instance;
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

        // Clear played cards
        PlayedCard.clear();
        // Clear each player's hand
        for (Player player : players) {
            player.getHand().clear();
        }
        // Stop turn timer
        turnTimer.stopTimer();
        turnTimer.startTimer(30); 
        gamePanel.setIsGameEnd(false);
        // Reset other game state variables if needed
        isAction = false;

        PlayedCard.add(cardFactory.giveCard(Deck,true, true, "")); // 已打出的牌顯示
        
        for (int i = 0; i < INITCARDSIZE; i++) {
            players.get(0).drawCard(cardFactory.giveCard(Deck,false, true, "")); // 玩家的牌顯示
            for(int j = 1 ; j < 4 ; j++){
                players.get(j).drawCard(cardFactory.giveCard(Deck,false, false, "")); // CPU的牌隱藏
            }
        }
        currentPlayer = players.get(0);
        System.out.println("Current Player: " + currentPlayer.getName());
        playDirection = 1;
    }

    public void getCardFromDeck(){
        if(!isAction){
            isAction = true;
            System.out.println(currentPlayer.getName()+ " draws a card from the deck.");
            currentPlayer.drawCard(cardFactory.giveCard(Deck,false, checkCurrentPlayer()==0 ? true:false, ""));
            passNextPlayer(1);
            eachRound();
            isAction = false;
        }
    }

    public boolean isGameEnd(Card card) {
        if (currentPlayer.getHand().size() == 0 && canPlayCard(card)) {
            System.out.println(currentPlayer.getName() + " win!");
            gamePanel.setIsGameEnd(true);
            return true;
        }
        return false;
    }

    public void eachRound(){ 
        gamePanel.updateDisplay();
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
        if(isGameEnd(card)) {
            return;
        }
        card.cardFunction(this);
        isAction = false;
    }

    public void passNextPlayer(int amount){
        int currentIndex = checkCurrentPlayer();
        int nextIndex = (currentIndex + amount * playDirection + players.size()) % players.size();
        currentPlayer = players.get(nextIndex);
        gamePanel.updateDisplay();
    }
    
    public boolean canPlayCard(Card playedCard) {
        Card topCard = getTopCard();
        
        // 如果没有顶牌（游戏刚开始或PlayedCard为空），允许任何牌
        if (topCard == null) {
            return true;
        }
        
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

    public ChallengeViewer getChallengeViewer(){
        return this.challengeViewer;
    }

    public void startGame() {
        initializeGame();
        
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll();
            
            if (gamePanel == null) {
                gamePanel = new UNOGamePanel(this);
            }
            
            mainFrame.add(gamePanel);
            mainFrame.revalidate();
            gamePanel.requestFocusInWindow();
            gamePanel.startGame(); 
        }

        // Ensure currentPlayer is human before starting the round
        currentPlayer = players.get(0);

        // Only call eachRound() once, after everything is set up
        this.eachRound();
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

    public ArrayList<Card> getHumanPlayedCard() {
        return players.get(0).getHand();
    }

    public ArrayList<Card> getCPUCard(int index) {
        return players.get(index+1).getHand();
    }

    public Card getTopCard() {
        if (PlayedCard == null || PlayedCard.isEmpty()) {
            return null;
        }
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

    public UNOGamePanel getGamePanel() {
        return gamePanel;
    }

    public int checkCurrentPlayer(){
        return players.indexOf(currentPlayer);
    }

    public int checkPlayer(Player p){
        return players.indexOf(p);
    }
}