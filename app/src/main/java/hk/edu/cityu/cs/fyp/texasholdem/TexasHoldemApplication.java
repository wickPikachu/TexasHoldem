package hk.edu.cityu.cs.fyp.texasholdem;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;

import hk.edu.cityu.cs.fyp.texasholdem.db.TexasHoldemDataBase;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.NetworkHelper;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SharedPreferencesHelper;

public class TexasHoldemApplication extends Application {

    public static TexasHoldemDataBase db;

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkHelper.getInstance().init(this);

        db = Room.databaseBuilder(getApplicationContext(),
                TexasHoldemDataBase.class, "database-name").build();

        SharedPreferences pref = SharedPreferencesHelper.getSharedPreferences(this);
        int aiPlayer = pref.getInt(Constants.SharedPref.KEY_AI_PLAYER, -1);
        if (aiPlayer == -1) {
            pref.edit().putInt(Constants.SharedPref.KEY_AI_PLAYER, Constants.AI_PLAYER_RANDOM).apply();
        }
    }

}
