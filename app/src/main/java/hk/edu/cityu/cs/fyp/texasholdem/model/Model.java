package hk.edu.cityu.cs.fyp.texasholdem.model;

import java.util.ArrayList;
import java.util.Collections;

public class Model {

    private static final Model ourInstance = new Model();

    private ArrayList<String> myCardList = new ArrayList<>();

    private ArrayList<String> opponentCardList = new ArrayList<>();

    private ArrayList<String> deck = new ArrayList<>();

    private boolean isMyTurn;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }

    public void init() {
        myCardList.clear();
        opponentCardList.clear();
        deck.clear();

        isMyTurn = true;

        String[] cardClass = {"c", "d", "h", "s"};
        String[] cardNumbers = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k", "a"};
        for (String c : cardClass) {
            for (String n : cardNumbers) {
                deck.add(c + n);
            }
        }
        Collections.shuffle(deck);


    }

    public boolean isMyTurn(){
        return isMyTurn;
    }
}
