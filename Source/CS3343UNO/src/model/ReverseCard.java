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
    	return getColor().toString() + " Reverse";
    }
}
