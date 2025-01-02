package org.example;

import java.util.ArrayList;

public class Game {

    private int roundNum;
    private int gameID;
    //private ArrayList<Player> players;
    private Dealer dealer;

    public Game(ArrayList<Player> players){
        this.roundNum = 1;
        dealer = new Dealer();
        for(Player player : players ){
            dealer.addPlayer(player);
        }
    }

    public int getRoundNum(){
        return this.roundNum;
    }

    public int getGameID(){
        return this.gameID;
    }

    public void checkRound(int round){

    }

    public void progressRound(int round){
        if(roundNum<=MAX_ROUND){
            roundNum +=1;
            Dealer dealer = new Dealer();
            dealer.decideOrder();
        }
    }

    public void decideRankCutPlayer(){

    }

    public void renewRound(ArrayList<Player> rankList){

    }

    public void receiveNotification(){

    }


    public Dealer getDealer() {
        return dealer; // 現在のDealerを返す
    }


}
