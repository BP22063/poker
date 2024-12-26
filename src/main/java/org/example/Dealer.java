package org.example;

import java.util.ArrayList;
import java.util.List;

public class Dealer {

    private ArrayList<Player> players;
    private int fieldbetChip;
    private Deck deck;
    private ArrayList<Player> rankList;
    private ArrayList<String> disableHands;
    private ArrayList<Integer> usedSkills;
    ArrayList<Player> winners;



    //private Skill_handSwap  skill_handSwap ;

    //private Skill_disableHand skill_disableHand;


    //private Skill_exchangingHandsAgain skill_exchangingHandsAgain;

    public Dealer() {
        players = new ArrayList<>();
        deck = new Deck();
        deck.shuffle();
    }

    public void decideOrder() {
        players.add(players.remove(0));
    }

    public void provideSkill(int Skill) {

    }

    public void collectInitialChip() {

    }

    public void provideCard() {

    }

    public void changeCard(int playerIndex, int cardIndex) {
        if (playerIndex >= 0 && playerIndex < players.size()) {
            Player player = players.get(playerIndex);
            Card oldCard = player.hand.get(cardIndex);
            Card newCard = deck.draw();

            player.exchangeCard_player(cardIndex, newCard);
            deck.discard(oldCard);  // 捨て札に追加
        }
    }


    public void provideChip(Player players) {

    }



    private void adaptSkill() {

    }

    public void useSkills() {

    }

    public void sendApplicationCommunication(String JSON) {

    }

    public void performAction(int userID, int actionNumber, int betChip) {

    }

    public void sendCardInformation() {

    }


    public void addPlayer(Player player) {
        players.add(player);
    }

    public void dealInitialCards(int cardsPerPlayer) {
        for (Player player : players) {
            player.clearCard();
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.addCard(deck.draw());
            }
        }
    }
    public Player getCurrentDealer(){
        return players.get(0);
    }

    public void showAllHands() {
        for (Player player : players) {
            player.showHand_player();
        }
    }


    public void checkLostUser(ArrayList<Player> players, ArrayList<Player> rankList) {

    }


    public void addDisableHands(String hand) {

    }

    public void addUsedSkill(int skill) {

    }


    public void checkPlayersList(ArrayList<Player> players) {

    }

    public void decideWinner(){
        //List<Integer> point = new ArrayList<>();
        winners = new ArrayList<>();
        RoleControl roleControl = new RoleControl();
        int maxPoint=0;

        for(Player player: players){
            player.rolePoint=roleControl.judgeRole(player.hand);
        }

        for(Player player:players){
            if(player.rolePoint>maxPoint){
                maxPoint = player.rolePoint;
            }
        }

        for (Player player : players){
            if(player.rolePoint == maxPoint){
                winners.add(player);
            }
        }

    }

    public void showWinners(){
        for(Player player:winners){
            System.out.println("プレイヤー："+player.getName()+" 役："+getRoleName(player.rolePoint));
        }
    }

    public String getRoleName(int point){
        String roleName=null;
        switch(point){
            case 10:
                roleName="RoyalStraightFlush";
                break;
            case 9:
                roleName="StraightFlush";
                break;
            case 8:
                roleName="4cards";
                break;
            case 7:
                roleName="FullHouse";
                break;
            case 6:
                roleName="Flush";
                break;
            case 5:
                roleName="Straight";
                break;
            case 4:
                roleName="3cards";
                break;
            case 3:
                roleName="2pair";
                break;
            case 2:
                roleName="1pair";
                break;
            case 1:
                roleName="high card";
                break;
        }
        return roleName;
    }




}
