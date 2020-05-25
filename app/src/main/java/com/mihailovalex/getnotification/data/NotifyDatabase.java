package com.mihailovalex.getnotification.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



    @Database(entities = {NotifyApp.class}, version = 2)
    public abstract class NotifyDatabase extends RoomDatabase {

        private static NotifyDatabase INSTANCE;

        public abstract NotifysDAO notifyDao();


        public synchronized static NotifyDatabase getInstance(Context context) {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        NotifyDatabase.class, "Notifys.db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;

        }


    }

