package model;

import java.util.ArrayList;

import controller.UNOController;

public class WildDrawFourCard extends Card {
    public WildDrawFourCard(boolean isRevealed) {
        super(Type.WildDrawFour, null, isRevealed);
        loadImage("/asset/uno-card-images-master/Wild_Card_Draw_4.png");
    }

    @Override
    public int getValue() {
        return 50;
    }

    @Override
    public void cardFunction(UNOController controller) {
        System.out.println("Wild card played: change color");
        Player currentPlayer = controller.getCurrentPlayer();
        int playDirection = controller.getPlayDirection(); // 1 for clockwise, -1 for counter-clockwise

        if(controller.checkCurrentPlayer()==0){
            controller.getWildCardViewer().setWildCard(this);
            controller.getTurnTimer().startTimer(10);
        }else{
            Color chooseColor = ((CPUPlayer) controller.getCurrentPlayer()).chooseColor();
            this.setColor(chooseColor);
            loadImage("/asset/uno-card-images-master/Wild_Card_Draw_4_"+chooseColor.toString()+".jpg");
            System.out.println(chooseColor.toString());

            // Get current player index
            int currentIndex = controller.checkCurrentPlayer();

            // Calculate next player index (skip one player)
            ArrayList<Player> playerList = controller.getPlayerList();
            int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();

            // Get next player object
            Player nextPlayer = playerList.get(nextIndex);

            if(nextIndex==0){
                controller.getChallengeViewer().setChallenge(true);
                controller.getTurnTimer().startTimer(10);
            }else{
                nextPlayer.challengeDrawFour(currentPlayer);
            }
        }
    }
    
	public String toString() {
		return "Wild Draw Four";
	}
}
