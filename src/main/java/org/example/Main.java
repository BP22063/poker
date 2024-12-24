package org.example;

// Shift を 2 回押して 'どこでも検索' ダイアログを開き、`show whitespaces` と入力して
// Enter キーを押します。これでコードに空白文字が表示されます。
public class Main {
    public static void main(String[] args) {
        // ハイライトされたテキストにキャレットがある状態で Alt+Enter を押して
        // IntelliJ IDEA が提案する修正方法を表示します。
        System.out.printf("Hello and welcome!");

        // コードを実行するには Shift+F10 を押すか、ガターにある緑の矢印ボタンをクリックします。
        for (int i = 1; i <= 5; i++) {

            // Shift+F9 を押してコードのデバッグを開始します。ブレークポイントを 1 つ設定しましたが、
            // Ctrl+F8 を押すといつでも他のブレークポイントを追加できます。
            System.out.println("i = " + i);
        }


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
        dealer.changeCard(0, 1);
        dealer.showAllHands();

        System.out.println("勝者を決定");
        dealer.decideWinner();
        System.out.println("勝者：");
        dealer.showWinners();
        dealer.showAllHands();
    }
}