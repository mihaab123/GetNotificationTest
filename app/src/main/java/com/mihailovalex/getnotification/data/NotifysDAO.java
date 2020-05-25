package com.mihailovalex.getnotification.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotifysDAO {

    @Query("SELECT * FROM NOTIFYS order by date")
    List<NotifyApp> getNotifys();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotify(NotifyApp notifyApp);

    @Update
    int updateNotify(NotifyApp notifyApp);

    @Query("DELETE FROM notifys")
    void deleteNotifys();

    @Query("DELETE FROM notifys WHERE id = :notifyId")
    int deleteNotifyById(long notifyId);

}
