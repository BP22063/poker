package org.example;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.*;

public class Player {

    private int userID;
    private String name;
    private int haveChip;
    private int betChip;
    private int rank;
    private int rolePoint;
    public ArrayList<Card> hand;
    public ArrayList<Integer> skill;
    private boolean isInRound;
    public int flag_exchangeCard;
    public int flag_action;
    //public int flag_skill;
    private int position;

    private static final int SKILL_NUM = 3; // 一人当たりのスキル配布数
    private static final int SKILL_KIND_NUM = 4; // スキルの種類数
    //private Card[] 手札;

    public Player(int userID,String name){
        this.userID = userID;
        this.name = name;
        this.hand = new ArrayList<>();
        this.haveChip = 3000;
        this.betChip = 0;
        this.skill = new ArrayList<>();
        provideSkill();
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
    public void provideSkill() {
        List<Integer> availableSkills = new ArrayList<>();
        for (int i = 0; i < SKILL_KIND_NUM; i++) {
            availableSkills.add(i);
        }

        Collections.shuffle(availableSkills); // リストをランダムにシャッフル

        for (int i = 0; i < SKILL_NUM; i++) {
            addSkill(availableSkills.get(i)); // 先頭から3つのスキルを追加
        }
    }

    // 使用したスキルを削除
    public void removeSkill(int skillID) {
        if (this.skill != null && this.skill.contains(skillID)) {
            this.skill.remove(Integer.valueOf(skillID));
        }
    }
    public List<Card> getHand() {
        return this.hand; // 手札を保持する List<Card> フィールド
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

    public void setPosition(int i){
        this.position = i;
    }
    public int getPotision(){
        return this.position;
    }
    public String getHandAsString() {
        StringBuilder handString = new StringBuilder("[");
        for (Card card : hand) { // hand はプレイヤーの手札
            handString.append(card.toString()).append(", ");
        }
        if (!hand.isEmpty()) {
            handString.setLength(handString.length() - 2); // 最後の ", " を削除
        }
        handString.append("]");
        return handString.toString();
    }


    public void addChips(int chips) {
        this.haveChip += chips;
    }
}
