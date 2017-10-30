package com.example.dg123.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class ListAdapter extends BaseAdapter{
    public Context context;
    public List<Integer> goods;

    public ListAdapter(Context context, List<Integer> goods){
        this.context = context;
        this.goods = goods;
    }

    @Override
    public int getCount(){
        if (goods == null) return 0;
        return goods.size();
    }

    @Override
    public Object getItem(int i){
        if (goods == null) return null;
        return goods.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        View convertView;
        ViewHolder viewHolder;
        if (view == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.first = (TextView) convertView.findViewById(R.id.first);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(viewHolder);
        }
        else{
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.first.setText(MainActivity.listItems.get(goods.get(i)).get("firstLetter").toString());
        viewHolder.name.setText(MainActivity.listItems.get(goods.get(i)).get("name").toString());
        viewHolder.price.setText(MainActivity.listItems.get(goods.get(i)).get("price").toString());

        return convertView;

    }

    public class ViewHolder{
        public TextView first;
        public TextView name;
        public TextView price;
    }
}
