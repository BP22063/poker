package org.example;

import java.util.ArrayList;

public class Game {

<<<<<<< HEAD
    private static final int MAX_ROUND = 10;

=======
>>>>>>> e7044ff (Gameクラスの追加、コンストラクタとgetterの設定)
    private int roundNum;
    private int gameID;

    public Game(){
        this.roundNum = 1;
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

    }

    public void decideRankCutPlayer(){

    }

    public void renewRound(ArrayList<Player> rankList){

    }

    public void receiveNotification(){

    }


}
