package hk.edu.cityu.cs.fyp.texasholdem;


import org.junit.Test;

import java.util.HashMap;

import hk.edu.cityu.cs.fyp.texasholdem.model.Cards;
import hk.edu.cityu.cs.fyp.texasholdem.model.MiniMaxAIPlayer;

public class AIPlayerTest {

    @Test
    public void testMiniMaxAIPlayer() {
        MiniMaxAIPlayer miniMaxAIPlayer = new MiniMaxAIPlayer();
        HashMap<Cards.Combination, Double> probMap =miniMaxAIPlayer.evaluateProbMap(Cards.getCardsValues(new String[]{"s2", "s4"}));
        int a = 0;

    }
}