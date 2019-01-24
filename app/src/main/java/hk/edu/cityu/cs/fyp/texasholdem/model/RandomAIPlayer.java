package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.Random;

public class RandomAIPlayer extends AIPlayer {

    private Random random;

    public RandomAIPlayer() {
        this.random = new Random(100);
    }

    public RandomAIPlayer(int randomSeed) {
        this.random = new Random(randomSeed);
    }

    @Override
    public void takeAction(TexasHoldem texasHoldem) {
        switch (random.nextInt(3)) {
            case 0:
                texasHoldem.opponentFold();
            case 1:
                texasHoldem.opponentRaise((random.nextInt(texasHoldem.getOpponentMoney() / 100 - 1) + 1) * 100);
            case 2:
                texasHoldem.opponentCall();
        }
    }
}
