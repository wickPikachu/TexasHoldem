package hk.edu.cityu.cs.fyp.texasholdem.helper;

public class Constants {

    public interface SharedPref {
        String NAME = "TexasHoldem";
        String KEY_AI_PLAYER = "AI_Player";
        String KEY_AI_PLAYER_RANDOM_AUTO_SYNC = "AI_Player_random_auto_sync";
        String KEY_AI_PLAYER_MINIMAX_AUTO_SYNC = "AI_Player_minimax_auto_sync";
        String KEY_AI_PLAYER_MACHINE_LEARNING_AUTO_SYNC = "AI_Player_machine_learning_auto_sync";
    }

    public static final int SOCKET_ACTION_UPLOAD = 1;
    public static final int SOCKET_ACTION_PREDICT = 2;
    public static final int SOCKET_ACTION_GET = 3;

    public static final int AI_PLAYER_RANDOM = 1;
    public static final int AI_PLAYER_MINIMAX = 2;
    public static final int AI_PLAYER_MACHINE_LEARNING = 3;

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
            "Highcard (Scattered card)\n" +
                    "If the player does not have any special combination (one pair, or a higher combination ), then the card in hand is called a scattered card.\n" +
                    "Highcard A\n" +
                    "Suppose your hole card is {A} {6}, the common card is {2} {5} {8}, this means that you get scattered card, the best single card is {A}. If the opponent owns {K} {Q}, the opponent's card is also scattered card, the best single card is {K}. Thus, your card is {A} greater than {K}, so you are the winner.\n" +
                    "Highcard A, Kick K\n" +
                    "Player 1’s card is {A} {K}\n" +
                    "Player 2’s card is {A} {Q}\n" +
                    "Public card is {9} {6} {4} {3} {2}\n" +
                    "The two Players’ cards  are also scattered card, and their biggest card is {A}, but player 1’s second big card (kick) is {K}, which is greater than player 2’s second big card {Q}. Thus, player 1 wins.\n" +
                    "\n" +
                    "A Pair\n" +
                    "In the player’s hole card and the common card, the two cards with the same card are a pair.\n" +
                    "Suppose your card is {A} {K}, common card is {2} {7} {K} {9} {10}, then you have 2 {K}, a pair of {K}. If your opponent also has a pair of {K}, then use the kick to determine who is the winner. Another possibility is that there is already a pair in the common card (for example: {2} {Q} {Q} {7} {4}). At this moment, the key to the outcome is whether the player can get a second pair or it is a better deck, otherwise it is won by the players’ biggest kick.\n" +
                    "A pair of {A}\n" +
                    "Player 1’s card is {A} {K}\n" +
                    "Player 2’s card is {Q} {J}\n" +
                    "Public card is {A} {Q} {7} {5} {3}\n" +
                    " \n" +
                    "Player 1 can use 7 cards {A} {A} {K} {Q} {7} {5} {3}, and the best card group is a pair of {A}. Player 2 can use 7 cards {A} {Q} {Q} {J} {7} {5} {3}, player 2 best deck is pair of {Q}. Thus, player 1 wins.\n" +
                    " \n" +
                    "Two pairs\n" +
                    "In the player’s hole card and the common cards, two pairs are called two pairs.\n" +
                    "Two pairs {K} and {Q}\n" +
                    "Player 1’s card is {K} {K}\n" +
                    "Player 2’s card is {7} {7}\n" +
                    "Public card is {Q} {Q} {10} {9} {2}\n" +
                    "Player 1's hole card is a pair of {K} and a pair of {Q}, which is better than a pair of {Q} and a pair of {7} of player 2.\n" +
                    "\n" +
                    "Three of a kind\n" +
                    "In the player's hole card and the common card, the three cards with the same card are three.\n" +
                    "Three {9}, kick {K}\n" +
                    "Player 1’s card is {10} {9}\n" +
                    "Player 2’s card is {9} {K}\n" +
                    "Public card is {9] {8} {9} {A} {2}\n" +
                    "\n" +
                    "Player 1's cards are three {9} and kick {A}, 10, and player 2 also has three {9}, with kicks {A} and {K}. Thus, player 1 loses to player 2 because player 2 has a larger kick.\n" +
                    "\n" +
                    "Straight\n" +
                    "In the player's hole card and the common card, five cards of different suits and consecutive cards are called straights.\n" +
                    "Straight, K is the head\n" +
                    "Player 1’s card is {K} {Q}\n" +
                    "Player 2’s card is {A} {Q}\n" +
                    "Public card is {J} {10} {9} {8} {3}\n" +
                    "Player 1's card is a 9 to K straight {9} {10} {J} {Q} {K}, Player 2's card is a 8 to Q straight {8} {9} {10} {J} {Q}. Thus, player 1 wins.\n" +
                    "\n" +
                    "Flush\n" +
                    "A flush is a group of five cards that are not continuous but have the same suit.\n" +
                    "Flush vs Flush\n" +
                    "Player 1’s card is {3♠} {8♠}\n" +
                    "Player 2’s card is {9♠} {5♠}\n" +
                    "Public card is {A♠} {Q♠} {10♥} {7♠} {2♠}\n" +
                    "Both player 1 and player 2 have a flush, player 1 has a maximum card of 8, and player 2 has a maximum card of 9, so player 2 wins.\n" +
                    "\n" +
                    "Full house\n" +
                    "Full house refers to a deck with three and one pair at the same time.\n" +
                    "A-7 Full house\n" +
                    "Player 1’s card is {A} {A}\n" +
                    "Player 2’s card is {7} {5}\n" +
                    "Public card is {A} {7} {7} {K} {5}\n" +
                    "\n" +
                    "Player 1’s card is A-7 full house (three {A} and a pair of {7}), player 2’s card is 7-5 full house (three {7} and a pair of {5}). Thus, player 1 wins.\n" +
                    "\n" +
                    "Four of a kind\n" +
                    "Four of a kind is four cards with the number and one other card.\n" +
                    "Four {A}\n" +
                    "Player 1’s card is {10} {10}\n" +
                    "Player 2’s card is {A} {K}\n" +
                    "Public card is {A} {A} {A} {10} {10}\n" +
                    "\n" +
                    "Player 1's hole card is four {10}, and loses to player 2's four {A}.\n" +
                    "\n" +
                    "If two players have four of a kind (which may only occur if the four cards are common cards), so the fifth card will determine who is the winner.\n" +
                    "Straight flush\n" +
                    "Straight Flush is a straight with the same suit.\n" +
                    "6-10 Straight flush\n" +
                    "Player 1’s card is {4♦} {5♦}\n" +
                    "Player 2’s card is {9♦} {10♦}\n" +
                    "Public card is {A♠} {8♦} {7♦} {6♦} {2♠}\n" +
                    "\n" +
                    "Player 1’s card is 4-8 straight flush {4♦} {5♦} {6♦} {7♦} {8♦}, player 2’s card is 6-10 straight flush {6♦} {7♦} {8♦} {9♦} {10♦}. Thus, player 2 is the winner.\n" +
                    "\n" +
                    "Royal flush\n" +
                    "This is the biggest combination in texas hold’em.\n" +
                    "\n" +
                    "The straight flush with 10-J-Q-K-A five cards. There are only 4 combination in 260 million combination of 52 cards.\n" +
                    "\n" +
                    "Split pot\n" +
                    "If two players have the same values, that means their 5 cards cannot determine who are the winner. Then, two player splits the pot.\n" +
                    "Two pairs, kick Q:\n" +
                    "Player 1's card is {A} {2}\n" +
                    "Player 2's card is {A} {9}\n" +
                    "Public card is {A} {Q} {8} {8} {3}\n" +
                    "\n" +
                    "The best decks for both players are {A} {A} {8} {8} {Q}, so two players can split the pot.  Although {9} larger {2}, but each group can only select 5 cards, so ignore it {9} {2}.\n";
}
