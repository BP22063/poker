package org.example;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;




public class Game {

    private GameState currentState;
    static Gson gson = new Gson();
    private static final int MAX_ROUND = 10;

    List<Map<String, Object>> exchangeRequests;

    List<Map<String, Object>> actionRequests;
    private int roundNum;
    private int gameID;
    public ArrayList<Player> players;
    private Dealer dealer;

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
            //dealer = new Dealer(this.players);
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

    public void setState(GameState newState) {
        this.currentState = newState;
        handleStateChange();
    }

    private void handleStateChange() {
        switch (currentState) {
            case START:
                // ラウンドの開始処理
                startRound();
                break;
            case BETTING:
                // ベットフェーズの処理
                break;
            case EXCHANGING_CARDS:
                // カード交換フェーズの処理
                handleCardExchangePhase();
                break;
            case FINAL_BETTING:
                // 最終ベットフェーズの処理
                break;
            case ROUND_END:
                // ラウンド終了の処理
                break;
        }
    }

    private void startRound() {
        System.out.println("Starting Round " + roundNum);
        dealer = new Dealer(this.players);
        dealer.dealInitialCards(5);
        System.out.println("Initial hands dealt.");
        dealer.showAllHands(); // デバッグ用
    }

    //1ラウンドの流れを記述
    public void playRound() {
        setState(GameState.START);

        while (players.stream().anyMatch(Player::isInRound)) {


            // アクション情報を受け取る
            System.out.println("Waiting for players to select actions...");


            // アクションを行う
            dealer.executeActions(actionRequests);

        }

        // 手札交換情報を受け取る（サーバ経由）
        setState(GameState.EXCHANGING_CARDS);


        // 手札の交換を実行

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


    /*
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

     */

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

    private void handleCardExchangePhase() {
        System.out.println("Card exchange phase started.");

        for (Player player : players) {
            System.out.println("Requesting card exchange from: " + player.getName());

            // 1. ゲーム状態をクライアントに送信
            sendGameStateToClient(player.getUserID());

            // 2. プレイヤーからのカード交換情報を受信
            List<Integer> exchangeCardIndices = receiveCardExchangeFromPlayer(player);

            // 3. カード交換の実行
            dealer.changeHand(player.getUserID(), new ArrayList<>(exchangeCardIndices));

            // 4. 交換後の情報をクライアントに送信
            sendUpdatedHandToClient(player.getUserID());
        }

        System.out.println("Card exchange phase ended.");
    }

    private void sendGameStateToClient(int userId) {
        // 擬似コード: ゲームの現在の状態をクライアントに送信する
        System.out.println("Sending game state to user " + userId);
        // 実際にはWebSocketを使ってデータを送信する処理を実装
    }

    private List<Integer> receiveCardExchangeFromPlayer(Player player) {
        System.out.println("Receiving card exchange from player: " + player.getName());

        // デバッグ用のJSONデータ
        String jsonInput = "{" +
                "\"userID\": " + player.getUserID() + "," +
                "\"exchangeCardIndex\": [0, 2, 4]" +
                "}";

        // Gsonを使用してJSONデータを解析
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(jsonInput).getAsJsonObject();
        JsonArray exchangeCardArray = jsonObject.getAsJsonArray("exchangeCardIndex");

        // インデックスをリストに変換
        List<Integer> exchangeCardIndices = new ArrayList<>();
        for (int i = 0; i < exchangeCardArray.size(); i++) {
            exchangeCardIndices.add(exchangeCardArray.get(i).getAsInt());
        }

        System.out.println("Received exchange indices: " + exchangeCardIndices);
        return exchangeCardIndices;
    }

    private void sendUpdatedHandToClient(int userId) {
        // 擬似コード: 更新された手札情報をクライアントに送信
        System.out.println("Sending updated hand to user " + userId);
        // 実際にはWebSocketを使ってデータを送信する処理を実装
    }



}
