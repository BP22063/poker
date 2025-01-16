import org.example.Card;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CardTest {

    @Test
    void test_card(){

        Card S2 = new Card("Spades", 2);
        Card H5 = new Card("Hearts", 5);
        Card C7 = new Card("Clubs", 7);
        Card D10 = new Card("Diamonds", 10);
        Card SK = new Card("Spades", 13);

        assertThat(S2.getMark()).isEqualTo("Spades");
        assertThat(H5.getMark()).isEqualTo("Hearts");
        assertThat(C7.getMark()).isEqualTo("Clubs");
        assertThat(D10.getMark()).isEqualTo("Diamonds");
        assertThat(SK.getMark()).isEqualTo("Spades");

        assertThat(S2.getNumber()).isEqualTo(2);
        assertThat(H5.getNumber()).isEqualTo(5);
        assertThat(C7.getNumber()).isEqualTo(7);
        assertThat(D10.getNumber()).isEqualTo(10);
        assertThat(SK.getNumber()).isEqualTo(13);

        assertThat(S2.toString()).isEqualTo("2 of Spades");
        assertThat(H5.toString()).isEqualTo("5 of Hearts");
        assertThat(C7.toString()).isEqualTo("7 of Clubs");
        assertThat(D10.toString()).isEqualTo("10 of Diamonds");
        assertThat(SK.toString()).isEqualTo("K of Spades");

    }
}
