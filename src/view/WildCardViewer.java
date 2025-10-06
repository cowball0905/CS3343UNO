package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JButton;

import model.WildCard;

public class WildCardViewer {
    private boolean isHavingWild;
    private WildCard wild;
    private JButton redButton;
    private JButton blueButton;
    private JButton yellowButton;
    private JButton greenButton;

    public WildCardViewer(){
        isHavingWild = false;
    }

    public void setWildCard(WildCard card){
        this.wild = card;
        isHavingWild = true;
    }

    public void drawWindow(Graphics g){
      if (this.isHavingWild) {
        redButton = new JButton();
        redButton.setBackground(Color.RED);
        redButton.setForeground(Color.WHITE);
        redButton.setBounds(350, 250, 120, 60);
        redButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Red);
            this.isHavingWild = false;
        });
        
        blueButton = new JButton();
        blueButton.setBackground(Color.BLUE);
        blueButton.setForeground(Color.WHITE);
        blueButton.setBounds(490, 250, 120, 60);
        blueButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Blue);
            this.isHavingWild = false;
        });
        
        yellowButton = new JButton();
        yellowButton.setBackground(Color.YELLOW);
        yellowButton.setForeground(Color.BLACK);
        yellowButton.setBounds(350, 330, 120, 60);
        yellowButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Yellow);
            this.isHavingWild = false;
        });
        
        greenButton = new JButton();
        greenButton.setBackground(Color.GREEN);
        greenButton.setForeground(Color.WHITE);
        greenButton.setBounds(490, 330, 120, 60);
        greenButton.addActionListener(e -> {
            this.wild.setColor(model.Color.Green);
            this.isHavingWild = false;
        });

        g.drawRect(300, 200, 360, 230);
        g.setFont(new Font("Arial", 1, 28));
        g.drawString("Choose a new color", 350, 230);
      }
    }

    public void setHavingWild(boolean hasWild) {
        this.isHavingWild = hasWild;
    }
    
    public boolean isHavingWild() {
        return isHavingWild;
    }
}
