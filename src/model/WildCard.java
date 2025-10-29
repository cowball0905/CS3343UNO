package model;

import controller.UNOController;

public class WildCard extends Card {
    public WildCard(boolean isRevealed) {
        super(Type.Wild, null, isRevealed);
        loadImage("/asset/uno-card-images-master/Wild_Card_Change_Colour.png");
    }

    @Override
    public int getValue() {
        return 50;
    }

    @Override
    public void cardFunction(UNOController controller) {
        System.out.println("Wild card played: change color");

        if(controller.checkCurrentPlayer()==0){
            controller.getWildCardViewer().setWildCard(this);
            controller.getTurnTimer().startTimer(10);
        }else{
            Color chooseColor = ((CPUPlayer) controller.getCurrentPlayer()).chooseColor();
            this.setColor(chooseColor);
            this.loadImage("/asset/uno-card-images-master/Wild_Card_Change_Colour_"+chooseColor.toString()+".jpg");
            System.out.println(chooseColor.toString());
            controller.passNextPlayer(1);
            controller.eachRound();
        }
    }

	public String toString() {
		return "Wild Card";
	}
}
