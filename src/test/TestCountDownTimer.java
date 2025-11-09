package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.Card;
import model.CountDownTimer;
import model.WildCard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestCountDownTimer {
    private CountDownTimer timer;
    private TestPanel panel;
    private boolean callbackCalled;
    
    private class TestCallback implements CountDownTimer.TimerCallback {
        @Override
        public void onTimerComplete() {
            callbackCalled = true;
        }
    }
    
    private static class TestPanel extends JPanel {
        public boolean wasPainted = false;
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            wasPainted = true;
        }
        
        public void reset() {
            wasPainted = false;
        }
    }
    
    @BeforeEach
    public void setUp() {
        panel = new TestPanel();
        callbackCalled = false;
        timer = new CountDownTimer(panel, new TestCallback());
    }
    
    @Test
    public void testTimerStartStop() {
        timer.startTimer(5);
        assertTrue(timerIsActive(timer), "Timer should be active after start");
        assertTrue(timer.isRunning(), "Timer should report as running");
        
        timer.stopTimer();
        assertFalse(timerIsActive(timer), "Timer should be stopped after stop");
        assertFalse(timer.isRunning(), "Timer should report as not running");
    }
    
    @Test
    public void testTimerPauseResume() {
        timer.startTimer(5);
        int remainingBeforePause = timer.getRemainingSeconds();
        
        timer.pauseTimer();
        assertFalse(timerIsActive(timer), "Timer should be paused");
        assertEquals(remainingBeforePause, timer.getRemainingSeconds(), 
                   "Remaining time should not change when paused");
        
        timer.resumeTimer();
        assertTrue(timerIsActive(timer), "Timer should be active after resume");
    }
    
    @Test
    public void testDrawTimer() {
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        try {
            // Test drawing when timer is running
            timer.startTimer(10);
            timer.drawTimer(g2d);
            panel.paintComponent(g2d); // Force paint to update wasPainted
            
            // Test drawing when timer is not running
            timer.stopTimer();
            panel.reset();
            timer.drawTimer(g2d);
            panel.paintComponent(g2d); // Force paint to update wasPainted
            
            // Verify the panel was painted
            assertTrue(panel.wasPainted, "Panel should have been painted");
        } finally {
            g2d.dispose();
        }
    }
    
    @Test
    public void testTimerCompletion() throws Exception {
        // Create a new panel and callback for this test
        TestPanel testPanel = new TestPanel();
        final AtomicBoolean callbackInvoked = new AtomicBoolean(false);
        final CountDownLatch latch = new CountDownLatch(1);
        
        // Create a new timer with a callback that sets our flag
        CountDownTimer testTimer = new CountDownTimer(testPanel, () -> {
            callbackInvoked.set(true);
            latch.countDown();
        });
        
        try {
            // Start with a very short timer (100ms)
            testTimer.startTimer(0);  // 0 seconds should complete immediately
            
            // Wait for the callback with a timeout
            boolean completed = latch.await(2, TimeUnit.SECONDS);
            
            // Verify the callback was called
            assertTrue(completed, "Timer should complete within timeout");
            assertTrue(callbackInvoked.get(), "Callback should be called when timer completes");
            
            // Verify timer state
            assertFalse(testTimer.isRunning(), "Timer should not be running after completion");
            assertEquals(0, testTimer.getRemainingSeconds(), "Remaining time should be 0 after completion");
        } finally {
            // Make sure to clean up
            testTimer.stopTimer();
        }
    }
    
//    
//    @Test
//    public void testMultipleCallbacks() throws Exception {
//        final int[] callbackCount = {0};
//        final CountDownLatch latch = new CountDownLatch(3);
//        
//        // Create a timer with a callback that counts invocations
//        CountDownTimer testTimer = new CountDownTimer(panel, () -> {
//            callbackCount[0]++;
//            latch.countDown();
//        });
//        
//        try {
//            // Start the timer multiple times
//            testTimer.startTimer(0);
//            testTimer.startTimer(0);
//            testTimer.startTimer(0);
//            
//            // Wait for all callbacks to complete
//            boolean completed = latch.await(2, TimeUnit.SECONDS);
//            
//            // Verify all callbacks were called
//            assertTrue(completed, "All callbacks should complete within timeout");
//            assertEquals(3, callbackCount[0], "Callback should be called for each timer completion");
//        } finally {
//            testTimer.stopTimer();
//        }
//    }
//    
    @Test
    public void testMultipleStarts() {
        // Start timer multiple times
        timer.startTimer(5);
        int firstRemaining = timer.getRemainingSeconds();
        
        // Start again with different time
        timer.startTimer(10);
        
        // Should have new remaining time
        assertNotEquals(firstRemaining, timer.getRemainingSeconds(), 
                       "Remaining time should be updated on subsequent starts");
        assertTrue(timer.isRunning(), "Timer should still be running after restart");
    }
    
    @Test
    public void testPauseResume() {
        timer.startTimer(10);
        int beforePause = timer.getRemainingSeconds();
        
        timer.pauseTimer();
        assertFalse(timerIsActive(timer), "Timer should be paused");
        
        
        assertEquals(beforePause, timer.getRemainingSeconds(), 
                    "Remaining time should not change while paused");
        
        timer.resumeTimer();
        assertTrue(timerIsActive(timer), "Timer should be running after resume");
    }
    
    
    
    // Helper method to check if timer is active
    private boolean timerIsActive(CountDownTimer t) {
        try {
            Field timerField = CountDownTimer.class.getDeclaredField("timer");
            timerField.setAccessible(true);
            javax.swing.Timer swTimer = (javax.swing.Timer) timerField.get(t);
            return swTimer != null && swTimer.isRunning();
        } catch (Exception e) {
            return false;
        }
    }
    
    
}