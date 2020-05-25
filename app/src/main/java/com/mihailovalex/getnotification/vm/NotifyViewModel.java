package com.mihailovalex.getnotification.vm;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.mihailovalex.getnotification.MainActivity;
import com.mihailovalex.getnotification.NotifysFilterType;
import com.mihailovalex.getnotification.R;
import com.mihailovalex.getnotification.data.NotifyApp;
import com.mihailovalex.getnotification.data.NotifyDataSource;
import com.mihailovalex.getnotification.data.NotifyRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotifyViewModel extends AndroidViewModel {
    // These observable fields will update Views automatically
    public final ObservableList<NotifyApp> items = new ObservableArrayList<>();


    public final ObservableField<Drawable> noNotifyIconRes = new ObservableField<>();
    public final ObservableField<String> noNotifysLabel = new ObservableField<>();

    public final ObservableBoolean empty = new ObservableBoolean(false);

    private final NotifyRepository mNotifysRepository;


    private final Context mContext; // To avoid leaks, this must be an Application Context.


    private NotifysFilterType mCurrentFiltering = NotifysFilterType.ALL;


    private SharedPreferences sPref;

    private MainActivity.NotificationReceiver nReceiver;

    public final ObservableField<String> buttonText = new ObservableField<>();
    public final ObservableField<Drawable> buttonColor = new ObservableField<Drawable>();
    public final ObservableField<Integer> buttonColorText = new ObservableField<Integer>();

    public NotifyViewModel(Application context,
                           NotifyRepository repository) {
        super(context);
        mContext = context.getApplicationContext(); // Force use of Application Context.
        mNotifysRepository = repository;
        sPref = PreferenceManager.getDefaultSharedPreferences(mContext);

        // Set initial state
        setFiltering(NotifysFilterType.ALL);
    }
    public void setFiltering(NotifysFilterType requestType) {
        mCurrentFiltering = requestType;
        noNotifysLabel.set(mContext.getResources().getString(R.string.no_notifys_all));
        noNotifyIconRes.set(mContext.getResources().getDrawable(
                R.drawable.img_no_notifications));
    }

    public void start() {
        loadTasks();
    }

    public void loadTasks() {
        setButton(false);

        mNotifysRepository.getNotifys(new NotifyDataSource.LoadNotifyCallback() {
            @Override
            public void onNotifyLoaded(List<NotifyApp> notifyApps) {
                List<NotifyApp> notifysToShow = new ArrayList<>();
                Calendar curCalender = Calendar.getInstance();
                Calendar notifyCalender = Calendar.getInstance();
                // We filter the tasks based on the requestType
                for (NotifyApp notifyApp : notifyApps) {
                    switch (mCurrentFiltering) {
                        case ALL:
                            notifysToShow.add(notifyApp);
                            break;
                        case PER_HOUR:
                            curCalender.add(Calendar.HOUR_OF_DAY,-1);
                            notifyCalender.setTimeInMillis(notifyApp.getDate());
                            if (curCalender.before(notifyCalender)) {
                                notifysToShow.add(notifyApp);
                            }
                            break;
                        case PER_DAY:
                            curCalender.add(Calendar.DAY_OF_YEAR,-1);
                            notifyCalender.setTimeInMillis(notifyApp.getDate());
                            if (curCalender.before(notifyCalender)) {
                                notifysToShow.add(notifyApp);
                            }
                            break;
                        case PER_MONTH:
                            curCalender.add(Calendar.MONTH,-1);
                            notifyCalender.setTimeInMillis(notifyApp.getDate());
                            if (curCalender.before(notifyCalender)) {
                                notifysToShow.add(notifyApp);
                            }
                            break;
                        default:
                            notifysToShow.add(notifyApp);
                            break;
                    }
                }

                items.clear();
                items.addAll(notifysToShow);
                empty.set(items.isEmpty());
            }

            @Override
            public void onDataNotAvailable() {
            }
        });
    }
    public NotifyRepository getNotifysRepository() {
        return mNotifysRepository;
    }

    public Drawable getAppIcon(String packege){
        try
        {
            Drawable drawable = mContext.getPackageManager().getApplicationIcon(packege);
            return drawable;
        }catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }

    }
    public String getAppName(String packege){
        try {
            PackageManager packageManager = mContext.getPackageManager();
            String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packege, PackageManager.GET_META_DATA));
            return appName;
        }catch(Exception e) {return "";}
    }
    public void setButton(boolean stReceiver){

        Boolean startReceiver =  sPref.getBoolean("start",false);
        if(stReceiver){
            startReceiver=!startReceiver;
        }

        if(startReceiver){
            buttonColor.set(mContext.getResources().getDrawable(R.drawable.round_stop_btn));
            buttonColorText.set(mContext.getResources().getColor(R.color.colorStopText));
            buttonText.set("stop");
            if(stReceiver){
                nReceiver = new MainActivity.NotificationReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction("com.mihailovalex.getnotification.NOTIFICATION_LISTENER_EXAMPLE");
                mContext.registerReceiver(nReceiver,filter);
            }
        }else {
            buttonColor.set(mContext.getResources().getDrawable(R.drawable.round_shape_btn));
            buttonColorText.set(mContext.getResources().getColor(R.color.colorStartText));
            buttonText.set("start");
            if(stReceiver){
                if (nReceiver==null){
                    nReceiver = new MainActivity.NotificationReceiver();
                }
                try {
                    mContext.unregisterReceiver(nReceiver);
                }catch (Exception e){

                }
            }

        }
        if(stReceiver){
            SharedPreferences.Editor editor = sPref.edit();
            editor.putBoolean("start",startReceiver);
            editor.commit();
        }

    }

    public NotifysFilterType getCurrentFiltering() {
        return mCurrentFiltering;
    }
}
