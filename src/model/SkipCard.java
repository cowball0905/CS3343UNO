package model;

public class SkipCard extends Card {
    public SkipCard() {
        super(Type.Skip, Color.values()[new java.util.Random().nextInt(4)]);
    }

    @Override
    public void cardFunction() {
        System.out.println("Skip card effect");
    }
}
