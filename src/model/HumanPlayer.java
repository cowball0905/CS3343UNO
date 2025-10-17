package model;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.UNOController;

public class HumanPlayer extends Player {

    private Random random;
    private JPanel panel;

    public HumanPlayer(String name) {
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
            System.out.println(name + " plays " + card.getClass().getSimpleName());

            if (hand.size() == 1 && !isShout) {
                int delay = random.nextInt(2001); // Random delay around 2 sec
                Timer catchTimer = new Timer(delay, e -> {
                    controller.getPlayerList().get(random.nextInt(3) + 1).catchForgotShout(this);
                });
                catchTimer.setRepeats(false);
                catchTimer.start();
                System.out.println(name + " should shout UNO!");
            }
        }
    }
    
    @Override
    public String shoutUno() {
        
        String errorMessage =  null;
        if (this != controller.getCurrentPlayer() && this.getHand().size() > 1) {
            errorMessage = "You have more than 1 card!";
        } else if (this != controller.getCurrentPlayer() && this.getHand().size() == 1) {
            isShout = true;
            System.out.println(name + " shouts UNO!");
        } else if (this.getIsShout()){
            errorMessage = "You shouted UNO already!";
        } else if (this.getHand().size() > 2) {
            errorMessage = "You have more than 2 card!";
        } else {
            boolean hasPlayableCard = false;
            for (Card c : this.getHand()){
                if(controller.canPlayCard(c)){
                    hasPlayableCard = true;
                }
            }
            if (!hasPlayableCard) {
                errorMessage = "You have no playable card!";
                return errorMessage;
            }
            isShout = true;
            System.out.println(name + " shouts UNO!");
        }
        return errorMessage;
    }
    
    @Override
    public void catchForgotShout(Player targetPlayer) {
        if (targetPlayer.getHand().size() == 1 && !targetPlayer.getIsShout()) {
            System.out.println(name + " catches " + targetPlayer.getName() + " for forgetting to shout UNO!");
            // Target player should draw 2 penalty cards
            for(int i = 0; i < 2; i++){
                targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
            }
            controller.getGamePanel().updateDisplay();
        }
    }
    
    @Override
    public void challengeDrawFour(Player targetPlayer) {
        System.out.println(name + " challenges " + targetPlayer.getName() + "'s Wild Draw Four card!");
        UNOController controller = UNOController.getInstance();
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
                    targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
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
        controller.passNextPlayer(2);
        controller.eachRound();
    }
}