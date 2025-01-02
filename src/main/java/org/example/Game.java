package org.example;

import java.util.ArrayList;

public class Game {

    private static final int MAX_ROUND = 10;

    private int roundNum;
    private int gameID;
    //private ArrayList<Player> players;

    public Game(ArrayList<Player> players){
        this.roundNum = 1;
        Dealer dealer = new Dealer();
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




}
