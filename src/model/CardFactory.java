package model;

import java.util.ArrayList;

public abstract class CardFactory {
    abstract Card createCard(ArrayList<String> Deck,Boolean isTop, boolean isRevealed, String cardString);

    public Card giveCard(ArrayList<String> Deck,Boolean isTop, boolean isRevealed, String cardString){ 
        return createCard(Deck, isTop, isRevealed, cardString);
    };
}
