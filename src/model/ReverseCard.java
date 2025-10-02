package model;

public class ReverseCard extends Card {
    public ReverseCard(Color color, boolean isRevealed) {
        super(Type.Reverse, color, isRevealed);
        loadImage("/asset/uno-card-images-master/"+color.toString()+"_Reverse.png");
    }

    @Override
    public void cardFunction() {  
        System.out.println("ReverseCard function called");
    }
}
