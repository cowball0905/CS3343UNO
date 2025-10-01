import java.util.ArrayList;

public class Mai {
    public static void main(String[] args) {
        ArrayList<String> deck = new ArrayList<>();
        deck.add("r5");
        deck.add("bSkip");
        deck.add("yDrawTwo");
        deck.add("WildCard");
        deck.add("rReverse");
        deck.add("WildDrawFour");
        deck.add("g2");

        ConcreteCardFactory factory = new ConcreteCardFactory();

        System.out.println("Original deck: " + deck);

        Card card = factory.createCard(deck);

        System.out.println("Created card: " + card);

        System.out.println("Deck after removal: " + deck);
    }
}
