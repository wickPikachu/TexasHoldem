package hk.edu.cityu.cs.fyp.texasholdem.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardsTest {

    @Test
    public void getValues() {
        assertEquals('d', Cards.CARD_SUIT_LIST.get(0).charValue());
        assertEquals('c', Cards.CARD_SUIT_LIST.get(1).charValue());
        assertEquals('h', Cards.CARD_SUIT_LIST.get(2).charValue());
        assertEquals('s', Cards.CARD_SUIT_LIST.get(3).charValue());

        assertEquals('2', Cards.CARD_NUMBER_LIST.get(0).charValue());
        assertEquals('3', Cards.CARD_NUMBER_LIST.get(1).charValue());
        assertEquals('4', Cards.CARD_NUMBER_LIST.get(2).charValue());
        assertEquals('5', Cards.CARD_NUMBER_LIST.get(3).charValue());
        assertEquals('6', Cards.CARD_NUMBER_LIST.get(4).charValue());
        assertEquals('7', Cards.CARD_NUMBER_LIST.get(5).charValue());
        assertEquals('8', Cards.CARD_NUMBER_LIST.get(6).charValue());
        assertEquals('9', Cards.CARD_NUMBER_LIST.get(7).charValue());
        assertEquals('t', Cards.CARD_NUMBER_LIST.get(8).charValue());
        assertEquals('j', Cards.CARD_NUMBER_LIST.get(9).charValue());
        assertEquals('q', Cards.CARD_NUMBER_LIST.get(10).charValue());
        assertEquals('k', Cards.CARD_NUMBER_LIST.get(11).charValue());
        assertEquals('a', Cards.CARD_NUMBER_LIST.get(12).charValue());

        String[] _cards1 = {"s3", "d3", "d4", "d5", "d6"};
        Cards cards1 = new Cards(_cards1);
        assertEquals(cards1.getCards(), Cards.getCardsValues(_cards1));
    }

    @Test
    public void testCompareTo() {
        // none
        Cards cardsA1_ = new Cards(new String[]{"s3", "d7", "d4", "da", "dk", "s2", "st"});
        Cards cardsB1_ = new Cards(new String[]{"s2", "dq", "d4", "h9", "d6", "sa", "ht"});
        assertEquals(1, cardsA1_.compareTo(cardsB1_));

        Cards cardsA2_ = new Cards(new String[]{"s2", "dj", "d5", "s9", "d6", "sa", "ht"});
        Cards cardsB2_ = new Cards(new String[]{"s2", "dj", "d3", "h9", "d6", "sa", "st"});
        assertEquals(0, cardsA2_.compareTo(cardsB2_));

        // pair
        Cards cardsA1 = new Cards(new String[]{"s3", "d3", "d4", "d8", "d6", "s2", "st"});
        Cards cardsB1 = new Cards(new String[]{"s2", "d2", "d4", "h9", "d6", "sa", "ht"});
        assertEquals(1, cardsA1.compareTo(cardsB1));

        Cards cardsA2 = new Cards(new String[]{"s3", "d3", "d4", "d5", "da", "sa", "st"});
        Cards cardsB2 = new Cards(new String[]{"c3", "h3", "c4", "c5", "ha", "sa", "st"});
        assertEquals(0, cardsA2.compareTo(cardsB2));

        // two pair
        Cards cardsA3 = new Cards(new String[]{"s3", "sk", "sq", "sj", "da", "sa", "st"});
        Cards cardsB3 = new Cards(new String[]{"c3", "h3", "c4", "c5", "sa", "sa", "st"});
        assertEquals(1, cardsA3.compareTo(cardsB3));

        Cards cardsA4 = new Cards(new String[]{"s3", "sk", "sq", "sj", "da", "sa", "st"});
        Cards cardsB4 = new Cards(new String[]{"c3", "ct", "ca", "cj", "cq", "ck", "st"});
        assertEquals(0, cardsA4.compareTo(cardsB4));

        Cards cardsA5 = new Cards(new String[]{"s3", "d3", "s9", "sa", "da", "sa", "st"});
        Cards cardsB5 = new Cards(new String[]{"c3", "hk", "dk", "ck", "sk", "sa", "st"});
        assertEquals(-1, cardsA5.compareTo(cardsB5));

        // four of a kind
        Cards cardsA6 = new Cards(new String[]{"c3", "hk", "dk", "ck", "sk", "sa", "st"});
        Cards cardsB6 = new Cards(new String[]{"c3", "hk", "dk", "ck", "sk", "s2", "st"});
        assertEquals(1, cardsA6.compareTo(cardsB6));

        Cards cardsA7 = new Cards(new String[]{"c3", "hk", "dk", "ck", "sk", "sa", "st"});
        Cards cardsB7 = new Cards(new String[]{"c3", "hk", "dk", "ck", "sk", "ha", "sq"});
        assertEquals(0, cardsA7.compareTo(cardsB7));

        // three of a kind
        Cards cardsA8 = new Cards(new String[]{"c3", "d8", "s6", "c2", "ha", "sa", "da"});
        Cards cardsB8 = new Cards(new String[]{"c3", "hk", "dk", "ck", "s8", "s2", "st"});
        assertEquals(1, cardsA8.compareTo(cardsB8));

        Cards cardsA9 = new Cards(new String[]{"c3", "hk", "dk", "ck", "c9", "sa", "st"});
        Cards cardsB9 = new Cards(new String[]{"c3", "hk", "dk", "ck", "h2", "ha", "ht"});
        assertEquals(0, cardsA9.compareTo(cardsB9));

    }

    @Test
    public void eval() {
        String[] cards1 = {"s3", "d3", "d4", "d5", "d6"};
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards1));

        String[] cards2 = {"d2", "s2", "d4", "d5", "h6"};
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards2));

        String[] cards3 = {"sa", "ha", "hq", "dj", "dk"};
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards3));

        String[] cards4 = {"c3", "da", "ck", "c6", "c2", "d6"};
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards4));

    }

    @Test
    public void isNone() {
        String[] cards1 = {"s3", "st", "d4", "d5", "d6"};
        boolean actual1 = Cards.isNone(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.None, Cards.evaluate(cards1));

        String[] cards2 = {"d2", "sa", "d7", "d5", "h6"};
        boolean actual2 = Cards.isNone(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.None, Cards.evaluate(cards2));

        String[] cards3 = {"sa", "h7", "hq", "dj", "dk"};
        boolean actual3 = Cards.isNone(cards3);
        assertEquals(true, actual3);
        assertEquals(Cards.Combination.None, Cards.evaluate(cards3));

        String[] cards4 = {"c3", "da", "ck", "c8", "c2", "d6"};
        boolean actual4 = Cards.isNone(cards4);
        assertEquals(true, actual4);
        assertEquals(Cards.Combination.None, Cards.evaluate(cards4));

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = Cards.isNone(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7", "s9"};
        boolean actual6 = Cards.isNone(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = Cards.isNone(cards7);
        assertEquals(false, actual7);

    }

    @Test
    public void isPair() {
        String[] cards1 = {"s3", "d3", "d4", "d5", "d6"};
        boolean actual1 = Cards.isPair(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards1));

        String[] cards2 = {"d2", "s2", "d4", "d5", "h6"};
        boolean actual2 = Cards.isPair(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards2));

        String[] cards3 = {"sa", "ha", "hq", "dj", "dk"};
        boolean actual3 = Cards.isPair(cards3);
        assertEquals(true, actual3);
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards3));

        String[] cards4 = {"c3", "da", "ck", "c6", "c2", "d6"};
        boolean actual4 = Cards.isPair(cards4);
        assertEquals(true, actual4);
        assertEquals(Cards.Combination.Pair, Cards.evaluate(cards4));

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = Cards.isPair(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = Cards.isPair(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = Cards.isPair(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isTwoPair() {
        String[] cards1 = {"d2", "d3", "s2", "s3", "d6"};
        boolean actual1 = Cards.isTwoPair(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.TwoPairs, Cards.evaluate(cards1));

        String[] cards2 = {"da", "d2", "s2", "ha", "h6"};
        boolean actual2 = Cards.isTwoPair(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.TwoPairs, Cards.evaluate(cards2));

        String[] cards3 = {"sa", "ha", "hq", "sq", "da"};
        boolean actual3 = Cards.isTwoPair(cards3);
        assertEquals(false, actual3);

        String[] cards4 = {"c3", "ca", "ck", "c6", "c2", "d6"};
        boolean actual4 = Cards.isTwoPair(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = Cards.isTwoPair(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = Cards.isTwoPair(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = Cards.isTwoPair(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isStraight() {
        String[] cards1 = {"d2", "d3", "d4", "d5", "c6"};
        boolean actual1 = Cards.isStraight(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.Straight, Cards.evaluate(cards1));

        String[] cards2 = {"d2", "d3", "d4", "d5", "h6"};
        boolean actual2 = Cards.isStraight(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.Straight, Cards.evaluate(cards2));

        String[] cards3 = {"st", "ha", "hq", "dj", "dk"};
        boolean actual3 = Cards.isStraight(cards3);
        assertEquals(true, actual3);
        assertEquals(Cards.Combination.Straight, Cards.evaluate(cards3));

        String[] cards4 = {"c3", "ca", "ck", "c6", "c2", "d6"};
        boolean actual4 = Cards.isStraight(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = Cards.isStraight(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = Cards.isStraight(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = Cards.isStraight(cards7);
        assertEquals(false, actual7);

        String[] cards8 = {"s2", "ha", "h3", "d4", "d5"};
        boolean actual8 = Cards.isStraight(cards3);
        assertEquals(true, actual8);
        assertEquals(Cards.Combination.Straight, Cards.evaluate(cards8));

    }

    @Test
    public void isThreeOfAKind() {
        String[] cards1 = {"d2", "d3", "d4", "d5", "dk"};
        boolean actual1 = Cards.isThreeOfAKind(cards1);
        assertEquals(false, actual1);

        String[] cards2 = {"st", "s8", "sa", "s3", "s6"};
        boolean actual2 = Cards.isThreeOfAKind(cards2);
        assertEquals(false, actual2);

        String[] cards3 = {"h4", "h5", "h6", "h7", "h9"};
        boolean actual3 = Cards.isThreeOfAKind(cards3);
        assertEquals(false, actual3);

        String[] cards4 = {"c3", "ca", "ck", "d2", "d3"};
        boolean actual4 = Cards.isThreeOfAKind(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = Cards.isThreeOfAKind(cards5);
        assertEquals(true, actual5);
        assertEquals(Cards.Combination.ThreeOfAKind, Cards.evaluate(cards5));

        String[] cards6 = {"c6", "d6", "s6", "c7", "s9"};
        boolean actual6 = Cards.isThreeOfAKind(cards6);
        assertEquals(true, actual6);
        assertEquals(Cards.Combination.ThreeOfAKind, Cards.evaluate(cards6));

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = Cards.isThreeOfAKind(cards7);
        assertEquals(true, actual7);
        assertEquals(Cards.Combination.ThreeOfAKind, Cards.evaluate(cards7));
    }

    @Test
    public void isFlush() {
        assertEquals(0x1L, 1L);
        assertEquals(0x2L, 1L << 1);
        assertEquals(0x4L, 1L << 2);
        assertEquals(0x8L, 1L << 3);

        String[] cards1 = {"d2", "d3", "d4", "d5", "dk"};
        boolean actual1 = Cards.isFlush(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.Flush, Cards.evaluate(cards1));

        String[] cards2 = {"st", "s8", "sa", "s3", "s6"};
        boolean actual2 = Cards.isFlush(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.Flush, Cards.evaluate(cards2));

        String[] cards3 = {"h4", "h5", "h6", "h7", "h9"};
        boolean actual3 = Cards.isFlush(cards3);
        assertEquals(true, actual3);
        assertEquals(Cards.Combination.Flush, Cards.evaluate(cards3));

        String[] cards4 = {"c3", "ca", "ck", "d2", "d3"};
        boolean actual4 = Cards.isFlush(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"c3", "ca", "ck", "cj", "cq"};
        boolean actual5 = Cards.isFlush(cards5);
        assertEquals(true, actual5);
        assertEquals(Cards.Combination.Flush, Cards.evaluate(cards5));

        String[] cards6 = {"c6", "d6", "s6", "c6"};
        boolean actual6 = Cards.isFlush(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c7", "c7", "c7", "d7", "dk"};
        boolean actual7 = Cards.isFlush(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isFullHouse() {
        String[] cards1 = {"d2", "d3", "s2", "h3", "c2"};
        boolean actual1 = Cards.isFullHouse(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.FullHouse, Cards.evaluate(cards1));

        String[] cards2 = {"da", "sa", "ca", "dk", "hk"};
        boolean actual2 = Cards.isFullHouse(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.FullHouse, Cards.evaluate(cards2));

        String[] cards3 = {"sa", "ha", "hq", "sq", "da"};
        boolean actual3 = Cards.isFullHouse(cards3);
        assertEquals(true, actual3);
        assertEquals(Cards.Combination.FullHouse, Cards.evaluate(cards3));

        String[] cards4 = {"c3", "ca", "ck", "c6", "c2", "d6"};
        boolean actual4 = Cards.isFullHouse(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = Cards.isFullHouse(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = Cards.isFullHouse(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = Cards.isFullHouse(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isFourOfAKind() {
        assertEquals(0xFL, 15);
        String[] cards = {"d2", "s2", "c2", "h2"};
        assertEquals(0xFL, Cards.getCardsValues(cards));

        String[] cards1 = {"d2", "s2", "c2", "h2", "dk"};
        boolean actual1 = Cards.isFourOfAKind(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.FourOfAKind, Cards.evaluate(cards1));

        String[] cards2 = {"d3", "s3", "c3", "h3", "s6"};
        boolean actual2 = Cards.isFourOfAKind(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.FourOfAKind, Cards.evaluate(cards2));

        String[] cards3 = {"d4", "s4", "c4", "h4", "h6"};
        boolean actual3 = Cards.isFourOfAKind(cards3);
        assertEquals(true, actual3);
        assertEquals(Cards.Combination.FourOfAKind, Cards.evaluate(cards3));

        String[] cards4 = {"c7", "d5", "s5", "c5", "h5",};
        boolean actual4 = Cards.isFourOfAKind(cards4);
        assertEquals(true, actual4);
        assertEquals(Cards.Combination.FourOfAKind, Cards.evaluate(cards4));

        String[] cards5 = {"ct", "cj", "cq", "ca", "ck"};
        boolean actual5 = Cards.isFourOfAKind(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "h6"};
        boolean actual6 = Cards.isFourOfAKind(cards6);
        assertEquals(true, actual6);
        assertEquals(Cards.Combination.FourOfAKind, Cards.evaluate(cards6));

        String[] cards7 = {"c7", "d7", "h7", "s7", "ck"};
        boolean actual7 = Cards.isFourOfAKind(cards7);
        assertEquals(true, actual7);
        assertEquals(Cards.Combination.FourOfAKind, Cards.evaluate(cards7));

        String[] cardsList = {"8", "9", "t", "j", "Q", "K", "A"};
        for (String c : cardsList) {
            ArrayList<String> fourOfKindList = new ArrayList<>();
            for (char suit : Cards.CARD_SUIT_LIST) {
                fourOfKindList.add(suit + c);
            }
            String[] cs = fourOfKindList.toArray(new String[4]);
            assertEquals(true, Cards.isFourOfAKind(cs));
            assertEquals(Cards.Combination.FourOfAKind, Cards.evaluate(cs));
        }
    }

    @Test
    public void isStraightFlush() {
        assertEquals(0x11111L, 1 | (1 << 4) | (1 << 8) | 1 << 12 | 1 << 16);
        String[] cards = {"d2", "d3", "d4", "d5", "d6"};
        assertEquals(0x11111L, Cards.getCardsValues(cards));

        String[] cards1 = {"dt", "dj", "d2", "da", "dk"};
        boolean actual1 = Cards.isStraightFlush(cards1);
        assertEquals(false, actual1);

        String[] cards2 = {"s2", "s3", "s4", "s5", "s6"};
        boolean actual2 = Cards.isStraightFlush(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.StraightFlush, Cards.evaluate(cards2));

        String[] cards3 = {"h2", "h3", "s4", "h5", "h6"};
        boolean actual3 = Cards.isStraightFlush(cards3);
        assertEquals(false, actual3);

        String[] cards4 = {"c7", "c6", "c9", "c8", "ct"};
        long x = 0x22222L;
        assertEquals(x << ((6 - 2) * 4), Cards.getCardsValues(cards4));
        boolean actual4 = Cards.isStraightFlush(cards4);
        assertEquals(true, actual4);
        assertEquals(Cards.Combination.StraightFlush, Cards.evaluate(cards4));

        String[] cards5 = {"ct", "cj", "cq", "c9", "ck"};
        boolean actual5 = Cards.isStraightFlush(cards5);
        assertEquals(true, actual5);
        assertEquals(Cards.Combination.StraightFlush, Cards.evaluate(cards5));

        String[] cards8 = {"sa", "s2", "s3", "s4", "s5"};
        boolean actual8 = Cards.isStraight(cards3);
        assertEquals(true, actual8);
        assertEquals(Cards.Combination.StraightFlush, Cards.evaluate(cards8));
    }

    @Test
    public void isRoyalFlush() {
        String[] cards1 = {"dt", "dj", "dq", "da", "dk"};
        boolean actual1 = Cards.isRoyalFlush(cards1);
        assertEquals(true, actual1);
        assertEquals(Cards.Combination.RoyalFlush, Cards.evaluate(cards1));

        String[] cards2 = {"st", "sj", "sq", "sa", "sk"};
        boolean actual2 = Cards.isRoyalFlush(cards2);
        assertEquals(true, actual2);
        assertEquals(Cards.Combination.RoyalFlush, Cards.evaluate(cards2));

        String[] cards3 = {"ht", "hj", "hq", "ha", "hk"};
        boolean actual3 = Cards.isRoyalFlush(cards3);
        assertEquals(true, actual3);
        assertEquals(Cards.Combination.RoyalFlush, Cards.evaluate(cards3));

        String[] cards4 = {"ct", "cj", "cq", "ca", "ck"};
        boolean actual4 = Cards.isRoyalFlush(cards4);
        assertEquals(true, actual4);
        assertEquals(Cards.Combination.RoyalFlush, Cards.evaluate(cards4));
    }
}