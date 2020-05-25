
package com.mihailovalex.getnotification.vm;


import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mihailovalex.getnotification.adapter.NotifyAdapter;
import com.mihailovalex.getnotification.data.NotifyApp;

import java.util.List;


public class NotifysListBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<NotifyApp> items) {

        NotifyAdapter adapter = (NotifyAdapter) recyclerView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }

}
