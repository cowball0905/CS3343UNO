package model;

public abstract class CardFactory {
    abstract Card createCard(Card[] cards);

    public void someCommonMethod() {
        // Common method implementation
    }
}
