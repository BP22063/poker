package org.example;

import java.util.ArrayList;

// Shift を 2 回押して 'どこでも検索' ダイアログを開き、`show whitespaces` と入力して
// Enter キーを押します。これでコードに空白文字が表示されます。
public class Main {
    public static void main(String[] args) {

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

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, "Alice"));
        players.add(new Player(2, "Bob"));
        players.add(new Player(3, "Charlie"));
        players.add(new Player(4, "Daisy"));

        Dealer d1 = new Dealer(players);
        d1.collectInitialChip();

        d1.performAction(1,0,100);
        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
        System.out.println("fieldBetChip : "+d1.fieldBetChip);

        d1.performAction(2,2,200);
        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
        System.out.println("fieldBetChip : "+d1.fieldBetChip);

        d1.performAction(3,3,0);
        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
        System.out.println("fieldBetChip : "+d1.fieldBetChip);

        d1.performAction(4,4,0);
        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
        System.out.println("fieldBetChip : "+d1.fieldBetChip);

        d1.performAction(1,3,0);
        System.out.println("totalFieldBetChip : "+d1.totalFieldBetChip);
        System.out.println("fieldBetChip : "+d1.fieldBetChip);

        d1.dealInitialCards(5);


        d1.decideWinner();
        d1.showWinners();
        d1.showAllHands();
        d1.distributeBetChip();

        for(Player player : players){
            System.out.println("HaveChip : "+ player.getHaveChip());
        }

        /*
        Game game1 = new Game(players);

        game1.progressRound();

         */




    }
}