package hk.edu.cityu.cs.fyp.texasholdem;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SharedPreferencesHelper;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SocketHelper;
import hk.edu.cityu.cs.fyp.texasholdem.viewmodel.LogsViewModel;

public class LogsActivity extends AppCompatActivity {

    public static final String TAG = "LogsActivity";

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.log_detail)
    TextView logDetail;

    @BindView(R.id.sync_button)
    Button syncButton;

    LogsViewModel logsViewModel;
    List<GameLog> unsyncGameLogs;
    AIPlayerNameAdapter aiPlayerNameAdapter;
    int aiPlayerValue;
    int hands = 0;
    double sumMoneyDivideBybb = 0;
    double totalMoney = 0;
    int syncNum = 0;
    SocketHelper socketHelper = SocketHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        ButterKnife.bind(this);

        String[] aiPlayerNames = getResources().getStringArray(R.array.ai_player_array);
        aiPlayerNameAdapter = new AIPlayerNameAdapter(aiPlayerNames);
        spinner.setAdapter(aiPlayerNameAdapter);

        aiPlayerValue = SharedPreferencesHelper.getAIPlayer(this).getConstantValue();
        spinner.setSelection(aiPlayerValue - 1);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

        logsViewModel = ViewModelProviders.of(this).get(LogsViewModel.class);
        logsViewModel.init(aiPlayerValue);

        logsViewModel.getUnsyncGameLogsLive().observe(this, gameLogs -> {
            unsyncGameLogs = gameLogs;
            updateUI();
        });

        logsViewModel.getCountAllGameLogsLive().observe(this, count -> {
            if (count != null) {
                hands = count;
            } else {
                this.hands = 0;
            }
            updateUI();
        });

        logsViewModel.getCountSyncGameLogsLive().observe(this, count -> {
            if (count != null) {
                this.syncNum = count;
            } else {
                this.syncNum = 0;
            }
            updateUI();
        });

        logsViewModel.getSumMoneyDivideBybbLive().observe(this, bb -> {
            if (bb != null) {
                this.sumMoneyDivideBybb = bb;
            } else {
                this.sumMoneyDivideBybb = 0;
            }
            updateUI();
        });

        logsViewModel.getTotalMoneyLive().observe(this, totalMoney -> {
            if (totalMoney != null) {
                this.totalMoney = totalMoney;
            } else {
                this.totalMoney = 0;
            }
            updateUI();
        });

        // TODO: delete, for test save csv
        TexasHoldemApplication.postToDataThread(() -> {
//            TexasHoldemApplication.db.getResultDao().updateAllIsSync(false);
        });
    }

    Spinner.OnItemSelectedListener onItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                logsViewModel.setAiPlayerValue(1);
            } else if (i == 1) {
                logsViewModel.setAiPlayerValue(3);
            } else {
                logsViewModel.setAiPlayerValue(1);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // nothing to do
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("DefaultLocale")
    public void updateUI() {
        if (unsyncGameLogs != null)
            Log.d(TAG, "updateUI: numberOfGameLogs: " + unsyncGameLogs.size());
        int unsyncNum = hands - syncNum;
        logDetail.setText(String.format("Hands: %d\n" +
                        "Total money earned: %.0f\n" +
                        "Win rate (bb/100): %.2f\n" +
                        "Synchronized: %d\n" +
                        "Un-synchronized: %d\n"
                , hands, totalMoney, sumMoneyDivideBybb / ((double) hands / 100), syncNum, unsyncNum));
        syncButton.setEnabled(unsyncNum > 0);
    }

    @OnClick(R.id.sync_button)
    public void onSyncButtonClicked() {
        socketHelper.connectToServer(new SocketHelper.SocketListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: " + jsonObject.toString());
                TexasHoldemApplication.postToDataThread(() -> {
                    if (jsonObject.has(Constants.Json.KEY_SUCCESS)) {
                        try {
                            JSONArray uuidArray = jsonObject.getJSONArray(Constants.Json.KEY_SUCCESS);
                            int len = uuidArray.length();
                            ArrayList<String> uuids = new ArrayList<>();
                            for (int i = 0; i < len; i++) {
                                uuids.add(uuidArray.getString(i));
                            }
                            TexasHoldemApplication.db.getResultDao().updateIsSync(uuids, true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, "onError: " + errorMsg);
            }
        });
        // upload action
        socketHelper.uploadGameLogs(unsyncGameLogs);
    }

    class AIPlayerNameAdapter extends BaseAdapter {

        String[] names;

        AIPlayerNameAdapter(String[] names) {
            this.names = names;
        }

        @Override
        public int getCount() {
            if (names == null)
                return 0;
            return names.length;
        }

        @Override
        public String getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(LogsActivity.this).inflate(R.layout.spinner_log_item, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.name.setText(names[i]);

            return view;
        }

        class ViewHolder {

            @BindView(R.id.name)
            TextView name;

            ViewHolder(View v) {
                ButterKnife.bind(this, v);
            }
        }

    }

}
