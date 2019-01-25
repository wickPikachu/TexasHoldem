package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.Random;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;

public class RandomAIPlayer extends AIPlayer {

    private Random random;

    public RandomAIPlayer() {
        this.random = new Random();
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
                try {
                    texasHoldem.opponentRaise((random.nextInt(texasHoldem.getOpponentMoney() / 100 - 1) + 1) * 100);
                } catch (TexasHoldemException e) {
                    e.printStackTrace();
                }
            case 2:
                texasHoldem.opponentCall();
        }
    }
}
