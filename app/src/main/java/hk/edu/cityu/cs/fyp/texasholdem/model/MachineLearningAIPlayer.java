package hk.edu.cityu.cs.fyp.texasholdem.model;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;

public class MachineLearningAIPlayer extends AIPlayer {

    public static final String NAME = "Machine Learning AIPlayer";

    @Override
    public void takeAction(TexasHoldem texasHoldem) {
        // TODO: actions
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getConstantValue() {
        return Constants.AI_PLAYER_MACHINE_LEARNING;
    }
}
