import model.*;

public class TestCardSize {
    public static void main(String[] args) {
        // 創建一個測試卡牌
        Card testCard = new NumberCard(model.Color.Red, 1);
        
        System.out.println("=== 測試開始 ===");
        System.out.println("初始大小: " + testCard.getWidth() + "x" + testCard.getHeight());
        
        // 測試 setSize
        System.out.println("\n=== 測試 setSize(150, 200) ===");
        testCard.setSize(150, 200);
        System.out.println("設定後大小: " + testCard.getWidth() + "x" + testCard.getHeight());
        
        // 測試 setRotation
        System.out.println("\n=== 測試 setRotation(90) ===");
        testCard.setRotation(90);
        System.out.println("旋轉後大小: " + testCard.getWidth() + "x" + testCard.getHeight());
        
        System.out.println("=== 測試結束 ===");
    }
}