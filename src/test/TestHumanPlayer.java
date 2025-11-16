
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
        controller.setIsFreezed(true);

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
    public void testDrawCardIncreasesHandSize() {
        int initialSize = humanPlayer.getHand().size();
        Card card = new NumberCard(Color.Red, 5, true);

        humanPlayer.drawCard(card);

        assertEquals(initialSize + 1, humanPlayer.getHand().size());
    }

    @Test
    public void testDrawCardAddsToHand() {
        Card card = new NumberCard(Color.Red, 5, true);

        humanPlayer.drawCard(card);

        assertEquals(true, humanPlayer.getHand().contains(card));
    }

    @Test
    public void testDrawNullCard() {
        int sizeBeforeNull = humanPlayer.getHand().size();

        humanPlayer.drawCard(null);

        assertEquals(sizeBeforeNull + 1, humanPlayer.getHand().size());
    }

    @Test
    public void testDrawNullCardAddsNull() {
        humanPlayer.drawCard(null);

        assertEquals(null, humanPlayer.getHand().get(humanPlayer.getHand().size() - 1));
    }

    @Test
    public void testDrawCardResetsShout() {
        humanPlayer.setIsShout(true);

        humanPlayer.drawCard(new NumberCard(Color.Blue, 2, true));

        assertEquals(false, humanPlayer.getIsShout());
    }

    @Test
    public void testShoutUnoNull() {
        Card newTopCard = new NumberCard(Color.Red, 3, true);
        controller.playCard(newTopCard);

        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        controller.setCurrentPlayer(humanPlayer);

        String result = humanPlayer.shoutUno();

        assertEquals(null, result);
    }

    @Test
    public void testShoutUnoSetsFlag() {
        Card newTopCard = new NumberCard(Color.Red, 3, true);
        controller.playCard(newTopCard);

        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        controller.setCurrentPlayer(humanPlayer);

        humanPlayer.shoutUno();

        assertEquals(true, humanPlayer.getIsShout());
    }

    @Test
    public void testShoutUNOMoreThanOneCard() {
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Green, 2, true));
        humanPlayer.getHand().add(new NumberCard(Color.Yellow, 4, true));
        humanPlayer.getHand().add(new NumberCard(Color.Yellow, 4, true));
        controller.setCurrentPlayer(humanPlayer);

        String result = humanPlayer.shoutUno();

        assertEquals("You have more than 1 card!", result);
    }

    @Test
    public void testShoutUNOTwice() {
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        humanPlayer.setIsShout(true);
        controller.setCurrentPlayer(humanPlayer);

        String result = humanPlayer.shoutUno();

        assertEquals("You shouted UNO already!", result);
    }

    @Test
    public void testShoutUNONoPlayableCard() {
        Card newTopCard = new NumberCard(Color.Blue, 3, true);
        controller.playCard(newTopCard);

        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        controller.setCurrentPlayer(humanPlayer);

        String result = humanPlayer.shoutUno();

        assertEquals("You have no playable card!", result);
    }

    @Test
    public void testPlayCardRemovesFromHand() {
        humanPlayer.getHand().clear();
        Card card = new NumberCard(Color.Red, 5, true);
        humanPlayer.getHand().add(card);

        humanPlayer.playCard(card);

        assertEquals(false, humanPlayer.getHand().contains(card));
    }

    @Test
    public void testPlayCardDoesNotSetShout() {
        Card secondCard = new NumberCard(Color.Blue, 7, true);
        humanPlayer.getHand().clear();
        humanPlayer.getHand().add(secondCard);
        humanPlayer.getHand().add(new NumberCard(Color.Green, 2, true));

        humanPlayer.playCard(secondCard);

        assertEquals(false, humanPlayer.getIsShout());
    }

    @Test
    public void testPlayCardNotInHand() {
        humanPlayer.getHand().clear();
        Card notInHandCard = new NumberCard(Color.Yellow, 3, true);

        humanPlayer.playCard(notInHandCard);

        assertEquals(true, humanPlayer.getHand().isEmpty());
    }

    @Test
    public void testCatchForgotShoutSuccess() {
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.setController();
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.setIsShout(false);

        humanPlayer.catchForgotShout(targetPlayer);

        assertEquals(3, targetPlayer.getHand().size());
    }

    @Test
    public void testCatchForgotShoutAlreadyShouted() {
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.setController();
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Blue, 7, true));
        targetPlayer.setIsShout(true);

        humanPlayer.catchForgotShout(targetPlayer);

        assertEquals(1, targetPlayer.getHand().size());
    }

    @Test
    public void testCatchForgotShoutMultipleCards() {
        Player targetPlayer = new CPUPlayer("Target");
        targetPlayer.setController();
        targetPlayer.getHand().clear();
        targetPlayer.getHand().add(new NumberCard(Color.Red, 5, true));
        targetPlayer.getHand().add(new NumberCard(Color.Green, 3, true));
        targetPlayer.setIsShout(false);

        humanPlayer.catchForgotShout(targetPlayer);

        assertEquals(2, targetPlayer.getHand().size());
    }

    @Test
    public void testChallengeDrawFourSuccess() {
        Player targetPlayer = cpuPlayer;
        targetPlayer.getHand().clear();
        Card topCard = controller.getTopCard(1);
        Card matchingCard = new NumberCard(topCard.getColor(), 5, true);
        targetPlayer.getHand().add(matchingCard);
        int initialTargetHandSize = targetPlayer.getHand().size();

        humanPlayer.challengeDrawFour(targetPlayer);

        assertEquals(initialTargetHandSize + 4, targetPlayer.getHand().size());
    }

    @Test
    public void testChallengeDrawFourFail() {
        Player targetPlayer = cpuPlayer;
        targetPlayer.getHand().clear();
        humanPlayer.getHand().clear();
        targetPlayer.getHand().add(new WildCard(true));
        int initialHandSize = humanPlayer.getHand().size();

        humanPlayer.challengeDrawFour(targetPlayer);

        assertEquals(initialHandSize + 6, humanPlayer.getHand().size());
    }

    @Test
    public void testShoutUnoEmptyHand() {
        controller.setCurrentPlayer(humanPlayer);
        humanPlayer.getHand().clear();

        String result = humanPlayer.shoutUno();

        assertEquals("You have no playable card!", result);
    }
}
