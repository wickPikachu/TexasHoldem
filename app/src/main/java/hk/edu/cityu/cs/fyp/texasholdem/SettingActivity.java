package hk.edu.cityu.cs.fyp.texasholdem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.edu.cityu.cs.fyp.texasholdem.model.MachineLearningAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.MiniMaxAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.RandomAIPlayer;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        String [] playersName = {
                RandomAIPlayer.class.getName(),
                MiniMaxAIPlayer.class.getName(),
                MachineLearningAIPlayer.class.getName()
        };




    }

}