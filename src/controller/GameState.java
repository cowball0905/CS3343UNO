// controller/GameState.java
package controller;

import model.*;
import java.util.*;

public class GameState {
    private List<Card> drawPile;
    private Stack<Card> discardPile;
    private Color currentColor;
    private boolean isPlayerTurn = true;

    public GameState() {
        initializeGame();
    }

    private void initializeGame() {
        drawPile = new ArrayList<>();
        discardPile = new Stack<>();
        ConcreteCardFactory factory = new ConcreteCardFactory();
        // Create a full deck
        for (int i = 0; i < 108; i++) {  // Adjust the number based on your deck size
            drawPile.add(factory.createCard());
        }
        Collections.shuffle(drawPile);
        
        // Make sure first card is a valid starting card
        Card firstCard;
        do {
            firstCard = drawCard();
        } while (firstCard.getType() == Type.Wild || firstCard.getType() == Type.WildDrawFour);
        
        discardPile.push(firstCard);
        currentColor = firstCard.getColor();
    }
    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardPile();
        }
        return drawPile.isEmpty() ? null : drawPile.remove(0);
    }

    private void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) return;
        
        Card topCard = discardPile.pop();
        Collections.shuffle(discardPile);
        drawPile.addAll(discardPile);
        discardPile.clear();
        discardPile.push(topCard);
    }

    public boolean isValidMove(Card card) {
        if (card == null || discardPile.isEmpty()) return false;
        
        Card topCard = getTopCard();
        return card.getColor() == currentColor || 
               card.getType() == topCard.getType() ||
               card.getType() == Type.Wild || 
               card.getType() == Type.WildDrawFour;
    }

    public Card getTopCard() {
        return discardPile.isEmpty() ? null : discardPile.peek();
    }

    public void playCard(Card card) {
        if (isValidMove(card)) {
            discardPile.push(card);
            currentColor = card.getColor();
            card.cardFunction();
            isPlayerTurn = !isPlayerTurn;
        }
    }

    // Getters
    public boolean isPlayerTurn() { return isPlayerTurn; }
    public Color getCurrentColor() { return currentColor; }
    public void setCurrentColor(Color color) { this.currentColor = color; }
    public List<Card> getDrawPile() { return drawPile; }
    public Stack<Card> getDiscardPile() { return discardPile; }
}