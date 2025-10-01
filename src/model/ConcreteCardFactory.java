package model;

import java.util.ArrayList;
import java.util.Random;

public class ConcreteCardFactory extends CardFactory {

    private static final Random random = new Random();

    @Override
    public Card createCard(ArrayList<String> Deck, Boolean isTop) {
        int Decklen = Deck.size();
        if(isTop){
            Decklen = 76;
        }
        int index = random.nextInt(Decklen);
        String card = Deck.get(index);
        Deck.remove(index);
        if (card.equals("WildCard")){
            return new WildCard();
        } else if (card.equals("WildDrawFour")){
            return new WildDrawFourCard();
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
                    return new SkipCard(cardcolor);
                case "Reverse":
                    return new ReverseCard(cardcolor);
                case "DrawTwo":
                    return new DrawTwoCard(cardcolor);
                default:
                    return new NumberCard(cardcolor, Integer.parseInt(card.substring(1)));
            }
        }
    }
}

