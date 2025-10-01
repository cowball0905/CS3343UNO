package model;

public class DrawTwoCard extends Card {
    public DrawTwoCard(Color color) {
        super(Type.DrawTwo, color);
    }

    @Override
    public void cardFunction() {
        System.out.println("Draw Two card played: next player draws 2 cards");
        // 具体让下家抽2张卡逻辑
    }
}
