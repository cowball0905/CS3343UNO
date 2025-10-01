package model;

public class NumberCard extends Card {
    private final int value;
    
    public NumberCard() {
        super(Type.Number, Color.values()[new java.util.Random().nextInt(4)]);
        this.value = new java.util.Random().nextInt(10); // 0-9
    }
    
    public NumberCard(Color color, int value) {
        super(Type.Number, color);
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public void cardFunction() {
        // Number cards have no special function
    }
}