package model;

public class WildCard extends Card {
    public WildCard(boolean isRevealed) {
        super(Type.Wild, null, isRevealed);
        loadImage("/asset/uno-card-images-master/Wild_Card_Change_Colour.png");
    }

    @Override
    public int cardFunction() {
        System.out.println("Wild card played: change color");
        return 1;
    }
}
