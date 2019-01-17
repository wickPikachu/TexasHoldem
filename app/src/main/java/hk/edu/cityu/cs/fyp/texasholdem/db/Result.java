package hk.edu.cityu.cs.fyp.texasholdem.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity
public class Result {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "data")
    public String data;

    @ColumnInfo(name = "is_sync")
    public boolean isSync;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "updated_at")
    public Date updatedAt;
}
