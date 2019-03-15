package hk.edu.cityu.cs.fyp.texasholdem.model;

public class MiniMaxAIPlayer extends AIPlayer {

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

    // Returns optimal value for
    // current player (Initially called
    // for root and maximizer)
    static int minimax(int depth, int nodeIndex,
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

}
