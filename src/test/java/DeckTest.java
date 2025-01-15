import org.example.Card;
import org.example.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class DeckTest {

    Deck deck;

    @BeforeEach
    void before(){
        this.deck = new Deck();
    }

    @Test
    void test_initializeDeck(){

        assertThat(deck.cards.size()).isEqualTo(52);

        assertThat(deck.cards.get(0).toString()).isEqualTo("A of Hearts");
        assertThat(deck.cards.get(1).toString()).isEqualTo("2 of Hearts");
        assertThat(deck.cards.get(2).toString()).isEqualTo("3 of Hearts");
        assertThat(deck.cards.get(3).toString()).isEqualTo("4 of Hearts");
        assertThat(deck.cards.get(4).toString()).isEqualTo("5 of Hearts");
        assertThat(deck.cards.get(5).toString()).isEqualTo("6 of Hearts");
        assertThat(deck.cards.get(6).toString()).isEqualTo("7 of Hearts");
        assertThat(deck.cards.get(7).toString()).isEqualTo("8 of Hearts");
        assertThat(deck.cards.get(8).toString()).isEqualTo("9 of Hearts");
        assertThat(deck.cards.get(9).toString()).isEqualTo("10 of Hearts");
        assertThat(deck.cards.get(10).toString()).isEqualTo("J of Hearts");
        assertThat(deck.cards.get(11).toString()).isEqualTo("Q of Hearts");
        assertThat(deck.cards.get(12).toString()).isEqualTo("K of Hearts");

        assertThat(deck.cards.get(13).toString()).isEqualTo("A of Diamonds");
        assertThat(deck.cards.get(14).toString()).isEqualTo("2 of Diamonds");
        assertThat(deck.cards.get(15).toString()).isEqualTo("3 of Diamonds");
        assertThat(deck.cards.get(16).toString()).isEqualTo("4 of Diamonds");
        assertThat(deck.cards.get(17).toString()).isEqualTo("5 of Diamonds");
        assertThat(deck.cards.get(18).toString()).isEqualTo("6 of Diamonds");
        assertThat(deck.cards.get(19).toString()).isEqualTo("7 of Diamonds");
        assertThat(deck.cards.get(20).toString()).isEqualTo("8 of Diamonds");
        assertThat(deck.cards.get(21).toString()).isEqualTo("9 of Diamonds");
        assertThat(deck.cards.get(22).toString()).isEqualTo("10 of Diamonds");
        assertThat(deck.cards.get(23).toString()).isEqualTo("J of Diamonds");
        assertThat(deck.cards.get(24).toString()).isEqualTo("Q of Diamonds");
        assertThat(deck.cards.get(25).toString()).isEqualTo("K of Diamonds");

        assertThat(deck.cards.get(26).toString()).isEqualTo("A of Clubs");
        assertThat(deck.cards.get(27).toString()).isEqualTo("2 of Clubs");
        assertThat(deck.cards.get(28).toString()).isEqualTo("3 of Clubs");
        assertThat(deck.cards.get(29).toString()).isEqualTo("4 of Clubs");
        assertThat(deck.cards.get(30).toString()).isEqualTo("5 of Clubs");
        assertThat(deck.cards.get(31).toString()).isEqualTo("6 of Clubs");
        assertThat(deck.cards.get(32).toString()).isEqualTo("7 of Clubs");
        assertThat(deck.cards.get(33).toString()).isEqualTo("8 of Clubs");
        assertThat(deck.cards.get(34).toString()).isEqualTo("9 of Clubs");
        assertThat(deck.cards.get(35).toString()).isEqualTo("10 of Clubs");
        assertThat(deck.cards.get(36).toString()).isEqualTo("J of Clubs");
        assertThat(deck.cards.get(37).toString()).isEqualTo("Q of Clubs");
        assertThat(deck.cards.get(38).toString()).isEqualTo("K of Clubs");

        assertThat(deck.cards.get(39).toString()).isEqualTo("A of Spades");
        assertThat(deck.cards.get(40).toString()).isEqualTo("2 of Spades");
        assertThat(deck.cards.get(41).toString()).isEqualTo("3 of Spades");
        assertThat(deck.cards.get(42).toString()).isEqualTo("4 of Spades");
        assertThat(deck.cards.get(43).toString()).isEqualTo("5 of Spades");
        assertThat(deck.cards.get(44).toString()).isEqualTo("6 of Spades");
        assertThat(deck.cards.get(45).toString()).isEqualTo("7 of Spades");
        assertThat(deck.cards.get(46).toString()).isEqualTo("8 of Spades");
        assertThat(deck.cards.get(47).toString()).isEqualTo("9 of Spades");
        assertThat(deck.cards.get(48).toString()).isEqualTo("10 of Spades");
        assertThat(deck.cards.get(49).toString()).isEqualTo("J of Spades");
        assertThat(deck.cards.get(50).toString()).isEqualTo("Q of Spades");
        assertThat(deck.cards.get(51).toString()).isEqualTo("K of Spades");

    }

    @Test
    void test_draw(){

        Card S2 = new Card("Spades", 2);
        Card H5 = new Card("Hearts", 5);
        Card C7 = new Card("Clubs", 7);
        Card D10 = new Card("Diamonds", 10);
        Card SK = new Card("Spades", 13);

        deck.cards = new ArrayList<Card>(List.of(S2,H5,C7,D10,SK));

        assertThat(deck.cards.size()).isEqualTo(5);
        assertThat(deck.draw().toString()).isEqualTo("2 of Spades");
        assertThat(deck.cards.size()).isEqualTo(4);
        assertThat(deck.draw().toString()).isEqualTo("5 of Hearts");
        assertThat(deck.cards.size()).isEqualTo(3);
        assertThat(deck.draw().toString()).isEqualTo("7 of Clubs");
        assertThat(deck.cards.size()).isEqualTo(2);
        assertThat(deck.draw().toString()).isEqualTo("10 of Diamonds");
        assertThat(deck.cards.size()).isEqualTo(1);
        assertThat(deck.draw().toString()).isEqualTo("K of Spades");
        assertThat(deck.cards.size()).isEqualTo(0);

        assertThat(deck.draw()).isEqualTo(null);

    }

    @Test
    void test_shuffle(){

        Card S2 = new Card("Spades", 2);
        Card H5 = new Card("Hearts", 5);
        Card C7 = new Card("Clubs", 7);
        Card D10 = new Card("Diamonds", 10);
        Card SK = new Card("Spades", 13);

        deck.cards = new ArrayList<Card>(List.of(S2,H5,C7,D10,SK));


        int index;
        int size = deck.cards.size();

        // シャッフル
        deck.shuffle();

        // S2
        assertThat(deck.cards.contains(S2)).isTrue();
        index = deck.cards.indexOf(S2);
        assertThat(index >= 0 && index < size).isTrue();

        // H5
        assertThat(deck.cards.contains(H5)).isTrue();
        index = deck.cards.indexOf(H5);
        assertThat(index >= 0 && index < size).isTrue();

        // C7
        assertThat(deck.cards.contains(C7)).isTrue();
        index = deck.cards.indexOf(C7);
        assertThat(index >= 0 && index < size).isTrue();

        // D10
        assertThat(deck.cards.contains(D10)).isTrue();
        index = deck.cards.indexOf(D10);
        assertThat(index >= 0 && index < size).isTrue();

        // SK
        assertThat(deck.cards.contains(SK)).isTrue();
        index = deck.cards.indexOf(SK);
        assertThat(index >= 0 && index < size).isTrue();

    }
}
