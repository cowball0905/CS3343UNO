package model;

public class WildCard extends Card {
    public WildCard() {
        super(Type.Wild, null);
        loadImage("/asset/uno-card-images-master/Wild_Card_Change_Colour.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Wild card played: change color");
    }
}
