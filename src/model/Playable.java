package model;

public interface Playable {
    void playCard(Card card);
    boolean canPlayCard(Card card, Card topCard);
}