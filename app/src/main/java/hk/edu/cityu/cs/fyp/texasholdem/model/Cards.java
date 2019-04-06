package hk.edu.cityu.cs.fyp.texasholdem.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;

public class Cards implements Comparable<Cards> {

    // Diamonds, Clubs, Hearts, Spades
    public static final List<Character> CARD_SUIT_LIST = new ArrayList<>();
    public static final List<Character> CARD_NUMBER_LIST = new ArrayList<>();

    static {
        CARD_SUIT_LIST.add('d');
        CARD_SUIT_LIST.add('c');
        CARD_SUIT_LIST.add('h');
        CARD_SUIT_LIST.add('s');
        CARD_NUMBER_LIST.add('2');
        CARD_NUMBER_LIST.add('3');
        CARD_NUMBER_LIST.add('4');
        CARD_NUMBER_LIST.add('5');
        CARD_NUMBER_LIST.add('6');
        CARD_NUMBER_LIST.add('7');
        CARD_NUMBER_LIST.add('8');
        CARD_NUMBER_LIST.add('9');
        CARD_NUMBER_LIST.add('t');
        CARD_NUMBER_LIST.add('j');
        CARD_NUMBER_LIST.add('q');
        CARD_NUMBER_LIST.add('k');
        CARD_NUMBER_LIST.add('a');
    }

    public enum Combination {
        None(0),
        Pair(1),
        TwoPairs(1 << 1),
        ThreeOfAKind(1 << 2),
        Straight(1 << 3),
        Flush(1 << 4),
        FullHouse(1 << 5),
        FourOfAKind(1 << 6),
        StraightFlush(1 << 7),
        RoyalFlush(1 << 8);

        private int value;

        Combination(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private Combination combination;
    private int combinationValue = 0;
    // form (2 to A);
    private int[] kicks = {0, 0, 0, 0, 0};

    // 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | ... | 0 0 0 0 | 0 0 0 0
    //    A         K         Q         J         T         9      ...      3         2
    private long cards = 0L;
    private long bestFiveCards = 0L;

    // use enum instead
    // straight_flush, four of a kind, flush, straight, three of a kind, full house, two pairs, pairs
    // 11111111
    @Deprecated
    private long combinations = 0;

    public Cards(String[] cards) {
        this.cards = getCardsValues(cards);
    }

    public Cards(long cards) {
        this.cards = cards;
    }

    public long getCards() {
        return cards;
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
        return 1L << (CARD_NUMBER_LIST.indexOf(cardNumber) * 4) << CARD_SUIT_LIST.indexOf(cardClass);
    }

    public static int getNumberOfCards(long cards) {
        int count = 0;
        for (int i = 0; i < 52; i++) {
            if (((cards >> i) & 0x1L) == 0x1L) {
                count++;
            }
        }
        return count;
    }

    public static int countSameNum(long cards) {
        int count = 0;
        long tempCards = cards;
        for (int i = 0; i < 4; i++) {
            if ((tempCards & 0x1L) == 0x1L) {
                ++count;
            }
            tempCards >>= 1;
        }
        return count;
    }

    public Combination evaluate() {
        if (isRoyalFlush()) {
            combination = Combination.RoyalFlush;
        } else if (isStraightFlush()) {
            combination = Combination.StraightFlush;
        } else if (isFourOfAKind()) {
            combination = Combination.FourOfAKind;
        } else if (isFullHouse()) {
            combination = Combination.FullHouse;
        } else if (isFlush()) {
            combination = Combination.Flush;
        } else if (isStraight()) {
            combination = Combination.Straight;
        } else if (isThreeOfAKind()) {
            combination = Combination.ThreeOfAKind;
        } else if (isTwoPair()) {
            combination = Combination.TwoPairs;
        } else if (isPair()) {
            combination = Combination.Pair;
        } else {
            combination = Combination.None;
            int[] highestCardValues = getHighestCardValues(cards);
            for (int i = 0; i < kicks.length; i++) {
                kicks[i] = highestCardValues[i];
            }
        }
        return combination;
    }

    public static Combination evaluate(String[] cards) {
        return new Cards(cards).evaluate();
    }

    public static boolean isNone(String[] cards) {
        return new Cards(cards).isNone();
    }

    public boolean isNone() {
        return this.evaluate() == Combination.None;
    }

    public boolean isPair() {
        long cards = this.cards;
        for (int num = CARD_NUMBER_LIST.size(); num > 0; num--) {
            if (countSameNum(cards >> (4 * (num - 1))) == 2) {
                kicks[0] = num;
                // than get highest cards
                cards &= ~(0xFL << ((kicks[0] - 1) * 4));
                int[] highestCardValues = getHighestCardValues(cards);
                kicks[1] = highestCardValues[0];
                kicks[2] = highestCardValues[1];
                kicks[3] = highestCardValues[2];
                return true;
            }
        }
        return false;
    }

    public static boolean isPair(String[] cards) {
        return new Cards(cards).isPair();
    }

    public boolean isTwoPair() {
        long cards = this.cards;
        int[] tempKicks = {0, 0, 0, 0, 0};
        int countPair = 0;
        for (int num = CARD_NUMBER_LIST.size(); num > 0; num--) {
            if (countSameNum(cards >> (4 * (num - 1))) == 2) {
                tempKicks[countPair] = num;
                ++countPair;
                if (countPair == 2) {
                    kicks = tempKicks;
                    // remove two pairs values from cards
                    // than get highest cards
                    cards &= ~(0xFL << ((kicks[0] - 1) * 4));
                    cards &= ~(0xFL << ((kicks[1] - 1) * 4));
                    kicks[2] = getHighestCardValues(cards)[0];
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTwoPair(String[] cards) {
        return new Cards(cards).isTwoPair();
    }

    public boolean isStraight() {
        long cards = this.cards;
        long straightValue = 0xFL;
        for (int i = 0; i < 9; i++) {
            long tempCards = cards >> (i * 4);
            for (int j = 0; j < 5; j++) {
                if ((tempCards & straightValue) > 0) {
                    // isStraight
                    if (j == 4) {
                        return true;
                    }
                    tempCards >>= 4;
                    continue;
                }

                break;
            }
        }

        // check A,2,3,4,5
        if ((cards & 0xFL) > 0 && (cards >> 4 & 0xFL) > 0 && (cards >> 8 & 0xFL) > 0 && (cards >> 12 & 0xFL) > 0 && (cards >> 48 & 0xFL) > 0) {
            return true;
        }

        return false;
    }

    public static boolean isStraight(String[] cards) {
        return new Cards(cards).isStraight();
    }

    public boolean isThreeOfAKind() {
        long cards = this.cards;
        for (int num = CARD_NUMBER_LIST.size(); num > 0; num--) {
            if (countSameNum(cards >> (4 * (num - 1))) == 3) {
                kicks[0] = num;
                // remove two pairs values from cards
                // than get highest cards
                cards &= ~(0xFL << ((kicks[0] - 1) * 4));
                int[] highestCardValues = getHighestCardValues(cards);
                kicks[1] = highestCardValues[0];
                kicks[2] = highestCardValues[1];
                return true;
            }
        }
        return false;
    }

    public static boolean isThreeOfAKind(String[] cards) {
        return new Cards(cards).isThreeOfAKind();
    }

    public boolean isFlush() {
        long cards = this.cards;
        // check suits,
        // from 0 to 3 represent {Diamonds, Clubs, Hearts, Spades}
        final long[] suitValues = {0x1L, 0x2L, 0x4L, 0x8L};
        ArrayList<Integer> diamonds = new ArrayList<>();
        ArrayList<Integer> clubs = new ArrayList<>();
        ArrayList<Integer> hearts = new ArrayList<>();
        ArrayList<Integer> spades = new ArrayList<>();
        for (long suitValue : suitValues) {
            long tempCards = cards;
            for (int num = 1; num <= 13; num++) {
                if ((tempCards & suitValue) == suitValue) {
                    if (suitValue == suitValues[0]) {
                        diamonds.add(num);
                    } else if (suitValue == suitValues[1]) {
                        clubs.add(num);
                    } else if (suitValue == suitValues[2]) {
                        hearts.add(num);
                    } else {
                        spades.add(num);
                    }
                }
                tempCards >>= 4;
            }
        }
        if (diamonds.size() >= 5) {
            setKicksFromSuitList(diamonds);
            return true;
        }
        if (clubs.size() >= 5) {
            setKicksFromSuitList(clubs);
            return true;
        }
        if (hearts.size() >= 5) {
            setKicksFromSuitList(hearts);
            return true;
        }
        if (spades.size() >= 5) {
            setKicksFromSuitList(spades);
            return true;
        }
        return false;
    }

    private void setKicksFromSuitList(ArrayList<Integer> suitList) {
        Collections.sort(suitList);
        int size = suitList.size();
        for (int i = 0; i < 5; i++) {
            kicks[i] = suitList.get(size - i - 1);
        }
    }

    public static boolean isFlush(String[] cards) {
        return new Cards(cards).isFlush();
    }

    public boolean isFullHouse() {
        long cards = this.cards;
        int[] countSameArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        boolean hasThreeOfKind = false;
        boolean hasPair = false;
        int threeOfKindValue = -1;
        int pairValue = -1;
        for (int i = 0; i < countSameArray.length; i++) {
            countSameArray[i] = countSameNum(cards);
            if (countSameArray[i] == 3 & countSameArray[i] > threeOfKindValue) {
                hasThreeOfKind = true;
                threeOfKindValue = i;
            } else if (countSameArray[i] == 2 && countSameArray[i] > pairValue) {
                hasPair = true;
                pairValue = i;
            }
            cards >>= 4;
        }

        if (hasThreeOfKind && hasPair) {
            kicks[0] = threeOfKindValue;
            kicks[1] = pairValue;
            return true;
        }
        return false;
    }

    public static boolean isFullHouse(String[] cards) {
        return new Cards(cards).isFullHouse();
    }

    public boolean isFourOfAKind() {
        long cards = this.cards;
        long fourOfKindValue = 0xFL;
        // check from 2 to A, total 13 cards
        for (int num = 1; num <= 13; num++) {
            if ((cards & fourOfKindValue) == fourOfKindValue) {
                kicks[0] = num;
                // remove fourOfKindCards;
                cards &= ~(0xFL << ((num - 1) * 4));
                kicks[1] = getHighestCardValues(cards)[0];
                return true;
            }
            fourOfKindValue <<= 4;
        }
        return false;
    }

    public static boolean isFourOfAKind(String[] cards) {
        return new Cards(cards).isFourOfAKind();
    }

    public boolean isStraightFlush() {
        long cards = this.cards;
        // check from Diamonds, Clubs, Hearts, Spades,
        // and from {10,J,Q,K,A} to {2,3,4,5,6}
        long straightFlushValue = 0x88888L << (8 * 4);
        // TODO: straight flush value
        // 23456, 34567, 45678, 56789, 6789t, 789tj, 89tjq, 9tjqk, tjqka
        //  5, 6, 7,8,9,10,11,12
        for (int num = 13; num >= 5; num--) {
            for (int suit = 0; suit < 4; suit++) {
                if ((cards & straightFlushValue) == straightFlushValue) {
                    kicks[0] = num;
                    return true;
                }
                straightFlushValue >>= 1;
            }
        }
        // check A,2,3,4,5
        long smallerStraightFlushValue = 0x1000000001111L;
        for (int suit = 0; suit < 4; suit++) {
            if ((cards & smallerStraightFlushValue) == smallerStraightFlushValue) {
                kicks[0] = 4;   // card number is 5
                return true;
            }
            smallerStraightFlushValue <<= 1;
        }
        return false;
    }

    public static boolean isStraightFlush(String[] cards) {
        return new Cards(cards).isStraightFlush();
    }

    public boolean isRoyalFlush() {
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

        long cards = this.cards;
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
        return new Cards(cards).isRoyalFlush();
    }

    // TODO: get best list
    public long getBestFiveCards() {
        return 0L;
    }


    public Combination getCombination() {
        return combination;
    }

    public int[] getKicks() {
        return kicks;
    }

    public void setKicks(int[] kicks) {
        this.kicks = kicks;
    }

    public void setKicks(int position, int k) {
        this.kicks[position] = k;
    }

    public void setKicks(int position, String cardString) {
        setKicks(position, Utils.valueOfCard(cardString));
    }

    @Override
    public int compareTo(@NonNull Cards cards) {
        this.evaluate();
        cards.evaluate();
        if (this.combination.getValue() > cards.combination.getValue()) {
            return 1;
        } else if (this.combination.getValue() < cards.getCombination().getValue()) {
            return -1;
        }

        for (int i = 0; i < kicks.length; ++i) {
            if (this.kicks[i] > cards.kicks[i]) {
                return 1;
            } else if (this.kicks[i] < cards.kicks[i]) {
                return -1;
            }
        }

        return 0;
    }

    private int[] getHighestCardValues(long cards) {
        ArrayList<Integer> highestCardArray = new ArrayList<>();
        // from 'A"
        for (int num = 13; num >= 1; num--) {
            if (((cards >> (num - 1) * 4) & 0xFL) > 0L) {
                highestCardArray.add(num);
            }
        }
        int[] result = Utils.toIntArray(highestCardArray);
        if (result.length == 0) {
            return new int[]{0};
        }
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        String name = combination.name();
        if (combination == Combination.None) {
            name = "Highcard";
        }

        return name;
    }
}
