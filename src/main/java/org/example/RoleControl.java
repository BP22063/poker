package org.example;

import java.util.*;

public class RoleControl {
    private Card card;

    public int judgeRole(ArrayList<Card> stack) {
        if(judgeRoyalStraightFlush(stack)==1){
            return 10;
        }else if(judgeStraightFlush(stack)==1){
            return 9;
        }else if(judge4cards(stack)==1){
            return 8;
        }else if(judgeFullHouse(stack)==1){
            return 7;
        }else if(judgeFlush(stack)==1){
            return 6;
        }else if(judgeStraight(stack)==1){
            return 5;
        }else if(judge3cards(stack)==1){
            return 4;
        }else if(judge2pair(stack)==1){
            return 3;
        }else if(judge1pair(stack)==1){
            return 2;
        }else{
            return 1;
        }
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
        boolean isHighStraight = numbers.contains(1) &&
                numbers.contains(10) &&
                numbers.contains(11) &&
                numbers.contains(12) &&
                numbers.contains(13);

        // 4. ストレートであれば1を返す
        if (isStraight || isHighStraight) {
            return 1;
        }

        // ストレートでなければ0を返す
        return 0;
    }

    private int judgeFlush(ArrayList<Card> stack) {
        // 1. 最初のカードのスートを取得
        String firstMark = stack.get(0).getMark();

        // 2. すべてのカードが同じスートかを確認
        for (Card card : stack) {
            if (!card.getMark().equals(firstMark)) {
                return 0; // 異なるスートがあればフラッシュではない
            }
        }

        // 3. 全て同じスートならフラッシュ
        return 1;
    }

    private int judgeFullHouse(ArrayList<Card> stack) {
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

        // 出現回数が3の番号をカウント
        int threeCount = 0;
        for (int count : numberCounts.values()) {
            if (count == 3) {
                threeCount++;
            }
        }

        //ペアの回数が1回かつスリーカードが1回なら1を返す
        if (pairCount == 1 && threeCount ==1) {
            return 1;//フルハウス
        } else {
            return 0;
        }
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
        // 1. フラッシュとストレートを判定
        int isFlush = judgeFlush(stack);
        int isStraight = judgeStraight(stack);

        // 2. 両方を満たす場合はストレートフラッシュ
        if (isFlush == 1 && isStraight == 1) {
            return 1; // ストレートフラッシュ
        }

        // 3. どちらかが満たされない場合
        return 0; // ストレートフラッシュではない
    }

    private int judgeRoyalStraightFlush(ArrayList<Card> stack) {
        boolean isFlush = false;
        if(judgeFlush(stack)==1){
            isFlush = true;
        }

        // 1. カードの番号を取得してソート
        List<Integer> numbers = new ArrayList<>();
        for (Card card : stack) {
            numbers.add(card.getNumber());
        }
        Collections.sort(numbers); // 昇順ソート

        // 3. 特殊ケース（A, 2, 3, 4, 5）を判定
        boolean isHighStraight = numbers.contains(1) &&
                numbers.contains(10) &&
                numbers.contains(11) &&
                numbers.contains(12) &&
                numbers.contains(13);

        if(isFlush && isHighStraight){
            return 1;
        }
        return 0;
    }
}
