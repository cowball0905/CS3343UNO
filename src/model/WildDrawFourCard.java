package model;

public class WildDrawFourCard extends Card {
    public WildDrawFourCard() {
        super(Type.WildDrawFour, null);
        loadImage("/asset/uno-card-images-master/Wild_Card_Draw_4.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Wild Draw Four card played: opponent draws 4 cards and change color");
        // 具体抽4张卡和变色逻辑实现
    }
}
