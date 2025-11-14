package model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CardFactory {
    abstract Card createCard(HashMap<String, Integer> Deck,Boolean isTop, boolean isRevealed, String cardString);

    public Card giveCard(HashMap<String, Integer> Deck,Boolean isTop, boolean isRevealed, String cardString){ 
        return createCard(Deck, isTop, isRevealed, cardString);
    };
}
