package model;

import java.util.ArrayList;

import controller.UNOController;

public class HumanPlayer extends Player {
    
    public HumanPlayer(String name) {
        super(name);
    }
    
    @Override
    public void drawCard(Card card) {
        this.hand.add(card);
    }
    
    @Override
    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            System.out.println(name + " plays " + card.getClass().getSimpleName());

            if (hand.size() == 1 && !isShout) {
                System.out.println(name + " should shout UNO!");
            }
        }
    }
    
    @Override
    public void shoutUno() {
        if (hand.size() == 1) {
            isShout = true;
            System.out.println(name + " shouts UNO!");
        } else {
            //print error message
            System.out.println(name + " CANNOT SHOUT UNO!");
        }
    }
    
    @Override
    public void catchForgotShout(Player targetPlayer) {
        if (targetPlayer.getHand().size() == 1 && !targetPlayer.getIsShout()) {
            System.out.println(name + " catches " + targetPlayer.getName() + " for forgetting to shout UNO!");
            // Target player should draw 2 penalty cards
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
                    targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false));
                }
                controller.passNextPlayer(1);
                controller.eachRound();
                return;
            }
        }

        System.out.println("Challenge Fail!");
        for(int i=0;i<6;i++){
            drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false));
        }
        controller.passNextPlayer(2);
        controller.eachRound();
    }
}