package hk.edu.cityu.cs.fyp.texasholdem.model;

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
//    public static final int ACTION_FOLD_BIT = 1;
//    public static final int ACTION_CALL_BIT = 1 << 1;
//    public static final int ACTION_RAISE_BIT = 1 << 2;

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
    private

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
        playerMoney = 20000;
        playerBets = 0;
        computerMoney = 20000;
        computerBets = 0;
    }

    /**
     * Round Start
     */
    public void startRound() {
        computerBets = 0;
        playerBets = 0;
        playerCardList.clear();
        computerCardList.clear();
        tableCardList.clear();
        deck.clear();

        rounds += 1;
        isPlayerBuildBets = !isPlayerBuildBets;
        message = "Round " + rounds + " start";

        // Game finish if cannot play next round
        // TODO: change who action first
        if (isPlayerBuildBets) {
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
            determineWinner();
        } else {    // table cards size is 3 or 4
            tableCardList.add(deck.pop());
        }
        actionHistory += "/";
        ++turn;
    }

    /**
     * Round end
     */
    public void endRound() {
        actionHistory += "";
        gameLogResults += rounds + ":" + actionHistory + "|" + cardHistory + "|" + betsResult + "\n";
//        message = "This round winner is ";
        gameState = GameState.Ended;

        // insert to database
        if (isSaveLogs) {
            Thread t = new Thread() {
                public void run() {
                    GameLog gameLog = new GameLog();
                    gameLog.setResult(actionHistory);
                    TexasHoldemApplication.db.getResultDao().insert(gameLog);
                }
            };
            t.start();
        }
    }

    public void gameFinished() {
        gameState = GameState.GameFinished;
        String result = "";
        if (playerMoney == computerMoney) {
            result = "Draw";
        } else if (playerMoney > computerMoney) {
            result = "You are winner";
        } else {
            result = "Computer is winner";
        }
        message = String.format("Game finished!\n (%s)", result);
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
            message = "Your money do not enough!";
            throw new TexasHoldemException(message);
        }
        message = "You raised $" + raiseBets;
        playerMoney -= raiseBets;
        playerBets += raiseBets;
        actionHistory += "b" + raiseBets;
        gameState = GameState.PlayerRaised;
    }

    public void computerFold() {
        actionHistory += "f";
        playerWin("Computer folded");
        endRound();
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
            message = "Computer money do not enough!";
            throw new TexasHoldemException(message);
        }
        computerMoney -= raiseBets;
        computerBets += raiseBets;
        actionHistory += "b" + raiseBets;
        message = "Computer raised $" + raiseBets;
        gameState = GameState.ComputerRaised;
    }

    public void takeAction(AIPlayer aiPlayer) {
        aiPlayer.takeAction(this);
    }

    private void playerWin(String reason) {
        message = "You Win! (" + reason + ")";
        playerMoney += getTotalBets();
    }

    private void computerWin(String reason) {
        message = "You Lose! (Computer " + reason + ")";
        computerMoney += getTotalBets();
    }

    private void playerDraw(String reason) {
        message = "Draw! (" + reason + ")";
        int totalBets = getTotalBets();
        playerMoney += totalBets / 2;
        computerMoney += totalBets / 2;
    }

    public void determineWinner() {
        Cards playerCards = new Cards(getPlayerCardsWithTable());
        Cards computerCards = new Cards(getComputerCardsWithTable());
        if (playerCards.compareTo(computerCards) > 0) {
            playerWin(playerCards.getCombination().name());
        } else if (playerCards.compareTo(computerCards) < 0) {
            computerWin(computerCards.getCombination().name());
        } else {
            playerDraw(playerCards.getCombination().name());
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
        return playerBets + computerBets;
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
