package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.ArrayList;
import java.util.List;

public class CardDecisions {

    private static final int PAIR = 1000;
    private static final int TWO_PAIRS = 2000;
    private static final int THREE_OF_A_KIND = 3000;
    private static final int STRAIGHT = 4000;
    private static final int FLUSH = 5000;
    private static final int FULL_HOUSE = 6000;
    private static final int FOUR_OF_A_KIND = 7000;
    private static final int STRAIGHT_FLUSH = 8000;
    private static final int ROYAL_FLUSH = 9000;

    private static final int SHIFT_CLASS = 1;
    private static final int SHIFT_NUMBER = 4;

    enum CardGroup {
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

        CardGroup(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

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

    public void clear() {
        cards = 0;
    }

    public static CardGroup eval(String[] cards) {
        return eval(getCardsValues(cards));
    }

    public static CardGroup eval(long cards) {
        if (isRoyalFlush(cards)) {
            return CardGroup.RoyalFlush;
        } else if (isStraightFlush(cards)) {
            return CardGroup.StraightFlush;
        } else if (isFourOfAKind(cards)) {
            return CardGroup.FourOfAKind;
        } else if (isFullHouse(cards)) {
            return CardGroup.FullHouse;
        } else if (isFlush(cards)) {
            return CardGroup.Flush;
        } else if (isStraight(cards)) {
            return CardGroup.Straight;
        } else if (isThreeOfAKind(cards)) {
            return CardGroup.ThreeOfAKind;
        } else if (isTwoPair(cards)) {
            return CardGroup.TwoPairs;
        } else if (isPair(cards)) {
            return CardGroup.Pair;
        }
        return CardGroup.None;
    }

    public static boolean isPair(long cards) {
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

    public static boolean isPair(String[] cards) {
        return isPair(getCardsValues(cards));
    }

    public static boolean isTwoPair(long cards) {
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
        return isTwoPair(getCardsValues(cards));
    }

    public static boolean isFullHouse(long cards) {
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
        return isFullHouse(getCardsValues(cards));
    }

    public static boolean isStraight(long cards) {
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
        return isStraight(getCardsValues(cards));
    }

    public static boolean isThreeOfAKind(long cards) {
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
        return isThreeOfAKind(getCardsValues(cards));
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
                    countSameSuit++;
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
