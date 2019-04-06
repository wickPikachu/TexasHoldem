package hk.edu.cityu.cs.fyp.texasholdem.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface GameLogDao {

    @Query("SELECT * FROM GameLog WHERE is_sync = 0")
    LiveData<List<GameLog>> findUnsyncGameLogs();

    @Query("SELECT * FROM GameLog WHERE is_sync = 0 and ai_player = :aiPlayer")
    LiveData<List<GameLog>> findUnsyncGameLogs(int aiPlayer);

    @Query("SELECT * FROM GameLog")
    LiveData<List<GameLog>> findAllGameLogs();

    @Query("SELECT COUNT(*) FROM GameLog where ai_player = :aiPlayer")
    LiveData<Integer> countGameLogsByAIPlayer(int aiPlayer);

    @Query("SELECT COUNT(*) FROM GameLog where ai_player = :aiPlayer and is_sync = :isSync")
    LiveData<Integer> countGameLogsByIsSyncAndAiPlayer(boolean isSync, int aiPlayer);

    @Query("SELECT SUM(money/bb) FROM GameLog where ai_player = :aiPlayer")
    LiveData<Double> sumMoneyDivideBybb(int aiPlayer);

    @Query("SELECT SUM(money) FROM GameLog where ai_player = :aiPlayer")
    LiveData<Double> getTotalMoney(int aiPlayer);

    @Query("UPDATE  GameLog set is_sync = :isSync where uuid IN (:uuids)")
    void updateIsSync(List<String> uuids, boolean isSync);

    @Deprecated
    @Query("UPDATE  GameLog set is_sync = :isSync where 1")
    void updateAllIsSync(boolean isSync);

    @Insert
    void insert(GameLog gameLog);

    @Insert
    void insertAll(GameLog... gameLogs);

    @Delete
    void delete(GameLog gameLog);
}
