package model;

public class SkipCard extends Card {
    public SkipCard(Color color, boolean isRevealed) {
        super(Type.Skip, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Skip.png");
    }

    @Override
    public int cardFunction() {
        System.out.println("Skip card effect");
        return 2;
    }
} 
