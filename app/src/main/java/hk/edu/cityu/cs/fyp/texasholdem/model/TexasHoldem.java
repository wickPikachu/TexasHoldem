package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;

public class TexasHoldem {

    public static final int BIG_BLIND_BET = 200;
    public static final int ROUNDS_LIMIT = 50;
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
        BothCalled
    }

    private GameState gameState;

    private int totalBets;

    private int playerMoney;
    private int playerBets;
    // bitwise
    private int playerActionsBits;

    private int computerMoney;
    private int computerBets;
    // bitwise
    private int computerActionBits;

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

        // fold is usually allow
        playerActionsBits = 1;
        computerActionBits = 1;

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

        // allow raise and call
        computerActionBits ^= 1 << 1;
        computerActionBits ^= 1 << 2;
        playerActionsBits ^= 1 << 1;
        playerActionsBits ^= 1 << 2;

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

        for (char c : CardDecisions.cardClassList) {
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
        playerActionsBits ^= 1 << 1;
        playerActionsBits ^= 1 << 2;
        computerActionBits ^= 1 << 1;
        computerActionBits ^= 1 << 2;
        actionHistory += "/";
        ++turn;
    }

    /**
     * Round end
     */
    public void endRound() {
        actionHistory += "";
        gameLogResults += rounds + ":" + actionHistory + "|" + cardHistory + "|" + betsResult + "\n";

        if (rounds < ROUNDS_LIMIT) {
            startRound();
        } else {
            message = "All rounds finished";
        }
    }

    public void playerFold() {
        actionHistory += "f";
        computerMoney += totalBets;
        endRound();
    }

    public void playerCall() {
        actionHistory += "c";
        playerActionsBits ^= 1 << 1;
        if (gameState == GameState.ComputerRaised) {
            int diff = computerBets - playerBets;
            if (diff > 0) {
                playerBets += diff;
                playerMoney -= diff;
            }
        } else if (gameState == GameState.ComputerCalled) {
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
        playerMoney -= raiseBets;
        playerBets += raiseBets;
        actionHistory += "b" + raiseBets;
        gameState = GameState.PlayerRaised;
    }

    public void computerFold() {
        message = "Computer fold";
        actionHistory += "f";
        playerMoney += totalBets;
        endRound();
    }

    public void computerCall() {
        message = "Computer Call";
        actionHistory += "c";
        computerActionBits ^= 1 << 1;
        if (gameState == GameState.PlayerRaised) {
            int diff = playerBets - computerBets;
            if (diff > 0) {
                computerBets += diff;
                computerMoney -= diff;
            }
        } else if (gameState == GameState.PlayerCalled) {
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
        endRound();
    }

    private void playerLose() {
        computerBets += totalBets;
        endRound();
    }

    private void playerDraw() {
        playerBets += totalBets / 2;
        computerBets += totalBets / 2;
        endRound();
    }

    // TODO: use bitwise
    public boolean canPlayerFold() {
        return (playerActionsBits & 1) == 1;
    }

    public boolean canPlayerCall() {
        return (playerActionsBits >> 1 & 1) == 1;
    }

    public boolean canPlayerRaise() {
        return (playerActionsBits >> 2 & 1) == 1;
    }

    public boolean canComputerFold() {
        return (computerActionBits & 1) == 1;
    }

    public boolean canComputerCall() {
        return (computerActionBits >> 1 & 1) == 1;
    }

    public boolean canComputerRaise() {
        return (computerActionBits >> 2 & 1) == 1;
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

}
