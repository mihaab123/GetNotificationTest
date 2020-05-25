package com.mihailovalex.getnotification.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NotifyRepository implements NotifyDataSource {

    private volatile static NotifyRepository INSTANCE = null;


    private final NotifyDataSource mNotifysLocalDataSource;
    Map<Long, NotifyApp> mCachedNitifys;

    private NotifyRepository(@NonNull NotifyDataSource notifyDataSource) {
        mNotifysLocalDataSource = notifyDataSource;
    }

    public static NotifyRepository getInstance(NotifyDataSource notifyDataSource) {
        if (INSTANCE == null) {
            synchronized (NotifyRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotifyRepository(notifyDataSource);
                }
            }
        }
        return INSTANCE;
    }
    @Override
    public void getNotifys(final @NonNull LoadNotifyCallback callback) {
        mNotifysLocalDataSource.getNotifys(new LoadNotifyCallback() {
            @Override
            public void onNotifyLoaded(List<NotifyApp> notify) {
                refreshCache(notify);
                callback.onNotifyLoaded(new ArrayList<>(mCachedNitifys.values()));
            }

            @Override
            public void onDataNotAvailable() {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedNitifys == null) {
                    mCachedNitifys = new LinkedHashMap<>();
                }
                callback.onNotifyLoaded(new ArrayList<>(mCachedNitifys.values()));
            }
        });
    }

    @Override
    public void saveNotify(@NonNull NotifyApp notifyApp) {
        mNotifysLocalDataSource.saveNotify(notifyApp);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedNitifys == null) {
            mCachedNitifys = new LinkedHashMap<>();
        }
        mCachedNitifys.put(notifyApp.getId(), notifyApp);
    }

    @Override
    public void deleteAllNotifys() {
        mNotifysLocalDataSource.deleteAllNotifys();

        if (mCachedNitifys == null) {
            mCachedNitifys = new LinkedHashMap<>();
        }
        mCachedNitifys.clear();
    }

    @Override
    public void deleteNotify(@NonNull long notifyId) {
        mNotifysLocalDataSource.deleteNotify(notifyId);

        mCachedNitifys.remove(notifyId);
    }
    private void refreshCache(List<NotifyApp> notifyApps) {
        if (mCachedNitifys == null) {
            mCachedNitifys = new LinkedHashMap<>();
        }
        mCachedNitifys.clear();
        for (NotifyApp notifyApp : notifyApps) {
            mCachedNitifys.put(notifyApp.getId(), notifyApp);
        }
    }
}
