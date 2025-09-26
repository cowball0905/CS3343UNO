package model;

import java.util.Random;

public class CardFactory {

    private static final Random random = new Random();

    public static Card createNumberCard() {
        return new NumberCard();
    }

    public static Card createSkipCard() {
        return new SkipCard();
    }

    public static Card createReverseCard() {
        return new ReverseCard();
    }

    public static Card createDrawTwoCard() {
        return new DrawTwoCard();
    }

    public static Card createWildCard() {
        return new WildCard();
    }

    public static Card createWildDrawFourCard() {
        return new WildDrawFourCard();
    }
    
    public static Card createDeckCard() {
        return new DeckCard();
    }

    public static Card createRandomCard() {
        Type[] types = Type.values();
        int typeCount = types.length - 1; // 排除Deck类型
        Type randomType = types[random.nextInt(typeCount)];

        switch (randomType) {
            case Number:
                int randomValue = random.nextInt(10);
                return createNumberCard();
            case Skip:
                return createSkipCard();
            case Reverse:
                return createReverseCard();
            case DrawTwo:
                return createDrawTwoCard();
            case Wild:
                return createWildCard();
            case WildDrawFour:
                return createWildDrawFourCard();
            default:
                throw new IllegalArgumentException("Unknown card type: " + randomType);
        }
    }
}

