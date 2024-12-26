package org.example;
import java.util.ArrayList;

public class Player {

    private int userID;
    private String name;
    public int haveChip;
    public int betChip;
    public int rank;
    public int rolePoint;
    public ArrayList<Card> hand;
    public ArrayList<Integer> skill;
    private boolean isInRound;
    //private Card[] 手札;

    public Player(int userID,String name){
        this.userID = userID;
        this.name = name;
        this.hand = new ArrayList<>();
        this.haveChip = 3000;
        this.betChip = 0;
    }

    public int getUserID() {
        return userID;
    }

    public int getHaveChip() {
        return haveChip;
    }

    public void setHaveChip(int haveChip) {
        this.haveChip = haveChip;
    }

    public int getBetChip() {
        return betChip;
    }

    public void setBetChip(int betChip){
        this.betChip = betChip;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRolePoint() {
        return rolePoint;
    }

    public void setRolePoint(int rolePoint) {
        this.rolePoint = rolePoint;
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

    public boolean isInRound() {
        return isInRound;
    }

    public void setisInRound(boolean isInRound) {
        this.isInRound = isInRound;
    }
}
