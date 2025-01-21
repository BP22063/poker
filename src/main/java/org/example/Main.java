package org.example;

import java.util.ArrayList;
import java.sql.*;

// Shift を 2 回押して 'どこでも検索' ダイアログを開き、`show whitespaces` と入力して
// Enter キーを押します。これでコードに空白文字が表示されます。
public class Main {
    public static void main(String[] args) throws SQLException {

        /*
        //テスト用コード
        Dealer dealer = new Dealer();

        // 4人のプレイヤーを作成して追加
        dealer.addPlayer(new Player(1, "Alice"));
        dealer.addPlayer(new Player(2, "Bob"));
        dealer.addPlayer(new Player(3, "Charlie"));
        dealer.addPlayer(new Player(4, "Daisy"));

        System.out.println("-- Game 1 --");
        System.out.println("Dealer: " + dealer.getCurrentDealer().getName());
        dealer.dealInitialCards(5);

        dealer.showAllHands();

        System.out.println("\n-- Aliceが2枚目を交換 --");
        dealer.changeCard(1, 1);
        dealer.showAllHands();



        System.out.println("勝者を決定");
        dealer.decideWinner();
        dealer.showWinners();
        dealer.showAllHands();

         */

//        ArrayList<Player> players = new ArrayList<>();
//        players.add(new Player(1, "Alice"));
//        players.add(new Player(2, "Bob"));
//        players.add(new Player(3, "Charlie"));
//        players.add(new Player(4, "Daisy"));
//
//        System.out.println(players.get(0).skill);
//        System.out.println(players.get(1).skill);
//        System.out.println(players.get(2).skill);
//        System.out.println(players.get(3).skill);
//
//        Dealer d1 = new Dealer(players);
//        d1.collectInitialChip();
//
//        d1.performAction(1,0,100);
//        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
//        System.out.println("fieldBetChip : "+d1.fieldBetChip);
//
//        d1.performAction(2,2,200);
//        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
//        System.out.println("fieldBetChip : "+d1.fieldBetChip);
//
//        d1.performAction(3,3,0);
//        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
//        System.out.println("fieldBetChip : "+d1.fieldBetChip);
//        d1.showAllHands();
//
//        d1.performAction(4,4,0);
//        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
//        System.out.println("fieldBetChip : "+d1.fieldBetChip);
//        d1.showAllHands();
//
//        d1.performAction(1,3,0);
//        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
//        System.out.println("fieldBetChip : "+d1.fieldBetChip);
//
//
//
//        d1.decideWinner();
//        d1.showWinners();
//        d1.showAllHands();
//        d1.distributeBetChip();
//
//        for(Player player : players){
//            System.out.println("HaveChip : "+ player.getHaveChip());
//        }

        /*
        Game game1 = new Game(players);

        game1.progressRound();

         */

        // DBテストコード
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, "Alice"));
        players.add(new Player(2, "Bob"));
        players.add(new Player(3, "Charlie"));
        players.add(new Player(1000, "Daisy"));
        Game game = new Game(players);

        game.saveGameResult(2, "test1", 1);
//
//
        String URL = "jdbc:mysql://sql.yamazaki.se.shibaura-it.ac.jp:13308/db_group_a"
                + "?useUnicode=true&character_set_server=utf8mb4&useSSL=false";
        Connection connection = DriverManager.getConnection(URL, "group_a", "group_a");
        Statement stmt = connection.createStatement();
        String queryString = "show tables"; // テーブル一覧を取得
        ResultSet rs = stmt.executeQuery(queryString);

        while (rs.next()) {
            String tableName = rs.getString(1);
            System.out.println("Table: " + tableName);

            // 各テーブルのデータを取得
            Statement tableStmt = connection.createStatement();
            ResultSet tableRs = tableStmt.executeQuery("SELECT * FROM " + tableName);

            // メタデータを取得して列名を動的に表示
            ResultSetMetaData metaData = tableRs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (tableRs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + tableRs.getString(i) + " | ");
                }
                System.out.println();
            }
        }





    }
}