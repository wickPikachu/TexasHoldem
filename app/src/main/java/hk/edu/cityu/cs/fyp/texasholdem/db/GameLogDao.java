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

    @Query("SELECT * FROM GameLog")
    LiveData<List<GameLog>> findAllGameLogs();

    @Insert
    void insert(GameLog gameLog);

    @Insert
    void insertAll(GameLog... gameLogs);

    @Delete
    void delete(GameLog gameLog);
}
