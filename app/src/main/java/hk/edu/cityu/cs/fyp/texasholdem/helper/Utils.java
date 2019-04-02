package hk.edu.cityu.cs.fyp.texasholdem.helper;

import android.content.Context;

import java.util.List;
import java.util.Random;

import hk.edu.cityu.cs.fyp.texasholdem.model.Cards;
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

    public static int suitOfCard(String cardString) {
        cardString = cardString.toLowerCase();
        char cardSuit = cardString.charAt(0);
        return Cards.CARD_SUIT_LIST.indexOf(cardSuit);
    }

    public static int valueOfCard(String cardString) {
        cardString = cardString.toLowerCase();
        char cardNumber = cardString.charAt(1);
        return Cards.CARD_NUMBER_LIST.indexOf(cardNumber);
    }

    public static boolean isPossibleOf(int denominator) {
        return isPossibleOf(denominator, random);
    }

    public static boolean isPossibleOf(int denominator, Random random) {
        return isPossibleOf(1, denominator, random);
    }

    public static boolean isPossibleOf(int numerator, int denominator) {
        return isPossibleOf(numerator, denominator, random);
    }

    public static boolean isPossibleOf(int numerator, int denominator, Random random) {
        return random.nextInt(denominator) < numerator;
    }

    public static int[] toIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static String toNumSuitFormat(String cardStr) {
        return String.valueOf(cardStr.charAt(1)).toUpperCase() + cardStr.charAt(0);
    }
}
