package model;

public class SkipCard extends Card {
    public SkipCard(Color color) {
        super(Type.Skip, color);
    }

    @Override
    public void cardFunction() {
        System.out.println("Skip card effect");
    }
}
