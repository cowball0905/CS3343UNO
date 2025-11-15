package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import controller.UNOController;
import view.*;

import java.util.ArrayList;

public class TestUNOController {
    private UNOController controller;
    private ArrayList<Player> players;

    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        controller = UNOController.getInstance();
        controller.startGame();
        players = controller.getPlayerList();
        controller.setIsFreezed(true);
    }

    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }

    @Test
    public void testGetInstance() {
        UNOController instance1 = UNOController.getInstance();
        UNOController instance2 = UNOController.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    public void testResetInstance() {
        UNOController instance1 = UNOController.getInstance();
        UNOController.resetInstance();
        UNOController instance2 = UNOController.getInstance();

        assertNotSame(instance1, instance2);
    }

    @Test
    public void testSetPlayers() {
        assertEquals(4, players.size());
    }

    @Test
    public void testFirstPlayerIsHuman() {
        assertTrue(players.get(0) instanceof HumanPlayer);
    }

    @Test
    public void testSecondPlayerIsCPU() {
        assertTrue(players.get(1) instanceof CPUPlayer);
    }

    @Test
    public void testThirdPlayerIsCPU() {
        assertTrue(players.get(2) instanceof CPUPlayer);
    }

    @Test
    public void testFourthPlayerIsCPU() {
        assertTrue(players.get(3) instanceof CPUPlayer);
    }

    @Test
    public void testHumanPlayerName() {
        assertEquals("Player", players.get(0).getName());
    }

    @Test
    public void testCPU1Name() {
        assertEquals("CPU1", players.get(1).getName());
    }

    @Test
    public void testCPU2Name() {
        assertEquals("CPU2", players.get(2).getName());
    }

    @Test
    public void testCPU3Name() {
        assertEquals("CPU3", players.get(3).getName());
    }

    @Test
    public void testStartGameSetsCurrentPlayer() {

        assertNotNull(controller.getCurrentPlayer());
    }

    @Test
    public void testStartGameFirstPlayerCurrent() {

        assertEquals(controller.getPlayerList().get(0), controller.getCurrentPlayer());
    }

    @Test
    public void testStartGamePlayDirection() {

        assertEquals(1, controller.getPlayDirection());
    }

    @Test
    public void testStartGameInitializesDeck() {

        assertNotNull(controller.getDeck());
    }

    @Test
    public void testStartGameSetsTopCard() {

        assertNotNull(controller.getTopCard(1));
    }

    @Test
    public void testSetAndGetCurrentPlayer() {
        Player testPlayer = players.get(2);
        controller.setCurrentPlayer(testPlayer);

        assertEquals(testPlayer, controller.getCurrentPlayer());
    }

    @Test
    public void testSetPlayDirectionClockwise() {
        controller.setPlayDirection(1);

        assertEquals(1, controller.getPlayDirection());
    }

    @Test
    public void testSetPlayDirectionCounterClockwise() {
        controller.setPlayDirection(-1);

        assertEquals(-1, controller.getPlayDirection());
    }

    @Test
    public void testCheckCurrentPlayerFirst() {
        controller.setCurrentPlayer(players.get(0));

        assertEquals(0, controller.checkCurrentPlayer());
    }

    @Test
    public void testCheckCurrentPlayerThird() {
        controller.setCurrentPlayer(players.get(2));

        assertEquals(2, controller.checkCurrentPlayer());
    }

    @Test
    public void testCheckPlayerFirst() {
        assertEquals(0, controller.checkPlayer(players.get(0)));
    }

    @Test
    public void testCheckPlayerSecond() {
        assertEquals(1, controller.checkPlayer(players.get(1)));
    }

    @Test
    public void testCheckPlayerFourth() {
        assertEquals(3, controller.checkPlayer(players.get(3)));
    }

    @Test
    public void testPassNextPlayerClockwise() {
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);

        controller.passNextPlayer(1);

        assertEquals(players.get(1), controller.getCurrentPlayer());
    }

    @Test
    public void testPassNextPlayerClockwiseTwice() {
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);

        controller.passNextPlayer(1);
        controller.passNextPlayer(1);

        assertEquals(players.get(2), controller.getCurrentPlayer());
    }

    @Test
    public void testPassNextPlayerCounterClockwise() {
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(-1);

        controller.passNextPlayer(1);

        assertEquals(players.get(3), controller.getCurrentPlayer());
    }

    @Test
    public void testPassNextPlayerCounterClockwiseTwice() {
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(-1);

        controller.passNextPlayer(1);
        controller.passNextPlayer(1);

        assertEquals(players.get(2), controller.getCurrentPlayer());
    }

    @Test
    public void testPassNextPlayerWrapsAround() {
        controller.setCurrentPlayer(players.get(3));
        controller.setPlayDirection(1);

        controller.passNextPlayer(1);

        assertEquals(players.get(0), controller.getCurrentPlayer());
    }

    @Test
    public void testPassNextPlayerWithSkip() {
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);

        controller.passNextPlayer(2);

        assertEquals(players.get(2), controller.getCurrentPlayer());
    }

    @Test
    public void testCanPlayCardMatchingColor() {
        Card topCard = new NumberCard(Color.Red, 5, true);
        Card playCard = new NumberCard(Color.Red, 3, true);

        assertTrue(controller.canPlayCard(playCard, topCard));
    }

    @Test
    public void testCanPlayCardMatchingNumber() {
        Card topCard = new NumberCard(Color.Red, 5, true);
        Card playCard = new NumberCard(Color.Blue, 5, true);

        assertTrue(controller.canPlayCard(playCard, topCard));
    }

    @Test
    public void testCanPlayCardWildCard() {
        Card topCard = new NumberCard(Color.Red, 5, true);
        Card wildCard = new WildCard(true);
        wildCard.setColor(Color.Red);

        assertTrue(controller.canPlayCard(wildCard, topCard));
    }

    @Test
    public void testCanPlayCardWildDrawFour() {
        Card topCard = new NumberCard(Color.Red, 5, true);
        Card wildDrawFour = new WildDrawFourCard(true);
        wildDrawFour.setColor(Color.Red);

        assertTrue(controller.canPlayCard(wildDrawFour, topCard));
    }

    @Test
    public void testCanPlayCardNonMatching() {
        Card topCard = new NumberCard(Color.Red, 5, true);
        Card playCard = new NumberCard(Color.Blue, 3, true);

        assertFalse(controller.canPlayCard(playCard, topCard));
    }

    @Test
    public void testCanPlayCardMatchingActionCards() {
        Card topCard = new SkipCard(Color.Red, true);
        Card playCard = new SkipCard(Color.Blue, true);

        assertTrue(controller.canPlayCard(playCard, topCard));
    }

    @Test
    public void testCanPlayCardNullTopCard() {
        Card playCard = new NumberCard(Color.Red, 5, true);

        assertTrue(controller.canPlayCard(playCard, null));
    }

    @Test
    public void testPlayCard() {
        Card card = new NumberCard(Color.Red, 5, true);
        controller.playCard(card);
        assertEquals(card, controller.getTopCard(1));
    }

    @Test
    public void testPlayCardRemovesFromHand() {
        controller.setCurrentPlayer(players.get(0));

        Player player = controller.getCurrentPlayer();
        player.getHand().clear();
        Card card = new NumberCard(Color.Red, 5, true);
        player.drawCard(card);

        controller.playCard(card);

        assertEquals(0, player.getHand().size());
    }

    @Test
    public void testSetIsFreezedTrue() {
        controller.setIsFreezed(true);

        assertTrue(controller.getIsFreezed());
    }

    @Test
    public void testSetIsFreezedFalse() {
        controller.setIsFreezed(false);

        assertFalse(controller.getIsFreezed());
    }

    @Test
    public void testGetCardFactory() {
        assertNotNull(controller.getCardFactory());
    }

    @Test
    public void testGetCardFactoryType() {
        assertTrue(controller.getCardFactory() instanceof CardFactory);
    }

    @Test
    public void testGetDeck() {

        assertNotNull(controller.getDeck());
    }

    @Test
    public void testGetDeckHasCards() {

        assertTrue(controller.getDeck().size() > 0);
    }

    @Test
    public void testGetTurnTimer() {
        assertNotNull(controller.getTurnTimer());
    }

    @Test
    public void testGetWildCardViewer() {
        assertNotNull(controller.getWildCardViewer());
    }

    @Test
    public void testGetChallengeViewer() {
        assertNotNull(controller.getChallengeViewer());
    }

    @Test
    public void testGetResultViewer() {
        assertNotNull(controller.getResultViewer());
    }

    @Test
    public void testGetDeckPlayCardViewer() {
        assertNotNull(controller.getDeckPlayCardViewer());
    }

    @Test
    public void testGetGamePanel() {
        assertNotNull(controller.getGamePanel());
    }

    @Test
    public void testGetHumanCard() {
        ArrayList<Card> humanHand = controller.getPlayerCard(0);

        assertEquals(controller.getPlayerList().get(0).getHand(), humanHand);
    }

    @Test
    public void testGetCPU1Card() {

        ArrayList<Card> cpu1Hand = controller.getPlayerCard(1);

        assertEquals(controller.getPlayerList().get(1).getHand(), cpu1Hand);
    }

    @Test
    public void testGetCPU2Card() {

        ArrayList<Card> cpu2Hand = controller.getPlayerCard(2);

        assertEquals(controller.getPlayerList().get(2).getHand(), cpu2Hand);
    }

    @Test
    public void testIsGameEnd() {
        controller.setIsFreezed(true);

        Player player = controller.getPlayerList().get(0);
        controller.setCurrentPlayer(player);
        player.getHand().clear();

        Card matchingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(new NumberCard(Color.Red, 3, true));

        assertTrue(controller.isGameEnd(matchingCard));
    }

    @Test
    public void testIsGameEndWithCards() {
        controller.setIsFreezed(true);

        Player player = controller.getPlayerList().get(0);
        controller.setCurrentPlayer(player);
        player.getHand().clear();
        player.drawCard(new NumberCard(Color.Blue, 3, true));

        Card card = new NumberCard(Color.Red, 5, true);
        controller.playCard(new NumberCard(Color.Red, 3, true));

        assertFalse(controller.isGameEnd(card));
    }

    @Test
    public void testGetSortedPlayersScoreNotNull() {
        controller.setCurrentPlayer(players.get(0));

        ArrayList<Player> sortedPlayers = controller.getSortedPlayersScore();

        assertNotNull(sortedPlayers);
    }

    @Test
    public void testGetSortedPlayersScoreSize() {
        controller.setCurrentPlayer(players.get(0));

        ArrayList<Player> sortedPlayers = controller.getSortedPlayersScore();

        assertEquals(4, sortedPlayers.size());
    }

    @Test
    public void testGetSortedPlayersScoreCurrentFirst() {
        controller.setCurrentPlayer(players.get(0));

        ArrayList<Player> sortedPlayers = controller.getSortedPlayersScore();

        assertEquals(players.get(0), sortedPlayers.get(0));
    }

    @Test
    public void testGetCardFromDeck() {
        controller.setCurrentPlayer(players.get(0));

        Player player = controller.getCurrentPlayer();
        int initialHandSize = player.getHand().size();

        controller.getCardFromDeck();

        assertEquals(initialHandSize + 1, player.getHand().size());
    }

    @Test
    public void testCanPlayCardSkipMatchingColor() {
        Card topCard = new SkipCard(Color.Red, true);
        Card playCard = new NumberCard(Color.Red, 5, true);

        assertTrue(controller.canPlayCard(playCard, topCard));
    }

    @Test
    public void testCanPlayCardReverseMatchingType() {
        Card topCard = new ReverseCard(Color.Red, true);
        Card playCard = new ReverseCard(Color.Blue, true);

        assertTrue(controller.canPlayCard(playCard, topCard));
    }

    @Test
    public void testCanPlayCardDrawTwoMatchingType() {
        Card topCard = new DrawTwoCard(Color.Red, true);
        Card playCard = new DrawTwoCard(Color.Blue, true);

        assertTrue(controller.canPlayCard(playCard, topCard));
    }

    @Test
    public void testGetPlayerListNotNull() {
        ArrayList<Player> playerList = controller.getPlayerList();

        assertNotNull(playerList);
    }

    @Test
    public void testGetPlayerListSize() {
        ArrayList<Player> playerList = controller.getPlayerList();

        assertEquals(4, playerList.size());
    }

    @Test
    public void testGetPlayerList() {
        ArrayList<Player> playerList = controller.getPlayerList();

        assertSame(players, playerList);
    }

    @Test
    public void testGetTopCardMostRecent() {
        controller.setIsFreezed(true);

        Card card1 = new NumberCard(Color.Red, 5, true);
        Card card2 = new NumberCard(Color.Blue, 3, true);

        controller.playCard(card1);
        controller.playCard(card2);

        assertEquals(card2, controller.getTopCard(1));
    }

    @Test
    public void testGetTopCardSecondRecent() {
        controller.setIsFreezed(true);

        Card card1 = new NumberCard(Color.Red, 5, true);
        Card card2 = new NumberCard(Color.Blue, 3, true);

        controller.playCard(card1);
        controller.playCard(card2);

        assertEquals(card1, controller.getTopCard(2));
    }
}
