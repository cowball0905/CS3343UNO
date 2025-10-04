package model;

public class WildDrawFourCard extends Card {
    public WildDrawFourCard(boolean isRevealed) {
        super(Type.WildDrawFour, null, isRevealed);
        loadImage("/asset/uno-card-images-master/Wild_Card_Draw_4.png");
    }

    @Override
    public int cardFunction() {
        System.out.println("Wild Draw Four card played: opponent draws 4 cards and change color");
        // 具体抽4张卡和变色逻辑实现
        return 1;
    }
}
