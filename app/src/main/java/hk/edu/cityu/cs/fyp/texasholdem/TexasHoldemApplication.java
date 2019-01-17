package hk.edu.cityu.cs.fyp.texasholdem;

import android.app.Application;
import android.arch.persistence.room.Room;

import hk.edu.cityu.cs.fyp.texasholdem.db.TexasHoldemDataBase;

public class TexasHoldemApplication extends Application {

    public static TexasHoldemDataBase db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = Room.databaseBuilder(getApplicationContext(),
                TexasHoldemDataBase.class, "database-name").build();
    }

}
