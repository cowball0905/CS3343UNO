package model;

import controller.UNOController;

public class SkipCard extends Card {
    public SkipCard(Color color, boolean isRevealed) {
        super(Type.Skip, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Skip.png");
    }

    @Override
    public int getValue() {
        return 20;
    }

    @Override
    public void cardFunction(UNOController controller) {
        System.out.println("Skip card effect");
        controller.passNextPlayer(2);
        controller.eachRound();
    }

    @Override
    public boolean checkCard(Card playedCard){
        if (playedCard.type == Type.Wild || playedCard.type == Type.WildDrawFour) {
            return true;
        }
        if (playedCard.color == this.color || playedCard.type == this.type) {
            return true;
        }
        return false;
    }
    
	public String toString() {
		return getColor().toString() + " Skip";
	}
} 
