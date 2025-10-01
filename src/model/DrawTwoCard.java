package model;

public class DrawTwoCard extends Card {
    public DrawTwoCard(Color color) {
        super(Type.DrawTwo, color);
        this.loadImage("../asset/uno-card-images-master/" + this.getColor().toString() + "_Draw_2.png");
    }

    @Override
    public void cardFunction() {
        System.out.println("Draw Two card played: next player draws 2 cards");
        // 具体让下家抽2张卡逻辑
    }
}
