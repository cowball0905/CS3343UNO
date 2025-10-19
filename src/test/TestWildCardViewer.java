package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;
import view.*;

// Mock CountDownTimer for testing
class MockCountDownTimer extends CountDownTimer {
    public boolean wasStopped = false;
    public boolean wasStarted = false;
    public int remainingSeconds = 30;
    public boolean isRunning = false;

    public MockCountDownTimer(JPanel panel, CountDownTimer.TimerCallback callback) {
        super(panel, callback);
    }
    
    @Override
    public void startTimer(int seconds) {
        wasStarted = true;
        isRunning = true;
        remainingSeconds = seconds;
    }
    
    @Override
    public void stopTimer() {
        wasStopped = true;
        isRunning = false;
    }
    
    @Override
    public int getRemainingSeconds() {
        return remainingSeconds;
    }
    
    @Override
    public boolean isRunning() {
        return isRunning;
    }
}

class TestWildCardViewer {
    private WildCardViewer wildCardViewer;
    private UNOController controller;
    private MockCountDownTimer testTimer;
    private JPanel testPanel;
    private Card testCard;
    private List<Player> testPlayers;
    private Player originalCurrentPlayer;
    private UNOController originalController;

    @BeforeEach
    void setUp() throws Exception {
        // Store original controller instance
        Field instanceField = UNOController.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        originalController = (UNOController) instanceField.get(null);
        
        // Create a new controller instance
        instanceField.set(null, null); // Clear existing instance
        controller = UNOController.getInstance();
        
        // Set up test players
        testPlayers = new ArrayList<>();
        Player humanPlayer = new HumanPlayer("Test Player");
        testPlayers.add(humanPlayer);
        testPlayers.add(new CPUPlayer("CPU 1"));
        testPlayers.add(new CPUPlayer("CPU 2"));
        testPlayers.add(new CPUPlayer("CPU 3"));
        testPlayers.add(new CPUPlayer("CPU 4"));
        
        // Initialize the controller with test players
        Field playersField = UNOController.class.getDeclaredField("players");
        playersField.setAccessible(true);
        playersField.set(controller, testPlayers);
        
        // Set current player
        originalCurrentPlayer = humanPlayer;
        Field currentPlayerField = UNOController.class.getDeclaredField("currentPlayer");
        currentPlayerField.setAccessible(true);
        currentPlayerField.set(controller, originalCurrentPlayer);
        
        // Initialize player hands
        for (Player player : testPlayers) {
            if (player.getHand() == null) {
                Field handField = Player.class.getDeclaredField("hand");
                handField.setAccessible(true);
                handField.set(player, new ArrayList<Card>());
            }
            player.getHand().add(new NumberCard(model.Color.Red, 0, false));
            player.getHand().add(new NumberCard(model.Color.Blue, 1, false));
        }
        
        // Set play direction
        Field playDirectionField = UNOController.class.getDeclaredField("playDirection");
        playDirectionField.setAccessible(true);
        playDirectionField.set(controller, 1);
        
        // Initialize WildCardViewer
        wildCardViewer = new WildCardViewer();
        testPanel = new JPanel();
        wildCardViewer.setPanel(testPanel);
        
        // Set up mock timer
        testTimer = new MockCountDownTimer(testPanel, () -> {});
        wildCardViewer.setTimer(testTimer);
        
        // Set the controller
        wildCardViewer.setController(controller);
        
        // Initialize test card
        testCard = new WildCard(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Restore original controller instance
        if (originalController != null) {
            Field instanceField = UNOController.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, originalController);
        }
    }

    private JButton getButton(String buttonName) throws Exception {
        Field field = WildCardViewer.class.getDeclaredField(buttonName);
        field.setAccessible(true);
        return (JButton) field.get(wildCardViewer);
    }

    private Object getFieldValue(String fieldName) throws Exception {
        Field field = WildCardViewer.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(wildCardViewer);
    }

    @Test
    void testTimerStartsWhenWindowDrawn() {
        // Given
        wildCardViewer.setWildCard(testCard);
        testTimer.wasStarted = false;
        
        // When
        wildCardViewer.drawWindow(testPanel.getGraphics());
        
        // Then
        assertTrue(testTimer.wasStarted, "Timer should be started when window is drawn");
        assertEquals(30, testTimer.getRemainingSeconds(), "Timer should be set to 30 seconds");
    }

    @Test
    void testInitialState() {
        assertFalse(wildCardViewer.isHavingWild());
        try {
            assertNull(getFieldValue("wild"));
        } catch (Exception e) {
            fail("Failed to access wild field");
        }
    }

    @Test
    void testSetWildCard() {
        wildCardViewer.setWildCard(testCard);
        assertTrue(wildCardViewer.isHavingWild());
        try {
            assertEquals(testCard, getFieldValue("wild"));
        } catch (Exception e) {
            fail("Failed to access wild field");
        }
    }

@Test
void testColorButtonActions() throws Exception {
    String[] colors = {"red", "blue", "yellow", "green"};
    
    // Set up the played cards list (PlayedCard field in UNOController)
    Field playedCardField = UNOController.class.getDeclaredField("PlayedCard");
    playedCardField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<Card> playedCards = (List<Card>) playedCardField.get(controller);
    playedCards.clear();
    playedCards.add(new NumberCard(model.Color.Red, 0, false));
    
    // Get the players list
    Field playersField = UNOController.class.getDeclaredField("players");
    playersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<Player> players = (List<Player>) playersField.get(controller);
    
    // Make sure all players have cards
    for (Player player : players) {
        if (player.getHand().isEmpty()) {
            player.getHand().add(new NumberCard(model.Color.Red, 1, false));
        }
    }
    
    // Test with each color button
    for (int i = 0; i < colors.length; i++) {
        // Reset test state
        testTimer.wasStopped = false;
        
        // Create a buffered image to get a valid Graphics object
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        
        try {
            // Set up the test
            wildCardViewer.setWildCard(testCard);
            wildCardViewer.drawWindow(g);
            
            // Get the button and simulate click
            JButton button = getButton(colors[i] + "Button");
            assertNotNull(button, colors[i] + " button should not be null");
            
            // Store current player before click
            Field currentPlayerField = UNOController.class.getDeclaredField("currentPlayer");
            currentPlayerField.setAccessible(true);
            Player playerBefore = (Player) currentPlayerField.get(controller);
            
            // Make sure next player has cards
            int currentIndex = players.indexOf(playerBefore);
            int nextIndex = (currentIndex + 1) % players.size();
            Player nextPlayer = players.get(nextIndex);
            if (nextPlayer.getHand().isEmpty()) {
                nextPlayer.getHand().add(new NumberCard(model.Color.Red, 2, false));
            }
            
            button.doClick(); // This will trigger the action listener
            
            // Verify the results
            Player playerAfter = (Player) currentPlayerField.get(controller);
            assertNotSame(playerBefore, playerAfter, "Current player should change after color selection");
            assertTrue(testTimer.wasStopped, "Timer should be stopped after color selection");
        } finally {
            g.dispose();
        }
    }
}
    @Test
    void testCleanUp() throws Exception {
        wildCardViewer.setWildCard(testCard);
        wildCardViewer.drawWindow(testPanel.getGraphics());
        assertNotNull(getButton("redButton"));
        
        // Use reflection to call cleanUp since it's private
        Method cleanUp = WildCardViewer.class.getDeclaredMethod("cleanUp");
        cleanUp.setAccessible(true);
        cleanUp.invoke(wildCardViewer);
        
        assertFalse(wildCardViewer.isHavingWild());
        assertNull(getFieldValue("redButton"));
    }

@Test
void testWildDrawFourCard() throws Exception {
    // Set up the played cards list (PlayedCard field in UNOController)
    Field playedCardField = UNOController.class.getDeclaredField("PlayedCard");
    playedCardField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<Card> playedCards = (List<Card>) playedCardField.get(controller);
    playedCards.clear(); // Clear any existing cards
    playedCards.add(new NumberCard(model.Color.Red, 0, false));
    
    // Make sure players have cards
    Field playersField = UNOController.class.getDeclaredField("players");
    playersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<Player> players = (List<Player>) playersField.get(controller);
    for (Player player : players) {
        if (player.getHand().isEmpty()) {
            player.getHand().add(new NumberCard(model.Color.Red, 1, false));
        }
    }
    
    // Test the wild draw four card
    testCard = new WildDrawFourCard(true);
    wildCardViewer.setWildCard(testCard);
    
    // Create a graphics context for drawing
    BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    Graphics g = img.getGraphics();
    try {
        wildCardViewer.drawWindow(g);
        JButton redButton = getButton("redButton");
        assertNotNull(redButton, "Red button should not be null");

        // Verify the button action
        redButton.doClick();
        assertTrue(testTimer.wasStopped, "Timer should be stopped after button click");
    } finally {
        g.dispose();
    }
}
    
}