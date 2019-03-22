package hk.edu.cityu.cs.fyp.texasholdem.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;

public class Cards implements Comparable<Cards> {

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

    enum Combination {
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
    // form (2 to A);
    private int[] kicks = {0, 0, 0, 0, 0};

    // 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | 0 0 0 0 | ... | 0 0 0 0 | 0 0 0 0
    //    A         K         Q         J         T         9      ...      3         2
    private long cards = 0L;
    private long bestCards = 0L;

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
        return 1L << (cardNumberList.indexOf(cardNumber) * 4) << cardSuitList.indexOf(cardClass);
    }

    private static int countSameNum(long cards) {
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

    public Combination eval() {
        if (isRoyalFlush()) {
            return Combination.RoyalFlush;
        } else if (isStraightFlush()) {
            return Combination.StraightFlush;
        } else if (isFourOfAKind()) {
            return Combination.FourOfAKind;
        } else if (isFullHouse()) {
            return Combination.FullHouse;
        } else if (isFlush()) {
            return Combination.Flush;
        } else if (isStraight()) {
            return Combination.Straight;
        } else if (isThreeOfAKind()) {
            return Combination.ThreeOfAKind;
        } else if (isTwoPair()) {
            return Combination.TwoPairs;
        } else if (isPair()) {
            return Combination.Pair;
        }
        return Combination.None;
    }

    public static Combination eval(String[] cards) {
        return new Cards(cards).eval();
    }

    public boolean isPair() {
        long cards = this.cards;
        for (int i = 0; i < cardNumberList.size(); i++) {
            int countSameNum = 0;
            for (int j = 0; j < cardSuitList.size(); j++) {
                if ((cards & 1L) == 1L) {
                    countSameNum++;
                }
                cards >>= 1;
            }
            if (countSameNum == 2) {
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
        int countPair = 0;
        for (int i = 0; i < cardNumberList.size(); i++) {
            if (countSameNum(cards) == 2) {
                ++countPair;
                if (countPair == 2) {
                    return true;
                }
            }
            cards >>= 4;
        }
        return false;
    }

    public static boolean isTwoPair(String[] cards) {
        return new Cards(cards).isTwoPair();
    }

    public boolean isFullHouse() {
        long cards = this.cards;
        int[] countSameArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        boolean hasThreeOfKind = false;
        boolean hasTwoPair = false;
        int threeOfKindValue = -1;
        int twoPairValue = -1;
        for (int i = 0; i < countSameArray.length; i++) {
            countSameArray[i] = countSameNum(cards);
            if (countSameArray[i] == 3 & countSameArray[i] > threeOfKindValue) {
                hasThreeOfKind = true;
                threeOfKindValue = i;
            } else if (countSameArray[i] == 2 && countSameArray[i] > twoPairValue) {
                hasTwoPair = true;
                twoPairValue = i;
            }
            cards >>= 4;
        }

        return hasThreeOfKind && hasTwoPair;
    }

    public static boolean isFullHouse(String[] cards) {
        return new Cards(cards).isFullHouse();
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
        return false;
    }

    public static boolean isStraight(String[] cards) {
        return new Cards(cards).isStraight();
    }

    public boolean isThreeOfAKind() {
        long cards = this.cards;
        for (int i = 0; i < cardNumberList.size(); i++) {
            int countSameNum = 0;
            for (int j = 0; j < cardSuitList.size(); j++) {
                if ((cards & 1L) == 1L) {
                    countSameNum++;
                }
                cards >>= 1;
            }
            if (countSameNum == 3) {
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
        long[] suitValues = {0x1L, 0x2L, 0x4L, 0x8L};
        for (long suitValue : suitValues) {
            int countSameSuit = 0;
            long tempCards = cards;
            for (int j = 0; j < 13; j++) {
                if ((tempCards & suitValue) == suitValue)
                    countSameSuit++;
                tempCards >>= 4;
            }
            if (countSameSuit >= 5)
                return true;
        }
        return false;
    }

    public static boolean isFlush(String[] cards) {
        return new Cards(cards).isFlush();
    }

    public boolean isFourOfAKind() {
        long cards = this.cards;
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
        return new Cards(cards).isFourOfAKind();
    }

    public boolean isStraightFlush() {
        long cards = this.cards;
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
    public long getBestCards() {
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
}
