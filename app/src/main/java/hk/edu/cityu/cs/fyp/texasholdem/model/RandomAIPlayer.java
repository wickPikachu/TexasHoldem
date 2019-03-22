package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.Random;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;
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
        int playerBets = texasHoldem.getPlayerBets();

        // determine fold or not
        if (Utils.isPossibleOf(playerBets, playerMoney * 2, random)) {
            texasHoldem.computerFold();
            return;
        }

        // determine raise or not
        if (Utils.isPossibleOf(2)) {
            try {
                texasHoldem.computerRaise((random.nextInt(texasHoldem.getComputerMoney() / 100 - 1) + 1) * 100);
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
}
