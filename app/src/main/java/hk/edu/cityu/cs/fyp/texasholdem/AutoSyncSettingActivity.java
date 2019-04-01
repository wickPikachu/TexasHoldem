package hk.edu.cityu.cs.fyp.texasholdem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SharedPreferencesHelper;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Utils;
import hk.edu.cityu.cs.fyp.texasholdem.model.MachineLearningAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.MiniMaxAIPlayer;
import hk.edu.cityu.cs.fyp.texasholdem.model.RandomAIPlayer;

@Deprecated
public class AutoSyncSettingActivity extends AppCompatActivity {

    public static final String TAG = "AutoSyncSettingActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AutoSyncSettingAdapter autoSyncSettingAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sync_setting);
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
        autoSyncSettingAdapter = new AutoSyncSettingAdapter(playersName);
        recyclerView.setAdapter(autoSyncSettingAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoSyncSettingAdapter.notifyDataSetChanged();
    }

    class AutoSyncSettingAdapter extends RecyclerView.Adapter<AutoSyncSettingAdapter.ViewHolder> {

        private String[] names;

        public AutoSyncSettingAdapter(String[] names) {
            this.names = names;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_auto_sync_setting, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            String name = names[i];
            viewHolder.name.setText(name);
            int aiPlayerValue = Utils.getAIPlayerValue(name);
            viewHolder.autoSyncSwitch.setChecked(SharedPreferencesHelper.getIsAIPlayerAutoSync(AutoSyncSettingActivity.this, aiPlayerValue));
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.name)
            TextView name;

            @BindView(R.id.auto_sync_switch)
            Switch autoSyncSwitch;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                autoSyncSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
            }

            Switch.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isAutoSync) {
                    int aiPlayerValue = Utils.getAIPlayerValue(name.getText().toString());
                    SharedPreferencesHelper.setAIPlayerAutoSync(AutoSyncSettingActivity.this, aiPlayerValue, isAutoSync);
                }
            };
        }

    }

}
