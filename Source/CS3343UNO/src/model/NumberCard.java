package model;

import controller.UNOController;

public class NumberCard extends Card {
    private final int value;
    
    public NumberCard(Color color, int value, boolean isRevealed) {
        super(Type.Number, color, isRevealed);
        this.value = value; // 0-9
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_"+value+".png");
    }
    
    @Override
    public int getValue() {
        return value;
    }
    
    public String toString() {
    	return getColor().toString() + " " + value;
    }
    
    @Override
    public void cardFunction(UNOController controller) {
        controller.passNextPlayer(1);
        controller.eachRound();
    }

    @Override
    public boolean checkCard(Card playedCard){
        if (playedCard.type == Type.Wild || playedCard.type == Type.WildDrawFour) {
            return true;
        }
        if (playedCard.color == this.color) {
            return true;
        }
        if (playedCard.type == Type.Number && ((NumberCard) playedCard).value == this.value) {
            return true;
        }
        return false;
    }
}