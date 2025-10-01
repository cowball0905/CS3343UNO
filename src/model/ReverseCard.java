package model;

public class ReverseCard extends Card {
    public ReverseCard(Color color) {
        super(Type.Number, color);
    }

    @Override
    public void cardFunction() {  
        System.out.println("ReverseCard function called");
    }
}
