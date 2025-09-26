package model;

public class NumberCard extends Card {
    private int value;

    public NumberCard() {
        super(Type.Number, Color.values()[new java.util.Random().nextInt(4)]);
        this.value = new java.util.Random().nextInt(10);
    }

    @Override
    public void cardFunction() {  
        System.out.println("NumberCard function called with value: " + value);
    }
}
