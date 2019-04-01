package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;
import android.content.SharedPreferences;

import hk.edu.cityu.cs.fyp.texasholdem.model.AIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.MachineLearningAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.MiniMaxAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.RandomAIPlayer;

public class SharedPreferencesHelper {

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.SharedPref.NAME, Context.MODE_PRIVATE);
    }

    public static AIPlayer getAIPlayer(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        switch (pref.getInt(Constants.SharedPref.KEY_AI_PLAYER, -1)) {
            default:
            case Constants.AI_PLAYER_RANDOM:
                return new RandomAIPlayer();
            case Constants.AI_PLAYER_MINIMAX:
                return new MiniMaxAIPlayer();
            case Constants.AI_PLAYER_MACHINE_LEARNING:
                return new MachineLearningAIPlayer();
        }
    }

    public static void setAIPlayer(Context context, int aiPlayerValue) {
        SharedPreferences pref = getSharedPreferences(context);
        pref.edit().putInt(Constants.SharedPref.KEY_AI_PLAYER, aiPlayerValue).apply();
    }

    public static boolean getIsAIPlayerAutoSync(Context context, int aiPlayerValue) {
        SharedPreferences pref = getSharedPreferences(context);
        switch (aiPlayerValue) {
            default:
            case Constants.AI_PLAYER_RANDOM:
                return pref.getBoolean(Constants.SharedPref.KEY_AI_PLAYER_RANDOM_AUTO_SYNC, false);
            case Constants.AI_PLAYER_MINIMAX:
                return pref.getBoolean(Constants.SharedPref.KEY_AI_PLAYER_MINIMAX_AUTO_SYNC, false);
            case Constants.AI_PLAYER_MACHINE_LEARNING:
                return pref.getBoolean(Constants.SharedPref.KEY_AI_PLAYER_MACHINE_LEARNING_AUTO_SYNC, false);
        }
    }

    public static void setAIPlayerAutoSync(Context context, int aiPlayerValue, boolean isAutoSync) {
        SharedPreferences pref = getSharedPreferences(context);
        switch (aiPlayerValue) {
            default:
            case Constants.AI_PLAYER_RANDOM:
                pref.edit().putBoolean(Constants.SharedPref.KEY_AI_PLAYER_RANDOM_AUTO_SYNC, isAutoSync).apply();
                break;
            case Constants.AI_PLAYER_MINIMAX:
                pref.edit().putBoolean(Constants.SharedPref.KEY_AI_PLAYER_MINIMAX_AUTO_SYNC, isAutoSync).apply();
                break;
            case Constants.AI_PLAYER_MACHINE_LEARNING:
                pref.edit().putBoolean(Constants.SharedPref.KEY_AI_PLAYER_MACHINE_LEARNING_AUTO_SYNC, isAutoSync).apply();
                break;
        }
    }
}
