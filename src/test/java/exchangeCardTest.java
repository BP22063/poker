import org.example.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
public class exchangeCardTest {
    @Test
    void exchangeCardTest(){
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(1,"a");
        Player player2 = new Player(2,"b");
        Player player3 = new Player(3,"c");
        Player player4 = new Player(4,"d");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        ArrayList<Integer> exchangeCardIndex = new ArrayList<>(Arrays.asList(0,1,3));

        Dealer d1 = new Dealer(players);
        d1.dealInitialCards(5);

        ArrayList<Card> Hand = new ArrayList<Card>(player1.hand);

        d1.changeHand(1,exchangeCardIndex);

        assertThat(Hand).isNotEqualTo(player1.hand);




    }
}
