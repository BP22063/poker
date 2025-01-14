import org.example.Card;
import org.example.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    Player player;

    @BeforeEach
    void before(){

        this.player = new Player(1,"player1");

    }

    // 初期値確認（getterのテスト）
    @Test
    void test_initialState(){

        // 初期化されているフィールドのみ
        assertThat(player.getUserID()).isEqualTo(1);
        assertThat(player.getName()).isEqualTo("player1");
        assertThat(player.getHand()).isEmpty();
        assertThat(player.getHaveChip()).isEqualTo(3000);
        assertThat(player.getBetChip()).isEqualTo(0);
        assertThat(player.getSkills().size()).isEqualTo(3);

        // スキルリストの要素が0~3であることの確認
        for (Integer skill : player.getSkills()){
            assertThat(skill >= 0 || skill <=3).isTrue();
        }
    }

    // setter,getter
    @Test
    void test_setterAndGetter(){

        player.setHaveChip(2000);
        assertThat(player.getHaveChip()).isEqualTo(2000);

        player.setBetChip(123);
        assertThat(player.getBetChip()).isEqualTo(123);

        player.setRank(2);
        assertThat(player.getRank()).isEqualTo(2);

        player.setRolePoint(6);
        assertThat(player.getRolePoint()).isEqualTo(6);

        player.setIsInRound(true);
        assertThat(player.getIsInRound()).isTrue();
        player.setIsInRound(false);
        assertThat(player.getIsInRound()).isFalse();

        player.setPosition(3);
        assertThat(player.getPosition()).isEqualTo(3);

    }

    // スキルリストの挙動
    @Test
    void test_skills(){

        player.addSkill(3);
        assertThat(player.getSkills().get(3)).isEqualTo(3);
        player.addSkill(0);
        assertThat(player.getSkills().get(4)).isEqualTo(0);

        int skill0 = Collections.frequency(player.getSkills(),Integer.valueOf(0));
        int skill1 = Collections.frequency(player.getSkills(),Integer.valueOf(1));
        int skill2 = Collections.frequency(player.getSkills(),Integer.valueOf(2));
        int skill3 = Collections.frequency(player.getSkills(),Integer.valueOf(3));

        player.removeSkill(0);
        assertThat(Collections.frequency(player.getSkills(),0) < skill0).isTrue();
        player.removeSkill(1);
        assertThat(Collections.frequency(player.getSkills(),1) <= skill1).isTrue();
        player.removeSkill(2);
        assertThat(Collections.frequency(player.getSkills(),2) <= skill2).isTrue();
        player.removeSkill(3);
        assertThat(Collections.frequency(player.getSkills(),3) < skill3).isTrue();

    }

    // 手札の挙動
    @Test
    void test_hand(){

        // 手札登録
        Card S2 = new Card("Spades", 2);
        Card H5 = new Card("Hearts", 5);
        Card C7 = new Card("Clubs", 7);
        Card D10 = new Card("Diamonds", 10);
        Card SK = new Card("Spades", 13);

        player.addCard(S2);
        player.addCard(H5);
        player.addCard(C7);
        player.addCard(D10);
        player.addCard(SK);

        assertThat(player.getHand().size()).isEqualTo(5);

        assertThat(player.getHand().get(0).toString()).isEqualTo("2 of Spades");
        assertThat(player.getHand().get(1).toString()).isEqualTo("5 of Hearts");
        assertThat(player.getHand().get(2).toString()).isEqualTo("7 of Clubs");
        assertThat(player.getHand().get(3).toString()).isEqualTo("10 of Diamonds");
        assertThat(player.getHand().get(4).toString()).isEqualTo("K of Spades");

        assertThat(player.getHandAsString()).isEqualTo("[2 of Spades, 5 of Hearts, 7 of Clubs, 10 of Diamonds, K of Spades]");

        // 手札交換
        Card HA = new Card("Hearts",1);
        Card D6 = new Card("Diamonds",6);

        // - 交換されない
        player.exchangeCard_player(6,HA);
        assertThat(player.getHand().get(0).toString()).isEqualTo("2 of Spades");
        assertThat(player.getHand().get(1).toString()).isEqualTo("5 of Hearts");
        assertThat(player.getHand().get(2).toString()).isEqualTo("7 of Clubs");
        assertThat(player.getHand().get(3).toString()).isEqualTo("10 of Diamonds");
        assertThat(player.getHand().get(4).toString()).isEqualTo("K of Spades");

        // - 交換されない
        player.exchangeCard_player(-2,HA);
        assertThat(player.getHand().get(0).toString()).isEqualTo("2 of Spades");
        assertThat(player.getHand().get(1).toString()).isEqualTo("5 of Hearts");
        assertThat(player.getHand().get(2).toString()).isEqualTo("7 of Clubs");
        assertThat(player.getHand().get(3).toString()).isEqualTo("10 of Diamonds");
        assertThat(player.getHand().get(4).toString()).isEqualTo("K of Spades");

        // - 0番目を交換
        player.exchangeCard_player(0,HA);
        assertThat(player.getHand().get(0).toString()).isEqualTo("A of Hearts");
        assertThat(player.getHand().get(1).toString()).isEqualTo("5 of Hearts");
        assertThat(player.getHand().get(2).toString()).isEqualTo("7 of Clubs");
        assertThat(player.getHand().get(3).toString()).isEqualTo("10 of Diamonds");
        assertThat(player.getHand().get(4).toString()).isEqualTo("K of Spades");

        // - 3番目を交換
        player.exchangeCard_player(3,D6);
        assertThat(player.getHand().get(0).toString()).isEqualTo("A of Hearts");
        assertThat(player.getHand().get(1).toString()).isEqualTo("5 of Hearts");
        assertThat(player.getHand().get(2).toString()).isEqualTo("7 of Clubs");
        assertThat(player.getHand().get(3).toString()).isEqualTo("6 of Diamonds");
        assertThat(player.getHand().get(4).toString()).isEqualTo("K of Spades");

        player.clearCard();
        assertThat(player.getHand()).isEmpty();

    }

    // チップ数の挙動
    @Test
    void test_chips(){

        // haveChip
        assertThat(player.getHaveChip()).isEqualTo(3000);

        player.setHaveChip(2000);
        assertThat(player.getHaveChip()).isEqualTo(2000);

        player.addChips(50);
        assertThat(player.getHaveChip()).isEqualTo(2050);

        // betChip
        assertThat(player.getBetChip()).isEqualTo(0);

        player.setBetChip(2000);
        assertThat(player.getBetChip()).isEqualTo(2000);

    }

}
