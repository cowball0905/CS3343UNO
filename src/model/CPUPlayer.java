package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        List<Card> playableCards = new ArrayList<>();
        
        // Find all playable cards
        for (Card card : hand) {
            if (isCardPlayable(card, topCard)) {
                playableCards.add(card);
            }
        }
        
        if (playableCards.isEmpty()) {
            return null;
        }
        
        // Simple strategy: prefer special cards
        for (Card card : playableCards) {
            if (card.getType() != Type.Number) {
                return card;
            }
        }
        
        // Otherwise return the first playable card
        return playableCards.get(0);
    }

    private boolean isCardPlayable(Card card, Card topCard) {
        if (card == null) return false;
        if (topCard == null) return true;
        
        // Wild cards can always be played
        if (card.getType() == Type.Wild || card.getType() == Type.WildDrawFour) {
            return true;
        }
        
        // Match color or type/number
        return card.getColor() == topCard.getColor() || 
               card.getType() == topCard.getType() || 
               (card instanceof NumberCard && topCard instanceof NumberCard && 
                ((NumberCard)card).getValue() == ((NumberCard)topCard).getValue());
    }

    private Card chooseBestStrategy(List<Card> validCards, Card topCard) {
        // Optimal strategy: prioritize cards that benefit CPU most

        // 1. If hand is large (>3 cards), save Wild cards for later unless no other
        // option
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
    }

        // 3. Prefer action cards that hinder opponent (Skip, Reverse, Draw Two)
    @Override
    public void allowPlay() {
        UNOController controller = UNOController.getInstance();
        ArrayList<Card> playerCards = this.getHand();
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card c : playerCards) {
            if (controller.playCard(c)) {
                validCards.add(c);
            }
        }

        if (validCards.isEmpty()) {
            System.out.println(name + " (CPU) has no valid cards to play and must draw.");
            this.drawCard(controller.getCardFactory().giveCard(controller.getDeck(),false, false));
            return;
        }

        Card chosenCard = chooseCard(validCards); //Use random function to choose a card

        playCard(chosenCard);
        System.out.println(" (CPU) chose their Card!");
        chosenCard.setRevealed(true);
        Thread.sleep(10);
        UNOController.getInstance().playCard(chosenCard);

        // // If the played card is a Wild card, choose a color
        // if (chosenCard instanceof WildCard || chosenCard instanceof WildDrawFourCard) {
        //     Color chosenColor = chooseWildCardColor();
        //     System.out.println(name + " (CPU) chooses color: " + chosenColor);
        //     UNOController.getInstance().setCurrentColor(chosenColor);
        // }
    }

    private Card chooseCard(List<Card> validCards) {
        Random random = new Random();
        int randomIndex = random.nextInt(validCards.size());
        return validCards.get(randomIndex);
    }
}