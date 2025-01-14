package org.example.Skills;

import org.example.Card;
import org.example.Player;
import org.example.Dealer;

import java.util.ArrayList;

public class Skill_exchangingHandsAgain{

    private static final int skillID = 2;
    private Dealer dealer;
    Player player;
    ArrayList<Integer> cardIndexList;

    // コンストラクタ
    public Skill_exchangingHandsAgain( Player player, Dealer dealer ,ArrayList<Integer> cardIndexList){

        this.player = player;
        this.dealer = dealer;
        this.cardIndexList = cardIndexList;

    }

    public int getSkillID(){
        return skillID;
    }

    public void useSkill_exchangingHandsAgain(){
        exchangingHandsAgain();
    }

    private void exchangingHandsAgain(){
        //DealerクラスのchangeCardメソッドを呼び出す

            //プレイヤーの手札の最初のカードを交換（例）
            for(int cardIndex : cardIndexList) {
                dealer.changeCard(player.getUserID(), cardIndex);
            }
    }

}
