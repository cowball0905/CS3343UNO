package model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CPUPlayer extends Player {
    
    private Random random;
    private DifficultyLevel difficulty;
    
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
    
    public CPUPlayer(String name) {
        this(name, DifficultyLevel.MEDIUM);
    }
    
    public CPUPlayer(String name, DifficultyLevel difficulty) {
        super(name);
        this.random = new Random();
        this.difficulty = difficulty;
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
        // CPU has a chance to catch human player forgetting UNO based on difficulty
        double catchChance = switch (difficulty) {
            case EASY -> 0.3;    // 30% chance
            case MEDIUM -> 0.6;  // 60% chance
            case HARD -> 0.9;    // 90% chance
        };
        
        if (targetPlayer.getHand().size() == 1 && !targetPlayer.getIsShout() && 
            random.nextDouble() < catchChance) {
            System.out.println(name + " (CPU) catches " + targetPlayer.getName() + " for forgetting to shout UNO!");
        }
    }
    
    @Override
    public void challengeDrawFour(Player targetPlayer) {
        // CPU decides whether to challenge based on difficulty and probability
        double challengeChance = switch (difficulty) {
            case EASY -> 0.1;    // 10% chance
            case MEDIUM -> 0.3;  // 30% chance
            case HARD -> 0.5;    // 50% chance
        };
        
        if (random.nextDouble() < challengeChance) {
            System.out.println(name + " (CPU) challenges " + targetPlayer.getName() + "'s Wild Draw Four card!");
        }
    }
    
    // AI logic to choose which card to play
    public Card chooseCardToPlay(Card topCard) {
        List<Card> validCards = getValidCards(topCard);
        
        if (validCards.isEmpty()) {
            return null; // Must draw a card
        }
        
        return switch (difficulty) {
            case EASY -> chooseRandomCard(validCards);
            case MEDIUM -> chooseMediumStrategy(validCards, topCard);
            case HARD -> chooseHardStrategy(validCards, topCard);
        };
    }
    
    private Card chooseRandomCard(List<Card> validCards) {
        return validCards.get(random.nextInt(validCards.size()));
    }
    
    private Card chooseMediumStrategy(List<Card> validCards, Card topCard) {
        // Prefer action cards over number cards
        List<Card> actionCards = validCards.stream()
            .filter(card -> !(card instanceof NumberCard))
            .collect(Collectors.toList());
            
        if (!actionCards.isEmpty() && random.nextDouble() < 0.7) {
            return actionCards.get(random.nextInt(actionCards.size()));
        }
        
        return chooseRandomCard(validCards);
    }
    
    private Card chooseHardStrategy(List<Card> validCards, Card topCard) {
        // Advanced strategy: prioritize cards that benefit CPU most
        
        // 1. Save Wild cards for last resort unless hand is small
        if (hand.size() > 3) {
            List<Card> nonWildCards = validCards.stream()
                .filter(card -> !(card instanceof WildCard) && !(card instanceof WildDrawFourCard))
                .collect(Collectors.toList());
            
            if (!nonWildCards.isEmpty()) {
                validCards = nonWildCards;
            }
        }
        
        // 2. Prefer action cards that hinder opponent
        List<Card> actionCards = validCards.stream()
            .filter(card -> card instanceof SkipCard || card instanceof ReverseCard || 
                          card instanceof DrawTwoCard || card instanceof WildDrawFourCard)
            .collect(Collectors.toList());
            
        if (!actionCards.isEmpty() && random.nextDouble() < 0.8) {
            return actionCards.get(random.nextInt(actionCards.size()));
        }
        
        return chooseMediumStrategy(validCards, topCard);
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
    
    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }
}