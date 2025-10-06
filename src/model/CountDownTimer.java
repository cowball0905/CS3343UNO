package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CountDownTimer {
    private Timer timer;
    private int remainingSeconds;
    private boolean isRunning;
    private JPanel panel; // The panel to repaint for timer display
    private TimerCallback callback;

    // Interface for timer completion callback
    public interface TimerCallback {
        void onTimerComplete();
    }

    public CountDownTimer(JPanel panel, TimerCallback callback) {
        this.panel = panel;
        this.callback = callback;
        this.isRunning = false;
    }
    
    public void startTimer(int seconds) {
        remainingSeconds = seconds;
        isRunning = true;
        
        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingSeconds > 0) {
                    remainingSeconds--;
                    panel.repaint();
                } else {
                    stopTimer();
                    if (callback != null) {
                        callback.onTimerComplete();
                    }
                }
            }
        });
        
        timer.start();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
        isRunning = false;
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void resumeTimer() {
        if (timer != null && isRunning) {
            timer.start();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public void drawTimer(Graphics g) {
        if (isRunning) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String timerText = "Time: " + remainingSeconds;
            
            
            g.drawString(timerText, 700, 100);
        }
    }
}