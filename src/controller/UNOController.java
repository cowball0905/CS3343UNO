package controller;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import view.UNOGamePanel;
import view.UNOMenuPanel;

public class UNOController extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private static UNOController instance;
    private UNOGamePanel gamePanel;
    private UNOMenuPanel menuPanel;
    
    public static UNOController getInstance() {
        if (instance == null) {
            instance = new UNOController();
        }
        return instance;
    }
    
    private UNOController() {
        super("UNO Game");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // Initialize and show menu panel first
        showMenu();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void showMenu() {
        if (menuPanel == null) {
            menuPanel = new UNOMenuPanel(this);
        }
        if (gamePanel != null) {
            remove(gamePanel);
        }
        add(menuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void startGame() {
        if (gamePanel == null) {
            gamePanel = new UNOGamePanel(this);
        }
        remove(menuPanel);
        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        gamePanel.startGame();
    }
}
