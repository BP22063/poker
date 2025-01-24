package Skills;

import org.example.Skills.Skill_disableSkill;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Skill_disableSkillTest {

    @Test
    void testGetSkillID() {
        Skill_disableSkill skill = new Skill_disableSkill();
        int expectedSkillID = 0; // クラス内で定義されたスキルID
        int actualSkillID = skill.getSkillID();

        // 値が期待通りであることを検証
        assertEquals(expectedSkillID, actualSkillID, "The skill ID should be 0");
    }
}
