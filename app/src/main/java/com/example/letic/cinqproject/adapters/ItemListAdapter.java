package com.example.letic.cinqproject.adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.letic.cinqproject.R;
import com.example.letic.cinqproject.models.ItemList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by letic on 07/09/2018.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>{

    private List<ItemList> mItemList;
    private LayoutInflater mInflater;
    private Context context;

    public ItemListAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItemList = new ArrayList<>();
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list, parent, false);
        ItemListViewHolder viewHolder = new ItemListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {
        ItemList itemList = mItemList.get(position);
        String picture = itemList.getPhoto();
        Glide.with(context)
                .load(picture)
                .into(holder.photo);
        holder.title.setText(itemList.getTitle());
    }

    @Override
    public int getItemCount() {
        return (mItemList == null) ? 0 : mItemList.size();
    }

    public void setItemList(List<ItemList> ItemOrderList){
        this.mItemList.clear();
        this.mItemList.addAll(ItemOrderList);
        notifyDataSetChanged();
    }

    class ItemListViewHolder extends RecyclerView.ViewHolder{

        private ImageView photo;
        private TextView title;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            this.photo = itemView.findViewById(R.id.photo);
            this.title = itemView.findViewById(R.id.title);
        }
    }
}
