package com.mihailovalex.getnotification.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifys")
public class NotifyApp {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private  long id;

    @NonNull
    private  String title;

    private  long date;

    private String packageName;

    public NotifyApp(@NonNull String title, long date, String packageName) {
        this.title = title;
        this.date = date;
        this.packageName = packageName;
    }
    @Ignore
    public NotifyApp(@NonNull String title, long date) {
        this.title = title;
        this.date = date;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
