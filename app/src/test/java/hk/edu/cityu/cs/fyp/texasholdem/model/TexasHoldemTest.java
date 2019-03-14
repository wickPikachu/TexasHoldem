package hk.edu.cityu.cs.fyp.texasholdem.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TexasHoldemTest {

    TexasHoldem texasHoldem = null;

    @Before
    public void setUp() throws Exception {
        texasHoldem = TexasHoldem.getInstance();
        texasHoldem.init();
    }

    @Test
    public void startRound() {
        assertEquals(20000, texasHoldem.getComputerMoney());
        assertEquals(20000, texasHoldem.getPlayerMoney());
        assertEquals(0, texasHoldem.getComputerBets());
        assertEquals(0, texasHoldem.getComputerBets());
        assertEquals(0, texasHoldem.getTotalBets());

        texasHoldem.startRound();
        assertEquals(20000 - TexasHoldem.BIG_BLIND_BET, texasHoldem.getComputerMoney());
        assertEquals(20000 - TexasHoldem.BIG_BLIND_BET / 2, texasHoldem.getPlayerMoney());
    }

    @Test
    public void next() {
    }

    @Test
    public void endRound() {
    }

    @Test
    public void playerFold() {
    }

    @Test
    public void playerCall() {
    }

    @Test
    public void playerRaise() {
    }

    @Test
    public void computerFold() {
    }

    @Test
    public void computerCall() {
    }

    @Test
    public void computerRaise() {
    }

    @Test
    public void takeAction() {
    }

    @Test
    public void canPlayerFold() {
    }

    @Test
    public void canPlayerCall() {
    }

    @Test
    public void canPlayerRaise() {
    }

    @Test
    public void canComputerFold() {
    }

    @Test
    public void canComputerCall() {
    }

    @Test
    public void canComputerRaise() {
    }

    @Test
    public void isPlayerTurn() {
    }

    @Test
    public void isComputerTurn() {
    }

    @Test
    public void isBothCalled() {
    }

    @Test
    public void getGameState() {
    }

    @Test
    public void getGameLogResults() {
    }

    @Test
    public void getRounds() {
    }

    @Test
    public void getTotalBets() {
    }

    @Test
    public void getPlayerMoney() {
    }

    @Test
    public void getPlayerBets() {
    }

    @Test
    public void getComputerMoney() {
    }

    @Test
    public void getComputerBets() {
    }

    @Test
    public void getPlayerCardList() {
    }

    @Test
    public void getComputerCardList() {
    }

    @Test
    public void getTableCardList() {
    }

    @Test
    public void isPlayerBuildBets() {
    }

    @Test
    public void getMessage() {
    }
}