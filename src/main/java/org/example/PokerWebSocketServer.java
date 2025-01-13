package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
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
        JsonObject json = gson.fromJson(message, JsonObject.class);
        String action = json.get("action").getAsString();

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
            case "playerActionAgain":

            default:
                System.out.println("Unknown action: " + action);
        }
    }

    @OnError
    public void onError(jakarta.websocket.Session session, Throwable error) {
        System.out.println("[WebSocketServerSample] onError:" + session.getId());
    }



    private void handleRegistration(Session session, JsonObject json) {
        String name = json.get("name").getAsString();
        int userID = json.get("useID").getAsInt();
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
        Object[] args;

        switch (skillID) {
            case 0:
                // No additional arguments needed for skillID 0
                args = new Object[]{};
                break;
            case 1:
                // For skillID 1, "disableHand" is needed
                String disableHand = json.get("disableHand").getAsString();
                args = new Object[]{disableHand};
                break;
            case 2:
                // For skillID 2, "cardIndexList" is needed
                JsonArray jsonArray = json.get("cardIndexList").getAsJsonArray();
                List<Integer> cardIndexList = new ArrayList<>();
                for (JsonElement element : jsonArray) {
                    cardIndexList.add(element.getAsInt());
                }
                args = new Object[]{cardIndexList};
                break;
            case 3:
                // For skillID 3, "swapPlayerID" is needed
                int swapPlayerID = json.get("swapPlayerID").getAsInt();
                args = new Object[]{swapPlayerID};
                break;
            default:
                throw new IllegalArgumentException("Unknown skillID: " + skillID);
        }

        game.handleSkillUse(userID, args);
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

    public void broadcastGameStateWithDetails(GameState phase) {
        JsonObject gameState = new JsonObject();

        // フェーズ情報
        gameState.addProperty("phase", phase.toString());

        // 全プレイヤーの名前とIDと配置位置の情報
        JsonArray playersArray = new JsonArray();
        for (Player player : game.getPlayers()) {
            JsonObject playerJson = new JsonObject();
            playerJson.addProperty("name", player.getName());
            playerJson.addProperty("id", player.getUserID());
            playerJson.addProperty("position",player.getPotision());
            playersArray.add(playerJson);
        }
        gameState.add("players", playersArray);

        // 他プレイヤーの情報
        JsonArray othersArray = new JsonArray();
        for (Player player : game.getPlayers()) {
            if (player != game.getCurrentPlayer()) {
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
        Player currentPlayer = game.getCurrentPlayer();
        JsonObject selfJson = new JsonObject();
        selfJson.addProperty("id", currentPlayer.getUserID());
        selfJson.addProperty("chips", currentPlayer.getHaveChip());
        JsonArray selfSkillsArray = new JsonArray();
        for (Integer skill : currentPlayer.getSkills()) {
            selfSkillsArray.add(skill);
        }
        selfJson.add("skills", selfSkillsArray);
        JsonArray handArray = new JsonArray();
        for (Card card : currentPlayer.getHand()) {
            handArray.add(card.toString()); // カードの情報を文字列化
        }
        selfJson.add("hand", handArray);
        gameState.add("self", selfJson);

        // 全プレイヤーに送信
        broadcast("gameStateUpdate", gameState.toString());
    }


    public void broadcastPlayersData() {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        // Create a list of all players from the map
        List<Player> players = new ArrayList<>(playerSessions.values());

        // Convert players list to JSON
        jsonObject.add("players", gson.toJsonTree(players));
        String playersJson = gson.toJson(jsonObject);

        // Send the JSON to all sessions
        for (Session session : playerSessions.keySet()) {
            try {
                session.getBasicRemote().sendText(playersJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void updatePlayerData(Session session, Player updatedPlayer) {
        playerSessions.put(session, updatedPlayer);
    }

    public void broadcastGameState(GameState state) {
        broadcast("gameStateUpdate", "Current phase: " + state.toString());
    }

    public void notifyActionResult(Player player, String result) {
        broadcast("actionResult", player.getName() + " " + result);
    }

}


