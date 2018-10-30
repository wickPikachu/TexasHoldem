package hk.edu.cityu.cs.fyp.texasholdem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.play)
    public void onPlayClicked() {

    }

    @OnClick(R.id.help)
    public void onHelpClicked() {

    }
}
