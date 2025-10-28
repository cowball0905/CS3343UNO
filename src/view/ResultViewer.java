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

public class ResultViewer {
    private UNOController controller;
    private JPanel panel;

    public ResultViewer(){
        
    }

    public void setController() {
        this.controller = UNOController.getInstance();
    }
    
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    
}
