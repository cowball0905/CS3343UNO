package model;

import java.util.List;

public class HumanPlayer extends Player {
    
    public HumanPlayer(String name) {
        super(name);
    }
    
    @Override
    public void drawCard() {
        System.out.println(name + " draws a card from the deck");
    }
    
    @Override
    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            System.out.println(name + " plays " + card.getClass().getSimpleName());
            
            // Check if player should shout UNO (when only 1 card left)
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
    
    // Method to check if the human player can play a card
    public boolean canPlayCard(Card card, Card topCard) {
        // Basic UNO rules: same color, same number/type, or wild card
        if (card instanceof WildCard || card instanceof WildDrawFourCard) {
            return true;
        }
        
        return card.getColor().equals(topCard.getColor()) || 
               card.getClass().equals(topCard.getClass());
    }
    
    // Get valid cards that can be played
    public List<Card> getValidCards(Card topCard) {
        return hand.stream()
                  .filter(card -> canPlayCard(card, topCard))
                  .toList();
    }
}