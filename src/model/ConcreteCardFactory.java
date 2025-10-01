package model;

import java.util.Random;

public class ConcreteCardFactory extends CardFactory {

    private static final Random random = new Random();

    @Override
    public Card createCard(Card[] cards) {
        Type[] types = Type.values();
        int typeCount = types.length-1; // 排除Deck类型
        int randomValue = random.nextInt(typeCount);

        switch (randomValue) {
            case 1:
                return new DrawTwoCard();
            case 2:
                return new ReverseCard();
            case 3: 
                return new SkipCard();
            case 4:
                return new WildCard();
            case 5:
                return new WildDrawFourCard();
            default:
                return new NumberCard();
        }
    }
}

