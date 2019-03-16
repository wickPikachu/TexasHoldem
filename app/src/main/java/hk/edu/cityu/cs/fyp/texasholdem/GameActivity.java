package hk.edu.cityu.cs.fyp.texasholdem;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.edu.cityu.cs.fyp.texasholdem.Exeption.TexasHoldemException;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;
import hk.edu.cityu.cs.fyp.texasholdem.model.AIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.RandomAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.TexasHoldem;
import hk.edu.cityu.cs.fyp.texasholdem.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {

    public static final String TAG = "GameActivity";

    @BindViews({
            R.id.table_card1,
            R.id.table_card2,
            R.id.table_card3,
            R.id.table_card4,
            R.id.table_card5,
    })
    List<ImageView> tableCards;

    @BindView(R.id.fold)
    Button foldButton;

    @BindView(R.id.raise)
    Button raiseButton;

    @BindView(R.id.call)
    Button callButton;

    @BindView(R.id.my_hand1)
    ImageView myHand1;

    @BindView(R.id.my_hand2)
    ImageView myHand2;

    @BindView(R.id.my_money)
    TextView myMoneyText;

    @BindView(R.id.computer_money)
    TextView computerMoneyText;

    @BindView(R.id.computer_bets_money)
    TextView computerBetsMoneyText;

    @BindView(R.id.my_bets_money)
    TextView myBetsMoneyText;

    @BindView(R.id.round)
    TextView roundText;

    @BindView(R.id.raise_bets_edit)
    EditText raiseBetsEditText;

    @BindView(R.id.raise_value_bar)
    SeekBar raiseValueSeekBar;

    @BindView(R.id.message)
    TextView messageView;

    @BindView(R.id.computer_text)
    TextView computerNameText;

    GameViewModel gameViewModel;
    TexasHoldem texasHoldem;
    AIPlayer aiPlayer;
    List<GameLog> unSyncGameLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        // from shared preference get AI player level
        aiPlayer = Utils.getAIPlayer(this);
        computerNameText.setText(aiPlayer.getClass().getSimpleName() + ":");

        // TODO: view texasHoldem to control DB
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        gameViewModel.getUnsyncResult().observe(this, new Observer<List<GameLog>>() {
            @Override
            public void onChanged(@Nullable List<GameLog> gameLogs) {
                if (unSyncGameLogs == null)
                    unSyncGameLogs = gameLogs;
            }
        });

        // default $200 in raise editText
        raiseBetsEditText.setText("200");

        texasHoldem = TexasHoldem.getInstance();
        texasHoldem.init();

        raiseValueSeekBar.setOnSeekBarChangeListener(onRaiseValueSeekBarChangeListener);
        texasHoldem.startRound();
        updateUI();
    }

    private void updateUI() {
        // AIPlayer turn
        if (texasHoldem.isComputerTurn()) {
            texasHoldem.takeAction(aiPlayer);
        }

        if (texasHoldem.isBothCalled()) {
            texasHoldem.next();
        }

        roundText.setText(String.format("Round: %d", texasHoldem.getRounds()));

        myMoneyText.setText("$" + texasHoldem.getPlayerMoney());
        myBetsMoneyText.setText("Bet: $" + texasHoldem.getPlayerBets());

        computerMoneyText.setText("$" + texasHoldem.getComputerMoney());
        computerBetsMoneyText.setText("Bet: $" + texasHoldem.getComputerBets());

        ArrayList<String> playerCards = texasHoldem.getPlayerCardList();
        myHand1.setImageResource(Utils.getDrawableResByString(this, playerCards.get(0)));
        myHand2.setImageResource(Utils.getDrawableResByString(this, playerCards.get(1)));
        messageView.setText(texasHoldem.getMessage());

        ArrayList<String> tableCardList = texasHoldem.getTableCardList();
        for (int i = 0; i < tableCardList.size(); i++) {
            tableCards.get(i).setImageResource(Utils.getDrawableResByString(this, tableCardList.get(i)));
        }

        if (texasHoldem.isPlayerTurn()) {
            foldButton.setEnabled(true);
            callButton.setEnabled(true);
            raiseButton.setEnabled(true);
        }
    }

    @OnClick({
            R.id.fold,
            R.id.call,
            R.id.raise,
    })
    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.fold:
                texasHoldem.playerFold();
                break;
            case R.id.call:
                texasHoldem.playerCall();
                break;
            case R.id.raise:
                int raiseBets = Integer.parseInt(raiseBetsEditText.getText().toString());
                try {
                    texasHoldem.playerRaise(raiseBets);
                } catch (TexasHoldemException e) {
                    e.printStackTrace();
                }
                break;
        }

        updateUI();
    }

    private SeekBar.OnSeekBarChangeListener onRaiseValueSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int playerMoney = texasHoldem.getPlayerMoney();
            int changedBets = ((int) (progress * (double) playerMoney / 100) / 100) * 100;
            raiseBetsEditText.setText("" + changedBets);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    @OnClick(R.id.round)
    public void onRoundTextClicked() {
        if (unSyncGameLogs != null && !unSyncGameLogs.isEmpty()) {
            Log.d(TAG, "onRoundTextClicked: " + unSyncGameLogs.size());
            Log.d(TAG, "onRoundTextClicked: " + unSyncGameLogs.get(0).toString());
        }
    }

    @OnClick({
            R.id.add2,
            R.id.add1,
            R.id.minus1,
            R.id.minus2
    })
    public void onAddMinusButtonClicked(Button button) {
        int playerMoney = texasHoldem.getPlayerMoney();
        int raiseBets = Integer.parseInt(raiseBetsEditText.getText().toString());
        if (raiseBets % 100 != 0) {
            raiseBets = 0;
        }
        switch (button.getId()) {
            case R.id.add2:
                raiseBets += 200;
            case R.id.add1:
                raiseBets += 100;
                break;
            case R.id.minus1:
                raiseBets -= 100;
                break;
            case R.id.minus2:
                raiseBets -= 200;
                break;

        }
        if (raiseBets > playerMoney) {
            raiseBets = playerMoney;
        } else if (raiseBets < 0) {
            raiseBets = 0;
        }

        raiseBetsEditText.setText("" + raiseBets);
    }

}
