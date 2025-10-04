package model;

import java.util.ArrayList;

import controller.UNOController;

public class SkipCard extends Card {
    public SkipCard(Color color, boolean isRevealed) {
        super(Type.Skip, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Skip.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Skip card effect");
        UNOController controller = UNOController.getInstance();
        Player currentPlayer = controller.getCurrentPlayer();
        ArrayList<Player> playerList = controller.getPlayerList();
        int playDirection = controller.getPlayDirection(); // 1 for clockwise, -1 for counter-clockwise

        // Get current player index
        int currentIndex = playerList.indexOf(currentPlayer);

        // Calculate next player index (skip one player)
        int nextIndex = (currentIndex + (2 * playDirection)) % playerList.size();

        // Set the next player
        Player nextPlayer = playerList.get(nextIndex);
        controller.setCurrentPlayer(nextPlayer);
    }
} 
