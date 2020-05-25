

package com.mihailovalex.getnotification;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mihailovalex.getnotification.data.NotifyDatabase;
import com.mihailovalex.getnotification.data.NotifyLocalDataSource;
import com.mihailovalex.getnotification.data.NotifyRepository;
import com.mihailovalex.getnotification.utils.AppExecutors;
import com.mihailovalex.getnotification.vm.NotifyViewModel;


/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;
    private final NotifyRepository mNotifysRepository;
    private final Application mApplication;
    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    NotifyDatabase database = NotifyDatabase.getInstance(application.getApplicationContext());
                    INSTANCE = new ViewModelFactory(application,
                            NotifyRepository.getInstance(NotifyLocalDataSource.getInstance(new AppExecutors(),
                                    database.notifyDao())));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public ViewModelFactory(Application application, NotifyRepository notifyRepository) {
        mNotifysRepository = notifyRepository;
        mApplication = application;

    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NotifyViewModel.class)) {
            return (T) new NotifyViewModel(mApplication, mNotifysRepository);
        }
        //noinspection unchecked
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
