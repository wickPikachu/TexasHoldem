package hk.edu.cityu.cs.fyp.texasholdem.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import hk.edu.cityu.cs.fyp.texasholdem.TexasHoldemApplication;
import hk.edu.cityu.cs.fyp.texasholdem.db.Result;

public class GameViewModel extends ViewModel {

    private LiveData<List<Result>> resultsLive;

    public LiveData<List<Result>> getUnsyncResult() {
        if (resultsLive == null) {
            resultsLive = TexasHoldemApplication.db.getResultDao().findUnsyncResults();
        }
        return resultsLive;
    }

}
