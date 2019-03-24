package hk.edu.cityu.cs.fyp.texasholdem;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;
import hk.edu.cityu.cs.fyp.texasholdem.viewmodel.LogsViewModel;

public class LogsActivity extends AppCompatActivity {

    public static final String TAG = "LogsActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    LogsViewModel logsViewModel;
    List<GameLog> allGameLogs;
    LogsAdapter logsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        ButterKnife.bind(this);

        logsAdapter = new LogsAdapter();
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(logsAdapter);

        logsViewModel = ViewModelProviders.of(this).get(LogsViewModel.class);
        logsViewModel.getAllGameLogsLive().observe(this, new Observer<List<GameLog>>() {
            @Override
            public void onChanged(@Nullable List<GameLog> gameLogs) {
                if (allGameLogs == null) {
                    allGameLogs = gameLogs;
                    logsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_log, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.result.setText(allGameLogs.get(i).getResult());
        }

        @Override
        public int getItemCount() {
            if (allGameLogs == null) {
                return 0;
            }
            return allGameLogs.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.result)
            TextView result;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(onClickListener);
            }

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            };
        }

    }


}
