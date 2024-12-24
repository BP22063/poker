package org.example;

import java.util.*;

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
        // 1. カードの番号を取得してソート
        List<Integer> numbers = new ArrayList<>();
        for (Card card : stack) {
            numbers.add(card.getNumber());
        }
        Collections.sort(numbers); // 昇順ソート

        // 2. 通常のストレートを判定
        boolean isStraight = true;
        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i + 1) != numbers.get(i) + 1) {
                isStraight = false;
                break;
            }
        }

        // 3. 特殊ケース（A, 2, 3, 4, 5）を判定
        boolean isLowStraight = numbers.contains(1) &&
                numbers.contains(2) &&
                numbers.contains(3) &&
                numbers.contains(4) &&
                numbers.contains(5);

        // 4. ストレートであれば1を返す
        if (isStraight || isLowStraight) {
            return 1;
        }

        // ストレートでなければ0を返す
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

    private int judgeStraightFlush(ArrayList<Card> stack) {
        return 0;
    }

    private int judgeRoyalStraightFlush(ArrayList<Card> stack) {
        return 0;
    }
}
