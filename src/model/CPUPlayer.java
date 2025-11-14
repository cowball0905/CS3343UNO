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
            System.out.println(name + " (CPU) plays " + card.toString());
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
                 * int delay = random.nextInt(2001); // Random delay around 2 sec
                 * System.out.println(name + " (CPU) will shout UNO in " + delay +
                 * " ms if not caught.");
                 * Timer shoutTimer = new Timer(delay, e -> {
                 * if (hand.size() == 1) { // Check again to make sure it hasn't been caught
                 * shoutUno();
                 * }
                 * });
                 * shoutTimer.setRepeats(false);
                 * shoutTimer.start();
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
            for (int i = 0; i < 2; i++) {
                targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false,
                        (controller.getPlayerList().get(0) == targetPlayer), ""));
            }
            controller.getGamePanel().updateDisplay();
        }
    }

    @Override
    public void challengeDrawFour(Player targetPlayer) {
        // challenge only when opponent has 2 or fewer cards (likely trying to win)
        boolean shouldChallenge = targetPlayer.getHand().size() >= 5;

        if (shouldChallenge) {
            System.out.println(name + " (CPU) challenges " + targetPlayer.getName() + "'s Wild Draw Four card!");
            ArrayList<Card> cards = targetPlayer.getHand();
            ArrayList<Card> validCards = new ArrayList<>();

            for (Card card : cards) {
                if (controller.canPlayCard(card, controller.getTopCard())) {
                    validCards.add(card);
                }
            }

            for (Card card : validCards) {
                if (card.getType() != Type.Wild && card.getType() != Type.WildDrawFour) {
                    System.out.println("Challenge Success!");
                    for (int i = 0; i < 4; i++) {
                        targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false,
                                controller.checkCurrentPlayer() == 0 ? true : false, ""));
                    }
                    controller.passNextPlayer(1);
                    controller.eachRound();
                    return;
                }
            }

            System.out.println("Challenge Fail!");
            for (int i = 0; i < 6; i++) {
                drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
            }
            controller.passNextPlayer(2);
            controller.eachRound();
        } else {
            System.out.println(name + " (CPU) does not challenge " + targetPlayer.getName() + "'s Wild Draw Four card");
            for (int i = 0; i < 4; i++) {
                drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
            }
            controller.passNextPlayer(2);
            controller.eachRound();
            return;
        }
    }

    public void chooseCard() {
        ArrayList<Card> playerCards = this.getHand();
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card c : playerCards) {
            if (controller.canPlayCard(c, controller.getTopCard())) {
                validCards.add(c);
            }
        }

        if (validCards.isEmpty()) {
            System.out.println(name + " (CPU) has no valid cards to play and must draw.");
            this.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
            controller.passNextPlayer(1);
            controller.eachRound();
            return;
        }

        Card chosenCard = randomChoose(validCards); // Use random function to choose a card

        playCard(chosenCard);
        // System.out.println(name + " (CPU) chose their Card!");
        chosenCard.setRevealed(true);
        controller.playCard(chosenCard);
    }

    private Card randomChoose(List<Card> validCards) {
        Random random = new Random();
        int randomIndex = random.nextInt(validCards.size());
        return validCards.get(randomIndex);
    }

    public Color chooseColor() {
        ArrayList<Card> playerCards = this.getHand();
        int max = 0;
        int each = 0;
        int colorIndex = 0;

        for (int i = 0; i < Color.values().length; i++) {
            for (Card c : playerCards) {
                if (c.getColor() == Color.values()[i]) {
                    each++;
                }
            }
            if (each > max) {
                max = each;
                colorIndex = i;
            }
            each = 0;
        }
        System.out.println("CPU change to " + Color.values()[colorIndex]);
        return Color.values()[colorIndex];
    }
}