package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controller.UNOController;

public class CPUPlayer extends Player {

    private Random random;

    public CPUPlayer(String name) {
        super(name);
        this.random = new Random();
    }

    @Override
    public void drawCard(Card card) {
        this.hand.add(card);
    }

    @Override
    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            System.out.println(name + " (CPU) plays " + card.getClass().getSimpleName());

            if (hand.size() == 1) {
                shoutUno();
            }
        }
    }

    @Override
    public void shoutUno() {
        if (hand.size() == 1) {
            isShout = true;
            System.out.println(name + " (CPU) shouts UNO!");
        }
    }

    @Override
    public void catchForgotShout(Player targetPlayer) {
        // CPU always catches human player forgetting UNO with high probability (90%)
        if (targetPlayer.getHand().size() == 1 && !targetPlayer.getIsShout() &&
                random.nextDouble() < 0.9) {
            System.out.println(name + " (CPU) catches " + targetPlayer.getName() + " for forgetting to shout UNO!");
        }
    }

    @Override
    public void challengeDrawFour(Player targetPlayer) {
        // CPU challenges with optimal probability (50%)
        if (random.nextDouble() < 0.5) {
            System.out.println(name + " (CPU) challenges " + targetPlayer.getName() + "'s Wild Draw Four card!");
        }
    }

    public void chooseCard() {
        UNOController controller = UNOController.getInstance();
        ArrayList<Card> playerCards = this.getHand();
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card c : playerCards) {
            if (controller.canPlayCard(c)) {
                validCards.add(c);
            }
        }

        if (validCards.isEmpty()) {
            System.out.println(name + " (CPU) has no valid cards to play and must draw.");
            this.drawCard(controller.getCardFactory().giveCard(controller.getDeck(),false, false));
            controller.passNextPlayer(false);
            controller.eachRound();
            return;
        }

        Card chosenCard = randomChoose(validCards); //Use random function to choose a card

        playCard(chosenCard);
        System.out.println(name + " (CPU) chose their Card!");
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