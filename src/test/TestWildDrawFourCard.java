package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;
import controller.UNOController;

public class TestWildDrawFourCard {
	private UNOController controller;

	@BeforeEach
	public void setUp() {
		UNOController.resetInstance();
		controller = UNOController.getInstance();
		controller.setPlayers();
		controller.setViewers();
	}

	@AfterEach
	public void tearDown() {
		UNOController.resetInstance();
	}

	@Test
	public void testWildDrawFourCardToString() {
		WildDrawFourCard card = new WildDrawFourCard(true);

		assertEquals("Wild Draw Four", card.toString());
	}

	@Test
	public void testWildDrawFourCardNotNull() {
		WildDrawFourCard card = new WildDrawFourCard(true);

		assertNotNull(card);
	}

	@Test
	public void testWildDrawFourCardColorNull() {
		WildDrawFourCard card = new WildDrawFourCard(true);

		assertEquals(null, card.getColor());
	}

	@Test
	public void testWildDrawFourCardRevealed() {
		WildDrawFourCard card = new WildDrawFourCard(true);

		assertEquals(true, card.isRevealed());
	}

	@Test
	public void testWildDrawFourCardType() {
		WildDrawFourCard card = new WildDrawFourCard(true);

		assertEquals(Type.WildDrawFour, card.getType());
	}

	@Test
	public void testWildDrawFourCardValue() {
		WildDrawFourCard card = new WildDrawFourCard(true);

		assertEquals(50, card.getValue());
	}

	@Test
	public void testWildDrawFourCardSetColor() {
		WildDrawFourCard card = new WildDrawFourCard(true);

		card.setColor(Color.Red);

		assertEquals(Color.Red, card.getColor());
	}

	@Test
	public void testWildDrawFourCardFunctionWithHuman() {
		controller.startGame();
		controller.setIsFreezed(true);
		controller.setCurrentPlayer(controller.getPlayerList().get(0));

		WildDrawFourCard card = new WildDrawFourCard(true);
		card.cardFunction(controller);

		assertNotNull(controller.getWildCardViewer().getCard());
	}

	@Test
	public void testWildDrawFourCardFunctionSetsColor() {
		controller.startGame();
		controller.setIsFreezed(true);
		controller.setPlayDirection(-1);
		controller.setCurrentPlayer(controller.getPlayerList().get(1));

		WildDrawFourCard card = new WildDrawFourCard(true);
		card.cardFunction(controller);

		assertNotNull(card.getColor());
	}

    @Test
    public void testCheckCardNumberCard() {
        WildDrawFourCard topCard = new WildDrawFourCard(true);
        topCard.setColor(Color.Red);
        NumberCard playedCard = new NumberCard(Color.Red, 5, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardSkipCard() {
        WildDrawFourCard topCard = new WildDrawFourCard(true);
        topCard.setColor(Color.Blue);
        SkipCard playedCard = new SkipCard(Color.Blue, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardReverseCard() {
        WildDrawFourCard topCard = new WildDrawFourCard(true);
        topCard.setColor(Color.Green);
        ReverseCard playedCard = new ReverseCard(Color.Green, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardDrawTwoCard() {
        WildDrawFourCard topCard = new WildDrawFourCard(true);
        topCard.setColor(Color.Yellow);
        DrawTwoCard playedCard = new DrawTwoCard(Color.Yellow, true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardWildCard() {
        WildDrawFourCard topCard = new WildDrawFourCard(true);
        WildCard playedCard = new WildCard(true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testCheckCardWildDrawFour() {
        WildDrawFourCard topCard = new WildDrawFourCard(true);
        WildDrawFourCard playedCard = new WildDrawFourCard(true);
        
        assertEquals(true, topCard.checkCard(playedCard));
    }

    @Test
    public void testWildDrawFourWithEmptyDeck() {
        controller.getDeck().clear();
        Player nextPlayer = controller.getPlayerList().get(1);
        int initialSize = nextPlayer.getHand().size();
        
        WildDrawFourCard card = new WildDrawFourCard(true);
        controller.setCurrentPlayer(controller.getPlayerList().get(0));
        card.cardFunction(controller);
        
        // Hand size should not change since deck is empty and drawCard rejects null
        assertEquals(initialSize, nextPlayer.getHand().size());
    }
}