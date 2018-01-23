package com.example.dg123.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class ViewHolder9 extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View convertView;
    private ViewHolder9(final View view) {
        super(view);
        convertView = view;
        views = new SparseArray<>();
    }
    @NonNull
    static ViewHolder9 get(final Context context, final ViewGroup parent, final int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder9(itemView);
    }
    View getView(final int viewId) {
        View view = views.get(viewId);
        if(view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return view;
    }
}
