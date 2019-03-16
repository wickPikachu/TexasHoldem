package hk.edu.cityu.cs.fyp.texasholdem;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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

    @OnClick(R.id.help)
    public void onHelpClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        View v = LinearLayout.inflate(this, R.layout.alert_help, null);
        builder.setView(v);
        builder.show();
    }

}
