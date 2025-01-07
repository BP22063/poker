package org.example;

import org.glassfish.tyrus.server.Server;

public class WebSocketServerLauncher {
    public static void main(String[] args) {
        Server server = new Server("localhost", 8080, "/ws", PokerWebSocketServer.class);

        try {
            server.start();
            System.out.println("WebSocket server started on ws://localhost:8080/ws");
            Thread.currentThread().join(); // サーバーを停止させない
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
