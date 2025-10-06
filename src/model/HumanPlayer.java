package model;

import java.util.ArrayList;
import java.util.List;

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
        // Game controller should handle the challenge logic
    }
}