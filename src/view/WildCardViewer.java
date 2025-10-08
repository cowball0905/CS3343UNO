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

public class WildCardViewer {
    private boolean isHavingWild;
    private Card wild;
    private CountDownTimer timer;
    private UNOController controller;
    private JPanel panel;
    private JButton redButton;
    private JButton blueButton;
    private JButton yellowButton;
    private JButton greenButton;

    public WildCardViewer(){
        isHavingWild = false;
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

    public void setWildCard(Card card){
        this.wild = card;
        isHavingWild = true;
    }

    public void drawWindow(Graphics g){
      if (this.isHavingWild) {
        drawButtons(g);
      }
    }

    public Card getCard(){
        return wild;
    }

    public void drawButtons(Graphics g){
        if(redButton!=null){return;}
        redButton = new JButton();
        redButton.setBackground(Color.RED);
        redButton.setOpaque(true);
        redButton.setBounds(350, 250, 120, 60);
        redButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Red);
            System.out.println("Change to Red");
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            if(this.wild.getType()==Type.WildDrawFour){
                this.drawFour();
                return;
            }
            controller.passNextPlayer(1);
            controller.eachRound();
        });
        panel.add(redButton);
        
        blueButton = new JButton();
        blueButton.setBackground(Color.BLUE);
        blueButton.setOpaque(true);
        blueButton.setBounds(490, 250, 120, 60);
        blueButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Blue);
            System.out.println("Change to Blue");
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            if(this.wild.getType()==Type.WildDrawFour){
                this.drawFour();
                return;
            }
            controller.passNextPlayer(1);
            controller.eachRound();
        });
        panel.add(blueButton);
        
        yellowButton = new JButton();
        yellowButton.setBackground(Color.YELLOW);
        yellowButton.setOpaque(true);
        yellowButton.setBounds(350, 330, 120, 60);
        yellowButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Yellow);
            System.out.println("Change to Yellow");
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            if(this.wild.getType()==Type.WildDrawFour){
                this.drawFour();
                return;
            }
            controller.passNextPlayer(1);
            controller.eachRound();
        });
        panel.add(yellowButton);
        
        greenButton = new JButton();
        greenButton.setBackground(Color.GREEN);
        greenButton.setOpaque(true);
        greenButton.setBounds(490, 330, 120, 60);
        greenButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Green);
            System.out.println("Change to Green");
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            if(this.wild.getType()==Type.WildDrawFour){
                this.drawFour();
                return;
            }
            controller.passNextPlayer(1);
            controller.eachRound();
        });
        panel.add(greenButton);
        
        if (timer != null) {
            int remaining = timer.getRemainingSeconds();
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Time: " + remaining + "s", 420, 415);
        }
    }
    

    public void setHavingWild(boolean hasWild) {
        this.isHavingWild = hasWild;
    }
    
    public boolean isHavingWild() {
        return isHavingWild;
    }
    
    public void autoSelectColor() {
        wild.setColor(model.Color.Red);
    }
    
    public void removeButtons() {
        panel.remove(redButton);
        panel.remove(blueButton);
        panel.remove(yellowButton);
        panel.remove(greenButton);
        redButton = null;
        blueButton = null;
        yellowButton = null;
        greenButton = null;
        panel.repaint();
    }

    public void drawFour(){
        UNOController controller = UNOController.getInstance();
        Player currentPlayer = controller.getCurrentPlayer();
        ArrayList<Player> playerList = controller.getPlayerList();
        int playDirection = controller.getPlayDirection(); // 1 for clockwise, -1 for counter-clockwise
        CardFactory cardFactory = controller.getCardFactory();

        // Get current player index
        int currentIndex = playerList.indexOf(currentPlayer);

        // Calculate next player index (skip one player)
        int nextIndex = (currentIndex + (1 * playDirection) + playerList.size()) % playerList.size();

        // Get next player object
        Player nextPlayer = playerList.get(nextIndex);

        // Make the next player draw 2 cards
        for (int i = 0; i < 4; i++) {
            nextPlayer.drawCard(cardFactory.giveCard(controller.getDeck(),false, playerList.indexOf(nextPlayer)==0 ? true:false));
        }
        System.out.println(nextPlayer.getName()+ " got 4 cards");

        controller.passNextPlayer(2);
        controller.eachRound();
    }
}
