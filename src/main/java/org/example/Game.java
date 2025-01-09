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
    private GameState currentGameState;

    public void setWebSocketServer(PokerWebSocketServer server) {
        this.webSocketServer = server;
    }

    public Game(ArrayList<Player> players){
        this.roundNum = 1;
        this.players = players;
        //dealer = new Dealer(this.players);
    }

    public void updateGameState(GameState newState) {
        this.currentGameState = newState;
        webSocketServer.broadcast("updateGameState", newState.toString());
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

        // クライアントにアクション結果を通知
        webSocketServer.broadcast("actionResult", player.getName() + " performed action " + actionNumber);

        // 全員のアクションが完了した場合、次のフェーズへ進行
        if (allPlayersActed()) {
            if (currentGameState == GameState.BET_PASS) {
                startPhase2();
            } else if (currentGameState == GameState.FINAL_BETTING) {
                startPhase5();
            }
        }
    }


    public void handleCardExchange(int userID, ArrayList<Integer> exchangeCardIndex) {
        Player player = dealer.getUserByID(userID);
        dealer.changeHand(userID, exchangeCardIndex);

        // クライアントに更新された手札を送信
        webSocketServer.sendToPlayer(player, "updateHand", gson.toJson(player.hand));

        // 全員がカード交換を完了したら次のフェーズへ
        if (allPlayersExchanged()) {
            startPhase4();
        }
    }


    public void handleSkillUse(int userID, Object... args) {
        Player player = dealer.getUserByID(userID);
        dealer.adaptSkill(player, args);
        dealer.useSkills();

        // スキル使用結果をクライアントに通知
        webSocketServer.broadcast("skillUsed", player.getName() + " used a skill.");

        // 全員がスキルを選択した場合、ラウンド終了
        if (allPlayersSelectedSkill()) {
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

        if (roundNum >= MAX_ROUND || dealer.getPlayers().size() <= 1) {
            webSocketServer.broadcast("gameOver", "The game is over!");
        } else {
            roundNum++;
            playRound(); // 次のラウンドへ進む
        }
    }


    //1ラウンドの流れを記述
    public void playRound() {
        System.out.println("Starting Round " + roundNum);

        // フェーズ: START
        updateGameState(GameState.START);
        webSocketServer.broadcast("roundStart", "Round " + roundNum + " has started.");

        // フェーズ: BET_PASS
        startPhase1();

        // フェーズ: RAISE_CALL_FOLD
        startPhase2();

        // フェーズ: EXCHANGE_HAND
        startPhase3();

        // フェーズ: FINAL_BETTING
        startPhase4();

        // フェーズ: SELECT_SKILL
        startPhase5();

        // ラウンド終了処理
        endRound();
    }

    private void startPhase1() {
        updateGameState(GameState.BET_PASS);
        webSocketServer.broadcast("startPhase1", "Place your bets or pass.");

        // ベット処理を待機
        waitForActions();
    }

    private void startPhase2() {
        updateGameState(GameState.RAISE_CALL_FOLD);
        webSocketServer.broadcast("startPhase2", "Raise, Call, or Fold.");

        // アクション処理を待機
        waitForActions();
    }

    private void startPhase3() {
        updateGameState(GameState.EXCHANGE_HAND);
        webSocketServer.broadcast("startPhase3", "Select cards to exchange.");

        // カード交換リクエストを待機
        waitForExchangeRequests();
    }

    private void startPhase4() {
        updateGameState(GameState.FINAL_BETTING);
        webSocketServer.broadcast("startPhase4", "Final betting: Raise, Call, or Fold.");

        // ベット処理を待機
        waitForActions();
    }

    private void startPhase5() {
        updateGameState(GameState.SELECT_SKILL);
        webSocketServer.broadcast("startPhase5", "Choose a skill to use.");

        // スキル選択リクエストを待機
        waitForSkillRequests();
    }

    private void waitForActions() {
        while (!allPlayersActed()) {
            try {
                Thread.sleep(30000); // プレイヤーの入力を待機
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForExchangeRequests() {
        while (!allPlayersExchanged()) {
            try {
                Thread.sleep(30000); // プレイヤーの入力を待機
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForSkillRequests() {
        while (!allPlayersSelectedSkill()) {
            try {
                Thread.sleep(30000); // プレイヤーの入力を待機
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean allPlayersActed() {
        for (Player player : dealer.getPlayers()) {
            if (player.isInRound() && player.getBetChip() == 0) {
                return false; // まだアクションしていないプレイヤーがいる
            }
        }
        return true; // 全員がアクションを完了
    }

    private boolean allPlayersExchanged() {
        for (Player player : dealer.getPlayers()) {
            if (player.isInRound() && player.hand.size() < 5) {
                return false; // 交換が完了していないプレイヤーがいる
            }
        }
        return true; // 全員が交換を完了
    }

    private boolean allPlayersSelectedSkill() {
        for (Player player : dealer.getPlayers()) {
            if (player.isInRound() && player.getSkills().isEmpty()) {
                return false; // スキル選択が完了していないプレイヤーがいる
            }
        }
        return true; // 全員がスキル選択を完了
    }


    public Dealer getDealer() {
        return dealer; // 現在のDealerを返す
    }


}
