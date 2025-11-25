package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import controller.Main;
import controller.UNOController;
import model.*;

import java.awt.Window;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TestMain {
    
    private UNOController controller;
    
    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
    }
    
    @AfterEach
    public void tearDown() {
        if (controller != null) {
            controller.setIsFreezed(true);
        }
        UNOController.resetInstance();
        
        for (Window window : Window.getWindows()) {
            window.dispose();
        }
    }
    
    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    public void testCompleteUNOGameSystem() {
        
        Main.main(new String[]{});
        controller = UNOController.getInstance();
        
        assertNotNull(controller);
        assertNotNull(controller.getPlayerList());
        assertNotNull(controller.getDeck());
        assertNotNull(controller.getCardFactory());
        assertNotNull(controller.getCurrentPlayer());
        
        ArrayList<Player> players = controller.getPlayerList();
        
        assertEquals(4, players.size());
        assertTrue(players.get(0) instanceof HumanPlayer);
        assertTrue(players.get(1) instanceof CPUPlayer);
        
        for (int i = 0; i < players.size(); i++) {
            assertEquals(7, players.get(i).getHand().size());
        }
        
        assertTrue(controller.getDeck().size() > 0);
        assertNotNull(controller.getTopCard(1));
        
        controller.setIsFreezed(true);
        controller.setCurrentPlayer(players.get(0));
        
        for (int i = 0; i < 4; i++) {
            assertEquals(players.get(i), controller.getCurrentPlayer());
            controller.passNextPlayer(1);
        }
        
        assertEquals(players.get(0), controller.getCurrentPlayer());
        
        controller.setPlayDirection(-1);
        controller.passNextPlayer(1);
        assertEquals(players.get(3), controller.getCurrentPlayer());
        
        Card numberCard = new NumberCard(Color.Red, 5, true);
        Card skipCard = new SkipCard(Color.Blue, true);
        Card reverseCard = new ReverseCard(Color.Green, true);
        Card drawTwoCard = new DrawTwoCard(Color.Yellow, true);
        Card wildCard = new WildCard(true);
        Card wildDrawFour = new WildDrawFourCard(true);
        
        assertNotNull(numberCard);
        assertNotNull(skipCard);
        assertNotNull(reverseCard);
        assertNotNull(drawTwoCard);
        assertNotNull(wildCard);
        assertNotNull(wildDrawFour);
        
        Card topCard = new NumberCard(Color.Red, 3, true);
        controller.playCard(topCard);
        
        assertTrue(controller.canPlayCard(new NumberCard(Color.Red, 7, true), topCard));
        assertTrue(controller.canPlayCard(new NumberCard(Color.Blue, 3, true), topCard));
        assertFalse(controller.canPlayCard(new NumberCard(Color.Blue, 7, true), topCard));
        
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);
        controller.passNextPlayer(2);
        assertEquals(players.get(2), controller.getCurrentPlayer());
        
        Player testPlayer = players.get(0);
        controller.setCurrentPlayer(testPlayer);
        int handSizeBefore = testPlayer.getHand().size();
        controller.getCardFromDeck();
        assertTrue(testPlayer.getHand().size() >= handSizeBefore);
        
        Player humanPlayer = players.get(0);
        controller.setCurrentPlayer(humanPlayer);
        Card currentTopCard = controller.getTopCard(1);
        Card playableCard = null;
        
        for (Card card : humanPlayer.getHand()) {
            if (controller.canPlayCard(card, currentTopCard)) {
                playableCard = card;
                break;
            }
        }
        
        if (playableCard != null) {
            handSizeBefore = humanPlayer.getHand().size();
            controller.playCard(playableCard);
            
            assertEquals(handSizeBefore - 1, humanPlayer.getHand().size());
            assertEquals(playableCard, controller.getTopCard(1));
        }
        
        for (int turn = 0; turn < 10; turn++) {
            controller.passNextPlayer(1);
            assertNotNull(controller.getCurrentPlayer());
        }
        
        assertNotNull(controller.getGamePanel());
        assertNotNull(controller.getWildCardViewer());
        assertNotNull(controller.getChallengeViewer());
        assertNotNull(controller.getResultViewer());
        assertNotNull(controller.getDeckPlayCardViewer());
        assertNotNull(controller.getTurnTimer());
        
        controller.getDeck().clear();
        Player player = controller.getCurrentPlayer();
        int handSize = player.getHand().size();
        controller.getCardFromDeck();
        
        assertEquals(handSize, player.getHand().size());
        assertTrue(controller.getIsDraw());
        
        Card invalidCard = new NumberCard(Color.Blue, 7, true);
        Card tableCard = new NumberCard(Color.Red, 3, true);
        assertFalse(controller.canPlayCard(invalidCard, tableCard));
        
        Player winner = players.get(0);
        controller.setCurrentPlayer(winner);
        winner.getHand().clear();
        
        Card finalCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(finalCard);
        
        assertTrue(controller.isGameEnd(finalCard));
        
        ArrayList<Player> rankings = controller.getSortedPlayersScore();
        assertEquals(winner, rankings.get(0));
        assertEquals(0, winner.getScore());
        
        assertNotNull(controller.getPlayerList());
        assertEquals(4, controller.getPlayerList().size());
        assertNotNull(controller.getCardFactory());
    }
}
