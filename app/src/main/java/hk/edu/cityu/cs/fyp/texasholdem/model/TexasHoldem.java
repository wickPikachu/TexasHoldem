package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;

public class TexasHoldem {

    public static final int BUILD_BET = 200;
    public static final int ROUNDS_LIMIT = 50;

    private static final TexasHoldem ourInstance = new TexasHoldem();

    private ArrayList<String> playerCardList = new ArrayList<>();

    private ArrayList<String> opponentCardList = new ArrayList<>();

    private ArrayList<String> tableCardList = new ArrayList<>();

    private Stack<String> deck = new Stack<>();

    private boolean isPlayerBuildBets;
    private boolean isPlayerTurn;
    // init = -1, start from 0
    private int rounds;
    // init = 0, start from 1
    private int turn;
    private String message;


    private int totalBets;

    private int playerMoney;
    private int playerBets;
    private boolean isPlayerAction;

    private int opponentMoney;
    private int opponentBets;
    private boolean isOpponentAction;

    private String actionHistory;
    private String cardHistory;
    private String betsResult;

    public static TexasHoldem getInstance() {
        return ourInstance;
    }

    private TexasHoldem() {
    }

    /**
     * Game Start
     * init player and opponent money
     */
    public void init() {
        isPlayerTurn = true;
        isPlayerBuildBets = false;
        actionHistory = "";

        rounds = -1;
        turn = 0;
        totalBets = 0;
        playerMoney = 20000;
        playerBets = 0;
        opponentMoney = 20000;
        opponentBets = 0;
    }

    /**
     * Round Start
     */
    public void startRound() {
        playerCardList.clear();
        opponentCardList.clear();
        deck.clear();

        rounds += 1;
        isPlayerBuildBets = !isPlayerBuildBets;
        message = "Round Start";

        // TODO: change who action first
        if (isPlayerBuildBets) {
            playerBets = BUILD_BET;
            isPlayerAction = false;
            isOpponentAction = true;
            opponentBets = BUILD_BET / 2;
        } else {
            playerBets = BUILD_BET / 2;
            isPlayerAction = true;
            isOpponentAction = false;
            opponentBets = BUILD_BET;
        }

        for (char c : CardDecisions.cardClassList) {
            for (char n : CardDecisions.cardNumberList) {
                deck.add("" + c + n);
            }
        }
        Collections.shuffle(deck);

        // pop two cards to hand
        for (int i = 0; i < 2; i++) {
            playerCardList.add(deck.pop());
            opponentCardList.add(deck.pop());
        }

    }

    public void next(){

    }

    public void nextTurn() {
        actionHistory += "/";
        ++turn;
    }

    /**
     * Round end
     */
    public void endRound() {
        // TODO: add actionHistory to DB
        actionHistory += "";
    }

    public void playerRaise(int raiseBets) throws TexasHoldemException {
        if (playerMoney < raiseBets) {
            message = "Your money do not enough";
            throw new TexasHoldemException(message);
        }
        actionHistory += "b" + raiseBets;
    }

    public void playerFold() {
        actionHistory += "f";
        opponentMoney += totalBets;
        endRound();
    }

    public void playerCall() {
        actionHistory += "c";
    }

    public void opponentRaise(int raiseBets) throws TexasHoldemException {
        if (opponentMoney < raiseBets) {
            message = "Opponent money do not enough";
            throw new TexasHoldemException(message);
        }
        actionHistory += "b" + raiseBets;
    }

    public void opponentFold() {
        actionHistory += "f";
        playerMoney += totalBets;
        endRound();
    }

    public void opponentCall() {
        actionHistory += "c";
    }

    public void takeAction(AIPlayer aiPlayer){
        aiPlayer.takeAction(this);
    }

    private void playerWin() {
        playerBets += totalBets;
        endRound();
    }

    private void playerLose() {
        opponentBets += totalBets;
        endRound();
    }

    private void playerDraw() {
        playerBets += totalBets / 2;
        opponentBets += totalBets / 2;
        endRound();
    }

    // Getters

    public boolean isPlayerTurn() {
        return isPlayerTurn;
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

    public int getOpponentMoney() {
        return opponentMoney;
    }

    public int getOpponentBets() {
        return opponentBets;
    }

    public ArrayList<String> getPlayerCardList() {
        return playerCardList;
    }

    public ArrayList<String> getOpponentCardList() {
        return opponentCardList;
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

}
