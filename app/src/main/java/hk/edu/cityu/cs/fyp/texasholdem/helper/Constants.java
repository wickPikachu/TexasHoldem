package hk.edu.cityu.cs.fyp.texasholdem.helper;

public class Constants {

    public interface SharedPref {
        String NAME = "TexasHoldem";
        String KEY_AI_PLAYER = "AI_Player";
        String KEY_AI_PLAYER_RANDOM_AUTO_SYNC = "AI_Player_random_auto_sync";
        String KEY_AI_PLAYER_MINIMAX_AUTO_SYNC = "AI_Player_minimax_auto_sync";
        String KEY_AI_PLAYER_MACHINE_LEARNING_AUTO_SYNC = "AI_Player_machine_learning_auto_sync";
    }

    public interface Socket {
        int ACTION_SYNC = 1;
        int ACTION_PREDICT = 2;
        int ACTION_GET = 3;
    }

    public interface Json {
        String KEY_ACTION = "action";
        String KEY_UUID = "uuid";
        String KEY_DATA = "data";
        String KEY_TYPE = "type";
        String KEY_SUCCESS = "success";
        String KEY_INPUT= "input";
        String KEY_OUTPUT= "output";
    }

    public static final int AI_PLAYER_RANDOM = 1;
    public static final int AI_PLAYER_MACHINE_LEARNING = 2;
    public static final int AI_PLAYER_MINIMAX = 3;

    public static final int CARD_2 = 10001;
    public static final int CARD_3 = 10002;
    public static final int CARD_4 = 10003;
    public static final int CARD_5 = 10004;
    public static final int CARD_6 = 10005;
    public static final int CARD_7 = 10006;
    public static final int CARD_8 = 10007;
    public static final int CARD_9 = 10008;
    public static final int CARD_T = 10009;
    public static final int CARD_J = 10010;
    public static final int CARD_Q = 10011;
    public static final int CARD_K = 10012;
    public static final int CARD_A = 10013;

    public static final String HELP_MESSAGE_RULE =
            "<b>How to play?</b><br>" +
                    "Player can choose fold/call/raise in the game. When the round is end(i.e. shown the winner in middle), you can start a new round by clicking the message or buttons layout.<br>" +
                    "<br>" +
                    "<b>Highcard (Scattered card)</b><br>" +
                    "If the player does not have any special combination (one pair, or a higher combination ), then the card in hand is called a scattered card.<br>" +
                    "Highcard A<br>" +
                    "Suppose your hole card is {A} {6}, the common card is {2} {5} {8}, this means that you get scattered card, the best single card is {A}. If the opponent owns {K} {Q}, the opponent's card is also scattered card, the best single card is {K}. Thus, your card is {A} greater than {K}, so you are the winner.<br>" +
                    "Highcard A, Kick K<br>" +
                    "Player 1’s card is {A} {K}<br>" +
                    "Player 2’s card is {A} {Q}<br>" +
                    "Public card is {9} {6} {4} {3} {2}<br>" +
                    "The two Players’ cards  are also scattered card, and their biggest card is {A}, but player 1’s second big card (kick) is {K}, which is greater than player 2’s second big card {Q}. Thus, player 1 wins.<br>" +
                    "<br>" +
                    "<b>A Pair</b><br>" +
                    "In the player’s hole card and the common card, the two cards with the same card are a pair.<br>" +
                    "Suppose your card is {A} {K}, common card is {2} {7} {K} {9} {10}, then you have 2 {K}, a pair of {K}. If your opponent also has a pair of {K}, then use the kick to determine who is the winner. Another possibility is that there is already a pair in the common card (for example: {2} {Q} {Q} {7} {4}). At this moment, the key to the outcome is whether the player can get a second pair or it is a better deck, otherwise it is won by the players’ biggest kick.<br>" +
                    "A pair of {A}<br>" +
                    "Player 1’s card is {A} {K}<br>" +
                    "Player 2’s card is {Q} {J}<br>" +
                    "Public card is {A} {Q} {7} {5} {3}<br>" +
                    " <br>" +
                    "Player 1 can use 7 cards {A} {A} {K} {Q} {7} {5} {3}, and the best card group is a pair of {A}. Player 2 can use 7 cards {A} {Q} {Q} {J} {7} {5} {3}, player 2 best deck is pair of {Q}. Thus, player 1 wins.<br>" +
                    " <br>" +
                    "<b>Two pairs</b><br>" +
                    "In the player’s hole card and the common cards, two pairs are called two pairs.<br>" +
                    "Two pairs {K} and {Q}<br>" +
                    "Player 1’s card is {K} {K}<br>" +
                    "Player 2’s card is {7} {7}<br>" +
                    "Public card is {Q} {Q} {10} {9} {2}<br>" +
                    "Player 1's hole card is a pair of {K} and a pair of {Q}, which is better than a pair of {Q} and a pair of {7} of player 2.<br>" +
                    "<br>" +
                    "<b>Three of a kind</b><br>" +
                    "In the player's hole card and the common card, the three cards with the same card are three.<br>" +
                    "Three {9}, kick {K}<br>" +
                    "Player 1’s card is {10} {9}<br>" +
                    "Player 2’s card is {9} {K}<br>" +
                    "Public card is {9] {8} {9} {A} {2}<br>" +
                    "<br>" +
                    "Player 1's cards are three {9} and kick {A}, 10, and player 2 also has three {9}, with kicks {A} and {K}. Thus, player 1 loses to player 2 because player 2 has a larger kick.<br>" +
                    "<br>" +
                    "<b>Straight</b><br>" +
                    "In the player's hole card and the common card, five cards of different suits and consecutive cards are called straights.<br>" +
                    "Straight, K is the head<br>" +
                    "Player 1’s card is {K} {Q}<br>" +
                    "Player 2’s card is {A} {Q}<br>" +
                    "Public card is {J} {10} {9} {8} {3}<br>" +
                    "Player 1's card is a 9 to K straight {9} {10} {J} {Q} {K}, Player 2's card is a 8 to Q straight {8} {9} {10} {J} {Q}. Thus, player 1 wins.<br>" +
                    "<br>" +
                    "<b>Flush</b><br>" +
                    "A flush is a group of five cards that are not continuous but have the same suit.<br>" +
                    "Flush vs Flush<br>" +
                    "Player 1’s card is {3♠} {8♠}<br>" +
                    "Player 2’s card is {9♠} {5♠}<br>" +
                    "Public card is {A♠} {Q♠} {10♥} {7♠} {2♠}<br>" +
                    "Both player 1 and player 2 have a flush, player 1 has a maximum card of 8, and player 2 has a maximum card of 9, so player 2 wins.<br>" +
                    "<br>" +
                    "<b>Full house</b><br>" +
                    "Full house refers to a deck with three and one pair at the same time.<br>" +
                    "A-7 Full house<br>" +
                    "Player 1’s card is {A} {A}<br>" +
                    "Player 2’s card is {7} {5}<br>" +
                    "Public card is {A} {7} {7} {K} {5}<br>" +
                    "<br>" +
                    "Player 1’s card is A-7 full house (three {A} and a pair of {7}), player 2’s card is 7-5 full house (three {7} and a pair of {5}). Thus, player 1 wins.<br>" +
                    "<br>" +
                    "<b>Four of a kind</b><br>" +
                    "Four of a kind is four cards with the number and one other card.<br>" +
                    "Four {A}<br>" +
                    "Player 1’s card is {10} {10}<br>" +
                    "Player 2’s card is {A} {K}<br>" +
                    "Public card is {A} {A} {A} {10} {10}<br>" +
                    "<br>" +
                    "Player 1's hole card is four {10}, and loses to player 2's four {A}.<br>" +
                    "<br>" +
                    "If two players have four of a kind (which may only occur if the four cards are common cards), so the fifth card will determine who is the winner.<br>" +
                    "<b>Straight flush</b><br>" +
                    "Straight Flush is a straight with the same suit.<br>" +
                    "6-10 Straight flush<br>" +
                    "Player 1’s card is {4♦} {5♦}<br>" +
                    "Player 2’s card is {9♦} {10♦}<br>" +
                    "Public card is {A♠} {8♦} {7♦} {6♦} {2♠}<br>" +
                    "<br>" +
                    "Player 1’s card is 4-8 straight flush {4♦} {5♦} {6♦} {7♦} {8♦}, player 2’s card is 6-10 straight flush {6♦} {7♦} {8♦} {9♦} {10♦}. Thus, player 2 is the winner.<br>" +
                    "<br>" +
                    "<b>Royal flush</b><br>" +
                    "This is the biggest combination in texas hold’em.<br>" +
                    "<br>" +
                    "The straight flush with 10-J-Q-K-A five cards. There are only 4 combination in 260 million combination of 52 cards.<br>" +
                    "<br>" +
                    "<b>Split pot</b><br>" +
                    "If two players have the same values, that means their 5 cards cannot determine who are the winner. Then, two player splits the pot.<br>" +
                    "Two pairs, kick Q:<br>" +
                    "Player 1's card is {A} {2}<br>" +
                    "Player 2's card is {A} {9}<br>" +
                    "Public card is {A} {Q} {8} {8} {3}<br>" +
                    "<br>" +
                    "The best decks for both players are {A} {A} {8} {8} {Q}, so two players can split the pot.  Although {9} larger {2}, but each group can only select 5 cards, so ignore it {9} {2}.<br>";
}
