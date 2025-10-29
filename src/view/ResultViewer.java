package view;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.UNOController;
import model.Player;

public class ResultViewer {
    private UNOController controller;
    private JPanel panel;

    public ResultViewer(){
        this.controller = UNOController.getInstance();
    }

    public void setController() {
        this.controller = UNOController.getInstance();
    }
    
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void displayResult(Graphics g){
        ArrayList<Player> sortedPlayers = controller.getSortedPlayersScore();

        int resultScreenWidth = 1000;
        int resultScreenHeight = 675;

        g.setColor(new Color(0, 0, 0, 128)); // Semi-transparent black
        g.fillRect(0, 0, resultScreenWidth, resultScreenHeight);

        // Draw the results
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        
        int currentY = 250;
        String[] rank = {"1st", "2nd", "3rd", "4th"};

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            g.drawString(rank[i], 300, currentY);
            g.drawString(player.getName(), 450, currentY);
            if (i == 0) {
                g.drawString("Winner", 630, currentY);
            } else {
                g.drawString( "" + player.getScore() * -1, 630, currentY);
            }
            currentY += 60;
        }
    }
    
}
