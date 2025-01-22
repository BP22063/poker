package org.example.Skills;

public class Skill_disableHand {

    private static final int skillID = 1;
    String disableHand;

    // コンストラクタ
    public Skill_disableHand( String disableHand ){
        this.disableHand = disableHand;
    }

    public int getSkillID(){
        return skillID;
    }

    public String getDisableHand(){
        return this.disableHand;
    }

}
