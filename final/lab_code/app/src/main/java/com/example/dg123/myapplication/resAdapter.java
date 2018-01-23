package com.example.dg123.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class resAdapter extends RecyclerView.Adapter {
    private ArrayList<Bundle> info;
    private Context context;
    private int layoutId;
    private OnItemClickListener onItemClickListener;
    resAdapter(final Context context, final int layoutId, final ArrayList<Bundle> info) {
        this.context = context;
        this.layoutId = layoutId;
        this.info = info;
    }
    @Override
    public ViewHolder9 onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return ViewHolder9.get(context, parent, layoutId);
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder9 viewHolder = (ViewHolder9)holder;
        ImageView imageShow = (ImageView) viewHolder.getView(R.id.imageShow);
        if(position < info.size()) imageShow.setImageURI(Uri.parse(info.get(position).getString("Uri")));
        if(onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onItemClickListener.onClick(v, holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    onItemClickListener.onLongClick(v, holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }
    @Override
    public int getItemCount() { return info.size(); }
    public interface OnItemClickListener {
        void onClick(final View view, final int position);
        void onLongClick(final View view, final int position);
    }
    void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
