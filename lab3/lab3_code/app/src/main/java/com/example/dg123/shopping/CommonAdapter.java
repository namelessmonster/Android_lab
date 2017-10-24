package com.example.dg123.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

public abstract class CommonAdapter extends RecyclerView.Adapter<ViewHolder>{
    private Context mContext;
    private int mLayoutId;
    private List<Map<String, Object>> mDatas;
    public CommonAdapter(Context context, int layoutId, List<Map<String, Object>> datas){
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    public void convert(ViewHolder holder, Map<String, Object> data){};

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount(){return mDatas.size();}

}