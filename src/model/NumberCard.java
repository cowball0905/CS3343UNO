package model;

import controller.UNOController;

public class NumberCard extends Card {
    private final int value;
    
    public NumberCard(Color color, int value, boolean isRevealed) {
        super(Type.Number, color, isRevealed);
        this.value = value; // 0-9
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_"+value+".png");
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public void cardFunction(UNOController controller) {
        controller.passNextPlayer(1);
        controller.eachRound();
    }
}