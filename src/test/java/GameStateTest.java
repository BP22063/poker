import org.example.GameState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameStateTest {

    @Test
    void test_enumValues(){

        GameState[] states = GameState.values();

        assertThat(states.length).isEqualTo(7);
        assertThat(GameState.START).isEqualTo(GameState.valueOf("START"));
        assertThat(GameState.BET_PASS).isEqualTo(GameState.valueOf("BET_PASS"));
        assertThat(GameState.RAISE_CALL_FOLD).isEqualTo(GameState.valueOf("RAISE_CALL_FOLD"));
        assertThat(GameState.EXCHANGE_HAND).isEqualTo(GameState.valueOf("EXCHANGE_HAND"));
        assertThat(GameState.FINAL_BETTING).isEqualTo(GameState.valueOf("FINAL_BETTING"));
        assertThat(GameState.SELECT_SKILL).isEqualTo(GameState.valueOf("SELECT_SKILL"));
        assertThat(GameState.END).isEqualTo(GameState.valueOf("END"));

    }
}
