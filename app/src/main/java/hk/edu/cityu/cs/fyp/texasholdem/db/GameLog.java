package hk.edu.cityu.cs.fyp.texasholdem.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.UUID;

@Entity
public class GameLog {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "result")
    private String result;

    @ColumnInfo(name = "is_sync")
    private boolean isSync;

    @ColumnInfo(name = "ai_player")
    private int aiPlayer;

    /**
     * the big blind bet size
     */
    @ColumnInfo(name = "bb")
    private double bb;

    @ColumnInfo(name = "money")
    private double money;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    public GameLog() {
        this.isSync = false;
        this.uuid = UUID.randomUUID().toString();
    }

    public GameLog(String data) {
        super();
        this.result = data;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public int getAiPlayer() {
        return aiPlayer;
    }

    public void setAiPlayer(int aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    public double getBb() {
        return bb;
    }

    public void setBb(double bb) {
        this.bb = bb;
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
