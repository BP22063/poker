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

        assertThat(player.getUserID()).isEqualTo(1);
        assertThat(player.getName()).isEqualTo("player1");
        assertThat(player.getHand()).isEmpty();
        assertThat(player.getHaveChip()).isEqualTo(3000);
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
        assertThat(Collections.frequency(player.getSkills(),1) <= skill0).isTrue();
        player.removeSkill(2);
        assertThat(Collections.frequency(player.getSkills(),2) <= skill0).isTrue();
        player.removeSkill(3);
        assertThat(Collections.frequency(player.getSkills(),3) < skill0).isTrue();

    }

    // 手札の挙動
    @Test
    void test_hand(){

    }

    // チップ数の挙動
    @Test
    void test_chips(){

    }

}
