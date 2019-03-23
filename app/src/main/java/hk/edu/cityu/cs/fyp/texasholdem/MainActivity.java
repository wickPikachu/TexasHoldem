package hk.edu.cityu.cs.fyp.texasholdem;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.edu.cityu.cs.fyp.texasholdem.helper.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.logs)
    Button logsButton;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        logsButton.setVisibility(View.GONE);

        // test network
        NetworkHelper networkHelper = NetworkHelper.getInstance();
        networkHelper.sendStringRequest("123", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
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
        View v = LinearLayout.inflate(this, R.layout.alert_help, null);
        builder.setView(v);
        builder.show();
    }

}
