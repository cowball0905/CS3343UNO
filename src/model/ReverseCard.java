package model;

import controller.UNOController;

public class ReverseCard extends Card {
    public ReverseCard(Color color, boolean isRevealed) {
        super(Type.Reverse, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Reverse.png");
    }

    @Override
    public int cardFunction() {
        System.out.println("ReverseCard function called");

        UNOController controller = UNOController.getInstance();

        int playDirection = controller.getPlayDirection();
        controller.setPlayDirection(-playDirection); // Reverse the direction
        return 1;
    }
}
