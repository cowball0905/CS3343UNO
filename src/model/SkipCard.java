package model;

public class SkipCard extends Card {
    public SkipCard(Color color) {
        super(Type.Skip, color);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Skip.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Skip card effect");
    }
}
