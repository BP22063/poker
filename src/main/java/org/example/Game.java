package org.example;

import java.lang.reflect.Type;
import java.util.*;

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
    private int currentPlayerIndex = 0;

    private List<Player> playersInRound = new ArrayList<>();
    private GameState currentGameState;
    private int BettingTimes = 0;

    public void setWebSocketServer(PokerWebSocketServer server) {
        this.webSocketServer = server;
    }

    public List<Player> getPlayers() {
        return this.players; // プレイヤーリスト (List<Player>) を保持していると仮定
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex); // currentPlayerIndex で現在のプレイヤーを追跡
    }

    public int getRound() {
        return this.roundNum; // 現在のラウンドを管理する変数
    }

    public int getFieldBetChip() {
        return dealer.getFieldBetChip(); // Dealer クラスに最高ベット額を管理するメソッドがあると仮定
    }

    public int getTotalFieldBetChip() {
        return dealer.getTotalFieldBetChip(); // 総ポット金額を管理するフィールド
    }


    public Game(ArrayList<Player> players){
        this.roundNum = 1;
        this.players = players;
        //dealer = new Dealer(this.players);
        //プレイヤーの配置位置
        for(int i=1;i<players.size();i++){
            players.get(i).setPosition(i);
        }
    }

    public void updateGameState(GameState newState) {
        this.currentGameState = newState;
        webSocketServer.broadcastGameStateWithDetails(newState);
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
            if(!dealer.collectInitialChip()){
                break;
            }
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


    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playersInRound.size();
        Player currentPlayer = playersInRound.get(currentPlayerIndex);

        // 次のプレイヤーにターン開始を通知
        webSocketServer.sendToPlayer(currentPlayer, "currentTurn", "It's your turn!");
    }



    private void endRound() {
        dealer.decideWinner();
        dealer.distributeBetChip();
        webSocketServer.broadcast("roundEnded", "The round has ended.");
        dealer.showWinners();

        if (roundNum >= MAX_ROUND || dealer.getPlayers().size() <= 1) {
            webSocketServer.broadcast("gameOver", "The game is over!");
            endGame();
        } else {
            roundNum++;
            //playRound(); // 次のラウンドへ進む
        }
    }

    public void endGame() {
        ArrayList<Player> ranking = new ArrayList<>(players); // playersの内容をコピー

        // Comparatorを使用してhaveChipの値で降順にソート
        Collections.sort(ranking, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getHaveChip() - p1.getHaveChip();
            }
        });
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

        // フェーズ: SELECT_SKILL
        startPhase4();

        // フェーズ: FINAL_BETTING
        startPhase5();


        // ラウンド終了処理
        endRound();
    }

    public void startPhase1() {
        updateGameState(GameState.BET_PASS);
        webSocketServer.broadcast("startPhase1", "Phase 1: Bet or Pass.");
        playersInRound = new ArrayList<>(players); // 全員が対象
        currentPlayerIndex = 0;

        // 最初のプレイヤーにターンを開始
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "currentTurn", "It's your turn!");
    }

    public void handleAction(int userID, int actionNumber, int betChip) {
        Player currentPlayer = playersInRound.get(currentPlayerIndex);

        if (currentPlayer.getUserID() != userID) {
            webSocketServer.sendToPlayer(currentPlayer, "error", "Not your turn!");
            return;
        }

        // アクションを処理
        dealer.performAction(userID, actionNumber, betChip);
        webSocketServer.broadcast("actionResult", currentPlayer.getName() + " performed action " + actionNumber);

        // 更新情報を送信
        webSocketServer.broadcastGameStateWithDetails(currentGameState);

        // 次のプレイヤーに進むかフェーズ終了
        if (currentPlayerIndex == playersInRound.size() - 1) {
            startPhase2();
        } else {
            moveToNextPlayer();
        }
    }


    public void startPhase2() {
        updateGameState(GameState.RAISE_CALL_FOLD);
        webSocketServer.broadcast("startPhase2", "Phase 2: Raise, Call, or Fold.");
        currentPlayerIndex = 0;//ここは親からではなく、最初にベットした人の次になるはず

        // 最初のプレイヤーにターンを開始
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "currentTurn", "It's your turn!");
    }


    public void startPhase3() {
        BettingTimes = 1;
        updateGameState(GameState.EXCHANGE_HAND);
        webSocketServer.broadcast("startPhase3", "Phase 3: Exchange cards.");
        playersInRound = new ArrayList<>(players); // 全員が対象
        currentPlayerIndex = 0;

        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "currentTurn", "It's your turn!");
    }

    public void handleCardExchange(int userID, ArrayList<Integer> exchangeCardIndex) {
        Player currentPlayer = playersInRound.get(currentPlayerIndex);

        if (currentPlayer.getUserID() != userID) {
            webSocketServer.sendToPlayer(currentPlayer, "error", "Not your turn!");
            return;
        }

        // カード交換処理
        dealer.changeHand(userID, exchangeCardIndex);
        webSocketServer.sendToPlayer(currentPlayer, "updateHand", gson.toJson(currentPlayer.hand));

        // 更新情報を送信
        webSocketServer.broadcastGameStateWithDetails(currentGameState);

        // 次のプレイヤーに進むかフェーズ終了
        if (currentPlayerIndex == playersInRound.size() - 1) {
            startPhase4();
        } else {
            moveToNextPlayer();
        }
    }

    public void startPhase4() {
        updateGameState(GameState.SELECT_SKILL);
        webSocketServer.broadcast("startPhase4", "Phase 4: Select a skill.");
        playersInRound = new ArrayList<>(players); // 全員が対象
        currentPlayerIndex = 0;

        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "currentTurn", "It's your turn!");
    }
    public void handleSkillUse(int userID, Object... args) {
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        //currentPlayer.flag_skill = 1;

        if (currentPlayer.getUserID() != userID) {
            webSocketServer.sendToPlayer(currentPlayer, "error", "Not your turn!");
            return;
        }

        // スキル使用処理
        dealer.adaptSkill(currentPlayer, args);
        dealer.useSkills();
        webSocketServer.broadcast("skillUsed", currentPlayer.getName() + " used a skill.");

        // 更新情報を送信
        webSocketServer.broadcastGameStateWithDetails(currentGameState);

        // 次のプレイヤーに進むかフェーズ終了
        if (currentPlayerIndex == playersInRound.size() - 1) {
            startPhase5();
        } else {
            moveToNextPlayer();
        }
    }
    public void startPhase5() {
        updateGameState(GameState.RAISE_CALL_FOLD);
        webSocketServer.broadcast("startPhase5", "Phase 5: Raise, Call, or Fold.");
        currentPlayerIndex = 0;

        // 最初のプレイヤーにターンを開始
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "currentTurn", "It's your turn!");
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
            if (player.isInRound() && player.flag_action == 0) {
                return false; // まだアクションしていないプレイヤーがいる
            }
        }
        return true; // 全員がアクションを完了
    }

    private boolean allPlayersExchanged() {
        for (Player player : dealer.getPlayers()) {
            if (player.isInRound() && player.flag_exchangeCard == 0) {
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
