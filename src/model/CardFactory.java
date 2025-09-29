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
        int randomValue = random.nextInt(typeCount);

        switch (randomValue) {
            case 1:
                return createNumberCard();
            case 2:
                return createSkipCard();
            case 3:
                return createReverseCard();
            case 4:
                return createDrawTwoCard();
            case 5:
                return createWildCard();
            case 6:
                return createWildDrawFourCard();
            default:
                throw new IllegalArgumentException("Unknown card type");
        }
    }
}

