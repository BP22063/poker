package org.example;

import java.util.*;

public class RoleControl {

    public enum HandRank {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_STRAIGHT_FLUSH
    }

    //役を判定するメソッド
    public static HandRank judgeRole(List<Card> cards){
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Hand must contain exactly 5 cards.");
        }

        boolean isFlush = checkFlush(cards);
        boolean isStraight = checkStraight(cards);
        Map<Integer, Integer> rankCounts = countRanks(cards);

        if (isFlush && isStraight) {
            return HandRank.STRAIGHT_FLUSH;
        } else if (rankCounts.containsValue(4)) {
            return HandRank.FOUR_OF_A_KIND;
        } else if (rankCounts.containsValue(3) && rankCounts.containsValue(2)) {
            return HandRank.FULL_HOUSE;
        } else if (isFlush) {
            return HandRank.FLUSH;
        } else if (isStraight) {
            return HandRank.STRAIGHT;
        } else if (rankCounts.containsValue(3)) {
            return HandRank.THREE_OF_A_KIND;
        } else if (Collections.frequency(rankCounts.values(), 2) == 2) {
            return HandRank.TWO_PAIR;
        } else if (rankCounts.containsValue(2)) {
            return HandRank.ONE_PAIR;
        } else {
            return HandRank.HIGH_CARD;
        }
    }

    // ストレートの判定
    private static boolean checkStraight(List<Card> cards){
        List<Integer> numbers = new ArrayList<>();
        for (Card card : cards){
            numbers.add(card.getNumber());
        }
        Collections.sort(numbers);

        // 通常のストレート
        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i) + 1 != numbers.get(i + 1)) {
                // A, 2, 3, 4, 5の特別ケースをチェック
                if (numbers.equals(Arrays.asList(1, 10, 11, 12, 13))) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    //フラッシュの判定
    private static boolean checkFlush(List<Card> cards){
        String suit = cards.get(0).getMark();
        for (Card card : cards) {
            if (!card.getMark().equals(suit)){
                return false;
            }
        }
        return true;
    }

    // 重複枚数をカウント
    private static Map<Integer, Integer> countRanks(List<Card> cards){
        Map<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card : cards) {
            rankCounts.put(card.getNumber(), rankCounts.getOrDefault(card.getNumber(), 0) + 1);
        }
        return rankCounts;
    }

    // デバッグ用のメインメソッド
    public static void main(String[] args) {
        List<Card> hand = Arrays.asList(
                new Card("Hearts", 10),
                new Card("Hearts", 11),
                new Card("Hearts", 12),
                new Card("Hearts", 13),
                new Card("Hearts", 1)
        );

        HandRank rank = judgeRole(hand);
        System.out.println("Hand rank: " + rank);
    }
}
