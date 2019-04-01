package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.HashMap;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;

public class MiniMaxAIPlayer extends AIPlayer {

    public static final String TAG = "MiniMaxAIPlayer";
    public static final String NAME = "MiniMax AIPlayer";

    double Pr2 = 4;
    double Pr3 = 4;
    double Pr4 = 4;
    double Pr5 = 4;
    double Pr6 = 4;
    double Pr7 = 4;
    double Pr8 = 4;
    double Pr9 = 4;
    double PrT = 4;
    double PrJ = 4;
    double PrQ = 4;
    double PrK = 4;
    double PrA = 4;

    private long originalCards = 0xFFFFFFFFFFFFFL;
    private HashMap<Cards.Combination, Double> probMap = new HashMap<>();
    private long cards = 0L;

    private int getNumberOfcards(long cards) {
        int num = 0;
        for (int i = 0; i < 52; i++) {
            if ((cards & 1L) == 1L) {
                num++;
            }
        }
        switch (num) {
            case 2:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 4;
        }
        // wrong state, maybe bug
        return -1;
    }

    private HashMap<Cards.Combination, Double> evalvateProbMap(long cards) {
        long total;
        total = 50 * 49 * 48 * 47 * 46;
        HashMap<Cards.Combination, Double> probMap = new HashMap<>();
        int numberOfCards = getNumberOfcards(cards);
        if (numberOfCards == 2) {

        } else if (numberOfCards == 5) {

        } else if (numberOfCards == 6) {

        } else if (numberOfCards == 7) {

        } else {

        }

        probMap.put(Cards.Combination.None, 1.0);
        probMap.put(Cards.Combination.Pair, 1.0);
        probMap.put(Cards.Combination.TwoPairs, 1.0);
        probMap.put(Cards.Combination.ThreeOfAKind, 1.0);
        probMap.put(Cards.Combination.Straight, 1.0);
        probMap.put(Cards.Combination.Flush, 1.0);
        probMap.put(Cards.Combination.FullHouse, 1.0);
        probMap.put(Cards.Combination.FourOfAKind, 1.0);
        probMap.put(Cards.Combination.StraightFlush, 1.0);
        probMap.put(Cards.Combination.RoyalFlush, 1.0);
        return probMap;
    }

    public int getBestAction() {

        return 0;
    }

    // Returns optimal value for
    // current player (Initially called
    // for root and maximizer)
    public int minimax(int depth, int nodeIndex,
                       Boolean maximizingPlayer,
                       int values[], int alpha,
                       int beta) {
        // Terminating condition. i.e
        // leaf node is reached
        if (depth == 3)
            return values[nodeIndex];

        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;

            // Recur for left and
            // right children
            for (int i = 0; i < 2; i++) {
                int val = minimax(depth + 1, nodeIndex * 2 + i,
                        false, values, alpha, beta);
                best = Math.max(best, val);
                alpha = Math.max(alpha, best);

                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            // Recur for left and
            // right children
            for (int i = 0; i < 2; i++) {

                int val = minimax(depth + 1, nodeIndex * 2 + i,
                        true, values, alpha, beta);
                best = Math.min(best, val);
                beta = Math.min(beta, best);

                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }
            return best;
        }
    }

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
        return Constants.AI_PLAYER_MINIMAX;
    }
}
