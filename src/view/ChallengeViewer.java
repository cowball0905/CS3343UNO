package view;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.UNOController;
import model.CountDownTimer;
import model.Player;
import model.Type;
import model.Card;
import model.CardFactory;

public class ChallengeViewer {
    private boolean isChallenging;
    private CountDownTimer timer;
    private UNOController controller;
    private JPanel panel;
    private JButton redButton;
    private JButton greenButton;

    public ChallengeViewer(){
        isChallenging = false;
    }

    public void setController(UNOController controller) {
        this.controller = controller;
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }
    
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void setChallenge(){
        this.isChallenging = true;
    }

    public void drawWindow(Graphics g){
      if (this.isChallenging) {
        drawButtons(g);
      }
    }

    public void drawButtons(Graphics g){
        if(redButton!=null){return;}
        redButton = new JButton();
        redButton.setBackground(Color.RED);
        redButton.setOpaque(true);
        redButton.setBounds(350, 250, 120, 60);
        redButton.addActionListener(e -> {
            System.out.println("Draw 4 cards");
            timer.stopTimer();
            this.isChallenging = false;
            removeButtons();
            Player currentPlayer = controller.getCurrentPlayer();
            ArrayList<Player> playerList = controller.getPlayerList();
            int playDirection = controller.getPlayDirection();
            int currentIndex = playerList.indexOf(currentPlayer);
            int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();
            Player nextPlayer = playerList.get(nextIndex);
            this.drawCards(4, nextPlayer);
        });
        panel.add(redButton);
        
        greenButton = new JButton();
        greenButton.setBackground(Color.GREEN);
        greenButton.setOpaque(true);
        greenButton.setBounds(490, 330, 120, 60);
        greenButton.addActionListener(e -> {
            Player currentPlayer = controller.getCurrentPlayer();
            ArrayList<Player> playerList = controller.getPlayerList();
            int playDirection = controller.getPlayDirection();
            int currentIndex = playerList.indexOf(currentPlayer);
            int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();
            Player nextPlayer = playerList.get(nextIndex);
            timer.stopTimer();
            if(this.checkCards()){
                this.drawCards(6, nextPlayer);
            }else{
                this.drawCards(4, currentPlayer);
            }
            this.isChallenging = false;
            removeButtons();
        });
        panel.add(greenButton);
        
        if (timer != null) {
            int remaining = timer.getRemainingSeconds();
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Time: " + remaining + "s", 420, 415);
        }
    }
    
    public boolean getIsChallenging() {
        return this.isChallenging;
    }
    
    public void removeButtons() {
        panel.remove(redButton);
        panel.remove(greenButton);
        redButton = null;
        greenButton = null;
        panel.repaint();
    }

    public void drawCards(int amount, Player player){
        UNOController controller = UNOController.getInstance();
        CardFactory cardFactory = controller.getCardFactory();
        ArrayList<Player> playerList = controller.getPlayerList();

        for (int i = 0; i < amount; i++) {
            player.drawCard(cardFactory.giveCard(controller.getDeck(),false, playerList.indexOf(player)==0 ? true:false));
        }
        System.out.println(player.getName()+ " got " + amount + " cards");

        controller.passNextPlayer(2);
        controller.eachRound();
    }

    public boolean checkCards(){
        Player currentPlayer = controller.getCurrentPlayer();
        ArrayList<Card> cards = currentPlayer.getHand();
        ArrayList<Card> validCards = new ArrayList<>();

        for(Card card:cards){
            if(controller.canPlayCard(card)){
                validCards.add(card);
            }
        }

        for(Card card:validCards){
            if(card.getType()!=Type.Wild && card.getType()!=Type.WildDrawFour){
                return true;
            }
        }
        return false;
    }
}
