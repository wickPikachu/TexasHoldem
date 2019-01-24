package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class TexasHoldem {

    public static final int BUILD_BET = 200;
    public static final int ROUNDS_LIMIT = 20;

    private static final TexasHoldem ourInstance = new TexasHoldem();

    private ArrayList<String> playerCardList = new ArrayList<>();

    private ArrayList<String> opponentCardList = new ArrayList<>();

    private ArrayList<String> tableCardList = new ArrayList<>();

    private Stack<String> deck = new Stack<>();

    private boolean isPlayerBuildBets;
    private boolean isPlayerTurn;
    private int rounds;
    private String message;
    private int totalBets;

    private int playerMoney;
    private int playerBets;
    private boolean playerAction;

    private int opponentMoney;
    private int opponentBets;
    private boolean opponentAction;

    private String history;

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
        history = "";

        rounds = 0;
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

    /**
     * Round end
     */
    public void endRound() {
        // TODO: add history to DB
    }

    public void playerRaise(int raiseBets) {

    }

    public void playerFold() {
        opponentMoney += totalBets;
        endRound();
    }

    public void playerCall() {

    }

    public void opponentRaise(int raiseBets) {

    }

    public void opponentFold() {
        playerMoney += totalBets;
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