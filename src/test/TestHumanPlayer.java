
package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;

public class TestHumanPlayer {
    private HumanPlayer humanPlayer;
    private UNOController controller;
    private CPUPlayer cpuPlayer;

    @BeforeEach
    public void setUp() {
        controller = UNOController.getInstance();
        controller.startGame();

        humanPlayer = (HumanPlayer) controller.getPlayerList().get(0);
        cpuPlayer = (CPUPlayer) controller.getPlayerList().get(1);
    }

    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }

    @Test
    public void testGetName() {
        assertEquals("Player", humanPlayer.getName());
    }

    @Test
    public void testDrawCard() {
        int initialSize = humanPlayer.getHand().size();

        Card card = new NumberCard(Color.Red, 5, true);
        humanPlayer.drawCard(card);
        assertEquals(initialSize + 1, humanPlayer.getHand().size());
        assertTrue(humanPlayer.getHand().contains(card));

        int sizeBeforeNull = humanPlayer.getHand().size();
        humanPlayer.drawCard(null);
        assertEquals(sizeBeforeNull + 1, humanPlayer.getHand().size(),
            "Should add null to hand when null card is drawn");
        assertNull(humanPlayer.getHand().get(humanPlayer.getHand().size() - 1),
            "Last card in hand should be null");

        humanPlayer.setIsShout(true);
        humanPlayer.drawCard(new NumberCard(Color.Blue, 2, true));
        assertFalse(humanPlayer.getIsShout(), "isShout should be reset to false when drawing a card");
    }

    @Test
    public void testShoutUno() {
    	Card newTopCard = new NumberCard(Color.Red, 3, true);
        controller.playCard(newTopCard);
    	
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        
        controller.setCurrentPlayer(humanPlayer);
       
        String result = humanPlayer.shoutUno();
        
        assertNull(result, "Should return null when shout UNO is successful");
        assertTrue(humanPlayer.getIsShout());
    }
    
    @Test
    void testSHoutUNOMoreThanTwoCard() {
		humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        humanPlayer.getHand().add(new NumberCard(Color.Yellow, 4, true));
        humanPlayer.getHand().add(new NumberCard(Color.Yellow, 4, true));
        
        controller.setCurrentPlayer(humanPlayer);
        
        String result = humanPlayer.shoutUno();
        assertEquals("You have more than 2 card!", result,
            "Should return error when more than one card");}
    
    @Test
    void testSHoutUNOWithNoCard() {
        humanPlayer.getHand().clear();

        controller.setCurrentPlayer(cpuPlayer);

        String result = humanPlayer.shoutUno();
        assertEquals(result, "It's not your turn!");
        }
    
    @Test
    void testSHoutUNONotPlayerTurn() {
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));

        controller.setCurrentPlayer(cpuPlayer);

        String result = humanPlayer.shoutUno();
        assertEquals(result, "It's not your turn!",
			"Should return error when no cards in hand");
        }
    
    @Test
    void testSHoutUNOTwice() {
    	humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        humanPlayer.setIsShout(true);
        
        controller.setCurrentPlayer(humanPlayer);
        
        
        controller.setCurrentPlayer(humanPlayer);
        
        String result = humanPlayer.shoutUno();
        assertEquals(result,"You shouted UNO already!");}
    
    
    @Test
    void testSHoutUNONoPlayableCard() {

        Card newTopCard = new NumberCard(Color.Blue, 3, true);
        controller.playCard(newTopCard);

        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        controller.setCurrentPlayer(humanPlayer);

        String result = humanPlayer.shoutUno();
        assertEquals("You have no playable card!", result,
            "Should return error when no playable cards");
    }

    @Test
    public void testPlayCard() {
        humanPlayer.getHand().clear();
        Card card = new NumberCard(Color.Red, 5, true);
        humanPlayer.getHand().add(card);

        humanPlayer.playCard(card);
        assertFalse(humanPlayer.getHand().contains(card), "Card should be removed from hand after playing");

        Card secondCard = new NumberCard(Color.Blue, 7, true);
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(secondCard);
        humanPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        humanPlayer.playCard(secondCard);

        assertFalse(humanPlayer.getIsShout(), "Should not automatically set isShout to true");

        humanPlayer.getHand().clear();
        Card notInHandCard = new NumberCard(Color.Yellow, 3, true);
        humanPlayer.playCard(notInHandCard);
        assertTrue(humanPlayer.getHand().isEmpty(), "Hand should remain unchanged when playing card not in hand");
    }

    @Test
    public void testCatchForgotShout() {
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.setController();
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.setIsShout(false);

        humanPlayer.catchForgotShout(targetPlayer);
        assertEquals(3, targetPlayer.getHand().size());

        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Blue, 7, true));
        targetPlayer.setIsShout(true);
        humanPlayer.catchForgotShout(targetPlayer);
        assertEquals(1, targetPlayer.getHand().size());

        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.getHand().add(new NumberCard(Color.Green, 3, true));
        targetPlayer.setIsShout(false);
        humanPlayer.catchForgotShout(targetPlayer);
        assertEquals(2, targetPlayer.getHand().size());

        assertThrows(NullPointerException.class,
            () -> humanPlayer.catchForgotShout(null),
            "Should throw NullPointerException when target player is null");
    }

    @Test
    public void testChallengeDrawFour() {
        Player targetPlayer = cpuPlayer;
        targetPlayer.getHand().clear();

        Card topCard = controller.getTopCard(1);
        Card matchingCard = new NumberCard(topCard.getColor(), 5, true);
        targetPlayer.getHand().add(matchingCard);

        int initialTargetHandSize = targetPlayer.getHand().size();

        humanPlayer.challengeDrawFour(targetPlayer);

        assertEquals(initialTargetHandSize + 4, targetPlayer.getHand().size(),
            "Target should get 4 penalty cards when challenge succeeds");

        targetPlayer.getHand().clear();
        humanPlayer.getHand().clear();

        targetPlayer.getHand().add(new WildCard(true));
        int initialHandSize = humanPlayer.getHand().size();

        humanPlayer.challengeDrawFour(targetPlayer);

        assertEquals(initialHandSize + 6, humanPlayer.getHand().size(),
            "Human should get 6 penalty cards when challenge fails");

        assertThrows(NullPointerException.class,
            () -> humanPlayer.challengeDrawFour(null),
            "Should throw NullPointerException when target player is null");
    }

    @Test
    public void testShoutUnoEdgeCases() {
        controller.setCurrentPlayer(humanPlayer);

        humanPlayer.getHand().clear();
        String result = humanPlayer.shoutUno();
        assertEquals("You have no playable card!", result,
            "Should return error when hand is empty");

        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        humanPlayer.getHand().add(new NumberCard(Color.Red, 6, true));

        Card blueCard = new NumberCard(Color.Blue, 3, true);
        controller.playCard(blueCard);

        result = humanPlayer.shoutUno();
        assertEquals("You have more than 1 card!", result,
            "Should return error when more than 1 card in hand");
    }
}
