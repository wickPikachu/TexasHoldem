package hk.edu.cityu.cs.fyp.texasholdem;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SocketHelper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.logs)
    Button logsButton;

    public static final String TAG = "MainActivity";
    SocketHelper socketHelper = SocketHelper.getInstance();
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        logsButton.setVisibility(View.GONE);

        jsonObject = new JSONObject();
        try {
            jsonObject.put("test", "test message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // test network
        socketHelper.connectToServer(BuildConfig.SERVER_URL, BuildConfig.SERVER_PORT, new SocketHelper.SocketListener() {
            @Override
            public void onResponse(JSONObject message) {
                Log.d(TAG, "onResponse: " + message.toString());
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, "onError: " + errorMsg);
            }
        });
        socketHelper.sent(jsonObject);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketHelper.disconnect();
    }

    @OnClick(R.id.play)
    public void onPlayClicked() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.setting)
    public void onSettingClicked() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logs)
    public void onLogsClicked() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.help)
    public void onHelpClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        builder.setMessage(Constants.HELP_MESSAGE_RULE);
        View v = LinearLayout.inflate(this, R.layout.alert_help, null);
        builder.setView(v);
        builder.show();

        socketHelper.sent(jsonObject);
    }

}
