package hk.edu.cityu.cs.fyp.texasholdem.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import hk.edu.cityu.cs.fyp.texasholdem.TexasHoldemApplication;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;

public class GameViewModel extends ViewModel {

    private LiveData<List<GameLog>> resultsLive;

    public LiveData<List<GameLog>> getUnsyncResult() {
        if (resultsLive == null) {
            resultsLive = TexasHoldemApplication.db.getResultDao().findUnsyncResults();
        }
        return resultsLive;
    }

}
