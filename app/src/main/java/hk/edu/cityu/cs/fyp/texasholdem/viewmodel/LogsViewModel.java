package hk.edu.cityu.cs.fyp.texasholdem.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import hk.edu.cityu.cs.fyp.texasholdem.TexasHoldemApplication;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;

public class LogsViewModel extends ViewModel {

    private LiveData<List<GameLog>> logsLive;

    public LiveData<List<GameLog>> getAllGameLogsLive() {
        if (logsLive == null) {
            logsLive = TexasHoldemApplication.db.getResultDao().findAllGameLogs();
        }
        return logsLive;
    }

}
