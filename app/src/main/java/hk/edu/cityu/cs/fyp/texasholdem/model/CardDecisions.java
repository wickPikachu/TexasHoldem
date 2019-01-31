package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.ArrayList;
import java.util.List;

public class CardDecisions {

    private static final int PAIR = 1000;
    private static final int TWO_PAIRS = 2000;
    private static final int FULL_HOUSE = 6000;
    private static final int THREE_OF_A_KIND = 3000;
    private static final int STRAIGHT = 4000;
    private static final int FLUSH = 5000;
    private static final int FOUR_OF_A_KIND = 7000;
    private static final int STRAIGHT_FLUSH = 8000;

    private static final int SHIFT_CLASS = 1;
    private static final int SHIFT_NUMBER = 4;

    // Diamonds, Clubs, Hearts, Spades
    public static final List<Character> cardClassList = new ArrayList<>();
    public static final List<Character> cardNumberList = new ArrayList<>();

    static {
        cardClassList.add('d');
        cardClassList.add('c');
        cardClassList.add('h');
        cardClassList.add('s');
        cardNumberList.add('2');
        cardNumberList.add('3');
        cardNumberList.add('4');
        cardNumberList.add('5');
        cardNumberList.add('6');
        cardNumberList.add('7');
        cardNumberList.add('8');
        cardNumberList.add('9');
        cardNumberList.add('t');
        cardNumberList.add('j');
        cardNumberList.add('q');
        cardNumberList.add('k');
        cardNumberList.add('a');
    }
    // 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | ... | 0 0 0 0 | 0 0 0 0
    //    A         K         Q         J         T         9      ...      3         2
    private long cards = 0;

    // straight_flush, four of a kind, flush, straight, three of a kind, full house, two pairs, pairs
    // 11111111
    private int combinations = 0;

    public int getValues() {
        return 0;
    }

    public void addCard(String card) {
        char cardClass = card.charAt(0);
        char cardNumber = card.charAt(1);

        cards ^= 1 << cardNumberList.indexOf(cardNumber) << cardClassList.indexOf(cardClass);
    }



    public void clear() {
        cards = 0;
    }

    private boolean isPair() {
        return true;
    }

    private boolean isTwoPair() {
        return true;
    }

    private boolean isFullHouse() {
        // TODO:
        return true;
    }


}
