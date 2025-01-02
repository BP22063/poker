package org.example;

import org.example.Skills.Skill_disableHand;
import org.example.Skills.Skill_disableSkill;
import org.example.Skills.Skill_exchangingHandsAgain;
import org.example.Skills.Skill_handSwap;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Dealer {

    private static final int INITIAL_BET_CHIP = 1; // 初期ベットチップ数

    private ArrayList<Player> players;
    public int fieldBetChip;
    private Deck deck;
    private ArrayList<Player> rankList;
    private ArrayList<String> disableHands;
    private ArrayList<Integer> usedSkills; // ラウンド内で選択されたスキルのID
    private ArrayList<Object> skills; // ラウンド内で使用されるスキルのインスタンスを保持
    ArrayList<Player> winners;
    private Action action;

    private static final int SKILL_NUM = 3; // 一人当たりのスキル配布数
    private static final int SKILL_KIND_NUM = 4; // スキルの種類数

    private ArrayList<Skill_disableSkill> skills_disableSkill;
    private ArrayList<Skill_handSwap>  skills_handSwap ;
    private ArrayList<Skill_disableHand> skills_disableHand;
    private ArrayList<Skill_exchangingHandsAgain> skills_exchangingHandsAgain;

    public Dealer() {
        players = new ArrayList<>();
        deck = new Deck();
        deck.shuffle();
        action = new Action(this);
    }

    public void decideOrder() {
        players.add(players.remove(0));
    }

    // 各プレイヤーにスキルを配布
    // 配布されるスキルはランダム
    public void provideSkill() {

        Random random = new Random();

        for ( int i=0; i<SKILL_NUM; i++ ) {
            for (Player player : this.players) {
                player.addSkill(random.nextInt(Integer.valueOf(random.nextInt(SKILL_KIND_NUM))));
            }
        }
    }

    // 各プレイヤーに同数のチップを配布
    // ゲーム開始時に使用
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

    public void changeCard(int userID, int cardIndex) {

            Player player = getUserByID(userID);
            Card oldCard = player.hand.get(cardIndex);
            Card newCard = deck.draw();

            player.exchangeCard_player(cardIndex, newCard);
            deck.discard(oldCard);  // 捨て札に追加

    }

    // いらないかも（Playerのコンストラクタで初期チップを設定できるため）
    public void provideChip(Player players) {

    }

    // スキルの情報に応じてインスタンスを生成
    private void adaptSkill(){
        this.skills_disableSkill.add(new Skill_disableSkill());
    }

    private void adaptSkill(String disableHand){
        this.skills_disableHand.add(new Skill_disableHand(disableHand));
    }

    private void adaptSkill(Player player,ArrayList<Integer> cardIndexList){
        this.skills_exchangingHandsAgain.add(new Skill_exchangingHandsAgain(player,this,cardIndexList));
    }

    private void adaptSkill(Player player,Player swapPlayer){
        this.skills_handSwap.add(new Skill_handSwap(player,swapPlayer));
    }

    /*
    private void adaptSkill( int skillID, Player player, String disableHand , Player swapPlayer ) {

        switch (skillID){

            // Skill_disableSkill
            case 0:
                skills.add( new Skill_disableSkill() );
                break;

            // Skill_disableHand
            case 1:
                skills.add( new Skill_disableHand(disableHand) );
                break;

            // Skill_exchangingHandsAgain
            case 2:
                skills.add( new Skill_exchangingHandsAgain(player, this) );
                break;

            // Skill_handSwap
            case 3:
                skills.add( new Skill_handSwap(player,swapPlayer) );
                break;

            default:
                break;
        }

        player.removeSkill(skillID);

    }
     */

    // スキルのインスタンスを生成、リストを作成、実行
    public void useSkills() {
    }

    public void sendApplicationCommunication(String JSON) {

    }

    // actionNumberの値によってベット、パス、レイズ、コール、ドロップの操作を実行する
    // 0:ベット　1:パス　2:レイズ　3:コール　4:ドロップ
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

    //Skill_exchangingHandsAgain用
    public ArrayList<Player> getPlayers() {
        return players;
    }



}
