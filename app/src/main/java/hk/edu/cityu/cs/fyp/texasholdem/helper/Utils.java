package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;

import java.util.Random;

import hk.edu.cityu.cs.fyp.texasholdem.model.MachineLearningAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.MiniMaxAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.RandomAIPlayer;

public class Utils {

    private static Random random = new Random();

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

    public static boolean getProbabilityOf(int denominator) {
        return getProbabilityOf(denominator, random);
    }

    public static boolean getProbabilityOf(int denominator, Random random) {
        return getProbabilityOf(1, denominator, random);
    }

    public static boolean getProbabilityOf(int numerator, int denominator) {
        return getProbabilityOf(numerator, denominator, random);
    }

    public static boolean getProbabilityOf(int numerator, int denominator, Random random) {
        return random.nextInt(denominator) < numerator;
    }

}
