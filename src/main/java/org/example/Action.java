package org.example;

public class Action {

    public void executeBet(Player player, int actionNumber, int betChip) {
        if (player.haveChip >= betChip) {
            player.haveChip -= betChip;
            player.betChip += betChip;
            System.out.println(player.getName() + " bets " + betChip + " chips.");
        } else {
            System.out.println(player.getName() + " does not have enough chips to bet.");
        }
    }

    public void executePass(Player player, int actionNumber, int betChip) {
        player.isInRound = false; // パスの場合もラウンドから除外
        System.out.println(player.getName() + " passes and is out of the round.");
    }

    public void executeRaise(Player player, int actionNumber, int betChip) {
        if (player.haveChip >= betChip) {
            player.haveChip -= betChip;
            player.betChip += betChip;
            System.out.println(player.getName() + " raises by " + betChip + " chips.");
        } else {
            System.out.println(player.getName() + " does not have enough chips to raise.");
        }
    }

    public void executeCall(Player player, int actionNumber, int betChip) {
        int callAmount = betChip - player.betChip; // 必要なコール額を計算
        if (player.haveChip >= callAmount) {
            player.haveChip -= callAmount;
            player.betChip += callAmount;
            System.out.println(player.getName() + " calls with " + callAmount + " chips.");
        } else {
            System.out.println(player.getName() + " does not have enough chips to call.");
        }
    }

    public void executeDrop(Player player, int actionNumber, int betChip) {
        player.isInRound = false; // ドロップでラウンドから除外
        System.out.println(player.getName() + " drops out of the round.");
    }
}
