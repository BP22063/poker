package org.example;

import org.example.Skills.Skill_disableHand;
import org.example.Skills.Skill_disableSkill;
import org.example.Skills.Skill_exchangingHandsAgain;
import org.example.Skills.Skill_handSwap;

import java.util.*;
import java.util.stream.Collectors;

public class Dealer {

    private static final int INITIAL_BET_CHIP = 1; // 初期ベットチップ数

    private ArrayList<Player> players;
    public int fieldBetChip;
    private Deck deck;
    private ArrayList<Player> rankList;
    private ArrayList<String> disableHands;
    private ArrayList<Integer> usedSkills; // ラウンド内で選択されたスキルのID
    private ArrayList<Object> skills; // ラウンド内で使用されるスキルのインスタンスを保持
    private ArrayList<Integer>exchangeCardIndex;
    ArrayList<Player> winners;
    private Action action;

    private static final int SKILL_NUM = 3; // 一人当たりのスキル配布数
    private static final int SKILL_KIND_NUM = 4; // スキルの種類数

    // 使用される各スキルを格納
    private ArrayList<Skill_disableSkill> skills_disableSkill;
    private ArrayList<Skill_disableHand> skills_disableHand;
    private ArrayList<Skill_exchangingHandsAgain> skills_exchangingHandsAgain;
    private ArrayList<Skill_handSwap>  skills_handSwap ;

    public Dealer(ArrayList<Player> users) {
        players = new ArrayList<>();
        for(Player player : users ){
            addPlayer(player);
        }
        deck = new Deck();
        deck.shuffle();
        action = new Action(this);

        skills_disableSkill = new ArrayList<>();
        skills_disableHand = new ArrayList<>();
        skills_exchangingHandsAgain = new ArrayList<>();
        skills_handSwap = new ArrayList<>();
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

    //1枚ずつカード交換
    public void changeCard(int userID, int cardIndex) {

            Player player = getUserByID(userID);
            Card oldCard = player.hand.get(cardIndex);
            Card newCard = deck.draw();

            player.exchangeCard_player(cardIndex, newCard);
            deck.discard(oldCard);  // 捨て札に追加
    }
    //手札の中から交換したいカードを交換
    public void changeHand(int userID,ArrayList<Integer> exchangeCardIndex){
        for(int index:exchangeCardIndex){
            changeCard(userID,index);
        }
    }

    //プレイヤー4人がカードを交換


    // いらないかも（Playerのコンストラクタで初期チップを設定できるため）
    public void provideChip(Player players) {

    }

    public void adaptSkill(Player player, Object... args) {
        if (args.length == 0) {
            adaptSkill(player);
        } else if (args.length == 1) {
            if (args[0] instanceof String) {
                adaptSkill(player, (String) args[0]);
            } else if (args[0] instanceof Integer) {
                adaptSkill(player, (Integer) args[0]);
            }
        } else if (args.length == 1 && args[0] instanceof ArrayList) {
            adaptSkill(player, (ArrayList<Integer>) args[0]);
        } else {
            throw new IllegalArgumentException("Invalid arguments for adaptSkill method");
        }
    }

    // スキルの情報に応じてインスタンスを生成
    private void adaptSkill(Player player){
        player.removeSkill(0);
        this.skills_disableSkill.add(new Skill_disableSkill());
    }

    private void adaptSkill(Player player,String disableHand){
        player.removeSkill(1);
        this.skills_disableHand.add(new Skill_disableHand(disableHand));
    }

    private void adaptSkill(Player player,ArrayList<Integer> cardIndexList){
        player.removeSkill(2);
        this.skills_exchangingHandsAgain.add(new Skill_exchangingHandsAgain(player,this,cardIndexList));
    }

    private void adaptSkill(Player player,int opponent){
        player.removeSkill(3);
        this.skills_handSwap.add(new Skill_handSwap(player,getUserByID(opponent)));
    }

    // スキルを実行
    public void useSkills() {

        // この時点でスキルリスト作成,adaptSkillが完了している

        // スキル無効が選択されていない
        if(skills_disableSkill.isEmpty()){

            // 手札無効
            if(!skills_disableHand.isEmpty()){
                for ( Skill_disableHand skill : skills_disableHand ){
                    skill.useSkill_disableHand(); // 不要
                    this.disableHands.add(skill.getDisableHand());
                }
            }

            // 再度手札交換
            if(!skills_exchangingHandsAgain.isEmpty()){
                for ( Skill_exchangingHandsAgain skill : skills_exchangingHandsAgain ){
                    skill.useSkill_exchangingHandsAgain();
                }
            }

            // 他プレイヤーと手札交換
            if(!skills_handSwap.isEmpty()){
                for ( Skill_handSwap skill : skills_handSwap ){
                    skill.useSkill_handSwap();
                }
            }
        }
    }

    public void sendApplicationCommunication(String JSON) {

    }

    // actionNumberの値によってベット、パス、レイズ、コール、ドロップの操作を実行する
    // 0:ベット　1:パス　2:レイズ　3:コール　4:ドロップ
    public void performAction(int userID, int actionNumber, int betChip) {
        Player player = getUserByID(userID);

        switch (actionNumber) {
            case 0: // ベット
                action.executeBet(player, betChip);
                break;
            case 1: // パス
                action.executePass();
                break;
            case 2: // レイズ
                int raiseAmount = 10; // 仮の値
                action.executeRaise(player, betChip, raiseAmount);
                break;
            case 3: // コール
                action.executeCall(player);
                break;
            case 4: // フォールド
                action.executeDrop(player);
                break;
            default:
                break;
        }
    }


    public void executeActions(List<Map<String, Object>> actionRequests){
        for(Map<String,Object>request : actionRequests) {
            int userID = ((Double) request.get("userID")).intValue();
            int actionNumber = ((Double) request.get("actionNumber")).intValue();
            int betChip = ((Double) request.get("betChip")).intValue();
            performAction(userID, actionNumber, betChip);
        }
    }

    // ユーザIDを対応するユーザに変換
    public Player getUserByID(int userID){

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


            if(disableHands != null) {
                // 無効役判定
                for (String disableHand : this.disableHands) {
                    if (player.getRolePoint() == getRolePoint(disableHand)) {
                        player.setRolePoint(0);
                    }
                }
            }
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

    // 役名→ポイント数（無効役は考慮しない）
    public int getRolePoint(String roleName){

        switch(roleName){
            case "RoyalStraightFlush":
                return 10;
            case "StraightFlush":
                return 9;
            case "4cards":
                return 8;
            case "FullHouse":
                return 7;
            case "Flush":
                return 6;
            case "Straight":
                return 5;
            case "3cards":
                return 4;
            case "2pair":
                return 3;
            case "1pair":
                return 2;
            case "high card":
                return 1;
        }

        return 0;
    }

    //Skill_exchangingHandsAgain用
    public ArrayList<Player> getPlayers() {
        return players;
    }



}
