package com.example.dg123.shopping;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoActivity extends AppCompatActivity{
    private ImageView star;
    private TextView textView;
    private ImageView imageView;
    int pos;
    boolean tag;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_info);
        tag = false;
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos");
        tag = (Boolean) MainActivity.listItems.get(pos).get("tag");
        if (extras != null){
            textView = (TextView) findViewById(R.id.name);
            textView.setText(MainActivity.listItems.get(pos).get("name").toString());
            imageView = (ImageView) findViewById(R.id.show);
            imageView.setImageResource(MainActivity.pic.get(pos));
            textView = (TextView) findViewById(R.id.type);
            textView.setText(MainActivity.listItems.get(pos).get("type").toString());
            textView = (TextView) findViewById(R.id.price);
            textView.setText(MainActivity.listItems.get(pos).get("price").toString());
            textView = (TextView) findViewById(R.id.info);
            textView.setText(MainActivity.listItems.get(pos).get("info").toString());
            imageView = (ImageView) findViewById(R.id.star);
            if (!tag) imageView.setImageResource(R.mipmap.empty_star);
            else imageView.setImageResource(R.mipmap.full_star);
        }
    }
    public void starClick(View view){
        star = (ImageView) findViewById(R.id.star);
        if (!tag){
            star.setImageResource(R.mipmap.full_star);
            tag = true;
        }
        else{
            star.setImageResource(R.mipmap.empty_star);
            tag = false;
        }
        MainActivity.listItems.get(pos).put("tag", tag);
    }
    public void backClick(View view){
        setResult(RESULT_OK);
        finish();
    }
    public void carClick(View view){
        Toast.makeText(InfoActivity.this, "商品已添加到购物车",
                Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new MessageEvent(pos));
    }
}
