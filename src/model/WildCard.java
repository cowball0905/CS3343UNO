package model;

public class WildCard extends Card {
    public WildCard() {
        super(Type.Wild, null);
    }

    @Override
    public void cardFunction() {
        System.out.println("Wild card played: change color");
    }
}
