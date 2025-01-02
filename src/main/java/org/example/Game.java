package org.example;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Game {

    static Gson gson = new Gson();
    private static final int MAX_ROUND = 10;

    List<Map<String, Object>> exchangeRequests;
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

    //1ラウンドの流れを記述
    public void playRound(){
        dealer.executeChangeHand(exchangeRequests);
    }



    Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
    public void transformExchangeRequests(String jsonInput){
        exchangeRequests = gson.fromJson(jsonInput, listType);
    }

    public Dealer getDealer() {
        return dealer; // 現在のDealerを返す
    }


}
