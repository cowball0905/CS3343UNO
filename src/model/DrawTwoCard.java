package model;

import java.util.ArrayList;

import controller.UNOController;

public class DrawTwoCard extends Card {
    public DrawTwoCard(Color color, boolean isRevealed) {
        super(Type.DrawTwo, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Draw_2.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Draw Two card played: next player draws 2 cards");

        UNOController controller = UNOController.getInstance();
        Player currentPlayer = controller.getCurrentPlayer();
        ArrayList<Player> playerList = controller.getPlayerList();
        int playDirection = controller.getPlayDirection(); // 1 for clockwise, -1 for counter-clockwise

        CardFactory cardFactory = controller.getCardFactory();

        // Get current player index
        int currentIndex = controller.checkCurrentPlayer();

        // Calculate next player index (skip one player)
        int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();

        // Get next player object
        Player nextPlayer = playerList.get(nextIndex);

        // Make the next player draw 2 cards
        for (int i = 0; i < 2; i++) {
            nextPlayer.drawCard(cardFactory.giveCard(controller.getDeck(),false, controller.checkCurrentPlayer()==0 ? true:false, ""));
        }

        controller.passNextPlayer(2);
        controller.eachRound();
    }
}