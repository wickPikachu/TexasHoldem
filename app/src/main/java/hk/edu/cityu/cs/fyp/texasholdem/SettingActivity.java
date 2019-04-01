package hk.edu.cityu.cs.fyp.texasholdem;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SharedPreferencesHelper;

public class SettingActivity extends AppCompatActivity {

    public static final String TAG = "SettingActivity";

    @BindView(R.id.ai_player_random_tick)
    ImageView aiPlayerRandomTickImage;

    @BindView(R.id.ai_player_minimax_tick)
    ImageView aiPlayerMinimaxTickImage;

    @BindView(R.id.ai_player_ml_tick)
    ImageView aiPlayerMLTickImage;

    @BindView(R.id.ai_player_random_auto_sync_switch)
    Switch aiPlayerRandomAutoSyncSwitch;

    @BindView(R.id.ai_player_minimax_auto_sync_switch)
    Switch aiPlayerMinimaxAutoSyncSwitch;

    @BindView(R.id.ai_player_ml_auto_sync_switch)
    Switch aiPlayerMLAutoSyncSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        aiPlayerRandomAutoSyncSwitch.setTag(Constants.AI_PLAYER_RANDOM);
        aiPlayerMinimaxAutoSyncSwitch.setTag(Constants.AI_PLAYER_MINIMAX);
        aiPlayerMLAutoSyncSwitch.setTag(Constants.AI_PLAYER_MACHINE_LEARNING);

        updateUI();
    }

    public void updateUI() {
        int aiPlayerValue = SharedPreferencesHelper.getAIPlayer(this).getConstantValue();
        aiPlayerRandomTickImage.setVisibility(aiPlayerValue == Constants.AI_PLAYER_RANDOM ? View.VISIBLE : View.GONE);
        aiPlayerMinimaxTickImage.setVisibility(aiPlayerValue == Constants.AI_PLAYER_MINIMAX ? View.VISIBLE : View.GONE);
        aiPlayerMLTickImage.setVisibility(aiPlayerValue == Constants.AI_PLAYER_MACHINE_LEARNING ? View.VISIBLE : View.GONE);

        aiPlayerRandomAutoSyncSwitch.setChecked(SharedPreferencesHelper.getIsAIPlayerAutoSync(this, Constants.AI_PLAYER_RANDOM));
        aiPlayerMinimaxAutoSyncSwitch.setChecked(SharedPreferencesHelper.getIsAIPlayerAutoSync(this, Constants.AI_PLAYER_MINIMAX));
        aiPlayerMLAutoSyncSwitch.setChecked(SharedPreferencesHelper.getIsAIPlayerAutoSync(this, Constants.AI_PLAYER_MACHINE_LEARNING));
    }

    @OnClick({
            R.id.ai_player_random,
            R.id.ai_player_minimax,
            R.id.ai_player_ml,
    })
    public void onAIPlayerLayoutClicked(ConstraintLayout layout) {
        int aiPlayerValue;
        switch (layout.getId()) {
            default:
            case R.id.ai_player_random:
                aiPlayerValue = Constants.AI_PLAYER_RANDOM;
                break;
            case R.id.ai_player_minimax:
                aiPlayerValue = Constants.AI_PLAYER_MINIMAX;
                break;
            case R.id.ai_player_ml:
                aiPlayerValue = Constants.AI_PLAYER_MACHINE_LEARNING;
                break;
        }
        SharedPreferencesHelper.setAIPlayer(this, aiPlayerValue);
        updateUI();
    }

    @OnCheckedChanged({
            R.id.ai_player_random_auto_sync_switch,
            R.id.ai_player_minimax_auto_sync_switch,
            R.id.ai_player_ml_auto_sync_switch,
    })
    public void onAutoSyncSwitchChecked(Switch autoSyncSwitch, boolean isAutoSync) {
        int aiPlayerValue = Integer.valueOf(autoSyncSwitch.getTag().toString());
        SharedPreferencesHelper.setAIPlayerAutoSync(this, aiPlayerValue, isAutoSync);
    }
}
