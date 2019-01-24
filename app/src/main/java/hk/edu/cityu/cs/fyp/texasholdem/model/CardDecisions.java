package hk.edu.cityu.cs.fyp.texasholdem.model;

public class CardDecisions {

    private long cards = 0;

    private static final int PAIR = 1000;
    private static final int TWO_PAIRS = 2000;
    private static final int FULL_HOUSE = 6000;
    private static final int THREE_OF_A_KIND = 3000;
    private static final int STRAIGHT = 4000;
    private static final int FLUSH = 5000;
    private static final int FOUR_OF_A_KIND = 7000;
    private static final int STRAIGHT_FLUSH = 8000;

    public void addCard(){

    }

    public void clear(){
        cards = 0;
    }

    private boolean isPair() {
        return true;
    }

    private boolean isTwo(){
        return true;
    }


    private boolean isFullHouse() {
        // TODO:
        return true;
    }


}
