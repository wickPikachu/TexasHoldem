package hk.edu.cityu.cs.fyp.texasholdem.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardDecisionsTest {

    @Test
    public void getValues() {
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
    }

    @Test
    public void isTwoPair() {
    }

    @Test
    public void isFullHouse() {
    }

    @Test
    public void isStraight() {
    }

    @Test
    public void isThreeOfAKind() {
    }

    @Test
    public void isFlush() {
    }

    @Test
    public void isFourOfAKind() {
    }

    @Test
    public void isRoyalFlush() {
        String[] cards1 = {"dt", "dj", "dq", "da", "dk"};
        boolean actual1 = CardDecisions.isRoyalFlush(cards1);
        assertEquals(true, actual1);

        String[] cards2 = {"dt", "dj", "dq", "da", "dk"};
        boolean actual2 = CardDecisions.isRoyalFlush(cards2);
        assertEquals(true, actual2);

        String[] cards3 = {"dt", "dj", "dq", "da", "dk"};
        boolean actual3 = CardDecisions.isRoyalFlush(cards3);
        assertEquals(true, actual3);

        String[] cards4 = {"dt", "dj", "dq", "da", "dk"};
        boolean actual4 = CardDecisions.isRoyalFlush(cards4);
        assertEquals(true, actual4);
    }
}