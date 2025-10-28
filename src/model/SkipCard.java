package model;

import controller.UNOController;

public class SkipCard extends Card {
    public SkipCard(Color color, boolean isRevealed) {
        super(Type.Skip, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Skip.png");
    }

    @Override
    public void cardFunction(UNOController controller) {
        System.out.println("Skip card effect");
        controller.passNextPlayer(2);
        controller.eachRound();
    }
    
	public String toString() {
		return getColor().toString() + " Skip";
	}
} 
