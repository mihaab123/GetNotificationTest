package com.mihailovalex.getnotification;


import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mihailovalex.getnotification.adapter.NotifyAdapter;
import com.mihailovalex.getnotification.data.NotifyApp;


import com.mihailovalex.getnotification.databinding.ActivityMainBinding;
import com.mihailovalex.getnotification.vm.NotifyViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //SharedPreferences preferences;
   // private NotificationReceiver nReceiver;
    private String TAG = "MyLogs";
    private RecyclerView recyclerView;
    private NotifyAdapter adapter;
    private static NotifyViewModel mViewModel;

    private ActivityMainBinding mFragBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());

        mViewModel =
                ViewModelProviders.of(this, factory).get(NotifyViewModel.class);
        mViewModel.setFiltering(NotifysFilterType.ALL);
        mFragBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFragBinding.setViewmodel(mViewModel);
        setSupportActionBar(mFragBinding.toolbar);
        setupListAdapter();


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main , menu) ; //Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS" ) ;
                startActivity(intent) ;
                return true;
            case R.id.action_filter :
                showFilteringPopUpMenu();
                return true;
            case R.id.action_delete_all :
                mViewModel.getNotifysRepository().deleteAllNotifys();
                adapter.notifyDataSetChanged();
                return true;
            default :
                return super .onOptionsItemSelected(item) ;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public static class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String packageName = intent.getStringExtra("packageName");
            long date = intent.getLongExtra("date",0l);

            NotifyApp newNotify = new NotifyApp(title,date,packageName);
            mViewModel.getNotifysRepository().saveNotify(newNotify);
            mViewModel.start();
        }
    }
    private void setupListAdapter() {
        recyclerView = mFragBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotifyAdapter(
                new ArrayList<NotifyApp>(0),
                mViewModel
        );
        recyclerView.setAdapter(adapter);
    }
    private void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.action_filter));
        popup.getMenuInflater().inflate(R.menu.filter_notifys, popup.getMenu());
        MenuItem item;
        switch (mViewModel.getCurrentFiltering()){
            case ALL:
                item = popup.getMenu().findItem(R.id.all);
                item.setChecked(true);
                break;
            case PER_HOUR:
                item = popup.getMenu().findItem(R.id.per_hour);
                item.setChecked(true);
                break;
            case PER_DAY:
                item = popup.getMenu().findItem(R.id.per_day);
                item.setChecked(true);
                break;
            case PER_MONTH:
                item = popup.getMenu().findItem(R.id.per_month);
                item.setChecked(true);
                break;
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.all:
                        mViewModel.setFiltering(NotifysFilterType.ALL);
                        break;
                    case R.id.per_hour:
                        mViewModel.setFiltering(NotifysFilterType.PER_HOUR);
                        break;
                    case R.id.per_day:
                        mViewModel.setFiltering(NotifysFilterType.PER_DAY);
                        break;
                    case R.id.per_month:
                        mViewModel.setFiltering(NotifysFilterType.PER_MONTH);
                        break;
                    default:
                        mViewModel.setFiltering(NotifysFilterType.ALL);
                        break;
                }
                mViewModel.loadTasks();
                return true;
            }
        });

        popup.show();
    }
}
