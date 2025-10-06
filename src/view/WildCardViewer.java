package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.UNOController;
import model.CountDownTimer;
import model.Card;

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

    public void drawButtons(Graphics g){
        if(redButton!=null){return;}
        redButton = new JButton();
        redButton.setBackground(Color.RED);
        redButton.setForeground(Color.WHITE);
        redButton.setBounds(350, 250, 120, 60);
        redButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Red);
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            controller.passNextPlayer(false);
            controller.eachRound();
        });
        panel.add(redButton);
        
        blueButton = new JButton();
        blueButton.setBackground(Color.BLUE);
        blueButton.setForeground(Color.WHITE);
        blueButton.setBounds(490, 250, 120, 60);
        blueButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Blue);
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            controller.passNextPlayer(false);
            controller.eachRound();
        });
        panel.add(blueButton);
        
        yellowButton = new JButton();
        yellowButton.setBackground(Color.YELLOW);
        yellowButton.setForeground(Color.BLACK);
        yellowButton.setBounds(350, 330, 120, 60);
        yellowButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Yellow);
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            controller.passNextPlayer(false);
            controller.eachRound();
        });
        panel.add(yellowButton);
        
        greenButton = new JButton();
        greenButton.setBackground(Color.GREEN);
        greenButton.setForeground(Color.WHITE);
        greenButton.setBounds(490, 330, 120, 60);
        greenButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Green);
            this.isHavingWild = false;
            timer.stopTimer();
            removeButtons();
            controller.passNextPlayer(false);
            controller.eachRound();
        });
        panel.add(greenButton);

        g.drawRect(300, 200, 360, 230);
        g.setFont(new Font("Arial", 1, 28));
        g.drawString("Choose a new color", 350, 230);
        
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
    
    public void autoSelectRandomColor() {
        wild.setColor(model.Color.Red);
    }
    
    private void removeButtons() {
        panel.remove(redButton);
        panel.remove(blueButton);
        panel.remove(yellowButton);
        panel.remove(greenButton);
        panel.repaint();
    }
}
