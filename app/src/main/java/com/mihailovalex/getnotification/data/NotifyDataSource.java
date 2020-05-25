package com.mihailovalex.getnotification.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface NotifyDataSource {
    interface LoadNotifyCallback {

        void onNotifyLoaded(List<NotifyApp> notify);

        void onDataNotAvailable();
    }

    void getNotifys(@NonNull LoadNotifyCallback callback);

    void saveNotify(@NonNull NotifyApp notifyApp);

    void deleteAllNotifys();

    void deleteNotify(@NonNull long notifyId);
}
