package model;

public class ReverseCard extends Card {
    public ReverseCard() {
        super(Type.Number, Color.values()[new java.util.Random().nextInt(4)]);
    }

    @Override
    public void cardFunction() {  
        System.out.println("ReverseCard function called");
    }
}
