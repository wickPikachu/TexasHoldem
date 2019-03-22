package hk.edu.cityu.cs.fyp.texasholdem.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;
import hk.edu.cityu.cs.fyp.texasholdem.TexasHoldemApplication;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;

public class TexasHoldem {

    public static final int BIG_BLIND_BET = 100;
    public static final int ROUNDS_LIMIT = 100;
    // bitwise for actions:
    //      raise bit, call bit, fold bit
    //          0         0         0
    public static final int ACTION_FOLD_BIT = 1;
    public static final int ACTION_CALL_BIT = 1 << 1;
    public static final int ACTION_RAISE_BIT = 1 << 2;

    private static final TexasHoldem ourInstance = new TexasHoldem();

    private ArrayList<String> playerCardList = new ArrayList<>();
    private ArrayList<String> computerCardList = new ArrayList<>();
    private ArrayList<String> tableCardList = new ArrayList<>();
    private Stack<String> deck = new Stack<>();

    // for save log to db
    private boolean isSaveLogs = true;
    private boolean isPlayerBuildBets;
    // this round is player action first (computer is big blind bets)
    private boolean isPlayerActionFirst;
    // init = 0, start from 1
    private int rounds;
    // init = 0, start from 1
    private int turn;
    private String message;

    enum GameState {
        PlayerTurn,
        PlayerCalled,
        PlayerRaised,
        ComputerTurn,
        ComputerCalled,
        ComputerRaised,
        BothCalled,
        Ended,
    }

    private GameState gameState;

    private int totalBets;

    private int playerMoney;
    private int playerBets;

    private int computerMoney;
    private int computerBets;

    private String actionHistory;
    private String cardHistory;
    private String betsResult;
    private String gameLogResults;

    public static TexasHoldem getInstance() {
        return ourInstance;
    }

    private TexasHoldem() {
    }

    /**
     * Game Start
     * init player, computer, and settings
     */
    public void init() {
        isPlayerActionFirst = true;
        isPlayerBuildBets = true;
        actionHistory = "";
        cardHistory = "";
        betsResult = "";
        gameLogResults = "";

        rounds = 0;
        turn = 0;
        totalBets = 0;
        playerMoney = 20000;
        playerBets = 0;
        computerMoney = 20000;
        computerBets = 0;
    }

    /**
     * Round Start
     */
    public void startRound() {
        playerCardList.clear();
        computerCardList.clear();
        tableCardList.clear();
        deck.clear();

        rounds += 1;
        isPlayerBuildBets = !isPlayerBuildBets;
        message = "Round Start";

        // TODO: change who action first
        if (isPlayerBuildBets) {
            playerBets = BIG_BLIND_BET;
            gameState = GameState.ComputerTurn;
            computerBets = BIG_BLIND_BET / 2;
        } else {
            playerBets = BIG_BLIND_BET / 2;
            gameState = GameState.PlayerTurn;
            computerBets = BIG_BLIND_BET;
        }
        playerMoney -= playerBets;
        computerMoney -= computerBets;

        for (char c : CardDecisions.cardSuitList) {
            for (char n : CardDecisions.cardNumberList) {
                deck.add("" + c + n);
            }
        }
        Collections.shuffle(deck);

        // pop two cards to hand
        for (int i = 0; i < 2; i++) {
            playerCardList.add(deck.pop());
            computerCardList.add(deck.pop());
        }

    }

    public void next() {
        if (tableCardList.isEmpty()) {
            tableCardList.add(deck.pop());
            tableCardList.add(deck.pop());
            tableCardList.add(deck.pop());
        } else if (tableCardList.size() == 5) {
            // TODO: decision who are winner
        } else {    // table cards size is 3 or 4
            tableCardList.add(deck.pop());
        }
        actionHistory += "/";
        ++turn;
    }

    public String[] getPlayerCards() {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(playerCardList);
        tempList.addAll(tableCardList);
        return tempList.toArray(new String[0]);
    }

    public String[] getComputerCards() {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(playerCardList);
        tempList.addAll(tableCardList);
        return tempList.toArray(new String[0]);
    }

    /**
     * Round end
     */
    public void endRound() {
        actionHistory += "";
        gameLogResults += rounds + ":" + actionHistory + "|" + cardHistory + "|" + betsResult + "\n";
//        message = "This round winner is ";
        gameState = GameState.Ended;
        // determine who are the winner;
        CardDecisions.CardGroup playerCards = CardDecisions.eval(getPlayerCards());
        CardDecisions.CardGroup computerCards = CardDecisions.eval(getComputerCards());
        if (playerCards.getValue() > computerCards.getValue()) {
            playerWin();
        } else if (playerCards.getValue() < computerCards.getValue()) {
            playerLose();
        } else {
            playerDraw();
        }

        // insert to database
        if (isSaveLogs) {
            Thread t = new Thread() {
                public void run() {
                    GameLog gameLog = new GameLog();
                    gameLog.setResult("test");
                    TexasHoldemApplication.db.getResultDao().insert(gameLog);
                }
            };
            t.start();
        }
    }

    public void playerFold() {
        message = "You folded";
        actionHistory += "f";
        computerMoney += totalBets;
//        endRound();
    }

    public void playerCall() {
        message = "You called";
        actionHistory += "c";
        int diff = computerBets - playerBets;
        if (diff > 0) {
            playerBets += diff;
            playerMoney -= diff;
        }
        if (gameState == GameState.ComputerRaised || gameState == GameState.ComputerCalled) {
            gameState = GameState.BothCalled;
        } else {
            gameState = GameState.PlayerCalled;
        }
    }

    public void playerRaise(int raiseBets) throws TexasHoldemException {
        if (playerMoney < raiseBets) {
            message = "Your money do not enough";
            throw new TexasHoldemException(message);
        }
        message = "You raised " + raiseBets + " dollar";
        playerMoney -= raiseBets;
        playerBets += raiseBets;
        actionHistory += "b" + raiseBets;
        gameState = GameState.PlayerRaised;
    }

    public void computerFold() {
        message = "Computer folded";
        actionHistory += "f";
        playerMoney += totalBets;
//        endRound();
    }

    public void computerCall() {
        message = "Computer Called";
        actionHistory += "c";
        int diff = playerBets - computerBets;
        if (diff > 0) {
            computerBets += diff;
            computerMoney -= diff;
        }
        if (gameState == GameState.PlayerRaised || gameState == GameState.PlayerCalled) {
            gameState = GameState.BothCalled;
        } else {
            gameState = GameState.ComputerCalled;
        }
    }

    public void computerRaise(int raiseBets) throws TexasHoldemException {
        if (computerMoney < raiseBets) {
            message = "Computer money do not enough";
            throw new TexasHoldemException(message);
        }
        computerMoney -= raiseBets;
        computerBets += raiseBets;
        actionHistory += "b" + raiseBets;
        gameState = GameState.ComputerRaised;
    }

    public void takeAction(AIPlayer aiPlayer) {
        aiPlayer.takeAction(this);
    }

    private void playerWin() {
        playerBets += totalBets;
        message = "Player Win";
    }

    private void playerLose() {
        computerBets += totalBets;
        message = "Computer Win";
    }

    private void playerDraw() {
        message = "Draw!";
        playerBets += totalBets / 2;
        computerBets += totalBets / 2;
    }

    public boolean isPlayerTurn() {
        return gameState == GameState.PlayerTurn || gameState == GameState.ComputerCalled || gameState == GameState.ComputerRaised;
    }

    public boolean isComputerTurn() {
        return gameState == GameState.ComputerTurn || gameState == GameState.PlayerCalled || gameState == GameState.PlayerRaised;
    }

    public boolean isBothCalled() {
        return gameState == GameState.BothCalled;
    }

    public boolean isEnded() {
        return gameState == GameState.Ended;
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
        return totalBets;
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

    public boolean isPlayerBuildBets() {
        return isPlayerBuildBets;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSaveLogs() {
        return isSaveLogs;
    }

    public void setSaveLogs(boolean saveLogs) {
        isSaveLogs = saveLogs;
    }
}
