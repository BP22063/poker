package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoleControl {
    private Card card;

    public String judgeRole(ArrayList<Card> stack) {
        return null;
    }

    private int judge1pair(ArrayList<Card> stack) {
        Map<Integer, Integer> numberCounts = new HashMap<>();

        // カードの番号をカウント
        for (Card card : stack) {
            int number = card.getNumber();
            numberCounts.put(number, numberCounts.getOrDefault(number, 0) + 1);
            //numberCountsは1つ目に数字の種類、2つ目にその数字が出た回数を記録する
        }

        // 番号のカウントを確認して、2が1つ以上あれば1ペア
        for (int count : numberCounts.values()) {
            if (count == 2) {
                return 1; // 1ペア
            }
        }

        return 0; // 1ペアではない
    }

    private int judge2pair(ArrayList<Card> stack) {
        // カードの番号を記録するためのマップを作成
        Map<Integer, Integer> numberCounts = new HashMap<>();

        // カードの番号をカウント
        for (Card card : stack) {
            int number = card.getNumber();
            numberCounts.put(number, numberCounts.getOrDefault(number, 0) + 1);
        }

        // 出現回数が2の番号をカウント
        int pairCount = 0;
        for (int count : numberCounts.values()) {
            if (count == 2) {
                pairCount++;
            }
        }

        //ペアの回数が2回なら1を返す
        if (pairCount == 2) {
            return 1;//2ペア
        } else {
            return 0;
        }

    }

    private int judge3cards(ArrayList<Card> stack) {
        Map<Integer, Integer> numberCounts = new HashMap<>();

        // カードの番号をカウント
        for (Card card : stack) {
            int number = card.getNumber();
            numberCounts.put(number, numberCounts.getOrDefault(number, 0) + 1);
            //numberCountsは1つ目に数字の種類、2つ目にその数字が出た回数を記録する
        }

        // 番号のカウントを確認して、3が1つ以上あればスリーカード
        for (int count : numberCounts.values()) {
            if (count == 3) {
                return 1; // スリーカード
            }
        }

        return 0; // スリーカードではない
    }

    private int judgeStraight(ArrayList<Card> stack) {
        return 0;
    }

    private int judgeStraightFlush(ArrayList<Card> stack) {
        return 0;
    }

    private int judgeFlush(ArrayList<Card> stack) {
        return 0;
    }

    private int judgeFullHouse(ArrayList<Card> stack) {
        return 0;
    }

    private int judge4cards(ArrayList<Card> stack) {
        Map<Integer, Integer> numberCounts = new HashMap<>();

        // カードの番号をカウント
        for (Card card : stack) {
            int number = card.getNumber();
            numberCounts.put(number, numberCounts.getOrDefault(number, 0) + 1);
            //numberCountsは1つ目に数字の種類、2つ目にその数字が出た回数を記録する
        }

        // 番号のカウントを確認して、4が1つ以上あればフォーカード
        for (int count : numberCounts.values()) {
            if (count == 4) {
                return 1; // フォーカード
            }
        }

        return 0; // フォーカードではない
    }

    private int judgeRoyalStraightFlush(ArrayList<Card> stack) {
        return 0;
    }
}
