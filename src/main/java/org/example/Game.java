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

    List<Map<String, Object>> actionRequests;
    private int roundNum;
    private int gameID;
    public ArrayList<Player> players;
    private PokerWebSocketServer webSocketServer;
    private Dealer dealer;
    private int currentPlayerIndex;

    public void setWebSocketServer(PokerWebSocketServer server) {
        this.webSocketServer = server;
    }

    public Game(ArrayList<Player> players){
        this.roundNum = 1;
        this.players = players;
        //dealer = new Dealer(this.players);
    }

    public int getRoundNum(){
        return this.roundNum;
    }

    public int getGameID(){
        return this.gameID;
    }

    public void checkRound(int round){

    }

    public void progressRound(){
        while (roundNum<=10){
            if(roundNum>1)decideOrder();
            dealer = new Dealer(this.players);
            playRound();
        }
        System.out.println("Game Finished.");
    }

    public void decideOrder() {
        players.add(players.remove(0));
    }

    public void decideRankCutPlayer(){

    }

    public void renewRound(ArrayList<Player> rankList){

    }

    public void receiveNotification(){

    }

    public void handleAction(int userID, int actionNumber, int betChip) {
        Player player = dealer.getUserByID(userID);
        dealer.performAction(userID, actionNumber, betChip);
        webSocketServer.broadcast("actionResult", player.getName() + " performed action " + actionNumber);

        if (dealer.getPlayers().stream().anyMatch(Player::isInRound)) {
            moveToNextPlayer();
        } else {
            endRound();
        }
    }

    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % dealer.getPlayers().size();
        Player currentPlayer = dealer.getPlayers().get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "currentTurn", "It's your turn!");
    }

    private void endRound() {
        dealer.decideWinner();
        webSocketServer.broadcast("roundEnded", "The round has ended.");
        dealer.showWinners();

        if (dealer.getPlayers().size() > 1) {
            progressRound();
        } else {
            webSocketServer.broadcast("gameOver", "The game is over!");
        }
    }

    //1ラウンドの流れを記述
    public void playRound() {
        System.out.println("Starting Round " + roundNum);

        // 手札を配布
        dealer.dealInitialCards(5);
        System.out.println("Initial hands dealt.");
        dealer.showAllHands(); // デバッグ用

        while (players.stream().anyMatch(Player::isInRound)) {


            // アクション情報を受け取る
            System.out.println("Waiting for players to select actions...");

            // アクションを行う
            dealer.executeActions(actionRequests);

        }

        // 手札交換情報を受け取る（サーバ経由）
        System.out.println("Waiting for players to select cards to exchange...");
        waitForExchangeRequests(); // プレイヤーから交換情報を待つ

        // 手札の交換を実行
        dealer.executeChangeHand(exchangeRequests);
        System.out.println("Cards exchanged.");
        dealer.showAllHands(); // デバッグ用

        //スキルを使用する


        // 最後のベットを行う

        // 勝者を決定
        dealer.decideWinner();
        System.out.println("Winner decided.");
        dealer.showWinners(); // デバッグ用

        // ラウンド終了処理

        if (roundNum >= MAX_ROUND) {
            System.out.println("Game Over.");
        } else {
            System.out.println("Round " + roundNum + " completed.");
        }
        roundNum++;
    }


    private void waitForExchangeRequests() {
        // 仮のデータ取得 (実際はサーバから取得)
        String jsonInput = "[" +
                "{\"userID\": 1, \"exchangeCardIndex\": [0, 2, 4]}," +
                "{\"userID\": 2, \"exchangeCardIndex\": [1, 3]}," +
                "{\"userID\": 3, \"exchangeCardIndex\": []}," +
                "{\"userID\": 4, \"exchangeCardIndex\": [0, 1]}" +
                "]";

        // JSONを解析し、交換リクエストを格納
        transformExchangeRequests(jsonInput);
    }

    private void waitingForActionRequests() {
        // 仮のデータ取得 (実際はサーバから取得)
        String jsonInput = "[" +
                "{\"userID\": 1, \"actionNumber\": 0, \"betChip\": 10}," +
                "{\"userID\": 2, \"actionNumber\": 0, \"betChip\": 10}," +
                "{\"userID\": 1, \"actionNumber\": 1, \"betChip\": 0}," +
                "{\"userID\": 1, \"actionNumber\": 1, \"betChip\": 0}," +
                "]";

        // JSONを解析し、交換リクエストを格納
        transformActionRequests(jsonInput);
    }



    Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
    public void transformExchangeRequests(String jsonInput){
        exchangeRequests = gson.fromJson(jsonInput, listType);
    }

    public void transformActionRequests(String jsonInput){
        actionRequests = gson.fromJson(jsonInput, listType);
    }

    public Dealer getDealer() {
        return dealer; // 現在のDealerを返す
    }


}
