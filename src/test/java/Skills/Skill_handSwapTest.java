package Skills;

import org.example.Card;
import org.example.Player;
import org.example.Skills.Skill_handSwap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Skill_handSwapTest {

    private Player player1;
    private Player player2;
    private Skill_handSwap skillHandSwap;

    @BeforeEach
    void setUp() {
        // プレイヤー1の手札を準備
        ArrayList<Card> hand1 = new ArrayList<>();
        hand1.add(new Card("Spades", 1)); // Ace of Spades
        hand1.add(new Card("Hearts", 13)); // King of Hearts

        // プレイヤー2の手札を準備
        ArrayList<Card> hand2 = new ArrayList<>();
        hand2.add(new Card("Clubs", 12)); // Queen of Clubs
        hand2.add(new Card("Diamonds", 11)); // Jack of Diamonds

        // プレイヤーオブジェクトを生成
        player1 = new Player(1, "Player1");
        player2 = new Player(2, "Player2");

        // プレイヤーに手札を設定
        player1.hand.addAll(hand1);
        player2.hand.addAll(hand2);

        // スキルオブジェクトを生成
        skillHandSwap = new Skill_handSwap(player1, player2);
    }

    @Test
    void testGetSkillID() {
        // スキルIDの取得が正しいことを確認
        assertEquals(3, skillHandSwap.getSkillID());
    }

    @Test
    void testUseSkill_handSwap() {
        // 事前の手札確認
        ArrayList<Card> initialHand1 = new ArrayList<>(player1.hand);
        ArrayList<Card> initialHand2 = new ArrayList<>(player2.hand);

        // スキルを使用
        skillHandSwap.useSkill_handSwap();

        // 手札が交換されていることを確認
        assertEquals(initialHand2, player1.hand, "Player1's hand should be swapped with Player2's hand.");
        assertEquals(initialHand1, player2.hand, "Player2's hand should be swapped with Player1's hand.");
    }

    @Test
    void testUseSkill_handSwap_WithEmptyHands() {
        // プレイヤー1とプレイヤー2の手札を空にする
        player1.clearCard();
        player2.clearCard();

        // スキルを使用
        skillHandSwap.useSkill_handSwap();

        // 手札が空のままであることを確認
        assertTrue(player1.hand.isEmpty(), "Player1's hand should remain empty after swap.");
        assertTrue(player2.hand.isEmpty(), "Player2's hand should remain empty after swap.");
    }

    @Test
    void testUseSkill_handSwap_HandIntegrity() {
        // 事前に手札の内容を取得
        ArrayList<Card> initialHand1 = new ArrayList<>(player1.hand);
        ArrayList<Card> initialHand2 = new ArrayList<>(player2.hand);

        // スキルを使用
        skillHandSwap.useSkill_handSwap();

        // 手札が正しく交換され、順序や内容が保持されていることを確認
        assertEquals(initialHand2.size(), player1.hand.size(), "Player1's hand size should match Player2's original hand size.");
        assertEquals(initialHand1.size(), player2.hand.size(), "Player2's hand size should match Player1's original hand size.");

        for (int i = 0; i < initialHand2.size(); i++) {
            assertEquals(initialHand2.get(i).toString(), player1.hand.get(i).toString(), "Card order and content should remain intact.");
        }
        for (int i = 0; i < initialHand1.size(); i++) {
            assertEquals(initialHand1.get(i).toString(), player2.hand.get(i).toString(), "Card order and content should remain intact.");
        }
    }
}
