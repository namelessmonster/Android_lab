package com.example.dg123.shopping;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityCompatApi23;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CommonAdapter commonAdapter;
    public static List<Map<String, Object>> listItems;
    public List<Integer> p1, p2;
    private Map<String, Object> map;
    private FloatingActionButton fab;
    private ListView mListView;
    private ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getList();
        commonAdapter = new CommonAdapter(this, R.layout.goods_list_item, p1) {
            @Override
            public void convert(ViewHolder holder, int pos){
                TextView name = holder.getView(R.id.name);
                name.setText(listItems.get(pos).get("name").toString());
                TextView first = holder.getView(R.id.first);
                first.setText(listItems.get(pos).get("firstLetter").toString());
            }
        };
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (commonAdapter.tag == false) return;
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("name", listItems.get(commonAdapter.mDatas.get(position)).get("name").toString());
                intent.putExtra("type", listItems.get(commonAdapter.mDatas.get(position)).get("type").toString());
                intent.putExtra("price", listItems.get(commonAdapter.mDatas.get(position)).get("price").toString());
                intent.putExtra("info", listItems.get(commonAdapter.mDatas.get(position)).get("info").toString());
                intent.putExtra("pos", commonAdapter.mDatas.get(position));
                intent.putExtra("tag", (Boolean) listItems.get(commonAdapter.mDatas.get(position)).get("tag"));
                startActivityForResult(intent, 1);
            }
            @Override
            public void onLongClick(final int position) {
                if (commonAdapter.tag == false) return;
                commonAdapter.tag = false;
                check(commonAdapter.mDatas.get(position));
                commonAdapter.mDatas.remove(position);
                commonAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "移除第" + position + "个商品",
                        Toast.LENGTH_SHORT).show();
                commonAdapter.tag = true;
            }
        });
        mRecyclerView.setAdapter(commonAdapter);
        listAdapter = new ListAdapter(this, p2);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                if (mListView.getTag().equals("1")) return;
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("name", listItems.get(listAdapter.goods.get(position)).get("name").toString());
                intent.putExtra("type", listItems.get(listAdapter.goods.get(position)).get("type").toString());
                intent.putExtra("price", listItems.get(listAdapter.goods.get(position)).get("price").toString());
                intent.putExtra("info", listItems.get(listAdapter.goods.get(position)).get("info").toString());
                intent.putExtra("pos", listAdapter.goods.get(position));
                intent.putExtra("tag", (Boolean) listItems.get(listAdapter.goods.get(position)).get("tag"));
                startActivityForResult(intent, 1);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == 0) return false;
                if (mListView.getTag().equals("1")) return false;
                mListView.setTag("1");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("移除商品");
                builder.setMessage("从购物车移除" + listItems.get(listAdapter.goods.get(position)).get("name").toString() + "?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mListView.setTag("0");
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listAdapter.goods.remove(position);
                        listAdapter.notifyDataSetChanged();
                        mListView.setTag("0");
                    }
                });
                builder.create().show();
                return false;
            }
        });

    }

    public void check(int pos){
        int i = 1;
        while (i < listAdapter.goods.size()){
            if (listAdapter.goods.get(i).equals(pos)) listAdapter.goods.remove(i);
            else ++i;
        }
        listAdapter.notifyDataSetChanged();
    }

    public void getList(){
        listItems = new ArrayList<>();
        p1 = new ArrayList<>();
        p2 = new ArrayList<>();
        String[] goodsName = new String[]{"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
                "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
        String[] goodsPrice = new String[]{"¥5.00", "¥59.00", "¥79.00", "¥2399.00", "¥179.00", "¥14.90",
                "¥132.59", "¥141.43", "¥139.43", "¥28.90"};
        String[] goodsType = new String[]{"作者", "产地", "产地", "版本", "重量", "产地", "重量", "重量", "重量", "重量"};
        String[] goodsInfo = new String[]{"Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g", "118g", "249g", "640g"};
        map = new HashMap<>();
        map.put("name", "购物车");
        map.put("firstLetter", "*");
        map.put("price", "价格");
        map.put("tag", false);
        listItems.add(map);
        p2.add(0);
        for (int i=1;i<goodsName.length;++i){
            map = new HashMap<>();
            map.put("name", goodsName[i]);
            map.put("price", goodsPrice[i]);
            map.put("type", goodsType[i]);
            map.put("info", goodsInfo[i]);
            map.put("firstLetter", goodsName[i].charAt(0));
            map.put("tag", false);
            listItems.add(map);
            p1.add(i);
        }
    }
    public void fabClick(View view){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab.getTag().equals("0")){
            fab.setImageResource(R.mipmap.mainpage);
            fab.setTag("1");
            mRecyclerView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
        else{
            fab.setImageResource(R.mipmap.shoplist);
            fab.setTag("0");
            mRecyclerView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            int pos = extras.getInt("pos");
            int num = extras.getInt("num");
            boolean tag = extras.getBoolean("tag");
            listItems.get(pos).put("tag", tag);
            for (int i=0;i<num;++i){
                listAdapter.goods.add(pos);
            }
            listAdapter.notifyDataSetChanged();
        }
    }
}
