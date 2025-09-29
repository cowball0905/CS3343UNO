package model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CPUPlayer extends Player {
    
    private Random random;
    
    public CPUPlayer(String name) {
        super(name);
        this.random = new Random();
    }
    
    @Override
    public void drawCard() {
        System.out.println(name + " (CPU) draws a card from the deck");
    }
    
    @Override
    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            System.out.println(name + " (CPU) plays " + card.getClass().getSimpleName());
            
            // CPU automatically shouts UNO when appropriate
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
    
    // AI logic to choose which card to play using optimal strategy
    public Card chooseCardToPlay(Card topCard) {
        List<Card> validCards = getValidCards(topCard);
        
        if (validCards.isEmpty()) {
            return null; // Must draw a card
        }
        
        return chooseBestStrategy(validCards, topCard);
    }
    
    private Card chooseBestStrategy(List<Card> validCards, Card topCard) {
        // Optimal strategy: prioritize cards that benefit CPU most
        
        // 1. If hand is large (>3 cards), save Wild cards for later unless no other option
        if (hand.size() > 3) {
            List<Card> nonWildCards = validCards.stream()
                .filter(card -> !(card instanceof WildCard) && !(card instanceof WildDrawFourCard))
                .collect(Collectors.toList());
            
            if (!nonWildCards.isEmpty()) {
                validCards = nonWildCards;
            }
        }
        
        // 2. Prioritize Draw Four cards when hand is small (aggressive finish)
        if (hand.size() <= 2) {
            List<Card> drawFourCards = validCards.stream()
                .filter(card -> card instanceof WildDrawFourCard)
                .collect(Collectors.toList());
            
            if (!drawFourCards.isEmpty()) {
                return drawFourCards.get(0);
            }
        }
        
        // 3. Prefer action cards that hinder opponent (Skip, Reverse, Draw Two)
        List<Card> hinderCards = validCards.stream()
            .filter(card -> card instanceof SkipCard || card instanceof ReverseCard || 
                          card instanceof DrawTwoCard)
            .collect(Collectors.toList());
            
        if (!hinderCards.isEmpty()) {
            return hinderCards.get(random.nextInt(hinderCards.size()));
        }
        
        // 4. Play number cards that match color (preserve color control)
        List<Card> sameColorCards = validCards.stream()
            .filter(card -> card instanceof NumberCard && 
                          card.getColor().equals(topCard.getColor()))
            .collect(Collectors.toList());
            
        if (!sameColorCards.isEmpty()) {
            return sameColorCards.get(random.nextInt(sameColorCards.size()));
        }
        
        // 5. Finally, play any remaining valid card
        return validCards.get(random.nextInt(validCards.size()));
    }
    
    // Get valid cards that can be played
    public List<Card> getValidCards(Card topCard) {
        return hand.stream()
                  .filter(card -> canPlayCard(card, topCard))
                  .collect(Collectors.toList());
    }
    
    // Check if a card can be played
    public boolean canPlayCard(Card card, Card topCard) {
        // Wild cards can always be played
        if (card instanceof WildCard || card instanceof WildDrawFourCard) {
            return true;
        }
        
        // Same color or same type/number
        return card.getColor().equals(topCard.getColor()) || 
               card.getClass().equals(topCard.getClass());
    }
    
    // Choose color for wild cards
    public Color chooseWildCardColor() {
        // Count cards by color and choose the most frequent one
        Color[] colors = {Color.Red, Color.Green, Color.Blue, Color.Yellow};
        int[] colorCounts = new int[4];
        
        for (Card card : hand) {
            String cardColor = card.getColor();
            for (int i = 0; i < colors.length; i++) {
                if (colors[i].toString().equals(cardColor)) {
                    colorCounts[i]++;
                    break;
                }
            }
        }
        
        // Find color with most cards
        int maxCount = 0;
        Color bestColor = Color.Red;
        for (int i = 0; i < colorCounts.length; i++) {
            if (colorCounts[i] > maxCount) {
                maxCount = colorCounts[i];
                bestColor = colors[i];
            }
        }
        
        return bestColor;
    }
    

}