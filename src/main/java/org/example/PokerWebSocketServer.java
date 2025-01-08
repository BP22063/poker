package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/poker")
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
            default:
                System.out.println("Unknown action: " + action);
        }
    }



    private void handleRegistration(Session session, JsonObject json) {
        String name = json.get("name").getAsString();
        Player player = new Player(playerSessions.size() + 1, name);
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
        int betChip = json.get("betChip").getAsInt();

        game.handleAction(userID, actionNumber, betChip);
    }

    private void handleChangeCards(JsonObject json) {
        int userID = json.get("userID").getAsInt();
        JsonArray jsonArray = json.get("exchangeCardIndex").getAsJsonArray();
        ArrayList<Integer> exchangeCardIndex = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            exchangeCardIndex.add(element.getAsInt());
        }

        game.handleChangeCards(userID,exchangeCardIndex);
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
}
