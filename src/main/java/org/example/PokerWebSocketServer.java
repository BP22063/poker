package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.io.IOException;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/sample")
public class PokerWebSocketServer {
    private static final Map<Session, Player> playerSessions = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();
    private static Game game;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New connection: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        Player player = playerSessions.remove(session);
        if (player != null) {
            System.out.println("Player " + player.getName() + " disconnected.");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("have received a message.");
        JsonObject json = gson.fromJson(message, JsonObject.class);
        String action = json.get("action").getAsString();
        System.out.println("Action: " + action);

        switch (action) {
            case "register":
                handleRegistration(session, json);
                break;
            case "playerAction":
                handlePlayerAction(json);
                break;
            case "changeCards":
                handleChangeCards(json);
                break;
            case "useSkill":
                handleUseSkill(json);
                break;
            //case "playerActionAgain":

            default:
                System.out.println("Unknown action: " + action);
        }
    }

    @OnError
    public void onError(jakarta.websocket.Session session, Throwable error) {
        System.err.println("[WebSocketServerSample] onError:" + session.getId());
        error.printStackTrace(); // スタックトレースを出力
    }



    private void handleRegistration(Session session, JsonObject json) {
        String name = json.get("name").getAsString();
        int userID = json.get("userID").getAsInt();
        Player player = new Player(userID, name);
        playerSessions.put(session, player);
        System.out.println("Player " + name + " registered.");

        if (playerSessions.size() == 4) { // 4人揃ったらゲームを開始
            startGame();
        }
    }

    private void startGame() {
        game = new Game(new ArrayList<>(playerSessions.values()));
        game.setWebSocketServer(this);
        game.progressRound();
    }

    private void handlePlayerAction(JsonObject json) {
        int userID = json.get("userID").getAsInt();
        int actionNumber = json.get("actionNumber").getAsInt();
        int betChip = json.has("betChip") ? json.get("betChip").getAsInt() : 0;

        game.handleAction(userID, actionNumber, betChip);
    }

    private void handleChangeCards(JsonObject json) {
        int userID = json.get("userID").getAsInt();
        JsonArray jsonArray = json.get("exchangeCardIndex").getAsJsonArray();
        ArrayList<Integer> exchangeCardIndex = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            exchangeCardIndex.add(element.getAsInt());
        }

        game.handleCardExchange(userID,exchangeCardIndex);
    }

    private void handleUseSkill(JsonObject json) {
        int userID = json.get("userID").getAsInt();
        int skillID = json.get("skillID").getAsInt();
        String log;
        Object[] args;

        switch (skillID) {
            case -1:
                args = new Object[]{};
                log= game.getPlayer(userID).getName() + "did not select the skill.";
                break;
            case 0:
                // No additional arguments needed for skillID 0
                double token = json.get("token").getAsDouble();
                args = new Object[]{token};
                log = game.getPlayer(userID).getName() + " selected the skill “disableSkill”.\n\"You cannot use skills this turn\"";
                break;
            case 1:
                // For skillID 1, "disableHand" is needed
                String disableHand = json.get("disableHand").getAsString();
                args = new Object[]{disableHand};
                log = game.getPlayer(userID).getName() + " selected the skill “disableHand”. Hand: "+disableHand;
                break;
            case 2:
                // For skillID 2, "cardIndexList" is needed
                JsonArray jsonArray = json.get("cardIndexList").getAsJsonArray();
                List<Integer> cardIndexList = new ArrayList<>();
                for (JsonElement element : jsonArray) {
                    cardIndexList.add(element.getAsInt());
                }
                args = new Object[]{cardIndexList};
                log = game.getPlayer(userID).getName() + " selected the skill “exchangeHandsAgain” skill. Index: "+cardIndexList;
                break;
            case 3:
                // For skillID 3, "swapPlayerID" is needed
                int swapPlayerID = json.get("swapPlayerID").getAsInt();
                args = new Object[]{swapPlayerID};
                log = game.getPlayer(userID).getName() + " sekected the skill “handSwap” skill. opponent: " +game.getPlayer(swapPlayerID);
                break;
            default:
                throw new IllegalArgumentException("Unknown skillID: " + skillID);
        }

        game.handleSkillUse(userID, log , args);
    }




    public void sendToPlayer(Player player, String type, String message) {
        playerSessions.forEach((session, p) -> {
            if (p.equals(player)) {
                try {
                    JsonObject json = new JsonObject();
                    json.addProperty("type", type);
                    json.addProperty("message", message);
                    session.getBasicRemote().sendText(json.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void broadcast(String type, String message) {
        playerSessions.keySet().forEach(session -> {
            try {
                JsonObject json = new JsonObject();
                json.addProperty("type", type);
                json.addProperty("message", message);
                session.getBasicRemote().sendText(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendToPlayerGameStateWithDetails(Player self, GameState phase) {
        JsonObject gameState = new JsonObject();

        // フェーズ情報
        gameState.addProperty("phase", phase.toString());

        // 全プレイヤーの名前とIDと配置位置の情報
        JsonArray playersArray = new JsonArray();
        for (Player player : game.getPlayers()) {
            JsonObject playerJson = new JsonObject();
            playerJson.addProperty("name", player.getName());
            playerJson.addProperty("id", player.getUserID());
            playerJson.addProperty("position",player.getPosition());
            playersArray.add(playerJson);
        }
        gameState.add("players", playersArray);

        // 他プレイヤーの情報
        JsonArray othersArray = new JsonArray();
        for (Player player : game.getPlayers()) {
            if (player != self) {
                JsonObject otherJson = new JsonObject();
                otherJson.addProperty("id", player.getUserID());
                otherJson.addProperty("chips", player.getHaveChip());
                JsonArray skillsArray = new JsonArray();
                for (Integer skill : player.getSkills()) {
                    skillsArray.add(skill);
                }
                otherJson.add("skills", skillsArray);
                othersArray.add(otherJson);
            }
        }
        gameState.add("others", othersArray);

        // ゲーム全体の情報
        JsonObject stateJson = new JsonObject();
        stateJson.addProperty("round", game.getRound());
        stateJson.addProperty("highestBet", game.getFieldBetChip());
        stateJson.addProperty("totalPot", game.getTotalFieldBetChip());
        gameState.add("gameState", stateJson);

        // 自分の情報
        JsonObject selfJson = new JsonObject();
        selfJson.addProperty("id", self.getUserID());
        selfJson.addProperty("bettingChips", self.getBetChip());
        selfJson.addProperty("chips", self.getHaveChip());
        JsonArray selfSkillsArray = new JsonArray();
        for (Integer skill : self.getSkills()) {
            selfSkillsArray.add(skill);
        }
        selfJson.add("skills", selfSkillsArray);
        JsonArray handArray = new JsonArray();
        for (Card card : self.getHand()) {
            handArray.add(card.toString()); // カードの情報を文字列化
        }
        selfJson.add("hand", handArray);
        gameState.add("self", selfJson);

        // 全プレイヤーに送信
        broadcast("gameStateUpdate", gameState.toString());
    }

    // 他のメソッドにゲーム結果保存のロジックを統合
    public void handleGameResult(Player player, int rank) {
        try {
            game.saveGameResult(player.getUserID(), player.getName(), rank);
        } catch (Exception e) {
            System.err.println("Failed to update game result for player: " + player.getName());
            e.printStackTrace();
        }
    }

}


