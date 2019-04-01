package hk.edu.cityu.cs.fyp.texasholdem.model;

public abstract class AIPlayer {

    public abstract void takeAction(TexasHoldem texasHoldem);
    public abstract String getName();
    public abstract int getConstantValue();

}
