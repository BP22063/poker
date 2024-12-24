package org.example;
import java.util.ArrayList;

public class Player {

    private int userID;
    private String name;
    public int haveChip;
    public int betChip;
    public int rank;
    public ArrayList<Card> hand;
    public ArrayList<Integer> skill;
    private boolean isInRound;
    //private Card[] 手札;

    public Player(int userID,String name){
        this.userID = userID;
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public void updatePlayerInformation() {

    }

    public void updateHand() {


    }

    public void returnCard() {

    }

    public void clearCard(){
        hand.clear();
    }

    public void disconnectFromGame() {

    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void exchangeCard_player(int index, Card newCard) {
        if (index >= 0 && index < hand.size()) {
            hand.set(index, newCard);
        }
    }

    public void showHand_player() {
        System.out.println(name + "'s Hand: " + hand);//テスト用
    }

    public int removeSkill() {
        return 0;
    }

    public void provideChips() {

    }
    public String getName() {
        return name;
    }

}
