package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;

import hk.edu.cityu.cs.fyp.texasholdem.model.MachineLearningAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.MiniMaxAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.RandomAIPlayer;

public class Utils {

    public static int getDrawableResByString(Context context, String cardStr) {
        return context.getResources().getIdentifier(cardStr, "drawable", context.getPackageName());
    }

    public static int getAIPlayerValue(String aiPlayerName) {
        switch (aiPlayerName) {
            default:
            case RandomAIPlayer.NAME:
                return Constants.AI_PLAYER_RANDOM;
            case MiniMaxAIPlayer.NAME:
                return Constants.AI_PLAYER_MINIMAX;
            case MachineLearningAIPlayer.NAME:
                return Constants.AI_PLAYER_MACHINE_LEARNING;
        }
    }

}
