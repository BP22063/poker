package org.example;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

    private int userID;
    private String name;
    private int haveChip;
    private int betChip;
    private int rank;
    private int rolePoint;
    public ArrayList<Card> hand;
    private ArrayList<Integer> skill;
    private boolean isInRound;
    //private Card[] 手札;

    public Player(int userID,String name){
        this.userID = userID;
        this.name = name;
        this.hand = new ArrayList<>();
        this.haveChip = 3000;
        this.betChip = 0;
        this.skill = new ArrayList<>();
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

    // スキルを追加
    public void addSkill(int skillID){
        this.skill.add(Integer.valueOf(skillID));
    }

    // 使用したスキルを削除
    public void removeSkill(int skillID){
        this.skill.remove(Integer.valueOf(skillID));
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

    public List<Integer> getSkills() {
        return this.skill;
    }
}
