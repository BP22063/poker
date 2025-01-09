package org.example;

import org.glassfish.tyrus.server.Server;

public class WebSocketServerLauncher {

    public static void main(String[] args) throws Exception {
        // サーバ設定
        String host = "localhost";
        int port = 8080;
        String contextRoot = "/";

        // Serverインスタンス作成
        Server server = new Server(host, port, contextRoot, null, PokerWebSocketServer.class);

        System.out.println("WebSocket server starting on ws://" + host + ":" + port + contextRoot + "sample");

        try {
            server.start();
            System.out.println("Press any key to stop the server...");
            System.in.read(); // ユーザーのキー入力を待機
        } finally {
            server.stop();
            System.out.println("WebSocket server stopped.");
        }
    }
}


