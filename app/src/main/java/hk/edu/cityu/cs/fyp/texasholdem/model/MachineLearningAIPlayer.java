package hk.edu.cityu.cs.fyp.texasholdem.model;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SocketHelper;

public class MachineLearningAIPlayer extends AIPlayer {

    public static final String NAME = "Machine Learning AIPlayer";
    private static final String TAG = "MachineLearningAIPlayer";
    private SocketHelper socketHelper = SocketHelper.getInstance();
    private TexasHoldem texasHoldem;
    private Activity activity;
    private Random random = new Random();
    private int[] raiseX = {1, 2, 4, 8};

    enum Action {
        Fold,
        Call,
        Raise,
    }

    public MachineLearningAIPlayer(Activity activity) {
        this.activity = activity;
    }

    SocketHelper.SocketListener socketListener = new SocketHelper.SocketListener() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.d(TAG, "onResponse: " + jsonObject.toString());
            if (jsonObject.has(Constants.Json.KEY_OUTPUT)) {
                try {
                    JSONArray outputArray = jsonObject.getJSONArray(Constants.Json.KEY_OUTPUT);
                    double probFold = outputArray.getDouble(0);
                    double probCall = outputArray.getDouble(1);
                    double probRaise = outputArray.getDouble(2);
                    if (texasHoldem == null)
                        return;
                    // TODO: take action

                    activity.runOnUiThread(() -> {
                        Action a = getAction(probFold, probCall, probRaise);
                        if (a == Action.Fold) {
                            texasHoldem.computerFold();
                        } else if (a == Action.Call) {
                            texasHoldem.computerCall();
                        } else {
                            int bet = calculateRaiseBets(probCall, probRaise);
                            if (bet == 0) {
                                texasHoldem.computerCall();
                            } else {
                                texasHoldem.computerRaise(bet);
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }

            }
        }

        ;

        @Override
        public void onError(String errorMsg) {
            Log.e(TAG, "onError: " + errorMsg);
        }
    };

    private Action getAction(double f, double c, double r) {

        double rand = random.nextDouble();
        boolean maybeFold = texasHoldem.getGameState() != TexasHoldem.GameState.PlayerCalled;

        if (f > c && f > r)
            return Action.Fold;

        double l1, l2;
        l1 = c;
        l2 = l1 + r;


        double ran;
        while (true) {
            ran = random.nextDouble();
            if (ran < l1) {
                return Action.Call;
            } else if (ran < l2) {
                return Action.Raise;
            }
        }

    }

    @Override
    public void takeAction(TexasHoldem texasHoldem) {
        this.texasHoldem = texasHoldem;
        socketHelper.connectToServer(socketListener);

        int[] turnArray = new int[]{0, 0, 0, 0};
        turnArray[texasHoldem.getTurn()] = 1;
        double potRatio = (double) (texasHoldem.getComputerBetsInThisRound() / texasHoldem.getTotalBets());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Json.KEY_ACTION, 2);
            JSONArray jsonArray = new JSONArray();
            // turn
            for (int t : turnArray) {
                jsonArray.put(t);
            }
            // cards
            for (double d : texasHoldem.getSocketCallWithCards()) {
                jsonArray.put(d);
            }
            // pot ratio
            jsonArray.put(potRatio);
            jsonObject.put(Constants.Json.KEY_INPUT, jsonArray);
            Log.d(TAG, "takeAction: " + jsonObject.toString());
            socketHelper.sent(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "takeAction: " + e.getLocalizedMessage());
        }
    }

    public int calculateRaiseBets(double c, double r) {

        int raiseXIndex = random.nextInt(4);
        int raiseBet = raiseX[raiseXIndex] * texasHoldem.getMinBet();

        return raiseBet;
    }

    public void disconnectSocket() {
        socketHelper.disconnect();
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
