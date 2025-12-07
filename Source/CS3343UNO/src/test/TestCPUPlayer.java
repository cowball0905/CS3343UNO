
package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;
import java.util.ArrayList;

public class TestCPUPlayer {
    private CPUPlayer cpuPlayer;
    private UNOController controller;
    private ArrayList<Player> players;

    @BeforeEach
    public void setUp() {
        controller.resetInstance();
        controller = UNOController.getInstance();
        controller.startGame();
        players = controller.getPlayerList();
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);

        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);

        cpuPlayer = (CPUPlayer) players.get(1);
        controller.setIsFreezed(true);
    }
    
    @AfterEach
    public void tearDown() {
        controller.resetInstance();
    }
    

    @Test
    public void testDrawCard() {
        controller.setCurrentPlayer(cpuPlayer);
        cpuPlayer.getHand().clear();
        int initialHandSize = cpuPlayer.getHand().size();
        Card card = new NumberCard(Color.Red, 5, true);

        cpuPlayer.drawCard(card);

        assertEquals(initialHandSize + 1, cpuPlayer.getHand().size());
    }

    @Test
    public void testPlayCard() {
        controller.setCurrentPlayer(cpuPlayer);
        controller.setIsFreezed(false);
        cpuPlayer.getHand().clear();
        Card card = new NumberCard(Color.Red, 5, true);
        cpuPlayer.drawCard(card);

        controller.playCard(new NumberCard(Color.Red, 3, true));
        cpuPlayer.playCard(card);

        assertFalse(cpuPlayer.getHand().contains(card));
    }

    @Test
    public void testShoutUnoTrue() {
    	controller.setIsFreezed(true);
        controller.setCurrentPlayer(cpuPlayer);
        cpuPlayer.getHand().clear();
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.shoutUno();
        assertTrue(cpuPlayer.getIsShout());
    }

    @Test
    public void testShoutUnoFalse() {
        controller.setCurrentPlayer(cpuPlayer);
        cpuPlayer.getHand().clear();
        cpuPlayer.drawCard(new NumberCard(Color.Blue, 3, true));
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.setIsShout(false);
        cpuPlayer.shoutUno();
        assertFalse(cpuPlayer.getIsShout());
    }

    @Test
    public void testCatchForgotShout() {
        Player targetPlayer = players.get(2);
        targetPlayer.getHand().clear();
        targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        int initialHandSize = targetPlayer.getHand().size();

        targetPlayer.setIsShout(false);
        cpuPlayer.catchForgotShout(targetPlayer);
        assertEquals(initialHandSize + 2, targetPlayer.getHand().size());
    }


	@Test
	public void testChallengeDrawFour_ChallengeFails() {
	    controller.setIsFreezed(true);
	    Player targetPlayer = players.get(0);
	    targetPlayer.getHand().clear();
	    cpuPlayer.getHand().clear();
	
	    for(int i=0;i<5;i++) {
		    targetPlayer.drawCard(new NumberCard(Color.Blue, 5, true));
	    }
	
	    controller.playCard(new NumberCard(Color.Red, 2, true));
	
	    controller.playCard(new WildDrawFourCard(true));
	
	    controller.setCurrentPlayer(cpuPlayer);
	
	    int initialCpuHandSize = cpuPlayer.getHand().size();
	    cpuPlayer.challengeDrawFour(targetPlayer);
	
	    assertEquals(initialCpuHandSize + 6, cpuPlayer.getHand().size(),
	                 "CPU should draw exactly 6 cards when challenge fails");
	}
	
	@Test
	public void testChallengeDrawFour_ChallengeSucceeds() {
		controller.setIsFreezed(false);
	    Player targetPlayer = players.get(0);
	    targetPlayer.getHand().clear();
	    cpuPlayer.getHand().clear();
	
		for (int i = 0; i < 5; i++) {
			targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
		}
	    
	    controller.playCard(new NumberCard(Color.Red, 2, true));
	    controller.setIsFreezed(true);
	
	    controller.playCard(new WildDrawFourCard(true));
	
	    controller.setCurrentPlayer(cpuPlayer);
	
	    int initialTargetHandSize = targetPlayer.getHand().size();
	    cpuPlayer.challengeDrawFour(targetPlayer);
	
	    assertEquals(initialTargetHandSize + 4, targetPlayer.getHand().size(),
	                 "Target should draw exactly 4 cards when challenge succeeds");
	}

	@Test
	public void testChallengeDrawFour_notChallenge() {
		controller.setIsFreezed(false);
	    Player targetPlayer = players.get(0);
	    targetPlayer.getHand().clear();
	    cpuPlayer.getHand().clear();
	
		for (int i = 0; i < 1; i++) {
			targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
		}
	    
	    controller.playCard(new NumberCard(Color.Red, 2, true));
	    controller.setIsFreezed(true);
	
	    controller.playCard(new WildDrawFourCard(true));
	
	    controller.setCurrentPlayer(cpuPlayer);
	
	    int initialTargetHandSize = cpuPlayer.getHand().size();
	    cpuPlayer.challengeDrawFour(targetPlayer);
	
	    assertEquals(initialTargetHandSize + 4, cpuPlayer.getHand().size());
	}


    @Test                                                                                            
    public void testChooseCard_OnlyOnePlayableCard() {
        controller.setIsFreezed(true);
        cpuPlayer.getHand().clear();
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.drawCard(new NumberCard(Color.Green, 3, true));

        controller.playCard(new NumberCard(Color.Red, 2, true));
        controller.setCurrentPlayer(cpuPlayer);

        int initialHandSize = cpuPlayer.getHand().size();
        cpuPlayer.chooseCard();

        assertEquals(initialHandSize - 1, cpuPlayer.getHand().size());
        
        assertTrue(
            (cpuPlayer.getHand().get(0).getColor() != Color.Red && cpuPlayer.getHand().get(0).getType() == Type.Number && ((NumberCard) cpuPlayer.getHand().get(0)).getValue() != 5),
            "Top card should be Red 5 (the only playable card)"
        );
    }

    @Test
    public void testChooseColor_DominantColor() {
    	controller.setIsFreezed(true);
        cpuPlayer.getHand().clear();
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.drawCard(new NumberCard(Color.Red, 3, true));
        cpuPlayer.drawCard(new NumberCard(Color.Red, 2, true));
        cpuPlayer.drawCard(new NumberCard(Color.Blue, 1, true));

        Color chosenColor = cpuPlayer.chooseColor();

        assertEquals(Color.Red, chosenColor, "Should choose the dominant color Red");
    }

    @Test
    public void testCPUDrawFromEmptyDeck() {
        controller.getDeck().clear();
        int handSize = cpuPlayer.getHand().size();
        
        Card card = controller.getCardFactory().giveCard(controller.getDeck(), false, false, "");
        cpuPlayer.drawCard(card);
        
        assertEquals(handSize, cpuPlayer.getHand().size());
    }
}
