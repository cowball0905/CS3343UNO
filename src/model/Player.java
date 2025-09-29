package model;

import java.util.ArrayList;

public abstract class Player implements Drawable, Playable, Shoutable, Catchable, Challengeable {
    protected String name;
    protected ArrayList<Card> hand;
    protected boolean isShout; 
    
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isShout = false;
    }
    
    public String getName() { return name; }
    public ArrayList<Card> getHand() { return hand; }
    public boolean getIsShout() { return isShout; }
    public void setIsShout(boolean saidUno) { this.isShout = saidUno; }
    
    public abstract void drawCard();
    public abstract void playCard(Card card);
    public abstract void shoutUno();
    public abstract void catchForgotShout(Player targetPlayer);
    public abstract void challengeDrawFour(Player targetPlayer);
}

