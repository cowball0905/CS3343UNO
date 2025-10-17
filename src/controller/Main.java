package controller;

public class Main {
    // main function that starts the UNO game
    public static void main(String[] args) {
        UNOController uno = UNOController.getInstance();
        uno.startGame();
    }
}