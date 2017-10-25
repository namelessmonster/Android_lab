package com.example.dg123.shopping;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity{
    private ImageView star;
    private TextView textView;
    private ImageView imageView;
    private Map<String, Integer> Id;
    String data;
    int pos;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_info);
        Id = new HashMap<>();
        Id.put("Enchated Forest", R.mipmap.enchatedforest);
        Id.put("Arla Milk", R.mipmap.arla);
        Id.put("Devondale Milk", R.mipmap.devondale);
        Id.put("Kindle Oasis", R.mipmap.kindle);
        Id.put("waitrose 早餐麦片", R.mipmap.waitrose);
        Id.put("Mcvitie's 饼干", R.mipmap.mcvitie);
        Id.put("Ferrero Rocher", R.mipmap.ferrero);
        Id.put("Maltesers", R.mipmap.maltesers);
        Id.put("Lindt", R.mipmap.lindt);
        Id.put("Borggreve", R.mipmap.borggreve);
        data = "";
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos");
        if (extras != null){
            data = extras.getString("name");
            textView = (TextView) findViewById(R.id.name);
            textView.setText(data);
            imageView = (ImageView) findViewById(R.id.show);
            imageView.setImageResource(Id.get(data));
            data = extras.getString("type");
            textView = (TextView) findViewById(R.id.type);
            textView.setText(data);
            data = extras.getString("price");
            textView = (TextView) findViewById(R.id.price);
            textView.setText(data);
            data = extras.getString("info");
            textView = (TextView) findViewById(R.id.info);
            textView.setText(data);
        }
    }
    public void starClick(View view){
        star = (ImageView) findViewById(R.id.star);
        if (star.getTag().equals("0")){
            star.setImageResource(R.mipmap.full_star);
            star.setTag("1");
        }
        else{
            star.setImageResource(R.mipmap.empty_star);
            star.setTag("0");
        }
    }
    public void backClick(View view){
        finish();
    }
    public void carClick(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        Toast.makeText(InfoActivity.this, "商品已添加到购物车",
                Toast.LENGTH_SHORT).show();
    }
}
