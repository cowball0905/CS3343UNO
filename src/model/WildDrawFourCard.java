package model;

import java.util.ArrayList;

import controller.UNOController;

public class WildDrawFourCard extends Card {
    public WildDrawFourCard(boolean isRevealed) {
        super(Type.WildDrawFour, null, isRevealed);
        loadImage("/asset/uno-card-images-master/Wild_Card_Draw_4.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Wild card played: change color");
        UNOController controller = UNOController.getInstance();
        Player currentPlayer = controller.getCurrentPlayer();
        ArrayList<Player> playerList = controller.getPlayerList();
        int playDirection = controller.getPlayDirection(); // 1 for clockwise, -1 for counter-clockwise
        CardFactory cardFactory = controller.getCardFactory();

        if(controller.getPlayerList().indexOf(controller.getCurrentPlayer())==0){
            controller.getWildCardViewer().setWildCard(this);
            controller.getTurnTimer().startTimer(10);
        }else{
            Color chooseColor = ((CPUPlayer) controller.getCurrentPlayer()).chooseColor();
            this.setColor(chooseColor);
            System.out.println(chooseColor.toString());

            // Get current player index
            int currentIndex = playerList.indexOf(currentPlayer);

            // Calculate next player index (skip one player)
            int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();

            // Get next player object
            Player nextPlayer = playerList.get(nextIndex);

            // Make the next player draw 2 cards
            for (int i = 0; i < 4; i++) {
                nextPlayer.drawCard(cardFactory.giveCard(controller.getDeck(),false, playerList.indexOf(nextPlayer)==0 ? true:false));
            }
            controller.passNextPlayer(2);
            controller.eachRound();
        }
    }
}
