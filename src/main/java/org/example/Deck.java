package org.example;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    private ArrayList<Card> discardPile;

    //private Card[];
    public Deck() {
        cards = new ArrayList<>();
        discardPile = new ArrayList<>();
        initializeDeck();
    }

    public Card draw() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;  // 空の場合
    }



    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void discard(Card card) {
        discardPile.add(card);
    }


    public void initializeDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        for (String suit : suits) {
            for (int number = 1; number <= 13; number++) {
                cards.add(new Card(suit, number));
            }
        }

    }
}
