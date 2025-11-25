package controller;

import model.*;
import view.ChallengeViewer;
import view.ResultViewer;
import view.DeckPlayCardViewer;
import view.UNOGamePanel;
import view.WildCardViewer;

import java.util.*;
import javax.swing.Timer;

import javax.swing.JFrame;

public class UNOController {
    private static UNOController instance;
    private UNOGamePanel gamePanel;
    private JFrame mainFrame;
    private HashMap<String, Integer> Deck;
    private CardFactory cardFactory = new ConcreteCardFactory();
    private ArrayList<Card> PlayedCard = new ArrayList<>();
    private Player currentPlayer;
    private ArrayList<Player> players;
    private int playDirection;
    private CountDownTimer turnTimer;
    private boolean isAction = false;
    private boolean isFreezed = false;
    private WildCardViewer wildCardViewer;
    private ChallengeViewer challengeViewer;
    private ResultViewer resultViewer;
    private DeckPlayCardViewer deckPlayCardViewer;
    private boolean isDraw = false;
    private final int INITCARDSIZE = 7;

    private UNOController() {
        System.out.println("UNO Game window created and should be visible!");
    }

    public static UNOController getInstance() {
        if (instance == null) {
            instance = new UNOController();
        }
        return instance;
    }

    public void setViewers() {
        mainFrame = new JFrame("UNO Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        gamePanel = new UNOGamePanel();
        wildCardViewer = new WildCardViewer();
        challengeViewer = new ChallengeViewer();
        resultViewer = new ResultViewer();
        deckPlayCardViewer = new DeckPlayCardViewer();

        wildCardViewer.setController();
        challengeViewer.setController();
        deckPlayCardViewer.setController();

        turnTimer = new CountDownTimer(gamePanel, new CountDownTimer.TimerCallback() {
            @Override
            public void onTimerComplete() {
                if (wildCardViewer.isHavingWild()) {
                    wildCardViewer.autoSelectColor();
                    wildCardViewer.setHavingWild(false);
                    wildCardViewer.removeButtons();
                    if (wildCardViewer.getCard().getType() == Type.WildDrawFour) {
                        wildCardViewer.callChallenge();
                    } else {
                        passNextPlayer(1);
                    }
                    eachRound();
                } else if (challengeViewer.getIsChallenging()) {
                    challengeViewer.setChallenge(false);
                    challengeViewer.removeButtons();
                    int currentIndex = checkCurrentPlayer();
                    int nextIndex = (currentIndex + (1 * playDirection) + players.size()) % players.size();
                    Player nextPlayer = players.get(nextIndex);
                    for (int i = 0; i < 4; i++) {
                        Card card = getCardFactory().giveCard(Deck, false, nextIndex == 0 ? true : false, "");
                        if (card == null) {
                            deckEmpty();
                            return;
                        }
                        nextPlayer.drawCard(card);
                    }
                    passNextPlayer(2);
                    eachRound();
                } else if (deckPlayCardViewer.getIsDeciding()) {
                    deckPlayCardViewer.endDeckCardViewer();
                    Card card = currentPlayer.getHand().get(currentPlayer.getHand().size() - 1);
                    playCard(card);
                    isAction = false;
                } else {
                    getCardFromDeck();
                }
            }
        });

        wildCardViewer.setTimer(turnTimer);
        wildCardViewer.setPanel(gamePanel);

        challengeViewer.setTimer(turnTimer);
        challengeViewer.setPanel(gamePanel);

        resultViewer.setController();
        resultViewer.setPanel(gamePanel);

        deckPlayCardViewer.setTimer(turnTimer);
        deckPlayCardViewer.setPanel(gamePanel);

        // initializeGame();

        mainFrame.setVisible(true);
    }

    public void setPlayers() {
        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                players.add(new HumanPlayer("Player"));
            } else {
                players.add(new CPUPlayer("CPU" + (i)));
            }
        }
        for (Player p : players) {
            p.setController();
        }

    }

    private void initializeGame() {
        Deck = new HashMap<>();
        Deck = initialDeck(Deck);

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

        Card topCard = cardFactory.giveCard(Deck, true, true, "");
        if (topCard == null) {
            deckEmpty();
            return;
        }
        PlayedCard.add(topCard); // 已打出的牌顯示

        for (int i = 0; i < INITCARDSIZE; i++) {
            Card playerCard = cardFactory.giveCard(Deck, false, true, "");
            if (playerCard == null) {
                deckEmpty();
                return;
            }
            players.get(0).drawCard(playerCard); // 玩家的牌顯示
            for (int j = 1; j < 4; j++) {
                Card cpuCard = cardFactory.giveCard(Deck, false, false, "");
                if (cpuCard == null) {
                    deckEmpty();
                    return;
                }
                players.get(j).drawCard(cpuCard); // CPU的牌隱藏
            }
        }
        currentPlayer = players.get(0);
        System.out.println("Current Player: " + currentPlayer.getName());
        playDirection = 1;
    }

    public void getCardFromDeck() {
        if (!isAction) {
            isAction = true;
            if (currentPlayer != null) {
                System.out.println(currentPlayer.getName() + " draws a card from the deck.");
                Card newCard = cardFactory.giveCard(Deck, false, checkCurrentPlayer() == 0 ? true : false, "");
                if (newCard == null) {
                    deckEmpty();
                    isAction = false;
                    return;
                }
                currentPlayer.drawCard(newCard);
                Card card = currentPlayer.getHand().get(currentPlayer.getHand().size() - 1);
                gamePanel.updateDisplay();
                if (canPlayCard(card, getTopCard(1))) {
                    if (checkCurrentPlayer() == 0) {
                        deckPlayCardViewer.setIsDeciding(card);
                        turnTimer.startTimer(10);
                    } else {
                        System.out.println(
                                currentPlayer.getName() + " got a matching card! they choose to play the card");
                        currentPlayer.playCard(card);
                    }
                } else {
                    passNextPlayer(1);
                    eachRound();
                    isAction = false;
                }
            } else {
                System.err.println("currentPlayer variable is null in getCardFromDeck()");
            }
        }
    }

    private HashMap<String, Integer> initialDeck(HashMap<String, Integer> Deck) {
        Deck.clear();
        for (String color : new String[] { "r", "g", "b", "y" }) {
            Deck.put(color + "0", 1);
            for (int i = 1; i <= 9; i++) {
                Deck.put(color + i, 2);
            }
        }

        for (String color : new String[] { "r", "g", "b", "y" }) {
            Deck.put(color + "Skip", 2);
            Deck.put(color + "Reverse", 2);
            Deck.put(color + "DrawTwo", 2);
        }

        Deck.put("WildCard", 4);
        Deck.put("WildDrawFour", 4);
        return Deck;
    }

    public ArrayList<Player> getSortedPlayersScore() {
        ArrayList<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers = mergeSort(sortedPlayers);
        if (!isDraw) {
            // Move current player to first position
            sortedPlayers.remove(currentPlayer);
            sortedPlayers.add(0, currentPlayer);
        }
        return sortedPlayers;
    }

    /*
     * Merge sort implementation for sorting players by score
     */
    private ArrayList<Player> mergeSort(ArrayList<Player> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        ArrayList<Player> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<Player> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private ArrayList<Player> merge(ArrayList<Player> left, ArrayList<Player> right) {
        ArrayList<Player> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getScore() <= right.get(j).getScore()) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }

        return result;
    }

    public boolean isGameEnd(Card card) {
        if (currentPlayer.getHand().size() == 0 && canPlayCard(card, PlayedCard.get(PlayedCard.size() - 1))) {
            System.out.println(currentPlayer.getName() + " win!");
            gamePanel.setIsGameEnd(true);
            turnTimer.stopTimer();
            return true;
        }
        return false;
    }

    public void eachRound() {
        gamePanel.updateDisplay();
        if (currentPlayer == players.get(0)) {
            if (!isFreezed) {
                turnTimer.startTimer(30);
            }
        } else {
            turnTimer.stopTimer();
            CPUPlayer cpuPlayer = (CPUPlayer) currentPlayer;

            Timer cpuTimer = new Timer(2000, e -> cpuPlayer.chooseCard());
            cpuTimer.setRepeats(false);
            if (!isFreezed) {
                cpuTimer.start();
            }
        }
    }

    public void playCard(Card card) {
        if (card == null) {
            System.err.println("Card variable is empty in playCard()");
            return;
        }
        isAction = true;
        card.setRotation(0);
        PlayedCard.add(card);
        currentPlayer.playCard(card);
        gamePanel.updateDisplay();
        if (isGameEnd(card)) {
            return;
        }
        card.cardFunction(this);
        isAction = false;
    }

    public void passNextPlayer(int amount) {
        isAction = false;
        int currentIndex = checkCurrentPlayer();
        int nextIndex = (currentIndex + amount * playDirection + players.size()) % players.size();
        currentPlayer = players.get(nextIndex);
        gamePanel.updateDisplay();
    }

    public boolean canPlayCard(Card matchCard, Card topCard) {
        // 如果没有顶牌（游戏刚开始或PlayedCard为空），允许任何牌
        if (topCard == null || matchCard == null) {
            return true;
        }

        return topCard.checkCard(matchCard);
    }

    public ChallengeViewer getChallengeViewer() {
        return this.challengeViewer;
    }

    public void startGame() {
        setPlayers();
        setViewers();
        initializeGame();

        mainFrame.getContentPane().removeAll();

        mainFrame.add(gamePanel);
        mainFrame.revalidate();
        gamePanel.requestFocusInWindow();
        gamePanel.startGame();

        // Ensure currentPlayer is human before starting the round
        currentPlayer = players.get(0);

        // Only call eachRound() once, after everything is set up
        this.eachRound();
    }

    /*
     * Handles the scenario when the deck is empty
     */
    public void deckEmpty() {
        isDraw = true;
        gamePanel.setResultMessage("Deck is Empty !!!");
        gamePanel.setIsGameEnd(true);
    }

    public CardFactory getCardFactory() {
        return this.cardFactory;
    }

    public HashMap<String, Integer> getDeck() {
        return this.Deck;
    }

    public CountDownTimer getTurnTimer() {
        return this.turnTimer;
    }

    public WildCardViewer getWildCardViewer() {
        return wildCardViewer;
    }

    public DeckPlayCardViewer getDeckPlayCardViewer() {
        return deckPlayCardViewer;
    }

    public ResultViewer getResultViewer() {
        return resultViewer;
    }

    public ArrayList<Card> getPlayerCard(int index) {
        return players.get(index).getHand();
    }

    public Card getTopCard(int index) {
        return PlayedCard.get(PlayedCard.size() - index);
    }

    public Player getCurrentPlayer() {
        int index = players.indexOf(currentPlayer);
        return players.get(players.indexOf(currentPlayer));
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public ArrayList<Player> getPlayerList() {
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

    public int checkCurrentPlayer() {
        return players.indexOf(currentPlayer);
    }

    public int checkPlayer(Player p) {
        return players.indexOf(p);
    }

    public void setIsFreezed(boolean freeze) {
        isFreezed = freeze;
    }

    public boolean getIsFreezed() {
        return isFreezed;
    }

    public static void resetInstance() {
        if (instance != null) {
            // Stop timer first to prevent callbacks from accessing null state
            if (instance.turnTimer != null) {
                instance.turnTimer.stopTimer();
            }

            // Clean up player hands to remove any null cards before reset
            if (instance.players != null) {
                for (Player player : instance.players) {
                    if (player != null && player.getHand() != null) {
                        player.getHand().removeIf(card -> card == null);
                    }
                }
            }
            instance = null;
        }
    }

    public Boolean getIsDraw() {
        return isDraw;
    }
}