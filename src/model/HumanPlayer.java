package model;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.UNOController;

public class HumanPlayer extends Player {

    private Random random;
    private JPanel panel;

    public HumanPlayer(String name) {
        super(name);
        this.random = new Random();
    }
    private void sortHandCards() {
        if (hand.size() <= 1) {
            return;
        }
        hand = mergeSort(hand);
    }
    
    /*
     * Merge sort implementation for sorting cards
     */

    private ArrayList<Card> mergeSort(ArrayList<Card> cards) {
        if (cards.size() <= 1) {
            return cards;
        }

        int mid = cards.size() / 2;
        ArrayList<Card> left = new ArrayList<>(cards.subList(0, mid));
        ArrayList<Card> right = new ArrayList<>(cards.subList(mid, cards.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private ArrayList<Card> merge(ArrayList<Card> left, ArrayList<Card> right) {
        ArrayList<Card> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (compareCards(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }

        return result;
    }

    private int compareCards(Card c1, Card c2) {
        if (c1 == null && c2 == null) return 0;
        if (c1 == null) return 1;
        if (c2 == null) return -1;

        int color1Priority = getColorPriority(c1);
        int color2Priority = getColorPriority(c2);

        if (color1Priority != color2Priority) {
            return Integer.compare(color1Priority, color2Priority);
        }

        return compareCardValue(c1, c2);
    }

    private int getColorPriority(Card card) {
        if (card.getType() == Type.Wild) return 4;
        if (card.getType() == Type.WildDrawFour) return 5;

        Color color = card.getColor();
        switch (color) {
            case Yellow: return 0;
            case Blue: return 1;
            case Green: return 2;
            case Red: return 3;
            default: return 6;
        }
    }

    private int compareCardValue(Card c1, Card c2) {
        if (c1.getType() == Type.Wild || c1.getType() == Type.WildDrawFour) {
            return 0;
        }

        if (c1.getType() == Type.Number && c2.getType() == Type.Number) {
            return Integer.compare(c1.getValue(), c2.getValue());
        }

        if (c1.getType() == Type.Number) return -1;
        if (c2.getType() == Type.Number) return 1;

        int type1Priority = getTypePriority(c1);
        int type2Priority = getTypePriority(c2);

        return Integer.compare(type1Priority, type2Priority);
    }

    private int getTypePriority(Card card) {
        Type type = card.getType();
        switch (type) {
            case Skip: return 0;
            case Reverse: return 1;
            case DrawTwo: return 2;
            default: return 3;
        }
    }

    @Override
    public void drawCard(Card card) {
        this.hand.add(card);
        isShout = false;
        sortHandCards();
    }
    
    @Override
    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            System.out.println(name + " plays " + card.toString());

            if (hand.size() == 1 && !isShout) {
                int delay = random.nextInt(2001); // Random delay around 2 sec
                Timer catchTimer = new Timer(delay, e -> {
                    controller.getPlayerList().get(random.nextInt(3) + 1).catchForgotShout(this);
                });
                catchTimer.setRepeats(false);
                catchTimer.start();
                System.out.println(name + " should shout UNO!");
            }
        }
    }
    
    /*
    @Override
    public String shoutUno() {
        String errorMessage = null;

        // Only current player can shout UNO
        if (this != controller.getCurrentPlayer()) {
            errorMessage = "It's not your turn!";
        } else if (this.getIsShout()) {
            errorMessage = "You shouted UNO already!";
        } else if (this.getHand().size() != 1) {
            errorMessage = "You have more than one card!";
        } else {
            isShout = true;
            System.out.println(name + " shouts UNO!");
        }

        return errorMessage;
    }
    */

    @Override
    public String shoutUno() {
        
        String errorMessage =  null;
        if (this != controller.getCurrentPlayer() && this.getHand().size() > 1) {
            errorMessage = "You have more than 1 card!";
        } else if (this != controller.getCurrentPlayer() && this.getHand().size() == 1) {
            isShout = true;
            System.out.println(name + " shouts UNO!");
        } else if (this.getIsShout()){
            errorMessage = "You shouted UNO already!";
        } else if (this.getHand().size() > 2) {
            errorMessage = "You have more than 2 card!";
        } else {
            boolean hasPlayableCard = false;
            for (Card c : this.getHand()){
                if(controller.canPlayCard(c, controller.getTopCard(1))){
                    hasPlayableCard = true;
                }
            }
            if (!hasPlayableCard) {
                errorMessage = "You have no playable card!";
                return errorMessage;
            }
            isShout = true;
            System.out.println(name + " shouts UNO!");
        }
        return errorMessage;
    }
    
    
    @Override
    public void catchForgotShout(Player targetPlayer) {
        if (targetPlayer.getHand().size() == 1 && !targetPlayer.getIsShout()) {
            System.out.println(name + " catches " + targetPlayer.getName() + " for forgetting to shout UNO!");
            // Target player should draw 2 penalty cards
            for(int i = 0; i < 2; i++){
                Card card = controller.getCardFactory().giveCard(controller.getDeck(), false, false, "");
                if (card == null) {
                    controller.deckEmpty();
                    return;
                }
                targetPlayer.drawCard(card);
            }
            controller.getGamePanel().updateDisplay();
        }
    }
    
    @Override
    public void challengeDrawFour(Player targetPlayer) {
        System.out.println(name + " challenges " + targetPlayer.getName() + "'s Wild Draw Four card!");
        ArrayList<Card> cards = targetPlayer.getHand();
        ArrayList<Card> validCards = new ArrayList<>();

        for(Card card:cards){
            if(controller.canPlayCard(card,controller.getTopCard(1))){
                validCards.add(card);
            }
        }

        for(Card card:validCards){
            if(card.getType()!=Type.Wild && card.getType()!=Type.WildDrawFour){
                System.out.println("Challenge Success!");
                for(int i=0;i<4;i++){
                    Card drawnCard = controller.getCardFactory().giveCard(controller.getDeck(), false, false, "");
                    if (drawnCard == null) {
                        controller.deckEmpty();
                        return;
                    }
                    targetPlayer.drawCard(drawnCard);
                }
                controller.passNextPlayer(1);
                controller.eachRound();
                return;
            }
        }

        System.out.println("Challenge Fail!");
        for(int i=0;i<6;i++){
            Card drawnCard = controller.getCardFactory().giveCard(controller.getDeck(), false, false, "");
            if (drawnCard == null) {
                controller.deckEmpty();
                return;
            }
            drawCard(drawnCard);
        }
        controller.passNextPlayer(2);
        controller.eachRound();
    }
}