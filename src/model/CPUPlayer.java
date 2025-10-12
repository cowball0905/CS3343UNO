package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controller.UNOController;

public class CPUPlayer extends Player {

    private Random random;

    public CPUPlayer(String name) {
        super(name);
        this.random = new Random();
    }

    @Override
    public void drawCard(Card card) {
        this.hand.add(card);
        isShout = false;
    }

    @Override
    public void playCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            System.out.println(name + " (CPU) plays " + card.getClass().getSimpleName());
            if (hand.size() == 1) {
                shoutUno();
            }
        }
    }

    @Override
    public String shoutUno() {
        UNOController controller = UNOController.getInstance();
        if (!isShout && this == controller.getCurrentPlayer() && this.getHand().size() == 1) {
            isShout = true;
            System.out.println(name + " (CPU) shouts UNO!");
        } else {
            System.out.println(name + " (CPU) failed to shout UNO!");
        }
        return null;
    }

    @Override
    public void catchForgotShout(Player targetPlayer) {
        // CPU always catches human player forgetting UNO with high probability (90%)
        if (targetPlayer.getHand().size() == 1 && !targetPlayer.getIsShout() &&
                random.nextDouble() < 0.9) {
            System.out.println(name + " (CPU) catches " + targetPlayer.getName() + " for forgetting to shout UNO!");
        }
    }

    @Override
    public void challengeDrawFour(Player targetPlayer) {
        UNOController controller = UNOController.getInstance();
        if (random.nextDouble() < 0.5) {
            System.out.println(name + " (CPU) challenges " + targetPlayer.getName() + "'s Wild Draw Four card!");
            ArrayList<Card> cards = targetPlayer.getHand();
            ArrayList<Card> validCards = new ArrayList<>();
            ArrayList<Player> players = controller.getPlayerList();
            Player currentPlayer = controller.getCurrentPlayer();

            for(Card card:cards){
                if(controller.canPlayCard(card)){
                    validCards.add(card);
                }
            }

            for(Card card:validCards){
                if(card.getType()!=Type.Wild && card.getType()!=Type.WildDrawFour){
                    System.out.println("Challenge Success!");
                    for(int i=0;i<4;i++){
                        targetPlayer.drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, players.indexOf(currentPlayer)==0? true:false));
                    }
                    controller.passNextPlayer(1);
                    controller.eachRound();
                    return;
                }
            }

            System.out.println("Challenge Fail!");
            for(int i=0;i<6;i++){
                drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false));
            }
            controller.passNextPlayer(1);
            controller.eachRound();
        }else{
            System.out.println(name + " (CPU) does not challenge " + targetPlayer.getName() + "'s Wild Draw Four card");
            for(int i=0;i<4;i++){
                drawCard(controller.getCardFactory().giveCard(controller.getDeck(), false, false));
            }  
            controller.passNextPlayer(2);
            controller.eachRound();
            return;
        }
    }

    public void chooseCard() {
        UNOController controller = UNOController.getInstance();
        ArrayList<Card> playerCards = this.getHand();
        ArrayList<Card> validCards = new ArrayList<>();

        for (Card c : playerCards) {
            if (controller.canPlayCard(c)) {
                validCards.add(c);
            }
        }

        if (validCards.isEmpty()) {
            System.out.println(name + " (CPU) has no valid cards to play and must draw.");
            this.drawCard(controller.getCardFactory().giveCard(controller.getDeck(),false, false));
            controller.passNextPlayer(1);
            controller.eachRound();
            return;
        }

        Card chosenCard = randomChoose(validCards); //Use random function to choose a card

        playCard(chosenCard);
        System.out.println(name + " (CPU) chose their Card!");
        chosenCard.setRevealed(true);
        controller.playCard(chosenCard);
    }

    private Card randomChoose(List<Card> validCards) {
        Random random = new Random();
        int randomIndex = random.nextInt(validCards.size());
        return validCards.get(randomIndex);
    }

    public Color chooseColor(){
        ArrayList<Card> playerCards = this.getHand();
        int max = 0;
        int each = 0;
        int colorIndex = 0;

        for(int i=0; i< Color.values().length; i++){
            for (Card c : playerCards) {
                if (c.getColor()==Color.values()[i]) {
                    each++;
                }
            }
            if(each > max){
                max = each;
                colorIndex = i;
            }
            each = 0;
        }
        System.out.println("CPU change to "+Color.values()[colorIndex]);
        return Color.values()[colorIndex];
    }
}