package org.example.Skills;

import org.example.Card;
import org.example.Player;

import java.util.ArrayList;

public class Skill_handSwap {

    private static final int skillID = 3;
    private Player player1;
    private Player player2;

    // コンストラクタ
    public Skill_handSwap( Player player1, Player player2 ){
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getSkillID(){
        return skillID;
    }

    // プレイヤー１とプレイヤー２の手札を交換する
    public void useSkill_handSwap(){
        ArrayList<Card> player1Hand = this.player1.hand;
        this.player1.hand = this.player2.hand;
        this.player2.hand = player1Hand;
    }
}
