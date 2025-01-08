package org.example;

import org.glassfish.tyrus.server.Server;

public class WebSocketServerLauncher {

    static String contextRoot = "/poker";
    static String protocol = "ws";
    static int port = 8080;
    public static void main(String[] args) {
        Server server = new Server(protocol, port, contextRoot, null, PokerWebSocketServer.class);
        System.out.println("server: " + server);
        try {
            server.start();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }

}

