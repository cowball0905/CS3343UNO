
package test;

import static org.junit.jupiter.api.Assertions.*;
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
        controller = UNOController.getInstance();
        controller.startGame();
        players = controller.getPlayerList();
        controller.setCurrentPlayer(players.get(0));
        controller.setPlayDirection(1);

        Card startingCard = new NumberCard(Color.Red, 5, true);
        controller.playCard(startingCard);

        cpuPlayer = (CPUPlayer) players.get(1);
    }

    @Test
    public void testDrawCard() {
        int initialHandSize = cpuPlayer.getHand().size();
        Card card = new NumberCard(Color.Red, 5, true);

        cpuPlayer.drawCard(card);

        assertEquals(initialHandSize + 1, cpuPlayer.getHand().size());
        assertTrue(cpuPlayer.getHand().contains(card));
        assertFalse(cpuPlayer.getIsShout());
    }

    @Test
    public void testPlayCard() {
        Card card = new NumberCard(Color.Red, 5, true);
        cpuPlayer.drawCard(card);
        controller.playCard(new NumberCard(Color.Red, 3, true));

        cpuPlayer.playCard(card);
        assertFalse(cpuPlayer.getHand().contains(card));

        cpuPlayer.getHand().clear();
        Card card1 = new NumberCard(Color.Red, 5, true);
        Card card2 = new NumberCard(Color.Blue, 3, true);
        cpuPlayer.drawCard(card1);
        cpuPlayer.drawCard(card2);

        controller.playCard(new NumberCard(Color.Red, 3, true));
        cpuPlayer.playCard(card1);

        assertEquals(1, cpuPlayer.getHand().size());
    }

    @Test
    public void testShoutUno() {
        cpuPlayer.getHand().clear();
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.shoutUno();
        assertTrue(cpuPlayer.getIsShout());

        cpuPlayer.drawCard(new NumberCard(Color.Blue, 3, true));
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

        cpuPlayer.catchForgotShout(targetPlayer);
        assertEquals(initialHandSize + 2, targetPlayer.getHand().size());
    }


@Test
public void testChallengeDrawFour_ChallengeSucceeds() {
    // 挑战成功:上家有红色牌,却打了Draw4(作弊)
    Player targetPlayer = players.get(0);
    targetPlayer.getHand().clear();
    cpuPlayer.getHand().clear();

    // 上家有红色牌(证明他作弊了)
    targetPlayer.drawCard(new NumberCard(Color.Red, 5, true));

    // 设置当前颜色为红色
    controller.playCard(new NumberCard(Color.Red, 2, true));

    int initialTargetHandSize = targetPlayer.getHand().size();
    cpuPlayer.challengeDrawFour(targetPlayer);

    // 挑战成功:上家抽4张牌作为惩罚
    assertEquals(initialTargetHandSize + 4, targetPlayer.getHand().size(),
                 "Target should draw exactly 4 cards when challenge succeeds");
}

@Test
public void testChallengeDrawFour_ChallengeFails() {
    // 挑战失败:上家没有红色牌,合法打Draw4
    Player targetPlayer = players.get(0);
    targetPlayer.getHand().clear();
    cpuPlayer.getHand().clear();

    // 上家只有蓝色牌(证明他没有作弊)
    targetPlayer.drawCard(new NumberCard(Color.Blue, 5, true));

    // 设置当前颜色为红色
    controller.playCard(new NumberCard(Color.Red, 2, true));

    int initialCpuHandSize = cpuPlayer.getHand().size();
    cpuPlayer.challengeDrawFour(targetPlayer);

    // 挑战失败:CPU抽6张牌(4张原本+2张惩罚)
    assertEquals(initialCpuHandSize + 6, cpuPlayer.getHand().size(),
                 "CPU should draw exactly 6 cards when challenge fails");
}


    @Test
    public void testChooseCard_OnlyOnePlayableCard() {
        cpuPlayer.getHand().clear();
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.drawCard(new NumberCard(Color.Green, 3, true));

        controller.playCard(new NumberCard(Color.Red, 3, true));

        int initialHandSize = cpuPlayer.getHand().size();
        cpuPlayer.chooseCard();

        assertEquals(initialHandSize - 1, cpuPlayer.getHand().size());

        boolean greenThreeInHand = cpuPlayer.getHand().stream()
            .anyMatch(card -> card.getColor() == Color.Green &&
                             card.getType() == Type.Number &&
                             ((NumberCard) card).getValue() == 3);

        assertTrue(greenThreeInHand, "Green 3 should remain in hand");
        assertEquals(1, cpuPlayer.getHand().size(), "Only one card should remain");
    }

    @Test
    public void testChooseColor_DominantColor() {
        cpuPlayer.getHand().clear();
        cpuPlayer.drawCard(new NumberCard(Color.Red, 5, true));
        cpuPlayer.drawCard(new NumberCard(Color.Red, 3, true));
        cpuPlayer.drawCard(new NumberCard(Color.Red, 2, true));
        cpuPlayer.drawCard(new NumberCard(Color.Blue, 1, true));

        Color chosenColor = cpuPlayer.chooseColor();

        assertEquals(Color.Red, chosenColor, "Should choose the dominant color Red");
    }
}
