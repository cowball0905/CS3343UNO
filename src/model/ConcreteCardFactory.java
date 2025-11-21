package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.lang.Object;
import controller.UNOController;

public class ConcreteCardFactory extends CardFactory {

    private static final Random random = new Random();

    @Override
    public Card createCard(HashMap<String, Integer> Deck, Boolean isTop, boolean isRevealed, String cardString) {
        String card = null;
        
        if (isTop) {
            ArrayList<String> numberCards = getNumberCards(Deck);
            if (numberCards.isEmpty()) {
                return null;
            }
            card = getRandomCard(Deck, numberCards);
        } else if (!cardString.isEmpty() && Deck.containsKey(cardString) && Deck.get(cardString) > 0) { // create specific card
            card = cardString;
            removeCardFromDeck(Deck, card);
        } else {
            if (!cardString.isEmpty()) {
                System.out.println("Deck doesn't contain this card!!!");
            }
            ArrayList<String> availableCards = new ArrayList<>(Deck.keySet());
            if (availableCards.isEmpty()) {
                return null;
            }
            card = getRandomCard(Deck, availableCards);
        }

        if (card == null) {
            return null;
        }
        
        return createSpecificCard(card, isRevealed);
    }

    private ArrayList<String> getNumberCards(HashMap<String, Integer> Deck) {
        ArrayList<String> numberCards = new ArrayList<>();
            for (String key : Deck.keySet()) {
                if (!key.equals("WildCard") && !key.equals("WildDrawFour") && 
                    !key.contains("Skip") && !key.contains("Reverse") && !key.contains("DrawTwo")) {
                    numberCards.add(key);
                }
            }
        return numberCards;
    }

    private void removeCardFromDeck(HashMap<String, Integer> Deck, String card) {
        Deck.put(card, Deck.get(card) - 1);
        if (Deck.get(card) == 0) {
            Deck.remove(card);
        }
    }

    private String getRandomCard(HashMap<String, Integer> Deck, ArrayList<String> Cards) {
        int index = random.nextInt(Cards.size());
        String card = Cards.get(index);
        removeCardFromDeck(Deck, card);
        return card;
    }

    private Card createSpecificCard(String card, boolean isRevealed) {
        if (card.equals("WildCard")){
            return new WildCard(isRevealed);
        } 
        if (card.equals("WildDrawFour")){
            return new WildDrawFourCard(isRevealed);
        }

        Color cardcolor = getColor(card);
        switch (card.substring(1)) {
            case "Skip":
                return new SkipCard(cardcolor, isRevealed);
            case "Reverse":
                return new ReverseCard(cardcolor, isRevealed);
            case "DrawTwo":
                return new DrawTwoCard(cardcolor, isRevealed);
            default:
                return new NumberCard(cardcolor, Integer.parseInt(card.substring(1)), isRevealed);
        }
    }

    private Color getColor(String card) {
        switch (card.charAt(0)){
            case 'r':
                return Color.Red;   
            case 'b':
                return Color.Blue;
            case 'y':
                return Color.Yellow;
            default:
                return Color.Green;
        }
    }
}