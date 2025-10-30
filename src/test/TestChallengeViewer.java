package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.UNOController;
import model.*;
import view.ChallengeViewer;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

public class TestChallengeViewer {
    private ChallengeViewer challengeViewer;
    private UNOController controller;
    private JPanel testPanel;

    @BeforeEach
    public void setUp() {
        UNOController.resetInstance();
        controller = UNOController.getInstance();
        controller.setPlayers();
        controller.setViewers();

        challengeViewer = new ChallengeViewer();
        testPanel = new JPanel();
        challengeViewer.setPanel(testPanel);
        challengeViewer.setController();
        challengeViewer.setTimer(controller.getTurnTimer());
    }

    @AfterEach
    public void tearDown() {
        UNOController.resetInstance();
    }

    @Test
    public void testInitialStateNotChallenging() {
        assertEquals(false, challengeViewer.getIsChallenging());
    }

    @Test
    public void testSetChallengeSetsFlag() {
        challengeViewer.setChallenge(true);

        assertEquals(true, challengeViewer.getIsChallenging());
    }

    @Test
    public void testGetIsChallengingReturnsFalse() {
        assertEquals(false, challengeViewer.getIsChallenging());
    }

    @Test
    public void testSetControllerNotNull() {
        challengeViewer.setController();

        assertNotNull(controller);
    }

    @Test
    public void testSetTimerNotNull() {
        CountDownTimer timer = controller.getTurnTimer();

        challengeViewer.setTimer(timer);

        assertNotNull(timer);
    }

    @Test
    public void testSetPanelNotNull() {
        JPanel panel = new JPanel();

        challengeViewer.setPanel(panel);

        assertNotNull(panel);
    }

    @Test
    public void testSetChallengeAfterInitial() {
        challengeViewer.setChallenge(true);

        assertEquals(true, challengeViewer.getIsChallenging());
    }

    @Test
    public void testDrawWindowWhenNotChallenging() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        challengeViewer.drawWindow(g);
        Field redButtonField = ChallengeViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(challengeViewer);

        assertEquals(null, redButton);
        g.dispose();
    }

    @Test
    public void testDrawWindowWhenChallenging() throws Exception {
        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        challengeViewer.drawWindow(g);
        Field redButtonField = ChallengeViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(challengeViewer);

        assertNotNull(redButton);
        g.dispose();
    }

    @Test
    public void testDrawButtonsCreatesRedButton() throws Exception {
        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        challengeViewer.drawButtons(g);
        Field redButtonField = ChallengeViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(challengeViewer);

        assertNotNull(redButton);
        g.dispose();
    }

    @Test
    public void testDrawButtonsCreatesGreenButton() throws Exception {
        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        challengeViewer.drawButtons(g);
        Field greenButtonField = ChallengeViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(challengeViewer);

        assertNotNull(greenButton);
        g.dispose();
    }

    @Test
    public void testRedButtonText() throws Exception {
        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        challengeViewer.drawButtons(g);
        Field redButtonField = ChallengeViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(challengeViewer);

        assertEquals("Get 4 Cards", redButton.getText());
        g.dispose();
    }

    @Test
    public void testGreenButtonText() throws Exception {
        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        challengeViewer.drawButtons(g);
        Field greenButtonField = ChallengeViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(challengeViewer);

        assertEquals("Challenge", greenButton.getText());
        g.dispose();
    }

    @Test
    public void testRemoveButtonsClearsRedButton() throws Exception {
        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        challengeViewer.drawButtons(g);

        challengeViewer.removeButtons();
        Field redButtonField = ChallengeViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(challengeViewer);

        assertEquals(null, redButton);
        g.dispose();
    }

    @Test
    public void testRemoveButtonsClearsGreenButton() throws Exception {
        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        challengeViewer.drawButtons(g);

        challengeViewer.removeButtons();
        Field greenButtonField = ChallengeViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(challengeViewer);

        assertEquals(null, greenButton);
        g.dispose();
    }

    @Test
    public void testRedButtonClickAddsCards() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);
        Player nextPlayer = controller.getPlayerList().get(1);
        int initialHandSize = nextPlayer.getHand().size();

        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        challengeViewer.drawButtons(g);

        Field redButtonField = ChallengeViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(challengeViewer);
        redButton.doClick();

        assertEquals(initialHandSize + 4, nextPlayer.getHand().size());
        g.dispose();
    }

    @Test
    public void testRedButtonClickResetsChallenging() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);

        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        challengeViewer.drawButtons(g);

        Field redButtonField = ChallengeViewer.class.getDeclaredField("redButton");
        redButtonField.setAccessible(true);
        JButton redButton = (JButton) redButtonField.get(challengeViewer);
        redButton.doClick();

        assertEquals(false, challengeViewer.getIsChallenging());
        g.dispose();
    }

    @Test
    public void testGreenButtonClickResetsChallenging() throws Exception {
        controller.startGame();
        controller.setIsFreezed(true);

        Player currentPlayer = controller.getCurrentPlayer();
        Player nextPlayer = controller.getPlayerList().get(1);

        currentPlayer.getHand().clear();
        nextPlayer.getHand().clear();

        for (int i = 0; i < 3; i++) {
            currentPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, true, ""));
        }

        for (int i = 0; i < 6; i++) {
            nextPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false, ""));
        }

        challengeViewer.setChallenge(true);
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        challengeViewer.drawButtons(img.getGraphics());

        Field greenButtonField = ChallengeViewer.class.getDeclaredField("greenButton");
        greenButtonField.setAccessible(true);
        JButton greenButton = (JButton) greenButtonField.get(challengeViewer);

        assertEquals(true, challengeViewer.getIsChallenging());

        greenButton.doClick();

        assertEquals(false, challengeViewer.getIsChallenging());
    }
}
