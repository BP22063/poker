package org.example.Skills;

import org.example.Player;
import org.example.Dealer;

public class Skill_exchangingHandsAgain{

    private static final int skillID = 2;
    private Dealer dealer;
    Player player;

    // コンストラクタ
    public Skill_exchangingHandsAgain( Player player, Dealer dealer ){

        this.player = player;
        this.dealer = dealer;
    }

    public int getSkillID(){
        return skillID;
    }

    //public void useSkill_exchangingHandsAgain(){exchangingHandsAgain();}

    private void exchangingHandsAgain(int cardIndex){
        //DealerクラスのchangeCardメソッドを呼び出す
        int playerIndex = dealer.getPlayers().indexOf(player); //プレイヤーのインデックスを取得
        if (playerIndex >= 0) {
            //プレイヤーの手札の最初のカードを交換（例）
            dealer.changeCard(playerIndex, cardIndex);
        }
    }

}
