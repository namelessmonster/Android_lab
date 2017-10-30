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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoActivity extends AppCompatActivity{
    private ImageView star;
    private TextView textView;
    private ImageView imageView;
    String data;
    int pos;
    int num;
    boolean tag;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_info);
        tag = false;
        num = 0;
        data = "";
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos");
        tag = extras.getBoolean("tag");
        if (extras != null){
            data = extras.getString("name");
            textView = (TextView) findViewById(R.id.name);
            textView.setText(data);
            imageView = (ImageView) findViewById(R.id.show);
            imageView.setImageResource(MainActivity.pic.get(pos));
            data = extras.getString("type");
            textView = (TextView) findViewById(R.id.type);
            textView.setText(data);
            data = extras.getString("price");
            textView = (TextView) findViewById(R.id.price);
            textView.setText(data);
            data = extras.getString("info");
            textView = (TextView) findViewById(R.id.info);
            textView.setText(data);
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
    }
    public void backClick(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        bundle.putInt("num", num);
        bundle.putBoolean("tag", tag);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void carClick(View view){
        ++num;
        Toast.makeText(InfoActivity.this, "商品已添加到购物车",
                Toast.LENGTH_SHORT).show();
    }
}
