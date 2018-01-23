package com.example.dg123.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by hesongheng on 2018/1/2.
 */

public abstract class CommandAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    OnItemClickListener mOnItemClickListener;

    Context mcontext;
    int mlayoutId;
    List<T> mdatas;
    LayoutInflater minflater;

    public CommandAdapter(Context context,int layoutId,List datas) {
        mcontext=context;
        mlayoutId=layoutId;
        mdatas=datas;
        minflater=LayoutInflater.from(context);
    }

    public interface OnItemClickListener{
        void onClick(int position);
        boolean onLongClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mcontext,parent,mlayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.updatePosition(position);
        convert(holder,mdatas.get(position));

        if (mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }

    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

}




