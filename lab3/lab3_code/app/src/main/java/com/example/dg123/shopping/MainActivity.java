package com.example.dg123.shopping;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CommonAdapter commonAdapter;
    private List<Map<String, Object>> listItems;
    private Map<String, Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getList();

        commonAdapter = new CommonAdapter(this, R.layout.goods_list_item, listItems) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s){
                TextView name = holder.getView(R.id.name);
                name.setText(s.get("name").toString());
                TextView first = holder.getView(R.id.first);
                first.setText(s.get("firstLetter").toString());
            }
        };
        mRecyclerView.setAdapter(commonAdapter);

    }
    public void getList(){
        listItems = new ArrayList<>();
        String[] goodsName = new String[]{"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
                "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
        String[] goodsPrice = new String[]{"¥5.00", "¥59.00", "¥79.00", "¥2399.00", "¥179.00", "¥14.90",
                "¥132.59", "¥141.43", "¥139.43", "¥28.90"};
        String[] goodsType = new String[]{"作者", "产地", "产地", "版本", "重量", "产地", "重量", "重量", "重量", "重量"};
        String[] goodsInfo = new String[]{"Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g", "118g", "249g", "640g"};
        for (int i=0;i<goodsName.length;++i){
            map = new HashMap<>();
            map.put("name", goodsName[i]);
            map.put("price", goodsPrice[i]);
            map.put("type", goodsType[i]);
            map.put("info", goodsInfo[i]);
            map.put("firstLetter", goodsName[i].indexOf(0));
            listItems.add(map);
        }
    }

}
