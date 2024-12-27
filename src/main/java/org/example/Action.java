package org.example;

public class Action {

    public void executeBet(Player player, Dealer dealer, int betChip) {
        if (player.getHaveChip() >= betChip) {
            player.setHaveChip(player.getHaveChip() - betChip);
            player.setBetChip(betChip);
            dealer.fieldBetChip = betChip;
            System.out.println(player.getName() + " bets " + betChip + " chips.");
        } else {
            System.out.println(player.getName() + " does not have enough chips to bet.");
        }
    }

    public void executePass() {

    }

    public void executeRaise(Player player, Dealer dealer, int betChip, int raiseAmount) {
        int totalBet = betChip + raiseAmount;
        if (player.getHaveChip() >= totalBet) {
            player.setHaveChip(player.getHaveChip() - totalBet);
            player.setBetChip(totalBet);
            dealer.fieldBetChip = totalBet;
            System.out.println(player.getName() + " raises to " + totalBet + " chips.");
        } else {
            System.out.println(player.getName() + " does not have enough chips to raise.");
        }
    }

    public void executeCall(Player player, Dealer dealer) {
        int callAmount = dealer.fieldBetChip - player.getBetChip();
        if (player.getHaveChip() >= callAmount) {
            player.setBetChip(player.getHaveChip() - callAmount);
            player.setBetChip((dealer.fieldBetChip));
            System.out.println(player.getName() + " calls " + callAmount + " chips.");
        } else {
            System.out.println(player.getName() + " does not have enough chips to call.");
        }
    }

    public void executeDrop(Player player) {
        player.setisInRound(false);
        System.out.println(player.getName() + " folds.");
    }
}
