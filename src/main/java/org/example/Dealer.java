package org.example;

import java.util.ArrayList;

public class Dealer {

    private static final int INITIAL_BET_CHIP = 1; // 初期ベットチップ数

    private ArrayList<Player> players;
    public int fieldBetChip;
    private Deck deck;
    private ArrayList<Player> rankList;
    private ArrayList<String> disableHands;
    private ArrayList<Integer> usedSkills;
    ArrayList<Player> winners;
    private Action action;



    //private Skill_handSwap  skill_handSwap ;

    //private Skill_disableHand skill_disableHand;


    //private Skill_exchangingHandsAgain skill_exchangingHandsAgain;

    public Dealer() {
        players = new ArrayList<>();
        deck = new Deck();
        deck.shuffle();
        action = new Action(this);
    }

    public void decideOrder() {
        players.add(players.remove(0));
    }

    public void provideSkill(int Skill) {

    }

    public void collectInitialChip() {

        for( Player player : this.players ){

            // 最初のチップを払えない
            if(INITIAL_BET_CHIP > player.getHaveChip()){
                // 順位の決定
                this.rankList.add(player);
                this.players.remove(player);

            }else{
                player.setHaveChip( player.getHaveChip() - INITIAL_BET_CHIP );
            }
        }
    }

    public void provideCard() {
        for (int i = 0; i < 5; i++) {
            for (Player player : players) {
                player.addCard(deck.draw());
            }
        }
    }

    public void changeCard(int playerIndex, int cardIndex) {
        if (playerIndex >= 0 && playerIndex < players.size()) {
            Player player = players.get(playerIndex);
            Card oldCard = player.hand.get(cardIndex);
            Card newCard = deck.draw();

            player.exchangeCard_player(cardIndex, newCard);
            deck.discard(oldCard);  // 捨て札に追加
        }
    }

    // いらないかも（Playerのコンストラクタで初期チップを設定できるため）
    public void provideChip(Player players) {

    }



    private void adaptSkill() {

    }

    public void useSkills() {

    }

    public void sendApplicationCommunication(String JSON) {

    }

    public void performAction(int userID, int actionNumber, int betChip) {

        Player player = getUserByID(userID);

        switch (actionNumber){

            // bet
            case 0:
                this.action.executeBet(player,betChip);
                break;

            // pass
            case 1:
                this.action.executePass();
                break;

            // raise
            case 2:
                int raiseAmount = 0; // 要変更　ここでraiseの増加量の問い合わせをする？
                this.action.executeRaise(player,betChip,raiseAmount);
                break;

            // call
            case 3:
                this.action.executeCall(player);
                break;

            // drop
            case 4:
                this.action.executeDrop(player);
                break;

            default:
                break;
        }
    }

    // ユーザIDを対応するユーザに変換
    private Player getUserByID(int userID){

        for ( Player player : this.players ){
            if( userID == player.getUserID()){
                return player;
            }
        }

        for ( Player player : this.rankList ){
            if( userID == player.getUserID()){
                return player;
            }
        }

        return null;
    }

    public void sendCardInformation() {

    }


    public void addPlayer(Player player) {
        players.add(player);
    }

    public void dealInitialCards(int cardsPerPlayer) {
        for (Player player : players) {
            player.clearCard();
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.addCard(deck.draw());
            }
        }
    }
    public Player getCurrentDealer(){
        return players.get(0);
    }

    public void showAllHands() {
        for (Player player : players) {
            player.showHand_player();
        }
    }


    public void checkLostUser(ArrayList<Player> players, ArrayList<Player> rankList) {

    }


    public void addDisableHands(String hand) {

    }

    public void addUsedSkill(int skill) {

    }


    public void checkPlayersList(ArrayList<Player> players) {

    }

    public void decideWinner(){
        //List<Integer> point = new ArrayList<>();
        winners = new ArrayList<>();
        RoleControl roleControl = new RoleControl();
        int maxPoint=0;

        for(Player player: players){
            player.setRolePoint( roleControl.judgeRole(player.hand) );
        }

        for(Player player:players){
            if(player.getRolePoint() >maxPoint){
                maxPoint = player.getRolePoint();
            }
        }

        for (Player player : players){
            if(player.getRolePoint() == maxPoint){
                winners.add(player);
            }
        }

    }

    public void showWinners(){
        for(Player player:winners){
            System.out.println("プレイヤー："+player.getName()+" 役："+getRoleName(player.getRolePoint()));
        }
    }

    public String getRoleName(int point){
        String roleName=null;
        switch(point){
            case 10:
                roleName="RoyalStraightFlush";
                break;
            case 9:
                roleName="StraightFlush";
                break;
            case 8:
                roleName="4cards";
                break;
            case 7:
                roleName="FullHouse";
                break;
            case 6:
                roleName="Flush";
                break;
            case 5:
                roleName="Straight";
                break;
            case 4:
                roleName="3cards";
                break;
            case 3:
                roleName="2pair";
                break;
            case 2:
                roleName="1pair";
                break;
            case 1:
                roleName="high card";
                break;
        }
        return roleName;
    }




}
