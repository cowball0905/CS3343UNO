package model;

import java.util.ArrayList;
import java.util.Random;

public class ConcreteCardFactory extends CardFactory {

    private static final Random random = new Random();

    @Override
    public Card createCard(ArrayList<String> Deck, Boolean isTop, boolean isRevealed, String cardString) {
        int Decklen = Deck.size();
        if(isTop){
            Decklen = 56;
        }
        String card;
        if (cardString != "" && Deck.indexOf(cardString) != -1) { // create specific card
            card = cardString;
            Deck.remove(Deck.indexOf(card));
        } else {
            if(cardString != ""){
                System.out.println("Deck doesn't contain this card!!!");
            }
            int index = random.nextInt(Decklen);
            card = Deck.get(index);
            Deck.remove(index);
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

