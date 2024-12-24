package org.example;

public class Card {
    private String mark;   // カードのマーク（スート）
    private int number;    // カードの番号（1〜13）

    // コンストラクタ
    public Card(String mark, int number) {
        this.mark = mark;
        this.number = number;
    }

    // マークの取得
    public String getMark() {
        return mark;
    }

    // 番号の取得
    public int getNumber() {
        return number;
    }

    // カードの文字列表現
    @Override
    public String toString() {
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        return ranks[number - 1] + " of " + mark;
    }
}
