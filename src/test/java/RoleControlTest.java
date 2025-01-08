import org.example.Card;
import org.example.RoleControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class RoleControlTest {

    RoleControl roleControl;

    @BeforeEach
    void before(){
        this.roleControl = new RoleControl();
    }

    @Test
    void test_highCard(){

        // ハイカード
        List<Card> hand = Arrays.asList(
                new Card("Spades", 2),
                new Card("Hearts", 5),
                new Card("Clubs", 7),
                new Card("Diamonds", 10),
                new Card("Spades", 13) // King
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(1);

    }

    @Test
    void test_onePair(){

        // ワンペア
        List<Card> hand = Arrays.asList(
                new Card("Diamonds", 8),
                new Card("Hearts", 8),
                new Card("Clubs", 3),
                new Card("Spades", 6),
                new Card("Hearts", 10)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(2);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Clubs", 3),
                new Card("Hearts", 8),
                new Card("Spades", 6),
                new Card("Diamonds", 8),
                new Card("Hearts", 10)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(2);

    }

    @Test
    void test_twoPair(){

        // ツーペア
        List<Card> hand = Arrays.asList(
                new Card("Spades", 5),
                new Card("Clubs", 5),
                new Card("Hearts", 7),
                new Card("Diamonds", 7),
                new Card("Clubs", 2)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(3);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Spades", 5),
                new Card("Diamonds", 7),
                new Card("Hearts", 7),
                new Card("Clubs", 2),
                new Card("Clubs", 5)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(3);

    }

    @Test
    void test_threeOfAKind(){

        // スリーカード
        List<Card> hand = Arrays.asList(
                new Card("Hearts", 4),
                new Card("Diamonds", 4),
                new Card("Clubs", 4),
                new Card("Spades", 2),
                new Card("Hearts", 9)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(4);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Spades", 2),
                new Card("Hearts", 4),
                new Card("Hearts", 9),
        new Card("Diamonds", 4),
                new Card("Clubs", 4)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(4);

    }

    @Test
    void test_straight(){

        // ストレート
        List<Card> hand = Arrays.asList(
                new Card("Clubs", 9),
                new Card("Diamonds", 10),
                new Card("Hearts", 11),
                new Card("Spades", 12),
                new Card("Clubs", 13)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(5);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Clubs", 13),
                new Card("Spades", 12),
                new Card("Diamonds", 10),
                new Card("Hearts", 11),
                new Card("Clubs", 9)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(5);

    }

    @Test
    void test_flush(){

        // フラッシュ
        List<Card> hand = Arrays.asList(
                new Card("Hearts", 2),
                new Card("Hearts", 4),
                new Card("Hearts", 6),
                new Card("Hearts", 8),
                new Card("Hearts", 10)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(6);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Hearts", 8),
                new Card("Hearts", 4),
                new Card("Hearts", 10),
                new Card("Hearts", 2),
                new Card("Hearts", 6)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(6);

    }

    @Test
    void test_fullHouse(){

        // フルハウス
        List<Card> hand = Arrays.asList(
                new Card("Clubs", 10),
                new Card("Diamonds", 10),
                new Card("Hearts", 10),
                new Card("Spades", 3),
                new Card("Clubs", 3)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(7);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Clubs", 3) ,
                new Card("Diamonds", 10),
                new Card("Clubs", 10),
                new Card("Spades", 3),
                new Card("Hearts", 10)
                );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(7);

    }

    @Test
    void test_fourOfAKind(){

        // フォーカード
        List<Card> hand = Arrays.asList(
                new Card("Diamonds", 7),
                new Card("Hearts", 7),
                new Card("Clubs", 7),
                new Card("Spades", 7),
                new Card("Hearts", 3)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(8);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Hearts", 3) ,
                new Card("Hearts", 7),
                new Card("Spades", 7),
                new Card("Diamonds", 7),
                new Card("Clubs", 7)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(8);

    }

    @Test
    void test_straightFlush(){

        // ストレートフラッシュ
        List<Card> hand = Arrays.asList(
                new Card("Spades", 5),
                new Card("Spades", 6),
                new Card("Spades", 7),
                new Card("Spades", 8),
                new Card("Spades", 9)
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(9);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Spades", 8),
                new Card("Spades", 5),
                new Card("Spades", 6),
                new Card("Spades", 9),
                new Card("Spades", 7)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(9);

    }

    @Test
    void test_royal(){

        // ストレートフラッシュ
        List<Card> hand = Arrays.asList(
                new Card("Hearts", 10),
                new Card("Hearts", 11),
                new Card("Hearts", 12),
                new Card("Hearts", 13),
                new Card("Hearts", 1) // Ace
        );

        int score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(10);

        // 並べ替え
        hand = Arrays.asList(
                new Card("Hearts", 13),
                new Card("Hearts", 11),
                new Card("Hearts", 10),
                new Card("Hearts", 1),// Ace
                new Card("Hearts", 12)
        );

        score = this.roleControl.judgeRole(hand);
        assertThat(score).isEqualTo(10);

    }

    // 例外
    @Test
    void test_handSizeError(){

        // 手札が6枚
        final List<Card> hand = Arrays.asList(
                new Card("Hearts", 10),
                new Card("Hearts", 11),
                new Card("Hearts", 12),
                new Card("Hearts", 13),
                new Card("Hearts", 12),
                new Card("Hearts", 1) // Ace
        );

        assertThatThrownBy(()->this.roleControl.judgeRole(hand)).isInstanceOf(Exception.class);

        // 手札が3枚
        final List<Card> hand2 = Arrays.asList(
                new Card("Hearts", 10),
                new Card("Hearts", 11),
                new Card("Hearts", 12)
        );

        assertThatThrownBy(()->this.roleControl.judgeRole(hand2)).isInstanceOf(Exception.class);

    }

}
