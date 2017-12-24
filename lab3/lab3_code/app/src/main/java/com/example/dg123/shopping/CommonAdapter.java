package com.example.dg123.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

public abstract class CommonAdapter extends RecyclerView.Adapter<ViewHolder>{
    Context mContext;
    int mLayoutId;
    boolean tag;
    List<Integer> mDatas;
    OnItemClickListener mOnItemClickListener;
    public CommonAdapter(Context context, int layoutId, List<Integer> datas){
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
        tag = true;
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public void convert(ViewHolder holder, int data){};

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        convert(holder, mDatas.get(position));
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount(){return mDatas.size();}
}