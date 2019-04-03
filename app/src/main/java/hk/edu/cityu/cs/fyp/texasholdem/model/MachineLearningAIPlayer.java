package hk.edu.cityu.cs.fyp.texasholdem.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SocketHelper;

public class MachineLearningAIPlayer extends AIPlayer {

    public static final String NAME = "Machine Learning AIPlayer";
    private static final String TAG = "MachineLearningAIPlayer";
    private SocketHelper socketHelper = SocketHelper.getInstance();
    private TexasHoldem texasHoldem;

    public MachineLearningAIPlayer() {
        socketHelper.connectToServer(new SocketHelper.SocketListener() {
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
                        if (probFold > probCall && probFold > probRaise) {
                            texasHoldem.computerFold();
                        } else if (probCall > probRaise) {
                            texasHoldem.computerCall();
                        } else {
                            int bet = 100;
                            texasHoldem.computerRaise(bet);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, "onError: " + errorMsg);
            }
        });
    }

    @Override
    public void takeAction(TexasHoldem texasHoldem) {
        this.texasHoldem = texasHoldem;
        int[] turnArray = new int[]{0, 0, 0, 0};
        turnArray[texasHoldem.getTurn()] = 1;
        double potRatio = (double) (texasHoldem.getTotalBets() - texasHoldem.getPlayerBets()) / texasHoldem.getTotalBets();
        new Thread(() -> {
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
        }).start();
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
