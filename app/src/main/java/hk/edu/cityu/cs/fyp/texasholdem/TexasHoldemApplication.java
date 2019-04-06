package hk.edu.cityu.cs.fyp.texasholdem;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;

import hk.edu.cityu.cs.fyp.texasholdem.db.TexasHoldemDataBase;
import hk.edu.cityu.cs.fyp.texasholdem.helper.Constants;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SharedPreferencesHelper;
import hk.edu.cityu.cs.fyp.texasholdem.helper.SocketHelper;

public class TexasHoldemApplication extends Application {

    public static TexasHoldemDataBase db;
    private static HandlerThread dataHandlerThread;
    private static Handler dataHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        dataHandlerThread = new HandlerThread("DataThread");
        dataHandlerThread.start();
        dataHandler = new Handler(dataHandlerThread.getLooper());

        SocketHelper.getInstance().init(this);

        db = Room.databaseBuilder(getApplicationContext(),
                TexasHoldemDataBase.class, "database-name").build();

        SharedPreferences pref = SharedPreferencesHelper.getSharedPreferences(this);
        int aiPlayer = pref.getInt(Constants.SharedPref.KEY_AI_PLAYER, -1);
        if (aiPlayer == -1) {
            pref.edit().putInt(Constants.SharedPref.KEY_AI_PLAYER, Constants.AI_PLAYER_RANDOM).apply();
        }
    }

    public static void postToDataThread(Runnable runnable) {
        dataHandler.post(runnable);
    }
}
