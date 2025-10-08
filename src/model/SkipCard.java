package model;

import controller.UNOController;

public class SkipCard extends Card {
    public SkipCard(Color color, boolean isRevealed) {
        super(Type.Skip, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Skip.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Skip card effect");
        UNOController controller = UNOController.getInstance();
        controller.passNextPlayer(2);
        controller.eachRound();
    }
} 
