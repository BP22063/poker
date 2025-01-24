package Skills;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.Skills.Skill_disableHand;
// Test class for Skill_disableHand
public class Skill_disableHandTest {
    private Skill_disableHand skillDisableHand;

    @BeforeEach
    void setUp() {
        skillDisableHand = new Skill_disableHand("Flush");
    }

    @Test
    void testGetSkillID() {
        assertEquals(1, skillDisableHand.getSkillID());
    }

    @Test
    void testGetDisableHand() {
        assertEquals("Flush", skillDisableHand.getDisableHand());
    }
}

