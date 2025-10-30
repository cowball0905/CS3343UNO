package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.UNOController;
import model.Card;
import model.CountDownTimer; 
import javax.swing.Timer;

public class DeckPlayCardViewer {
    private boolean isDeciding;
    private CountDownTimer timer;
    private UNOController controller;
    Card card;
    private JPanel panel;
    private JButton redButton;
    private JButton greenButton;

    public DeckPlayCardViewer() {
        isDeciding = false;
    }

    public void setController() {
        this.controller = UNOController.getInstance();
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void setIsDeciding(Card card) {
        this.isDeciding = true;
        this.card = card;
    }

    public void endDeckCardViewer() {
        this.isDeciding = false;
        this.removeButtons();
    }

    public void drawWindow(Graphics g) {
        if (this.isDeciding) {
            drawButtons(g);
        }
    }

    public void drawButtons(Graphics g) {
        if (redButton != null) {
            return;
        }
        redButton = new JButton("Hold");
        redButton.setBackground(Color.RED);
        redButton.setOpaque(true);
        redButton.setBounds(350, 250, 120, 60);
        redButton.addActionListener(e -> {
            timer.stopTimer();
            this.isDeciding = false;
            removeButtons();
            this.timer.stopTimer();
            controller.passNextPlayer(1);
            controller.eachRound();
        });
        panel.add(redButton);

        greenButton = new JButton("Play");
        greenButton.setBackground(Color.GREEN);
        greenButton.setOpaque(true);
        greenButton.setBounds(490, 250, 120, 60);
        greenButton.addActionListener(e -> {
            timer.stopTimer();
            removeButtons();
            this.isDeciding = false;
            Timer delayTimer = new Timer(100, event -> {
                controller.playCard(card);
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        });
        panel.add(greenButton);
    }

    public boolean getIsDeciding() {
        return this.isDeciding;
    }

    public void removeButtons() {
        panel.remove(redButton);
        panel.remove(greenButton);
        redButton = null;
        greenButton = null;
        panel.repaint();
    }
}