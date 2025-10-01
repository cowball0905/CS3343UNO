package model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class CardFactory {
    abstract Card createCard(ArrayList<String> Deck,Boolean isTop);

    public Card giveCard(ArrayList<String> Deck,Boolean isTop){ 
        return createCard(Deck,isTop);
    };
}
