package model;

import controller.UNOController;

public class WildCard extends Card {
    public WildCard(boolean isRevealed) {
        super(Type.Wild, null, isRevealed);
        loadImage("/asset/uno-card-images-master/Wild_Card_Change_Colour.png");
    }

    @Override
    public void cardFunction(UNOController controller) {
        System.out.println("Wild card played: change color");

        if(controller.getPlayerList().indexOf(controller.getCurrentPlayer())==0){
            controller.getWildCardViewer().setWildCard(this);
            controller.getTurnTimer().startTimer(10);
        }else{
            Color chooseColor = ((CPUPlayer) controller.getCurrentPlayer()).chooseColor();
            this.setColor(chooseColor);
            System.out.println(chooseColor.toString());
            controller.passNextPlayer(1);
            controller.eachRound();
        }
    }


}
