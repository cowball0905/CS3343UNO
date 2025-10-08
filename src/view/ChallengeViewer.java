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
        redButton = new JButton("Get 4 Cards");
        redButton.setBackground(Color.RED);
        redButton.setOpaque(true);
        redButton.setBounds(350, 250, 120, 60);
        redButton.addActionListener(e -> {
            timer.stopTimer();
            this.isChallenging = false;
            removeButtons();
            Player currentPlayer = controller.getCurrentPlayer();
            ArrayList<Player> playerList = controller.getPlayerList();
            int playDirection = controller.getPlayDirection();
            int currentIndex = playerList.indexOf(currentPlayer);
            int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();
            Player nextPlayer = playerList.get(nextIndex);
            for(int i=0;i<4;i++){
                nextPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false));
            }
        });
        panel.add(redButton);
        
        greenButton = new JButton("Challenge");
        greenButton.setBackground(Color.GREEN);
        greenButton.setOpaque(true);
        greenButton.setBounds(490, 250, 120, 60);
        greenButton.addActionListener(e -> {
            Player currentPlayer = controller.getCurrentPlayer();
            ArrayList<Player> playerList = controller.getPlayerList();
            int playDirection = controller.getPlayDirection();
            int currentIndex = playerList.indexOf(currentPlayer);
            int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();
            Player nextPlayer = playerList.get(nextIndex);
            timer.stopTimer();
            removeButtons();
            nextPlayer.challengeDrawFour(currentPlayer);
            this.isChallenging = false;
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
}
