package hk.edu.cityu.cs.fyp.texasholdem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SharedPreferencesHelper;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;
import hk.edu.cityu.cs.fyp.texasholdem.model.MachineLearningAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.MiniMaxAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.RandomAIPlayer;

public class SettingActivity extends AppCompatActivity {

    public static final String TAG = "SettingActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SettingAdapter settingAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        String[] playersName = {
                RandomAIPlayer.NAME,
                MiniMaxAIPlayer.NAME,
                MachineLearningAIPlayer.NAME,
        };

        // specify an adapter (see also next example)
        settingAdapter = new SettingAdapter(playersName);
        recyclerView.setAdapter(settingAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingAdapter.notifyDataSetChanged();
    }

    class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

        private String[] names;

        public SettingAdapter(String[] names) {
            this.names = names;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_setting, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            String name = names[i];
            viewHolder.name.setText(name);
            if (name.equals(SharedPreferencesHelper.getAIPlayer(SettingActivity.this).getName())) {
                viewHolder.tickImage.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tickImage.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.name)
            TextView name;

            @BindView(R.id.tick)
            ImageView tickImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(onClickListener);
            }

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int aiPlayerValue = Utils.getAIPlayerValue(name.getText().toString());
                    SharedPreferencesHelper.setAIPlayer(SettingActivity.this, aiPlayerValue);
                    settingAdapter.notifyDataSetChanged();
                }
            };
        }

    }

}
