package model;

import controller.UNOController;

public class ReverseCard extends Card {
    public ReverseCard(Color color, boolean isRevealed) {
        super(Type.Reverse, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Reverse.png");
    }

    @Override
    public int getValue() {
        return 20;
    }

    @Override
    public void cardFunction(UNOController controller) {
        System.out.println("ReverseCard function called");

        controller.setPlayDirection(controller.getPlayDirection()*-1); // Reverse the direction
        controller.passNextPlayer(1);
        controller.eachRound();
    }
    
    public String toString() {
    	return getColor().toString() + " Reverse";
    }
}
