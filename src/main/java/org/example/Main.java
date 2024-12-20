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
    }
}