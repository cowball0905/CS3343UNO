package model;

public class DeckCard extends Card {
    
    public DeckCard() {
        super(Type.Deck, Color.Red); // Deck cards don't have a specific color
    }

    @Override
    public void cardFunction() {
        System.out.println("DeckCard function called - this represents the back of a card");
    }
}