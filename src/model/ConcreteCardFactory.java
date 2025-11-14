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
            ArrayList<String> numberCards = new ArrayList<>();
            for (String key : Deck.keySet()) {
                if (!key.equals("WildCard") && !key.equals("WildDrawFour") && 
                    !key.contains("Skip") && !key.contains("Reverse") && !key.contains("DrawTwo")) {
                    numberCards.add(key);
                }
            }
            
            if (numberCards.isEmpty()) {
                return null;
            }
            
            int index = random.nextInt(numberCards.size());
            card = numberCards.get(index);
            
            Deck.put(card, Deck.get(card) - 1);
            if (Deck.get(card) == 0) {
                Deck.remove(card);
            }
        } else if (!cardString.isEmpty() && Deck.containsKey(cardString) && Deck.get(cardString) > 0) { // create specific card
            card = cardString;
            Deck.put(card, Deck.get(card) - 1);
            if (Deck.get(card) == 0) {
                Deck.remove(card);
            }
        } else {
            if (!cardString.isEmpty()) {
                System.out.println("Deck doesn't contain this card!!!");
            }
            ArrayList<String> availableCards = new ArrayList<>(Deck.keySet());
            if (availableCards.isEmpty()) {
                return null;
            }
            int index = random.nextInt(availableCards.size());
            card = availableCards.get(index);
            
            Deck.put(card, Deck.get(card) - 1);
            if (Deck.get(card) == 0) {
                Deck.remove(card);
            }
        }

        if (card == null) {
            return null;
        }
        
        if (card.equals("WildCard")){
            return new WildCard(isRevealed);
        } else if (card.equals("WildDrawFour")){
            return new WildDrawFourCard(isRevealed);
        } else {
            Color cardcolor;
            switch (card.charAt(0)){
                case 'r':
                    cardcolor = Color.Red;   
                    break;
                case 'b':
                    cardcolor = Color.Blue;
                    break;
                case 'y':
                    cardcolor = Color.Yellow;
                    break;
                default:
                    cardcolor = Color.Green;
                    break;
            }
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
    }
}

