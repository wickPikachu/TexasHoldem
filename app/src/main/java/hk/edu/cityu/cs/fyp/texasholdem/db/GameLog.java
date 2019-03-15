package hk.edu.cityu.cs.fyp.texasholdem.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity
public class GameLog {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "result")
    private String result;

    @ColumnInfo(name = "is_sync")
    private boolean isSync;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    public GameLog() {
    }

    public GameLog(String data) {
        this.result = data;
        this.isSync = false;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String toString() {
        return "result:" + result + "\n" + "synced:" + isSync + "\n";
    }

}
