package org.example;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Game {

    static Gson gson = new Gson();
    private static final int MAX_ROUND = 10;
    private int roundNum;
    private int gameID;
    public ArrayList<Player> players;
    private PokerWebSocketServer webSocketServer;
    private Dealer dealer;
    private int currentPlayerIndex = 0;
    private int bettingTimes = 0;

    private List<Player> playersInRound = new ArrayList<>();
    private GameState currentGameState;
    private Player raisingPlayer = null;

    public ArrayList<String> skillLogs = new ArrayList<>();
    private int continuousCallTimes = 0;

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


    public Game(ArrayList<Player> players) {
        this.roundNum = 1;
        this.players = players;
        dealer = new Dealer(this.players);
        //プレイヤーの配置位置
        for (int i = 1; i < players.size(); i++) {
            players.get(i).setPosition(i);
        }
    }

    public void updateGameState(GameState newState) {
        this.currentGameState = newState;
        System.out.println("Now GameState: " + newState.toString());
        for (Player player : this.players){
            webSocketServer.sendToPlayerGameStateWithDetails(player, newState);
        }
    }


    public int getRoundNum() {
        return this.roundNum;
    }

    public int getGameID() {
        return this.gameID;
    }

    public void checkRound(int round) {

    }

    public void progressRound() {
        playRound();
    }

    public void decideOrder() {
        players.add(players.remove(0));
    }

    public void decideRankCutPlayer() {

    }

    public void renewRound(ArrayList<Player> Players) {
        for (Player player : Players) {
            player.setBetChip(0);
            player.setRolePoint(0);
        }
        this.dealer = new Dealer(Players);
    }

    public void receiveNotification() {

    }


    private void moveToNextPlayer() {
        System.out.println("moved to next player.");
        currentPlayerIndex = (currentPlayerIndex + 1) % playersInRound.size();
        Player currentPlayer = playersInRound.get(currentPlayerIndex);

        // 次のプレイヤーにターン開始を通知
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "It's your turn!");
    }


    private void endRound() {
        //４人パスだとおそらくbettingTimesは0のまま
        if(bettingTimes != 0) {
            // 勝者を決定
            List<Player> winners = dealer.decideWinner();

            // ラウンド結果を生成
            JsonArray roundResults = new JsonArray();
            for (Player player : players) {
                JsonObject playerResult = new JsonObject();
                playerResult.addProperty("name", player.getName());
                playerResult.addProperty("hand", player.getHandAsString()); // 手札を文字列化して送信
                playerResult.addProperty("role", dealer.getRoleName(player.getRolePoint())); // プレイヤーの役
                roundResults.add(playerResult);
            }

            // 勝者情報を付加
            JsonArray winnerArray = new JsonArray();
            for (Player winner : winners) {
                winnerArray.add(winner.getName());
            }

            JsonObject roundSummary = new JsonObject();
            roundSummary.addProperty("action", "roundresults");
            roundSummary.add("results", roundResults);
            roundSummary.add("winners", winnerArray);

            // 結果を全クライアントにブロードキャスト
            webSocketServer.broadcast("roundresults", roundSummary.toString());

            // ポットを分配（同点の場合、均等に分ける）
            int share = dealer.totalFieldBetChip / winners.size();
            for (Player winner : winners) {
                winner.addChips(share);
            }
        }
        dealer.fieldBetChip = 0;

        // ラウンド数を更新
        roundNum++;
        if (roundNum > 10) { // 最大ラウンド数を超えた場合
            endGame(); // ゲーム終了処理
        } else {
            decideOrder();
            renewRound(players);
            playRound(); // 次のラウンドを開始
        }
    }

    public void endGame() {
        // プレイヤーのランキングを生成
        ArrayList<Player> ranking = new ArrayList<>(players); // playersの内容をコピー
        ranking.sort(Comparator.comparingInt(Player::getHaveChip).reversed()); // 所持チップ数で降順ソート

        // ゲーム結果を生成
        JsonArray gameResults = new JsonArray();
        int rank = 1;
        for (Player player : ranking) {
            JsonObject playerResult = new JsonObject();
            playerResult.addProperty("rank", rank++);
            playerResult.addProperty("name", player.getName());
            playerResult.addProperty("chips", player.getHaveChip());
            gameResults.add(playerResult);
        }

        JsonObject gameSummary = new JsonObject();
        gameSummary.addProperty("action", "gameresults");
        gameSummary.add("results", gameResults);

        // ゲーム結果を全クライアントにブロードキャスト
        webSocketServer.broadcast("gameresults", gameSummary.toString());
    }



    //1ラウンドの流れを記述
    public void playRound() {

        this.bettingTimes = 0;

        System.out.println("Starting Round " + roundNum);

        if (!dealer.collectInitialChip()) {
            endGame();
        }

        // フェーズ: START
        updateGameState(GameState.START);
        webSocketServer.broadcast("log", "Round " + roundNum + " has started.");

        // フェーズ: BET_PASS
        startPhase1();

    }

    public void startPhase1() {
        updateGameState(GameState.BET_PASS);
        webSocketServer.broadcast("startPhase1", "Phase 1: Bet or Pass.");
        playersInRound = new ArrayList<>(players); // 全員が対象
        currentPlayerIndex = 0;

        // 最初のプレイヤーにターンを開始
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "It's your turn!");
    }

    public void handleAction(int userID, int actionNumber, int betChip) {
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        if (currentPlayer.getUserID() != userID) {
            webSocketServer.sendToPlayer(currentPlayer, "error", "Not your turn!");
            return;
        }

        // アクションを処理
        dealer.performAction(userID, actionNumber, betChip);
        if (actionNumber == 4) {
            currentPlayerIndex--;
        }
        playersInRound.removeIf(player -> !player.getIsInRound());
        System.out.println("playersInRound(number): " + playersInRound.size());
        System.out.println("playersInRound(name): ");
        for (Player player : playersInRound) {
            System.out.println(player.getName());
        }
        String action;
        switch (actionNumber){
            case 0:
                action = "Bet";
                break;
            case 1:
                action = "Pass";
                break;
            case 2:
                action = "Raise";
                break;
            case 3:
                action = "Call";
                break;
            case 4:
                action = "Fold";
                break;
            default:
                throw new IllegalStateException("Unexpected actionNumber value: " + actionNumber);

        }
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "your turn ended.");
        webSocketServer.broadcast("log", currentPlayer.getName() + " performed action " + action);
        // 更新情報を送信
        for (Player player : this.players){
            webSocketServer.sendToPlayerGameStateWithDetails(player, currentGameState);
        }

        switch (bettingTimes) {
            case 0:
                if (actionNumber == 0) {
                    bettingTimes++;
                    startPhase2();
                } else {
                    if (currentPlayerIndex == 3) {
                        endRound();
                    } else {
                        moveToNextPlayer();
                    }
                }
                break;

            case 1: {
                int nextPlayerIndex = (currentPlayerIndex + 1) % playersInRound.size(); // 安全なインデックス計算
                Player nextPlayer = playersInRound.get(nextPlayerIndex);

                if (actionNumber == 3) {
                    continuousCallTimes++;
                } else if (actionNumber == 4) {
                    continuousCallTimes = continuousCallTimes;
                } else {
                    continuousCallTimes = 0;
                }

                if ((nextPlayer == raisingPlayer && actionNumber != 2) || continuousCallTimes == playersInRound.size() - 1) {
                    startPhase3();
                    continuousCallTimes = 0;
                    bettingTimes = 2;
                    raisingPlayer = null;
                } else if (nextPlayer == raisingPlayer && actionNumber == 2) {
                    raisingPlayer = currentPlayer;
                    moveToNextPlayer();
                } else {
                    moveToNextPlayer();
                }
                break;
            }

            case 2:
                if (actionNumber == 0) {
                    bettingTimes = 3;
                    startPhase2();
                } else {
                    if (currentPlayerIndex == 3) {
                        endRound();
                    } else {
                        moveToNextPlayer();
                    }
                }
                break;

            case 3: {
                int nextPlayerIndex = (currentPlayerIndex + 1) % playersInRound.size(); // 安全なインデックス計算
                Player nextPlayer = playersInRound.get(nextPlayerIndex);

                if (actionNumber == 3) {
                    continuousCallTimes++;
                } else if (actionNumber == 4) {
                    continuousCallTimes = continuousCallTimes;
                } else {
                    continuousCallTimes = 0;
                }

                if ((nextPlayer == raisingPlayer && actionNumber != 2) || continuousCallTimes == playersInRound.size() - 1) {
                    endRound();
                    continuousCallTimes = 0;
                    bettingTimes = 0;
                    raisingPlayer = null;
                } else if (nextPlayer == raisingPlayer && actionNumber == 2) {
                    raisingPlayer = currentPlayer;
                    moveToNextPlayer();
                } else {
                    moveToNextPlayer();
                }
                break;
            }

            default:
                throw new IllegalStateException("Unexpected bettingTimes value: " + bettingTimes);
        }

    }


    public void startPhase2() {
        updateGameState(GameState.RAISE_CALL_FOLD);
        webSocketServer.broadcast("startPhase2", "Phase 2: Raise, Call, or Fold.");
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;//ここは親からではなく、最初にベットした人の次になるはず

        // 最初のプレイヤーにターンを開始
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "It's your turn!");
    }


    public void startPhase3() {
        updateGameState(GameState.EXCHANGE_HAND);
        webSocketServer.broadcast("startPhase3", "Phase 3: Exchange cards.");
        currentPlayerIndex = 0;

        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "It's your turn!");
    }

    public void handleCardExchange(int userID, ArrayList<Integer> exchangeCardIndex) {
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        System.out.println("current player ID: " + currentPlayer.getUserID());
        if (currentPlayer.getUserID() != userID) {
            webSocketServer.sendToPlayer(currentPlayer, "error", "Not your turn!");
            return;
        }

        // カード交換処理
        for (Integer cardIndex : exchangeCardIndex){
            webSocketServer.broadcast("log", currentPlayer.getName() + "exchanged" + currentPlayer.getHand().get(cardIndex).toString());
        }
        dealer.changeHand(userID, exchangeCardIndex);
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "your turn ended.");
        webSocketServer.sendToPlayer(currentPlayer, "updateHand", gson.toJson(currentPlayer.hand));

        // 更新情報を送信
        for (Player player : this.players){
            webSocketServer.sendToPlayerGameStateWithDetails(player, currentGameState);
        }

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
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "It's your turn!");
    }
    public void handleSkillUse(int userID,String log, Object... args) {
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        //currentPlayer.flag_skill = 1;

        skillLogs.add(log);

        if (currentPlayer.getUserID() != userID) {
            webSocketServer.sendToPlayer(currentPlayer, "error", "Not your turn!");
            return;
        }

        // スキル使用処理
        dealer.adaptSkill(currentPlayer, args);

        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "your turn ended.");
        webSocketServer.broadcast("skillUsed", currentPlayer.getName() + " used a skill.");

        // 更新情報を送信
        for (Player player : this.players){
            webSocketServer.sendToPlayerGameStateWithDetails(player, currentGameState);
        }

        // 次のプレイヤーに進むかフェーズ終了
        if (currentPlayerIndex == playersInRound.size() - 1) {
            dealer.useSkills();
            for(String l : skillLogs) {
                webSocketServer.broadcast("log",l );
            }
            startPhase5();
        } else {
            moveToNextPlayer();
        }
    }
    public void startPhase5() {
        updateGameState(GameState.BET_CHECK);
        webSocketServer.broadcast("startPhase5", "Phase 2: Bet or Check");
        currentPlayerIndex = 0;

        // 最初のプレイヤーにターンを開始
        Player currentPlayer = playersInRound.get(currentPlayerIndex);
        webSocketServer.sendToPlayer(currentPlayer, "turnNotice", "It's your turn!");
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
            if (player.getIsInRound() && player.flag_action == 0) {
                return false; // まだアクションしていないプレイヤーがいる
            }
        }
        return true; // 全員がアクションを完了
    }

    private boolean allPlayersExchanged() {
        for (Player player : dealer.getPlayers()) {
            if (player.getIsInRound() && player.flag_exchangeCard == 0) {
                return false; // 交換が完了していないプレイヤーがいる
            }
        }
        return true; // 全員が交換を完了
    }



    private boolean allPlayersSelectedSkill() {
        for (Player player : dealer.getPlayers()) {
            if (player.getIsInRound() && player.getSkills().isEmpty()) {
                return false; // スキル選択が完了していないプレイヤーがいる
            }
        }
        return true; // 全員がスキル選択を完了
    }


    public Dealer getDealer() {
        return dealer; // 現在のDealerを返す
    }



    public Player getPlayer(int userID){
        return dealer.getUserByID(userID);
    }
}
