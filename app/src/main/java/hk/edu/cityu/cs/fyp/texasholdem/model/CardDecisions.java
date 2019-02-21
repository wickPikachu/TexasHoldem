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
    private static final int ROYAL_FLUSH = 9000;

    private static final int SHIFT_CLASS = 1;
    private static final int SHIFT_NUMBER = 4;

    // Diamonds, Clubs, Hearts, Spades
    public static final List<Character> cardSuitList = new ArrayList<>();
    public static final List<Character> cardNumberList = new ArrayList<>();

    static {
        cardSuitList.add('d');
        cardSuitList.add('c');
        cardSuitList.add('h');
        cardSuitList.add('s');
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
    private long combinations = 0;

    public long getValues() {
        return 0;
    }

    public void addCard(String cardString) {
        cards |= valueOfCard(cardString);
    }

    public static long getCardsValues(String[] cards) {
        long values = 0L;
        for (String card : cards)
            values |= valueOfCard(card);
        return values;
    }

    public static long valueOfCard(String cardString) {
        cardString = cardString.toLowerCase();
        char cardClass = cardString.charAt(0);
        char cardNumber = cardString.charAt(1);
        return 1L << (cardNumberList.indexOf(cardNumber) * 4) << cardSuitList.indexOf(cardClass);
    }

    public int eval() {
        return 0;
    }

    public void clear() {
        cards = 0;
    }

    public boolean isPair() {
        return true;
    }

    public boolean isTwoPair() {
        return true;
    }

    public boolean isFullHouse() {
        // TODO:
        return true;
    }

    public boolean isStraight() {
        // TODO:
        return true;
    }

    public boolean isThreeOfAKind() {
        // TODO:
        return true;
    }

    public static boolean isFlush(long cards) {
        // check suits,
        // from 0 to 3 represent {Diamonds, Clubs, Hearts, Spades}
        long[] suitValues = {0x1L, 0x2L, 0x4L, 0x8L};
        for (long suitValue : suitValues) {
            int countSameSuit = 0;
            long tempCards = cards;
            for (int j = 0; j < 13; j++) {
                if ((tempCards & suitValue) == suitValue)
                    countSameSuit += 1;
                tempCards >>= 4;
            }
            if (countSameSuit >= 5)
                return true;
        }
        return false;
    }

    public static boolean isFlush(String[] cards) {
        return isFlush(getCardsValues(cards));
    }

    public static boolean isFourOfAKind(long cards) {
        long fourOfKindValue = 0xFL;
        // check from 2 to A, total 13 cards
        for (int num = 0; num < 13; num++) {
            if ((cards & fourOfKindValue) == fourOfKindValue)
                return true;
            fourOfKindValue <<= 4;
        }
        return false;
    }

    public static boolean isFourOfAKind(String[] cards) {
        return isFourOfAKind(getCardsValues(cards));
    }

    public static boolean isStraightFlush(long cards) {
        // check from Diamonds, Clubs, Hearts, Spades,
        // and from {2,3,4,5,6} to {10,J,Q,K,A};
        long straightFlushValue = 0x11111L;
        // TODO: straight flush value
        for (int num = 2; num <= 10; num++) {
            for (int suit = 0; suit < 4; suit++) {
                if ((cards & straightFlushValue) == straightFlushValue)
                    return true;
                straightFlushValue <<= 1;
            }
        }
        return false;
    }

    public static boolean isStraightFlush(String[] cards) {
        return isStraightFlush(getCardsValues(cards));
    }

    public static boolean isRoyalFlush(long cards) {
//        char[] suits = {'d', 'c', 'h', 's'};
//        char[] nums = {'t', 'j', 'q', 'k', 'a'};
//
//        for (char suit : suits) {
//            int value = 0;
//            for (char num : nums) {
//                value |= valueOfCard("" + suit + num);
//            }
//            if ((cards & value) == value) {
//                return true;
//            }
//        }
//        return false;

        long royalFlush1 = 0x11111L << 32;
        long royalFlush2 = 0x11111L << 33;
        long royalFlush3 = 0x11111L << 34;
        long royalFlush4 = 0x11111L << 35;

        return (cards & royalFlush1) == royalFlush1 ||
                (cards & royalFlush2) == royalFlush2 ||
                (cards & royalFlush3) == royalFlush3 ||
                (cards & royalFlush4) == royalFlush4;
    }

    public static boolean isRoyalFlush(String[] cards) {
        return isRoyalFlush(getCardsValues(cards));
    }

    // TODO: get best list

}
