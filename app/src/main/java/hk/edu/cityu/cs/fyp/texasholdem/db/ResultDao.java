package hk.edu.cityu.cs.fyp.texasholdem.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ResultDao {

    @Query("SELECT * FROM result WHERE is_sync = 0")
    LiveData<List<Result>> findUnsyncResults();

    @Insert
    void insertAll(Result... results);

    @Delete
    void delete(Result result);
}
