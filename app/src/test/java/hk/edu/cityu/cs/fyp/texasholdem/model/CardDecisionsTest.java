package hk.edu.cityu.cs.fyp.texasholdem.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardDecisionsTest {

    @Test
    public void getValues() {
        assertEquals('d', CardDecisions.cardSuitList.get(0).charValue());
        assertEquals('c', CardDecisions.cardSuitList.get(1).charValue());
        assertEquals('h', CardDecisions.cardSuitList.get(2).charValue());
        assertEquals('s', CardDecisions.cardSuitList.get(3).charValue());

        assertEquals('2', CardDecisions.cardNumberList.get(0).charValue());
        assertEquals('3', CardDecisions.cardNumberList.get(1).charValue());
        assertEquals('4', CardDecisions.cardNumberList.get(2).charValue());
        assertEquals('5', CardDecisions.cardNumberList.get(3).charValue());
        assertEquals('6', CardDecisions.cardNumberList.get(4).charValue());
        assertEquals('7', CardDecisions.cardNumberList.get(5).charValue());
        assertEquals('8', CardDecisions.cardNumberList.get(6).charValue());
        assertEquals('9', CardDecisions.cardNumberList.get(7).charValue());
        assertEquals('t', CardDecisions.cardNumberList.get(8).charValue());
        assertEquals('j', CardDecisions.cardNumberList.get(9).charValue());
        assertEquals('q', CardDecisions.cardNumberList.get(10).charValue());
        assertEquals('k', CardDecisions.cardNumberList.get(11).charValue());
        assertEquals('a', CardDecisions.cardNumberList.get(12).charValue());
    }

    @Test
    public void addCard() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void getValues1() {
    }

    @Test
    public void addCard1() {
    }

    @Test
    public void valueOfCard() {
    }

    @Test
    public void eval() {
    }

    @Test
    public void clear1() {
    }

    @Test
    public void isPair() {
        String[] cards1 = {"s3", "d3", "d4", "d5", "d6"};
        boolean actual1 = CardDecisions.isPair(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"d2", "s2", "d4", "d5", "h6"};
        boolean actual2 = CardDecisions.isPair(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"sa", "ha", "hq", "dj", "dk"};
        boolean actual3 = CardDecisions.isPair(cards3);
        assertEquals(true, actual3);

        String[] cards4 = {"c3", "ca", "ck", "c6", "c2", "d6"};
        boolean actual4 = CardDecisions.isPair(cards4);
        assertEquals(true, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = CardDecisions.isPair(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = CardDecisions.isPair(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = CardDecisions.isPair(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isTwoPair() {
        String[] cards1 = {"d2", "d3", "s2", "s3", "d6"};
        boolean actual1 = CardDecisions.isTwoPair(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"da", "d2", "s2", "ha", "h6"};
        boolean actual2 = CardDecisions.isTwoPair(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"sa", "ha", "hq", "sq", "da"};
        boolean actual3 = CardDecisions.isTwoPair(cards3);
        assertEquals(false, actual3);

        String[] cards4 = {"c3", "ca", "ck", "c6", "c2", "d6"};
        boolean actual4 = CardDecisions.isTwoPair(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = CardDecisions.isTwoPair(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = CardDecisions.isTwoPair(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = CardDecisions.isTwoPair(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isFullHouse() {
        String[] cards1 = {"d2", "d3", "s2", "h3", "c2"};
        boolean actual1 = CardDecisions.isFullHouse(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"da", "sa", "ca", "dk", "hk"};
        boolean actual2 = CardDecisions.isFullHouse(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"sa", "ha", "hq", "sq", "da"};
        boolean actual3 = CardDecisions.isFullHouse(cards3);
        assertEquals(true, actual3);

        String[] cards4 = {"c3", "ca", "ck", "c6", "c2", "d6"};
        boolean actual4 = CardDecisions.isFullHouse(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = CardDecisions.isFullHouse(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = CardDecisions.isFullHouse(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = CardDecisions.isFullHouse(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isStraight() {
        String[] cards1 = {"d2", "d3", "d4", "d5", "d6"};
        boolean actual1 = CardDecisions.isStraight(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"d2", "d3", "d4", "d5", "h6"};
        boolean actual2 = CardDecisions.isStraight(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"st", "ha", "hq", "dj", "dk"};
        boolean actual3 = CardDecisions.isStraight(cards3);
        assertEquals(true, actual3);

        String[] cards4 = {"c3", "ca", "ck", "c6", "c2", "d6"};
        boolean actual4 = CardDecisions.isStraight(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = CardDecisions.isStraight(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = CardDecisions.isStraight(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = CardDecisions.isStraight(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isThreeOfAKind() {
        String[] cards1 = {"d2", "d3", "d4", "d5", "dk"};
        boolean actual1 = CardDecisions.isThreeOfAKind(cards1);
        assertEquals(false, actual1);

        String[] cards2 = {"st", "s8", "sa", "s3", "s6"};
        boolean actual2 = CardDecisions.isThreeOfAKind(cards2);
        assertEquals(false, actual2);

        String[] cards3 = {"h4", "h5", "h6", "h7", "h9"};
        boolean actual3 = CardDecisions.isThreeOfAKind(cards3);
        assertEquals(false, actual3);

        String[] cards4 = {"c3", "ca", "ck", "d2", "d3"};
        boolean actual4 = CardDecisions.isThreeOfAKind(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"ha", "da", "ca", "cj", "cq"};
        boolean actual5 = CardDecisions.isThreeOfAKind(cards5);
        assertEquals(true, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c7"};
        boolean actual6 = CardDecisions.isThreeOfAKind(cards6);
        assertEquals(true, actual6);

        String[] cards7 = {"c2", "h2", "d2", "d7", "dk"};
        boolean actual7 = CardDecisions.isThreeOfAKind(cards7);
        assertEquals(true, actual7);
    }

    @Test
    public void isFlush() {
        assertEquals(0x1L, 1L);
        assertEquals(0x2L, 1L << 1);
        assertEquals(0x4L, 1L << 2);
        assertEquals(0x8L, 1L << 3);

        String[] cards1 = {"d2", "d3", "d4", "d5", "dk"};
        boolean actual1 = CardDecisions.isFlush(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"st", "s8", "sa", "s3", "s6"};
        boolean actual2 = CardDecisions.isFlush(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"h4", "h5", "h6", "h7", "h9"};
        boolean actual3 = CardDecisions.isFlush(cards3);
        assertEquals(true, actual3);

        String[] cards4 = {"c3", "ca", "ck", "d2", "d3"};
        boolean actual4 = CardDecisions.isFlush(cards4);
        assertEquals(false, actual4);

        String[] cards5 = {"c3", "ca", "ck", "cj", "cq"};
        boolean actual5 = CardDecisions.isFlush(cards5);
        assertEquals(true, actual5);

        String[] cards6 = {"c6", "d6", "s6", "c6"};
        boolean actual6 = CardDecisions.isFlush(cards6);
        assertEquals(false, actual6);

        String[] cards7 = {"c7", "c7", "c7", "d7", "dk"};
        boolean actual7 = CardDecisions.isFlush(cards7);
        assertEquals(false, actual7);
    }

    @Test
    public void isFourOfAKind() {
        assertEquals(0xFL, 15);
        String[] cards = {"d2", "s2", "c2", "h2"};
        assertEquals(0xFL, CardDecisions.getCardsValues(cards));

        String[] cards1 = {"d2", "s2", "c2", "h2", "dk"};
        boolean actual1 = CardDecisions.isFourOfAKind(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"d3", "s3", "c3", "h3", "s6"};
        boolean actual2 = CardDecisions.isFourOfAKind(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"d4", "s4", "c4", "h4", "h6"};
        boolean actual3 = CardDecisions.isFourOfAKind(cards3);
        assertEquals(true, actual3);

        String[] cards4 = {"c7", "d5", "s5", "c5", "h5",};
        boolean actual4 = CardDecisions.isFourOfAKind(cards4);
        assertEquals(true, actual4);

        String[] cards5 = {"ct", "cj", "cq", "ca", "ck"};
        boolean actual5 = CardDecisions.isFourOfAKind(cards5);
        assertEquals(false, actual5);

        String[] cards6 = {"c6", "d6", "s6", "h6"};
        boolean actual6 = CardDecisions.isFourOfAKind(cards6);
        assertEquals(true, actual6);

        String[] cards7 = {"c7", "d7", "h7", "s7", "ck"};
        boolean actual7 = CardDecisions.isFourOfAKind(cards7);
        assertEquals(true, actual7);

        String[] cardsList = {"8", "9", "t", "j", "Q", "K", "A"};
        for (String c : cardsList) {
            ArrayList<String> fourOfKindList = new ArrayList<>();
            for (char suit : CardDecisions.cardSuitList) {
                fourOfKindList.add(suit + c);
            }
            assertEquals(true, CardDecisions.isFourOfAKind(fourOfKindList.toArray(new String[4])));
        }
    }

    @Test
    public void isStraightFlush() {
        assertEquals(0x11111L, 1 | (1 << 4) | (1 << 8) | 1 << 12 | 1 << 16);
        String[] cards = {"d2", "d3", "d4", "d5", "d6"};
        assertEquals(0x11111L, CardDecisions.getCardsValues(cards));

        String[] cards1 = {"dt", "dj", "d2", "da", "dk"};
        boolean actual1 = CardDecisions.isStraightFlush(cards1);
        assertEquals(false, actual1);

        String[] cards2 = {"s2", "s3", "s4", "s5", "s6"};
        boolean actual2 = CardDecisions.isStraightFlush(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"h2", "h3", "s4", "h5", "h6"};
        boolean actual3 = CardDecisions.isStraightFlush(cards3);
        assertEquals(false, actual3);

        String[] cards4 = {"c7", "c6", "c9", "c8", "ct"};
        long x = 0x22222L;
        assertEquals(x << ((6 - 2) * 4), CardDecisions.getCardsValues(cards4));
        boolean actual4 = CardDecisions.isStraightFlush(cards4);
        assertEquals(true, actual4);

        String[] cards5 = {"ct", "cj", "cq", "ca", "ck"};
        boolean actual5 = CardDecisions.isRoyalFlush(cards5);
        assertEquals(true, actual5);
    }

    @Test
    public void isRoyalFlush() {
        String[] cards1 = {"dt", "dj", "dq", "da", "dk"};
        boolean actual1 = CardDecisions.isRoyalFlush(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"st", "sj", "sq", "sa", "sk"};
        boolean actual2 = CardDecisions.isRoyalFlush(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"ht", "hj", "hq", "ha", "hk"};
        boolean actual3 = CardDecisions.isRoyalFlush(cards3);
        assertEquals(true, actual3);

        String[] cards4 = {"ct", "cj", "cq", "ca", "ck"};
        boolean actual4 = CardDecisions.isRoyalFlush(cards4);
        assertEquals(true, actual4);
    }
}