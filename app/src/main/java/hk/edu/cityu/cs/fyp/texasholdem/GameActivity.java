package hk.edu.cityu.cs.fyp.texasholdem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {

    @BindViews({
            R.id.table_card1,
            R.id.table_card2,
            R.id.table_card3,
            R.id.table_card4,
            R.id.table_card5,
    })
    List<ImageView> tableCards;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        roundText.setText("Round: 1");

        myMoneyText.setText("$19900");
        myBetsMoneyText.setText("Bet: $100");

        opponentMoneyText.setText("$19800");
        opponentBetsMoneyText.setText("Bet: $200");

        myHand1.setImageResource(R.drawable.c8);
        myHand2.setImageResource(R.drawable.sk);

    }

    @OnClick({
            R.id.fold,
            R.id.call,
            R.id.raise
    })
    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.fold:
                break;
            case R.id.call:
                break;
            case R.id.raise:
                break;
        }
    }


}
