package hk.edu.cityu.cs.fyp.texasholdem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;
import hk.edu.cityu.cs.fyp.texasholdem.model.Model;

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

    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        model = Model.getInstance();
        model.init();

        model.startRound();
        updateUI();
    }

    private void updateUI() {
        boolean isPlayerTurn = model.isPlayerTurn();
        foldButton.setEnabled(isPlayerTurn);
        callButton.setEnabled(isPlayerTurn);
        raiseButton.setEnabled(isPlayerTurn);

        roundText.setText(String.format("Round: %d", model.getRounds()));

        myMoneyText.setText("$" + model.getPlayerMoney());
        myBetsMoneyText.setText("Bet: $" + model.getPlayerBets());

        opponentMoneyText.setText("$" + model.getOpponentMoney());
        opponentBetsMoneyText.setText("Bet: $" + model.getOpponentBets());

        myHand1.setImageResource(Utils.getDrawableResByString(this, "c9"));
        myHand2.setImageResource(Utils.getDrawableResByString(this, "s10"));
    }

    @OnClick({
            R.id.fold,
            R.id.call,
            R.id.raise,
    })
    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.fold:
                model.playerFold();
                break;
            case R.id.call:
                model.playerCall();
                break;
            case R.id.raise:
                // TODO: view raise bar
                int raiseBets = 0;
                model.playerRaise(raiseBets);
                break;
        }
        updateUI();
    }


}
