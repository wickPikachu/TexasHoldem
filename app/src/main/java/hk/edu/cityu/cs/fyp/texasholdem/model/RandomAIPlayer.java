package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.Random;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;

public class RandomAIPlayer extends AIPlayer {

    public static final String NAME = "Random AIPlayer";

    private Random random;

    public RandomAIPlayer() {
        this.random = new Random();
    }

    public RandomAIPlayer(int randomSeed) {
        this.random = new Random(randomSeed);
    }

    @Override
    public void takeAction(TexasHoldem texasHoldem) {
        int playerMoney = texasHoldem.getPlayerMoney();
        int computerMoney = texasHoldem.getComputerMoney();
        int playerBets = texasHoldem.getPlayerBets();

        // determine fold or not
        if (Utils.isPossibleOf(playerBets, playerMoney * 10, random)) {
            texasHoldem.computerFold();
            return;
        }

        // determine raise or not
        if (Utils.isPossibleOf(2) && Math.min(playerMoney, computerMoney) > 0) {
            try {
                int raiseBet = random.nextInt(Math.min(playerMoney, computerMoney)) / 100 * 100;
                if (raiseBet > 0) {
                    texasHoldem.computerRaise(raiseBet);
                } else {
                    texasHoldem.computerCall();
                }

            } catch (TexasHoldemException e) {
                e.printStackTrace();
            }
        } else {
            // call
            texasHoldem.computerCall();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getConstantValue() {
        return Constants.AI_PLAYER_RANDOM;
    }
}
