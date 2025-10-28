
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
        cpuPlayer.getHand().clear();

        // ✅ 清理所有玩家
        for (Player player : players) {
            player.getHand().clear();
            player.setIsShout(false);
        }

        // ✅ 重置 controller 状态
        controller.setIsFreezed(false);
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);

        ArrayList<Player> list = controller.getPlayerList();  // 假设有这个方法
        list.forEach(p -> p.getHand().clear());
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
	
	    // 上家只有1张蓝色牌(满足挑战条件 <= 2张,且没有红色牌)
	    targetPlayer.drawCard(new NumberCard(Color.Blue, 5, true));
	
	    // 设置倒数第二张牌为红色(用于挑战判断)
	    controller.playCard(new NumberCard(Color.Red, 2, true));
	
	    // 上家打出 Wild Draw Four(这是被挑战的牌)
	    controller.playCard(new WildDrawFourCard(true));
	
	    controller.setCurrentPlayer(cpuPlayer);
	
	    int initialCpuHandSize = cpuPlayer.getHand().size();
	    cpuPlayer.challengeDrawFour(targetPlayer);
	
	    // 挑战失败:CPU抽6张牌
	    assertEquals(initialCpuHandSize + 6, cpuPlayer.getHand().size(),
	                 "CPU should draw exactly 6 cards when challenge fails");
	}
	
	@Test
	public void testChallengeDrawFour_ChallengeSucceeds() {
		controller.setIsFreezed(false);
	    Player targetPlayer = players.get(0);
	    targetPlayer.getHand().clear();
	    cpuPlayer.getHand().clear();
	
	    // 上家有1张红色牌(满足挑战条件 <= 2张,且有匹配颜色)
	    targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));
	    
	    // 设置倒数第二张牌为红色
	    controller.playCard(new NumberCard(Color.Red, 2, true));
	    controller.setIsFreezed(true);
	
	    // 上家打出 Wild Draw Four(这是被挑战的牌)
	    controller.playCard(new WildDrawFourCard(true));
	
	    controller.setCurrentPlayer(cpuPlayer);
	
	    int initialTargetHandSize = targetPlayer.getHand().size();
	    cpuPlayer.challengeDrawFour(targetPlayer);
	
	    // 挑战成功:上家抽4张牌
	    assertEquals(initialTargetHandSize + 4, targetPlayer.getHand().size(),
	                 "Target should draw exactly 4 cards when challenge succeeds");
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

        assertEquals(initialHandSize - 1, cpuPlayer.getHand().size(),
                     "Hand size should decrease by 1 after playing a card");
        
        System.out.println("------------------");
        System.out.println(cpuPlayer.getHand().get(0));
        System.out.println("------------------");
        
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
}
