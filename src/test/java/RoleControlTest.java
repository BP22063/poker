import org.example.Card;
import org.example.RoleControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoleControlTest {

    RoleControl roleControl;

    @BeforeEach
    void before(){
        this.roleControl = new RoleControl();
    }

    @Test
    void test_high_card(){

        // ハイカード
        List<Card> highCard = Arrays.asList(
                new Card("Spades", 2),
                new Card("Hearts", 5),
                new Card("Clubs", 7),
                new Card("Diamonds", 10),
                new Card("Spades", 13) // King
        );

        int score = this.roleControl.judgeRole(highCard);
        assertThat(score).isEqualTo(1);

    }

}
