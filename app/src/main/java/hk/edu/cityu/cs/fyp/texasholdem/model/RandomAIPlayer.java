package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.Random;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;

public class RandomAIPlayer extends AIPlayer {

    public static final String NAME = "Random AIPlayer";

    private Random random;
    private int[] raiseX = {1, 2, 4, 8};

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

        TexasHoldem.GameState gameState = texasHoldem.getGameState();
        boolean maybeFold = gameState != TexasHoldem.GameState.PlayerCalled;

        // determine fold or not
        if (maybeFold && texasHoldem.getTurn() != 0 && Utils.isPossibleOf(10, random)) {
            texasHoldem.computerFold();
            return;
        }

        // determine raise or not
        if (Utils.isPossibleOf(2) && Math.min(playerMoney, computerMoney) > 0) {

            int raiseXIndex = random.nextInt(4);
            int raiseBet = raiseX[raiseXIndex] * texasHoldem.getMinBet();

            if (raiseBet > 0) {
                texasHoldem.computerRaise(raiseBet);
            } else {
                texasHoldem.computerCall();
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
