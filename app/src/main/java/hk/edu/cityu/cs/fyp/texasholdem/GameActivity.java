package hk.edu.cityu.cs.fyp.texasholdem;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;
import hk.edu.cityu.cs.fyp.texasholdem.model.TexasHoldem;
import hk.edu.cityu.cs.fyp.texasholdem.viewmodel.GameViewModel;

public class GameActivity extends AppCompatActivity {

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

    @BindView(R.id.opponent_money)
    TextView opponentMoneyText;

    @BindView(R.id.opponent_bets_money)
    TextView opponentBetsMoneyText;

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

    GameViewModel gameViewModel;
    TexasHoldem texasHoldem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        // TODO: view texasHoldem to control DB
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        texasHoldem = TexasHoldem.getInstance();
        texasHoldem.init();

        raiseValueSeekBar.setOnSeekBarChangeListener(onRaiseValueSeekBarChangeListener);
        texasHoldem.startRound();
        updateUI();
    }

    private void updateUI() {
        boolean isPlayerTurn = texasHoldem.isPlayerTurn();
        foldButton.setEnabled(isPlayerTurn);
        callButton.setEnabled(isPlayerTurn);
        raiseButton.setEnabled(isPlayerTurn);

        roundText.setText(String.format("Round: %d", texasHoldem.getRounds()));

        myMoneyText.setText("$" + texasHoldem.getPlayerMoney());
        myBetsMoneyText.setText("Bet: $" + texasHoldem.getPlayerBets());

        opponentMoneyText.setText("$" + texasHoldem.getOpponentMoney());
        opponentBetsMoneyText.setText("Bet: $" + texasHoldem.getOpponentBets());

        ArrayList<String> playerCards = texasHoldem.getPlayerCardList();
        myHand1.setImageResource(Utils.getDrawableResByString(this, playerCards.get(0)));
        myHand2.setImageResource(Utils.getDrawableResByString(this, playerCards.get(1)));
        messageView.setText(texasHoldem.getMessage());
        // default $200 in raise editText
        raiseBetsEditText.setText("200");
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
                texasHoldem.playerRaise(raiseBets);
                break;
        }
        updateUI();
    }

    private SeekBar.OnSeekBarChangeListener onRaiseValueSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // TODO: change raise bet
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

}
