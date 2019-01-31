package hk.edu.cityu.cs.fyp.texasholdem.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {
        GameLog.class
}, version = 1)
public abstract class TexasHoldemDataBase extends RoomDatabase {

    public abstract GameLogDao getResultDao();

}
