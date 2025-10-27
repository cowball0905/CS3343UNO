package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.UNOController;

public class CPUPlayer extends Player {

    private Random random;
    private JPanel panel;

    public CPUPlayer(String name) {
        super(name);
        this.random = new Random();
    }

    @Override
    public void drawCard(Card card) {
        this.hand.add(card);
        isShout = false;
    }

    @Override
    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            System.out.println(name + " (CPU) plays " + card.getClass().getSimpleName());
            if (hand.size() == 1) {
                double probability = 0.6; // 60% chance to shout UNO
                if (Math.random() <= probability) {
                    shoutUno();
                } else {
                    for (Player p : controller.getPlayerList()) {
                    if (p != this && p instanceof CPUPlayer) {
                        double catchProbability = 0.4; // 40% chance to catch
                        if (Math.random() <= catchProbability) {
                            int delay = random.nextInt(2001);
                            new Timer(delay, e -> {
                                if (this.hand.size() == 1 && !this.getIsShout()) {
                                    p.catchForgotShout(this);
                                }
                            }).start();
                        }
                    }
                }
                }
                /*
                int delay = random.nextInt(2001); // Random delay around 2 sec
                System.out.println(name + " (CPU) will shout UNO in " + delay + " ms if not caught.");
                Timer shoutTimer = new Timer(delay, e -> {
                    if (hand.size() == 1) { // Check again to make sure it hasn't been caught
                        shoutUno();
                    }
                });
                shoutTimer.setRepeats(false);
                shoutTimer.start();
                */
            }
        }
    }

    @Override
    public String shoutUno() {
        if (!isShout && this.getHand().size() == 1) {
            isShout = true;
            System.out.println(name + " (CPU) shouts UNO!");
        } else {
            System.out.println(name + " (CPU) failed to shout UNO!");
        }
        return null;
    }

    @Override
    public void catchForgotShout(Player targetPlayer) {
        if (targetPlayer.getHand().size() == 1 && !targetPlayer.getIsShout()) {
            System.out.println(name + " catches " + targetPlayer.getName() + " for forgetting to shout UNO!");
            // Target player should draw 2 penalty cards
            for(int i = 0; i < 2; i++){
                targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, (controller.getPlayerList().get(0) == targetPlayer), ""));
            }
            controller.getGamePanel().updateDisplay();
        }
    }

    @Override
    public void challengeDrawFour(Player targetPlayer) {
        // 智能挑战策略：根据游戏状态决定是否挑战
        boolean shouldChallenge = shouldChallengeDrawFour(targetPlayer);
        
        if (shouldChallenge) {
            System.out.println(name + " (CPU) challenges " + targetPlayer.getName() + "'s Wild Draw Four card!");
            ArrayList<Card> cards = targetPlayer.getHand();
            ArrayList<Card> validCards = new ArrayList<>();

            for(Card card:cards){
                if(controller.canPlayCard(card)){
                    validCards.add(card);
                }
            }

            for(Card card:validCards){
                if(card.getType()!=Type.Wild && card.getType()!=Type.WildDrawFour){
                    System.out.println("Challenge Success!");
                    for(int i=0;i<4;i++){
                        targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, controller.checkCurrentPlayer() == 0 ? true:false, ""));
                    }
                    controller.passNextPlayer(1);
                    controller.eachRound();
                    return;
                }
            }

            System.out.println("Challenge Fail!");
            for(int i=0;i<6;i++){
                drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
            }
            controller.passNextPlayer(1);
            controller.eachRound();
        }else{
            System.out.println(name + " (CPU) does not challenge " + targetPlayer.getName() + "'s Wild Draw Four card");
            for(int i=0;i<4;i++){
                drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
            }  
            controller.passNextPlayer(2);
            controller.eachRound();
            return;
        }
    }
    
    /**
     * 决定是否应该挑战 Wild Draw Four 卡
     * 基于以下因素：
     * 1. 对手手牌数量（手牌少的玩家更可能作弊来快速获胜）
     * 2. 顶牌颜色（某些颜色被打出的频率）
     * 3. 自己的手牌状态（如果自己快赢了，更激进地挑战）
     * 4. 风险评估（挑战失败的代价 vs 收益）
     */
    private boolean shouldChallengeDrawFour(Player targetPlayer) {
        Card topCard = controller.getTopCard();
        Color topColor = topCard.getColor();
        int opponentHandSize = targetPlayer.getHand().size();
        int myHandSize = this.getHand().size();
        
        // 策略 1: 对手手牌很少（2张以下）时，更可能在作弊
        // 因为他们想快速获胜，可能冒险使用非法的 Wild Draw Four
        if (opponentHandSize <= 2) {
            System.out.println(name + " thinks " + targetPlayer.getName() + " might be cheating (low cards)");
            return true; // 挑战！
        }
        
        // 策略 2: 如果自己快赢了（3张以下），不要冒险挑战
        // 挑战失败会抽 6 张牌，可能失去获胜机会
        if (myHandSize <= 3) {
            System.out.println(name + " won't risk challenging (close to winning)");
            return false; // 不挑战，保守策略
        }
        
        // 策略 3: 检查自己手中是否有很多相同颜色的牌
        // 如果顶牌颜色很常见，对手更可能有该颜色的牌，作弊概率低
        int sameColorCount = 0;
        for (Card card : this.getHand()) {
            if (card.getColor() == topColor) {
                sameColorCount++;
            }
        }
        
        // 如果我手上有很多该颜色的牌（3张以上），说明这个颜色很常见
        // 对手应该也有，打 Wild Draw Four 可能是作弊
        if (sameColorCount >= 3) {
            System.out.println(name + " thinks " + topColor + " is common, might challenge");
            return true; // 挑战！
        }
        
        // 策略 4: 如果我手上完全没有该颜色的牌
        // 说明这个颜色可能稀有，对手可能真的没有
        if (sameColorCount == 0) {
            System.out.println(name + " has no " + topColor + " cards, won't challenge");
            return false; // 不挑战
        }
        
        // 策略 5: 对手手牌中等（3-5张）且我们有一些该颜色的牌
        // 中等风险，采用保守策略：不挑战
        if (opponentHandSize >= 3 && opponentHandSize <= 5) {
            System.out.println(name + " uses conservative strategy, won't challenge");
            return false;
        }
        
        // 策略 6: 对手手牌很多（6张以上）
        // 他们不太可能冒险作弊，因为还没到关键时刻
        if (opponentHandSize >= 6) {
            System.out.println(name + " thinks opponent is playing safe, won't challenge");
            return false;
        }
        
        // 默认：中等情况下不挑战（保守策略）
        return false;
    }

    public void chooseCard() {
        ArrayList<Card> playerCards = this.getHand();
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card c : playerCards) {
            if (controller.canPlayCard(c)) {
                validCards.add(c);
            }
        }

        if (validCards.isEmpty()) {
            System.out.println(name + " (CPU) has no valid cards to play and must draw.");
            this.drawCard(controller.getCardFactory().giveCard(controller.getDeck(),false, false, ""));
            controller.passNextPlayer(1);
            controller.eachRound();
            return;
        }

        Card chosenCard = randomChoose(validCards); //Use random function to choose a card

        playCard(chosenCard);
        //System.out.println(name + " (CPU) chose their Card!");
        chosenCard.setRevealed(true);
        controller.playCard(chosenCard);
    }

    private Card randomChoose(List<Card> validCards) {
        Random random = new Random();
        int randomIndex = random.nextInt(validCards.size());
        return validCards.get(randomIndex);
    }

    public Color chooseColor(){
        ArrayList<Card> playerCards = this.getHand();
        int max = 0;
        int each = 0;
        int colorIndex = 0;

        for(int i=0; i< Color.values().length; i++){
            for (Card c : playerCards) {
                if (c.getColor()==Color.values()[i]) {
                    each++;
                }
            }
            if(each > max){
                max = each;
                colorIndex = i;
            }
            each = 0;
        }
        System.out.println("CPU change to "+Color.values()[colorIndex]);
        return Color.values()[colorIndex];
    }
}