package hk.edu.cityu.cs.fyp.texasholdem.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import hk.edu.cityu.cs.fyp.texasholdem.TexasHoldemApplication;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLog;
import hk.edu.cityu.cs.fyp.texasholdem.db.GameLogDao;

public class LogsViewModel extends ViewModel {

    private GameLogDao gameLogDao = TexasHoldemApplication.db.getResultDao();
    private LiveData<List<GameLog>> logsLive;
    private LiveData<Integer> countSyncGameLogsLive;
    private LiveData<Integer> countAllGameLogsLive;
    private LiveData<Double> sumbbLive;
    private LiveData<Double> totalMoneyLive;
    private MutableLiveData<Integer> aiPlayerValueLive;

    public void init(int aiPlayerValue) {
        aiPlayerValueLive = new MutableLiveData<>();
        aiPlayerValueLive.setValue(aiPlayerValue);

        countSyncGameLogsLive = Transformations.switchMap(aiPlayerValueLive, (aiPlayer) -> gameLogDao.countGameLogsByIsSyncAndAiPlayer(true, aiPlayer));
        countAllGameLogsLive = Transformations.switchMap(aiPlayerValueLive, (aiPlayer) -> gameLogDao.countGameLogsByAIPlayer(aiPlayer));
        sumbbLive = Transformations.switchMap(aiPlayerValueLive, (aiPlayer) -> gameLogDao.sumbb(aiPlayer));
        totalMoneyLive = Transformations.switchMap(aiPlayerValueLive, (aiPlayer) -> gameLogDao.getTotalMoney(aiPlayer));
    }

    public LiveData<List<GameLog>> getUnsyncGameLogsLive() {
        if (logsLive == null) {
            logsLive = gameLogDao.findUnsyncGameLogs();
        }
        return logsLive;
    }

    public void setAiPlayerValue(int aiPlayerValue) {
        aiPlayerValueLive.setValue(aiPlayerValue);
    }

    public LiveData<Integer> getCountSyncGameLogsLive() {
        return countSyncGameLogsLive;
    }

    public LiveData<Integer> getCountAllGameLogsLive() {
        return countAllGameLogsLive;
    }

    public LiveData<Double> getSumbbLive() {
        return sumbbLive;
    }

    public LiveData<Double> getTotalMoneyLive() {
        return totalMoneyLive;
    }

}
