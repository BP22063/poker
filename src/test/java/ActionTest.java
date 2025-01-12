import org.example.Dealer;
import org.example.Action;
import org.example.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {

    private Dealer dealer;
    private Action action;
    private Player player;
    private ArrayList<Player> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        player = new Player(1, "Alice");
        player.setHaveChip(100); // 初期チップを100に設定
        players.add(player);

        dealer = new Dealer(players);
        dealer.fieldBetChip = 0;
        dealer.totalFieldBetChip = 0;
        action = new Action(dealer);
    }

    @Test
    void testExecuteBet_SufficientChips() {
        action.executeBet(player, 50);

        assertEquals(50, player.getHaveChip(), "Player should have 50 chips left.");
        assertEquals(50, player.getBetChip(), "Player's bet should be 50 chips.");
        assertEquals(50, dealer.fieldBetChip, "Field bet chip should be 50.");
        assertEquals(50, dealer.totalFieldBetChip, "Total field bet chip should be 50.");
    }

    @Test
    void testExecuteBet_InsufficientChips() {
        action.executeBet(player, 150);

        assertEquals(100, player.getHaveChip(), "Player should still have 100 chips (no bet made).");
        assertEquals(0, player.getBetChip(), "Player's bet should remain 0.");
        assertEquals(0, dealer.fieldBetChip, "Field bet chip should remain 0.");
        assertEquals(0, dealer.totalFieldBetChip, "Total field bet chip should remain 0.");
    }

    @Test
    void testExecuteRaise_SufficientChips() {
        action.executeBet(player, 30); // 最初のベット
        action.executeRaise(player, 30, 20); // レイズを実行

        assertEquals(50, player.getHaveChip(), "Player should have 50 chips left after raising.");
        assertEquals(50, player.getBetChip(), "Player's total bet should be 50 chips.");
        assertEquals(50, dealer.fieldBetChip, "Field bet chip should be 50.");
        assertEquals(80, dealer.totalFieldBetChip, "Total field bet chip should be 80.");
    }

    @Test
    void testExecuteRaise_InsufficientChips() {
        action.executeBet(player, 30); // 最初のベット
        action.executeRaise(player, 30, 80); // 所持チップを超えるレイズ

        assertEquals(70, player.getHaveChip(), "Player should still have 70 chips (no raise made).");
        assertEquals(30, player.getBetChip(), "Player's bet should remain 30 chips.");
        assertEquals(30, dealer.fieldBetChip, "Field bet chip should remain 30.");
        assertEquals(30, dealer.totalFieldBetChip, "Total field bet chip should remain 30.");
    }

    @Test
    void testExecuteCall_SufficientChips() {
        dealer.fieldBetChip = 50; // フィールドのベット額を設定
        player.setBetChip(20); // プレイヤーの現在のベット額
        action.executeCall(player);

        assertEquals(70, player.getHaveChip(), "Player should have 70 chips left after calling.");
        assertEquals(50, player.getBetChip(), "Player's bet should match the field bet.");
        assertEquals(50, dealer.fieldBetChip, "Field bet chip should be 50.");
        assertEquals(30, dealer.totalFieldBetChip, "Total field bet chip should be 30 (call amount).");
    }

    @Test
    void testExecuteCall_InsufficientChips() {
        dealer.fieldBetChip = 120; // フィールドのベット額を設定
        player.setBetChip(20); // プレイヤーの現在のベット額
        action.executeCall(player);

        assertEquals(100, player.getHaveChip(), "Player should still have 100 chips (no call made).");
        assertEquals(20, player.getBetChip(), "Player's bet should remain 20 chips.");
        assertEquals(120, dealer.fieldBetChip, "Field bet chip should remain 120.");
        assertEquals(0, dealer.totalFieldBetChip, "Total field bet chip should remain 0.");
    }

    @Test
    void testExecuteDrop() {
        action.executeDrop(player);

        assertFalse(player.isInRound(), "Player should no longer be in the round after dropping.");
    }

    @Test
    void testExecutePass() {
        // パスの動作は単にメッセージを表示するのみなので、例外やエラーが起きないことを確認
        assertDoesNotThrow(() -> action.executePass(), "Pass should not throw any exception.");
    }
}