package hk.edu.cityu.cs.fyp.texasholdem.model;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;
import hk.edu.cityu.cs.fyp.texasholdem.TexasHoldemApplication;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SocketHelper;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;

@SuppressLint("DefaultLocale")
public class TexasHoldem {

    private static final String TAG = "TexasHoldem";
    public static final int BIG_BLIND_BET = 100;
    public static final int ROUNDS_LIMIT = 100;
    // bitwise for actions:
    //      raise bit, call bit, fold bit
    //          0         0         0
//    public static final int ACTION_FOLD_BIT = 1;
//    public static final int ACTION_CALL_BIT = 1 << 1;
//    public static final int ACTION_RAISE_BIT = 1 << 2;

    private static final TexasHoldem ourInstance = new TexasHoldem();

    private ArrayList<String> playerCardList = new ArrayList<>();
    private ArrayList<String> computerCardList = new ArrayList<>();
    private ArrayList<String> tableCardList = new ArrayList<>();
    private Stack<String> deck = new Stack<>();

    private AIPlayer aiPlayer;
    // for save log to db
    private boolean isSaveLogs = true;
    private boolean isPlayerBigBuildBets;
    // this round (turn 1) is player action first (computer is big blind bets) => true
    // then turn 2,3,4 is computer action first => false
    private boolean isPlayerAction;
    // init = 0, start from 1
    private int rounds;
    private int turn;
    private String message;
    // check is init
    private boolean isInit = false;
    private int pot;

    enum GameState {
        PlayerTurn,
        PlayerCalled,
        PlayerRaised,
        ComputerTurn,
        ComputerCalled,
        ComputerRaised,
        BothCalled,
        Ended,
        GameFinished,
    }

    private GameState gameState;

    private int playerMoney;
    private int playerBets;

    private int computerMoney;
    private int computerBets;

    private String actionHistory;
    private String cardHistory;
    private String betsResult;
    private String gameLogResults;
    private double bb;
    private TexasHoldemListener texasHoldemListener;
    private boolean isAutoSync = false;
    private SocketHelper socketHelper = SocketHelper.getInstance();

    public static TexasHoldem getInstance() {
        return ourInstance;
    }

    private TexasHoldem() {
    }

    /**
     * Game Start
     * init player, computer, and settings
     */
    public void init(AIPlayer aiPlayer, TexasHoldemListener texasHoldemListener) {
        this.isInit = true;
        this.aiPlayer = aiPlayer;
        this.texasHoldemListener = texasHoldemListener;
        startNewGame();
    }

    public void startNewGame() {
        isPlayerAction = true;
        isPlayerBigBuildBets = true;

        rounds = 0;
        playerMoney = 20000;
        playerBets = 0;
        computerMoney = 20000;
        computerBets = 0;
        pot = 0;
    }

    /**
     * Round Start
     */
    public void startRound() {
        actionHistory = "";
        cardHistory = "";
        betsResult = "";
        gameLogResults = "";
        computerBets = 0;
        playerBets = 0;
        bb = 0;
        pot = 0;
        playerCardList.clear();
        computerCardList.clear();
        tableCardList.clear();
        deck.clear();

        turn = 0;
        rounds += 1;
        isPlayerBigBuildBets = !isPlayerBigBuildBets;
        message = "Round " + rounds + " start";

        // Game finish if cannot play next round
        // TODO: change who action first
        if (isPlayerBigBuildBets) {
            if (playerMoney < BIG_BLIND_BET || computerMoney < BIG_BLIND_BET / 2) {
                gameFinished();
                return;
            }
            playerBets = BIG_BLIND_BET;
            gameState = GameState.ComputerTurn;
            computerBets = BIG_BLIND_BET / 2;
        } else {
            if (playerMoney < BIG_BLIND_BET / 2 || computerMoney < BIG_BLIND_BET) {
                gameFinished();
                return;
            }
            playerBets = BIG_BLIND_BET / 2;
            gameState = GameState.PlayerTurn;
            computerBets = BIG_BLIND_BET;
        }
        playerMoney -= playerBets;
        computerMoney -= computerBets;

        for (char c : Cards.CARD_SUIT_LIST) {
            for (char n : Cards.CARD_NUMBER_LIST) {
                deck.add("" + c + n);
            }
        }
        Collections.shuffle(deck);

        String playerCardsStr = "";
        String computerCardsStr = "";
        String card;
        // pop two cards to hand
        for (int i = 0; i < 2; i++) {
            card = deck.pop();
            playerCardList.add(card);
            playerCardsStr += Utils.toNumSuitFormat(card);
            card = deck.pop();
            computerCardList.add(card);
            computerCardsStr += Utils.toNumSuitFormat(card);
        }
        cardHistory += playerCardsStr + "|" + computerCardsStr;

    }

    public void next() {
        pot += playerBets + computerBets;
        computerBets = playerBets = 0;
        String card;
        if (tableCardList.isEmpty()) {
            cardHistory += "/";
            card = deck.pop();
            tableCardList.add(card);
            cardHistory += Utils.toNumSuitFormat(card);
            card = deck.pop();
            tableCardList.add(card);
            cardHistory += Utils.toNumSuitFormat(card);
            card = deck.pop();
            tableCardList.add(card);
            cardHistory += Utils.toNumSuitFormat(card);
        } else if (tableCardList.size() == 5) { // table cards size is 5
            determineWinner();
            return;
        } else {    // table cards size is 3 or 4
            cardHistory += "/";
            card = deck.pop();
            tableCardList.add(card);
            cardHistory += Utils.toNumSuitFormat(card);
        }
        actionHistory += "/";
        turn++;
        gameState = isPlayerBigBuildBets ? GameState.PlayerTurn : GameState.ComputerTurn;
    }

    /**
     * Round end
     */
    public void endRound() {
        actionHistory += "";
        String playerHistory = isPlayerBigBuildBets ? "AIPlayer|Human" : "Human|AIPlayer";
        gameLogResults += "STATE:" + rounds + ":" + actionHistory + ":" + cardHistory + ":" + betsResult + ":" + playerHistory;
//        message = "This round winner is ";
        gameState = GameState.Ended;
        String[] betsArray = betsResult.split("\\|");

        // insert to database
        GameLog gameLog = new GameLog();
        gameLog.setResult(gameLogResults);
        gameLog.setAiPlayer(aiPlayer.getConstantValue());
        gameLog.setMoney(Double.valueOf(betsArray[0]));
        gameLog.setBb(BIG_BLIND_BET);

        if (isSaveLogs) {
            TexasHoldemApplication.postToDataThread(() -> {
                TexasHoldemApplication.db.getResultDao().insert(gameLog);
            });

            if (isAutoSync) {
                socketHelper.connectToServer(new SocketHelper.SocketListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        TexasHoldemApplication.postToDataThread(() -> {
                            if (jsonObject.has(Constants.Json.KEY_SUCCESS)) {
                                try {
                                    JSONArray uuidArray = jsonObject.getJSONArray(Constants.Json.KEY_SUCCESS);
                                    int len = uuidArray.length();
                                    ArrayList<String> uuids = new ArrayList<>();
                                    for (int i = 0; i < len; i++) {
                                        uuids.add(uuidArray.getString(i));
                                    }
                                    TexasHoldemApplication.db.getResultDao().updateIsSync(uuids, true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                                }

                            }
                        });
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Log.e(TAG, "onError: " + errorMsg);
                    }
                });
                socketHelper.uploadGameLog(gameLog);
            }
        }
//        determineGameIsFinish();
    }

    private void determineGameIsFinish() {
        boolean nextIsPlayerBigBlindBets = !isPlayerBigBuildBets;
        if ((nextIsPlayerBigBlindBets && (playerMoney < BIG_BLIND_BET || computerMoney < BIG_BLIND_BET / 2))
                || (!nextIsPlayerBigBlindBets && (playerMoney < BIG_BLIND_BET / 2 || computerMoney < BIG_BLIND_BET))) {
            gameFinished();
        }
    }

    public void gameFinished() {
        gameState = GameState.GameFinished;
        String result;
        if (playerMoney == computerMoney) {
            result = "Draw";
        } else if (playerMoney > computerMoney) {
            result = "You are winner";
        } else {
            result = "Computer is winner";
        }
        message = String.format("Game finished!\n (%s)", result);
        texasHoldemListener.onGameIsFinished();
    }

    public void playerFold() {
        actionHistory += "f";
        computerWin("You folded");
        endRound();
    }

    public void playerCall() {
        message = "You called";
        actionHistory += "c";
        int diff = computerBets - playerBets;
        if (diff > 0) {
            if (diff > playerMoney) {
                playerBets += playerMoney;
                playerMoney = 0;
            } else {
                playerBets += diff;
                playerMoney -= diff;
            }
        }
        if (gameState == GameState.ComputerRaised || gameState == GameState.ComputerCalled) {
            gameState = GameState.BothCalled;
        } else {
            gameState = GameState.PlayerCalled;
        }
    }

    public void playerRaise(int raiseBets) throws TexasHoldemException {
        if (playerMoney < raiseBets) {
            message = "Your money do not enough!";
            throw new TexasHoldemException(message);
        }
        message = "You raised $" + raiseBets;
        playerMoney -= raiseBets;
        playerBets += raiseBets;
        actionHistory += "r" + playerBets;
        gameState = GameState.PlayerRaised;
        isPlayerAction = false;
    }

    public void computerFold() {
        actionHistory += "f";
        playerWin("Computer folded");
        texasHoldemListener.afterComputerTakeAction();
        endRound();
    }

    public void computerCall() {
        message = "Computer Called";
        actionHistory += "c";
        int diff = playerBets - computerBets;
        if (diff > 0) {
            if (diff > computerMoney) {
                computerBets += computerMoney;
                computerMoney = 0;
            } else {
                computerBets += diff;
                computerMoney -= diff;
            }
        }
        if (gameState == GameState.PlayerRaised || gameState == GameState.PlayerCalled) {
            gameState = GameState.BothCalled;
        } else {
            gameState = GameState.ComputerCalled;
        }
        texasHoldemListener.afterComputerTakeAction();
    }

    public void computerRaise(int raiseBets) throws TexasHoldemException {
        if (computerMoney < raiseBets) {
            message = "Computer money do not enough!";
            throw new TexasHoldemException(message);
        }
        computerMoney -= raiseBets;
        computerBets += raiseBets;
        actionHistory += "r" + computerBets;
        message = "Computer raised $" + raiseBets;
        gameState = GameState.ComputerRaised;
        texasHoldemListener.afterComputerTakeAction();
    }

    public void takeAction(AIPlayer aiPlayer) {
        aiPlayer.takeAction(this);

    }

    private void playerWin(String reason) {
        message = String.format("You Win! +%d\n(%s)", computerBets, reason);
        int totalBets = getTotalBets();
        playerMoney += totalBets;
        bb += isPlayerBigBuildBets ? 1 : 0.5;
        betsResult = (computerBets) + "|" + (-computerBets);
    }

    private void computerWin(String reason) {
        message = String.format("You Lose! -%d\n(%s)", playerBets, reason);
        int totalBets = getTotalBets();
        computerMoney += totalBets;
        bb += isPlayerBigBuildBets ? -1 : -0.5;
        betsResult = (-playerBets) + "|" + playerBets;
    }

    private void playerDraw(String reason) {
        message = String.format("Draw!\n(%s)", reason);
        int totalBets = getTotalBets();
        playerMoney += totalBets / 2;
        computerMoney += totalBets / 2;
        bb += 0;
        betsResult = "0|0";
    }

    public void determineWinner() {
        Cards playerCards = new Cards(getPlayerCardsWithTable());
        Cards computerCards = new Cards(getComputerCardsWithTable());
        if (playerCards.compareTo(computerCards) > 0) {
            playerWin(playerCards.toString());
        } else if (playerCards.compareTo(computerCards) < 0) {
            computerWin(computerCards.toString());
        } else {
            playerDraw(playerCards.toString());
        }
        endRound();
    }

    public String[] getPlayerCardsWithTable() {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(playerCardList);
        tempList.addAll(tableCardList);
        return tempList.toArray(new String[0]);
    }

    public String[] getComputerCardsWithTable() {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(computerCardList);
        tempList.addAll(tableCardList);
        return tempList.toArray(new String[0]);
    }

    public int getTurn() {
        switch (tableCardList.size()) {
            case 0:
                return 0;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
        }
        return 0;
    }

    public void disconnectSocketIfNeeded() {
        if (aiPlayer instanceof MachineLearningAIPlayer) {
            ((MachineLearningAIPlayer) aiPlayer).disconnectSocket();
        }
    }

    public boolean isPlayerAction() {
        if (gameState == GameState.GameFinished)
            return false;
//        return isPlayerAction || gameState == GameState.PlayerTurn;
        return gameState == GameState.PlayerTurn || gameState == GameState.ComputerCalled || gameState == GameState.ComputerRaised;
    }

    public boolean isComputerAction() {
        if (gameState == GameState.GameFinished)
            return false;
//        return !isPlayerAction || gameState == GameState.ComputerTurn;
        return gameState == GameState.ComputerTurn || gameState == GameState.PlayerCalled || gameState == GameState.PlayerRaised;
    }

    public boolean isBothCalled() {
        return gameState == GameState.BothCalled;
    }

    public boolean isEnded() {
        return gameState == GameState.Ended;
    }

    public boolean isGameFinished() {
        return gameState == GameState.GameFinished;
    }

    // Getters

    public GameState getGameState() {
        return gameState;
    }

    public String getGameLogResults() {
        return gameLogResults;
    }

    public int getRounds() {
        return rounds;
    }

    public int getTotalBets() {
        return pot + playerBets + computerBets;
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public int getPlayerBets() {
        return playerBets;
    }

    public int getComputerMoney() {
        return computerMoney;
    }

    public int getComputerBets() {
        return computerBets;
    }

    public ArrayList<String> getPlayerCardList() {
        return playerCardList;
    }

    public ArrayList<String> getComputerCardList() {
        return computerCardList;
    }

    public ArrayList<String> getTableCardList() {
        return tableCardList;
    }

    public boolean isPlayerBigBuildBets() {
        return isPlayerBigBuildBets;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAutoSync() {
        return isAutoSync;
    }

    public void setAutoSync(boolean autoSync) {
        isAutoSync = autoSync;
    }

    public boolean isSaveLogs() {
        return isSaveLogs;
    }

    public void setSaveLogs(boolean saveLogs) {
        isSaveLogs = saveLogs;
    }

    public int getPot() {
        return pot;
    }

    /**
     * For machine learning AI Player socket Call
     *
     * @return double[]
     */
    public double[] getSocketCallWithCards() {
        double[] doubles = new double[14];
        doubles[0] = Utils.valueOfCard(computerCardList.get(0)) + 1;
        doubles[1] = Utils.suitOfCard(computerCardList.get(0));
        doubles[2] = Utils.valueOfCard(computerCardList.get(1)) + 1;
        doubles[3] = Utils.suitOfCard(computerCardList.get(1));

        for (int i = 0; i < tableCardList.size(); i += 2) {
            doubles[4 + i] = Utils.valueOfCard(tableCardList.get(i)) + 1;
            doubles[5 + i] = Utils.suitOfCard(tableCardList.get(i));
        }
        return doubles;
    }

    public interface TexasHoldemListener {
        public void afterComputerTakeAction();

        public void onGameIsFinished();
    }
}
