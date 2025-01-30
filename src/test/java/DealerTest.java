import org.example.Card;
import org.example.Dealer;
import org.example.Deck;
import org.example.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
public class DealerTest {

    @Test
    void startRoundTest(){
        //初期準備
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        Dealer d1 = new Dealer(players);

        System.out.println(d1.getPlayers());

        d1.showAllHands();

        assertThat(d1.getFieldBetChip()).isEqualTo(0);
        assertThat(d1.getTotalFieldBetChip()).isEqualTo(0);

        assertThat(d1.getUserByID(1)).isEqualTo(player1);
        assertThat(d1.getUserByID(2)).isEqualTo(player2);
        assertThat(d1.getUserByID(3)).isEqualTo(player3);
        assertThat(d1.getUserByID(4)).isEqualTo(player4);

        //初期チップ数が3000になっていることを確認
        for(Player p :players){
            assertThat(p.getHaveChip()).isEqualTo(3000);
        }

        //スキルの配布数が3つになっていることを確認
        for(Player p :players){
            assertThat(p.skill.size()).isEqualTo(3);
        }

        // 初期チップの回収
        d1.collectInitialChip();

        //初期チップを回収できているか確認
        for(Player p :players){
            assertThat(p.getHaveChip()).isEqualTo(2950);
        }
        assertThat(d1.getTotalFieldBetChip()).isEqualTo(200);

        //プレイヤーの手札の枚数が５枚になっているか確認
        for(Player p :players){
            assertThat(p.hand.size()).isEqualTo(5);
        }

        //山札52枚から異なるカードが４枚ずつ配られ、山札が残り32枚になっていることを確認
        assertThat(d1.deck.cards.size()).isEqualTo(52-5*4);
    }
    @Test
    void actionTest(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);



        Dealer d1 = new Dealer(players);
        assertThat(d1.collectInitialChip()).isEqualTo(true);



        //プレイヤー１がパスを選択
        d1.performAction(1,1,0);
        assertThat(d1.totalFieldBetChip).isEqualTo(200);
        assertThat(d1.getFieldBetChip()).isEqualTo(0);
        assertThat(player1.getBetChip()).isEqualTo(0);
        //プライヤー2が１００ベット
        d1.performAction(2,0,100);
        assertThat(d1.totalFieldBetChip).isEqualTo(300);
        assertThat(d1.fieldBetChip).isEqualTo(100);
        assertThat(player1.getBetChip()).isEqualTo(0);
        assertThat(player2.getBetChip()).isEqualTo(100);
        //プレイヤー３が３００にレイズ
        d1.performAction(3,2,200);
        assertThat(d1.totalFieldBetChip).isEqualTo(600);
        assertThat(d1.fieldBetChip).isEqualTo(300);
        assertThat(player1.getBetChip()).isEqualTo(0);
        assertThat(player2.getBetChip()).isEqualTo(100);
        assertThat(player3.getBetChip()).isEqualTo(300);
        //プレイヤー４がコール
        d1.performAction(4,3,0);
        assertThat(d1.totalFieldBetChip).isEqualTo(900);
        assertThat(d1.fieldBetChip).isEqualTo(300);
        assertThat(player1.getBetChip()).isEqualTo(0);
        assertThat(player2.getBetChip()).isEqualTo(100);
        assertThat(player3.getBetChip()).isEqualTo(300);
        assertThat(player4.getBetChip()).isEqualTo(300);
        //プレイヤー１が６００にレイズ
        d1.performAction(1,2,300);
        assertThat(d1.totalFieldBetChip).isEqualTo(1500);
        assertThat(d1.fieldBetChip).isEqualTo(600);
        assertThat(player1.getBetChip()).isEqualTo(600);
        assertThat(player2.getBetChip()).isEqualTo(100);
        assertThat(player3.getBetChip()).isEqualTo(300);
        assertThat(player4.getBetChip()).isEqualTo(300);
        //プレイヤー２がコール
        d1.performAction(2,3,0);
        assertThat(d1.totalFieldBetChip).isEqualTo(2000);
        assertThat(d1.fieldBetChip).isEqualTo(600);
        assertThat(player1.getBetChip()).isEqualTo(600);
        assertThat(player2.getBetChip()).isEqualTo(600);
        assertThat(player3.getBetChip()).isEqualTo(300);
        assertThat(player4.getBetChip()).isEqualTo(300);
        assertThat(players.size()).isEqualTo(4);
        //プレイヤー３がフォールド
        d1.performAction(3,4,0);
        assertThat(d1.totalFieldBetChip).isEqualTo(2000);
        assertThat(d1.fieldBetChip).isEqualTo(600);
        assertThat(player1.getBetChip()).isEqualTo(600);
        assertThat(player2.getBetChip()).isEqualTo(600);
        assertThat(player3.getBetChip()).isEqualTo(300);
        assertThat(player4.getBetChip()).isEqualTo(300);
        assertThat(d1.players.size()).isEqualTo(3);
        //プレイヤー４がコール
        d1.performAction(4,3,0);
        assertThat(d1.totalFieldBetChip).isEqualTo(2300);
        assertThat(d1.fieldBetChip).isEqualTo(600);
        assertThat(player1.getBetChip()).isEqualTo(600);
        assertThat(player2.getBetChip()).isEqualTo(600);
        assertThat(player3.getBetChip()).isEqualTo(300);
        assertThat(player4.getBetChip()).isEqualTo(600);
        assertThat(d1.players.size()).isEqualTo(3);
    }
    @Test
    void exchangeCardTest(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        ArrayList<Integer> exchangeCardIndex = new ArrayList<>(Arrays.asList(0,1,3));

        Dealer d1 = new Dealer(players);


        ArrayList<Card> Hand1 = new ArrayList<Card>(player1.hand);
        ArrayList<Card> Hand2 = new ArrayList<Card>(player2.hand);
        ArrayList<Card> Hand3 = new ArrayList<Card>(player3.hand);
        ArrayList<Card> Hand4 = new ArrayList<Card>(player4.hand);

        d1.changeHand(1,exchangeCardIndex);
        d1.changeHand(2,exchangeCardIndex);
        d1.changeCard(4,0);

        //プレイヤー１と２がカードを交換しカードが異なることを確認
        assertThat(Hand1).isNotEqualTo(player1.hand);
        assertThat(Hand2).isNotEqualTo(player2.hand);

        //プレイヤー３はカードを交換していないので同じ手札
        assertThat(Hand3).isEqualTo(player3.hand);

        assertThat(Hand4).isNotEqualTo(player4.hand);

        //３枚と３枚をカード交換した
        assertThat(d1.deck.cards.size()).isEqualTo(52-5*4-3-3-1);

    }

    @Test
    void skillTest1(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        for(Player player : players){
            player.skill.clear();
        }

        player1.skill.add(0);//
        player1.skill.add(1);
        player1.skill.add(2);
        player2.skill.add(1);//
        player2.skill.add(2);
        player2.skill.add(3);
        player3.skill.add(1);
        player3.skill.add(2);
        player3.skill.add(3);//
        player4.skill.add(1);
        player4.skill.add(2);//
        player4.skill.add(3);

        Dealer d1 = new Dealer(players);
        d1.collectInitialChip();

        for(Player p :players){
            assertThat(p.skill.size()).isEqualTo(3);
        }

        d1.adaptSkill(player1,0.5);
        d1.adaptSkill(player2,"1pair");
        d1.adaptSkill(player3);
        ArrayList<Integer> exchangeCardIndex = new ArrayList<>(Arrays.asList(0,1,3));
        d1.adaptSkill(player4,exchangeCardIndex);


        assertThat(d1.getSkills_disableSkill()).isNotEmpty();
        assertThat(d1.getSkills_disableHand()).isNotEmpty();
        assertThat(d1.getSkills_exchangingHandsAgain()).isNotEmpty();

        //スキル無効が発動されているので、他のスキルは発動されない
        ArrayList<Card> Hand4 = new ArrayList<Card>(player4.hand);
        ArrayList<Card> Hand2 = new ArrayList<Card>(player2.hand);
        d1.useSkills();
        assertThat(d1.getDisableHands()).isNotIn();
        assertThat(Hand4).isEqualTo(player4.hand);
        //assertThat(Hand2).isNotEqualTo(player3.hand);

        assertThat(player3.skill.size()).isEqualTo(3);

        //d1.showAllHands();
        //スキル無効がない場合
        d1.getSkills_disableSkill().clear();
        Hand4 = new ArrayList<Card>(player4.hand);
        Hand2 = new ArrayList<Card>(player2.hand);
        d1.useSkills();
        assertThat(d1.getDisableHands()).contains("1pair");
        assertThat(Hand4).isNotEqualTo(player4.hand);
        //assertThat(Hand2).isEqualTo(player3.hand);

        //d1.showAllHands();

    }

    @Test
    void skillTest2(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        for(Player player : players){
            player.skill.clear();
        }

        player1.skill.add(0);
        player1.skill.add(1);
        player1.skill.add(2);
        player2.skill.add(1);
        player2.skill.add(2);
        player2.skill.add(3);
        player3.skill.add(1);
        player3.skill.add(2);
        player3.skill.add(3);
        player4.skill.add(1);
        player4.skill.add(2);
        player4.skill.add(3);

        Dealer d1 = new Dealer(players);
        d1.collectInitialChip();

        for(Player p :players){
            assertThat(p.skill.size()).isEqualTo(3);
        }

        //役無効を複数追加する
        d1.adaptSkill(player1,"1pair");
        d1.adaptSkill(player2,"2pair");
        d1.adaptSkill(player3,"3cards");
        d1.adaptSkill(player4,"Straight");


        for(Player p :players) {
            assertThat(p.skill.size()).isEqualTo(2);
        }

        d1.showAllHands();
        d1.useSkills();
        //リストに追加されているか確認
        assertThat(d1.getDisableHands()).contains("1pair");
        assertThat(d1.getDisableHands()).contains("2pair");
        assertThat(d1.getDisableHands()).contains("3cards");
        assertThat(d1.getDisableHands()).contains("Straight");

        //無効になった役の得点は１点になっている
        d1.decideWinner();
        d1.showWinners();
    }

    @Test
    void skillTest3(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        Dealer d1 = new Dealer(players);
        d1.collectInitialChip();

        for(Player player : players){
            player.skill.clear();
        }
        //全員がもう一度手札交換を行う
        player1.skill.add(2);
        player2.skill.add(2);
        player3.skill.add(2);
        player4.skill.add(2);

        ArrayList<Integer> exchangeCardIndex = new ArrayList<>(Arrays.asList(0,1,3));
        d1.adaptSkill(player1,exchangeCardIndex);
        d1.adaptSkill(player2,exchangeCardIndex);
        d1.adaptSkill(player3,exchangeCardIndex);
        d1.adaptSkill(player4,exchangeCardIndex);

        ArrayList<Card> Hand1 = new ArrayList<>(player1.hand);
        ArrayList<Card> Hand2 = new ArrayList<>(player2.hand);
        ArrayList<Card> Hand3 = new ArrayList<>(player3.hand);
        ArrayList<Card> Hand4 = new ArrayList<>(player4.hand);

        d1.showAllHands();
        d1.useSkills();
        d1.showAllHands();

        //スキルを使用する前とカードが異なることを確認
        assertThat(Hand1).isNotEqualTo(player1.hand);
        assertThat(Hand2).isNotEqualTo(player2.hand);
        assertThat(Hand3).isNotEqualTo(player3.hand);
        assertThat(Hand4).isNotEqualTo(player4.hand);
    }

    @Test
    void skillTest4(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        Dealer d1 = new Dealer(players);
        d1.collectInitialChip();

        for(Player player : players){
            player.skill.clear();
        }
        player1.skill.add(3);
        player2.skill.add(3);
        player3.skill.add(3);
        player4.skill.add(3);

        //全員が他プレイヤーと手札を交換する
        d1.adaptSkill(player1,2);
        d1.adaptSkill(player2,3);
        d1.adaptSkill(player3,4);
        d1.adaptSkill(player4,1);

        assertThat(d1.getSkills_handSwap()).isNotEmpty();

        ArrayList<Card> Hand1 = new ArrayList<>(player1.hand);
        ArrayList<Card> Hand2 = new ArrayList<>(player2.hand);
        ArrayList<Card> Hand3 = new ArrayList<>(player3.hand);
        ArrayList<Card> Hand4 = new ArrayList<>(player4.hand);

        d1.showAllHands();
        d1.useSkills();
        d1.showAllHands();

        //交換した後の手札と一致するか確認
        assertThat(player1.hand).isEqualTo(Hand1);
        assertThat(player2.hand).isEqualTo(Hand3);
        assertThat(player3.hand).isEqualTo(Hand4);
        assertThat(player4.hand).isEqualTo(Hand2);
    }

    @Test
    void judgeTest(){


        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        Dealer d1 = new Dealer(players);
        d1.collectInitialChip();
        Deck deck = new Deck();

        for(Player player : players){
            player.hand.clear();
        }

        player1.hand.add(new Card("Hearts",1));
        player1.hand.add(new Card("Hearts",10));
        player1.hand.add(new Card("Hearts",11));
        player1.hand.add(new Card("Hearts",12));
        player1.hand.add(new Card("Hearts",13));

        for(int i=0 ; i<5 ; i++){
            player2.hand.add(deck.draw());
        }

        for(int i=0 ; i<5 ; i++){
            player3.hand.add(deck.draw());
        }

        for(int i=0 ; i<5 ; i++){
            player4.hand.add(deck.draw());
        }

        d1.decideWinner();
        //役判定と得点の計算が行われ、正しい得点をプレイヤーが獲得しているか確認
        assertThat(player1.getRolePoint()).isEqualTo(10);
        d1.showWinners();


    }


}
