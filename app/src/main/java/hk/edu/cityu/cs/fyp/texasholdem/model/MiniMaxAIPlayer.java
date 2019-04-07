package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.HashMap;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;

@Deprecated
public class MiniMaxAIPlayer extends AIPlayer {

    public static final String TAG = "MiniMaxAIPlayer";
    public static final String NAME = "MiniMax AIPlayer";

    private long originalCards = 0xFFFFFFFFFFFFFL;
    private HashMap<Cards.Combination, Double> probMap = new HashMap<>();
    private long cards = 0L;

    private int getTurn(long cards) {
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

    public HashMap<Cards.Combination, Double> evaluateProbMap(long cards) {
        HashMap<Cards.Combination, Long> tempMap = new HashMap<>();
        tempMap.put(Cards.Combination.None, 0L);
        tempMap.put(Cards.Combination.Pair, 0L);
        tempMap.put(Cards.Combination.TwoPairs, 0L);
        tempMap.put(Cards.Combination.ThreeOfAKind, 0L);
        tempMap.put(Cards.Combination.Straight, 0L);
        tempMap.put(Cards.Combination.Flush, 0L);
        tempMap.put(Cards.Combination.FullHouse, 0L);
        tempMap.put(Cards.Combination.FourOfAKind, 0L);
        tempMap.put(Cards.Combination.StraightFlush, 0L);
        tempMap.put(Cards.Combination.RoyalFlush, 0L);
        long allCards = 0xFFFFFFFFFFFFFL;
        int numberOfCards = Cards.getNumberOfCards(cards);
        int needs = 7 - numberOfCards;
        allCards &= ~(cards);
        long total = 0;
//        for (int n = 0; n < needs; n++) {
//            long _cards = cards;
//            for (int i = 0; i < 52; i++) {
//                _cards |= ((allCards >> i) & 1);
//                if (Cards.getNumberOfCards(_cards) == 7) {
//                    Cards c = new Cards(_cards);
//                    c.evaluate();
//                    long count = 0;
//                    if (c.getCombination() != null || tempMap.get(c.getCombination()) != null) {
//                        count = tempMap.get(c.getCombination());
//                    }
//                }
//            }
//        }
        total = _evaluateProbMap(tempMap, cards, 0x0L, numberOfCards - 1);

        HashMap<Cards.Combination, Double> probMap = new HashMap<>();
        probMap.put(Cards.Combination.None, (double) tempMap.get(Cards.Combination.None) / total);
        probMap.put(Cards.Combination.Pair, (double) tempMap.get(Cards.Combination.Pair) / total);
        probMap.put(Cards.Combination.TwoPairs, (double) tempMap.get(Cards.Combination.TwoPairs) / total);
        probMap.put(Cards.Combination.ThreeOfAKind, (double) tempMap.get(Cards.Combination.ThreeOfAKind) / total);
        probMap.put(Cards.Combination.Straight, (double) tempMap.get(Cards.Combination.Straight) / total);
        probMap.put(Cards.Combination.Flush, (double) tempMap.get(Cards.Combination.Flush) / total);
        probMap.put(Cards.Combination.FullHouse, (double) tempMap.get(Cards.Combination.FullHouse) / total);
        probMap.put(Cards.Combination.FourOfAKind, (double) tempMap.get(Cards.Combination.FourOfAKind) / total);
        probMap.put(Cards.Combination.StraightFlush, (double) tempMap.get(Cards.Combination.StraightFlush) / total);
        probMap.put(Cards.Combination.RoyalFlush, (double) tempMap.get(Cards.Combination.RoyalFlush) / total);
        return probMap;
    }

    private int _evaluateProbMap(HashMap<Cards.Combination, Long> tempMap, long cards, long addedCards, int n) {
        int total = 0;
        if (n >= 7)
            return total;
        cards |= addedCards;
        if (Cards.getNumberOfCards(cards) == 7) {
            Cards c = new Cards(cards);
            c.evaluate();
            long count = 0;
            if (c.getCombination() != null || tempMap.get(c.getCombination()) != null) {
                count = tempMap.get(c.getCombination());
            }
            tempMap.put(c.getCombination(), count);
        } else {
            for (int i = 0; i < 52; i++) {
                total += _evaluateProbMap(tempMap, cards, (0x1L << i), n + 1);
            }
        }
        return total;
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
