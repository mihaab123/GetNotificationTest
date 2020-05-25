package com.mihailovalex.getnotification.data;

import androidx.annotation.NonNull;

import com.mihailovalex.getnotification.utils.AppExecutors;

import java.util.List;

public class NotifyLocalDataSource  implements  NotifyDataSource{

    private static volatile NotifyLocalDataSource INSTANCE;

    private NotifysDAO mNotifysDAO;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private NotifyLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull NotifysDAO notifysDAO) {
        mAppExecutors = appExecutors;
        mNotifysDAO = notifysDAO;
    }
    public static NotifyLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull NotifysDAO notifysDAO) {
        if (INSTANCE == null) {
            synchronized (NotifyLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotifyLocalDataSource(appExecutors, notifysDAO);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getNotifys(@NonNull final LoadNotifyCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final List<NotifyApp> notifys = mNotifysDAO.getNotifys();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (notifys.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onNotifyLoaded(notifys);
                        }
                    }
                });

            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveNotify(@NonNull final NotifyApp notifyApp) {

        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mNotifysDAO.insertNotify(notifyApp);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteAllNotifys() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mNotifysDAO.deleteNotifys();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteNotify(@NonNull final long notifyId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mNotifysDAO.deleteNotifyById(notifyId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
