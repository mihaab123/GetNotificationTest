package com.mihailovalex.getnotification.adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;


import com.mihailovalex.getnotification.R;
import com.mihailovalex.getnotification.data.NotifyApp;
import com.mihailovalex.getnotification.databinding.NotifyItemBinding;
import com.mihailovalex.getnotification.vm.NotifyViewModel;

import java.util.ArrayList;

import java.util.List;

public class NotifyAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<NotifyApp> items;
    NotifyViewModel notifyViewModel;


    public NotifyAdapter(List<NotifyApp> notifyApps,
                         NotifyViewModel notifyViewModel) {
        this.notifyViewModel = notifyViewModel;
        items = new ArrayList<>();
        setList(notifyApps);

    }
    public NotifyApp getItem(int position){
        return items.get(position);
    }
    public void addItem(NotifyApp item){
        items.add(item);
        notifyItemInserted(getItemCount()-1);
    }
    public void addItem(int location,NotifyApp item){
        items.add(location,item);
        notifyItemInserted(location);
    }

    public void removeItem(int location){
        items.remove(location);
        notifyItemRemoved(location);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class NotifyViewHolder extends RecyclerView.ViewHolder{
        protected Context context;
        protected NotifyItemBinding notifyItemBinding;

        public NotifyViewHolder(NotifyItemBinding  binding) {
            super(binding.getRoot());
            context = itemView.getContext();
            notifyItemBinding = binding;
        }

    }

    private void setList(List<NotifyApp> notifyApps) {
        items = notifyApps;
        notifyDataSetChanged();
    }
    public void replaceData(List<NotifyApp> notifyApps) {
        setList(notifyApps);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotifyItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.notify_item, parent, false);

        NotifyViewHolder holder = new NotifyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder rawHolder, int position) {
        int currentPosiotion = position;
        NotifyApp item = getItem(position);
        NotifyViewHolder holder = (NotifyViewHolder) rawHolder;
        //Bitmap image=BitmapManager.byteToBitmap(current.getImage());
        holder.notifyItemBinding.setNotify(item);
        holder.notifyItemBinding.setViewmodel(notifyViewModel);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

}