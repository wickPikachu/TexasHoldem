package hk.edu.cityu.cs.fyp.texasholdem.model;

import org.junit.Before;
import org.junit.Test;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;

import static org.junit.Assert.*;

public class TexasHoldemTest {

    TexasHoldem texasHoldem = null;

    @Before
    public void setUp() throws Exception {
        texasHoldem = TexasHoldem.getInstance();
        texasHoldem.setSaveLogs(false);
        texasHoldem.init(new RandomAIPlayer(), new TexasHoldem.TexasHoldemListener() {
            @Override
            public void afterComputerTakeAction() {

            }

            @Override
            public void onGameIsFinished() {

            }
        });
    }

    @Test
    public void startRound() throws TexasHoldemException {
        int playerMoney, computerMoney, totalBets;
        assertEquals(20000, texasHoldem.getComputerMoney());
        assertEquals(20000, texasHoldem.getPlayerMoney());
        assertEquals(0, texasHoldem.getComputerBets());
        assertEquals(0, texasHoldem.getComputerBets());
        assertEquals(0, texasHoldem.getTotalBets());

        texasHoldem.startRound();
        assertEquals(20000 - TexasHoldem.BIG_BLIND_BET, texasHoldem.getComputerMoney());
        assertEquals(20000 - TexasHoldem.BIG_BLIND_BET / 2, texasHoldem.getPlayerMoney());

        texasHoldem.playerCall();
        assertEquals(20000 - TexasHoldem.BIG_BLIND_BET, texasHoldem.getComputerMoney());
        assertEquals(20000 - TexasHoldem.BIG_BLIND_BET, texasHoldem.getPlayerMoney());
        assertEquals(40000,texasHoldem.getTotalBets()+texasHoldem.getPlayerMoney()+texasHoldem.getComputerMoney());



        playerMoney = texasHoldem.getPlayerMoney();
        computerMoney = texasHoldem.getComputerMoney();
        texasHoldem.computerRaise(1000);
        assertEquals(computerMoney - 1000, texasHoldem.getComputerMoney());
        assertEquals(playerMoney, texasHoldem.getPlayerMoney());
        assertEquals(40000,texasHoldem.getTotalBets()+texasHoldem.getPlayerMoney()+texasHoldem.getComputerMoney());

        texasHoldem.playerCall();
        assertEquals(computerMoney - 1000, texasHoldem.getComputerMoney());
        assertEquals(computerMoney - 1000, texasHoldem.getPlayerMoney());
        assertEquals(40000,texasHoldem.getTotalBets()+texasHoldem.getPlayerMoney()+texasHoldem.getComputerMoney());

        playerMoney = texasHoldem.getPlayerMoney();
        computerMoney = texasHoldem.getComputerMoney();
        totalBets = texasHoldem.getTotalBets();

        texasHoldem.computerFold();
        assertEquals(40000,texasHoldem.getPlayerMoney()+texasHoldem.getComputerMoney());

        // next round
        texasHoldem.startRound();


    }
}