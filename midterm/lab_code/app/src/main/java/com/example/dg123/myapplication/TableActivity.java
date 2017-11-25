package com.example.dg123.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableActivity extends AppCompatActivity {
    private InputMethodManager imm;
    private boolean mark;
    private ScrollView s1;
    private ScrollView s2;
    private RelativeLayout r1;
    private RelativeLayout r2;
    private CommonAdapter commonAdapter;
    private List<Map<String, String>> power;
    public Map<String, String> map;
    private RecyclerView powerRecycler;

    private EditText ev;
    private Thread mThead;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        mark = false;
        s1 = (ScrollView) findViewById(R.id.scrollview);
        r1 = (RelativeLayout) findViewById(R.id.bottomBar);
        s2 = (ScrollView) findViewById(R.id.hero_scrollview);
        r2 = (RelativeLayout) findViewById(R.id.hero_bottomBar);
        imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        setupUI(findViewById(R.id.bg));

        power = new ArrayList<Map<String, String>>();
        powerRecycler = (RecyclerView) findViewById(R.id.recyclerview_power);
        commonAdapter = new CommonAdapter(this, R.layout.power_info, power) {
            @Override
            public void convert(ViewHolder holder, int position){
                map = mDatas.get(position);
                TextView name = (TextView) holder.getView(R.id.power_info_name);
                name.setText(map.get("name"));
                TextView date = (TextView) holder.getView(R.id.power_info_date);
                date.setText(map.get("date"));
            }
        };
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
            }
            @Override
            public void onLongClick(final int position) {
                commonAdapter.mDatas.remove(position);
                commonAdapter.notifyDataSetChanged();
            }
        });
        powerRecycler.setAdapter(commonAdapter);

        mHandler = new Handler() {
            private ImageView iv;

            @Override
            public void handleMessage(Message msg) {
                iv = (ImageView) findViewById(R.id.edit);
                if (mark) {
                    iv.setImageResource(R.mipmap.edit_red);
                    setupUI(findViewById(R.id.bg));
                } else {
                    iv.setImageResource(R.mipmap.edit);
                    setupUI(findViewById(R.id.bg));
                }
                inputable(findViewById(R.id.bg));
            }
        };
        mThead = new Thread(new Runnable() {
            private boolean nmark;
            @Override
            public void run() {
                while (true) {
                    if (mark != nmark){
                        nmark = mark;
                        mHandler.obtainMessage(1).sendToTarget();
                    }
                }
            }
        });
        mThead.start();
    }

    private void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                                0);
                    }
                    return false;
                }
            });
        } else {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    r1.setVisibility(View.GONE);
                    return false;
                }
            });
            ev = (EditText) findViewById(view.getId());
            ev.setEnabled(false);
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void setEdit(View view) {
        mark = !mark;
        if (mark){
            loadData();
            s1.setVisibility(View.VISIBLE);
            s2.setVisibility(View.GONE);
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.GONE);
        }
        else {
            s1.setVisibility(View.GONE);
            s2.setVisibility(View.VISIBLE);
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.VISIBLE);
        }
    }

    public void inputable(View view) {
        if(view instanceof EditText) {
            ev = (EditText) findViewById(view.getId());
            ev.setEnabled(mark);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                inputable(innerView);
            }
        }
    }

    public void back(View view) {
        finish();
    }

    public void ok(View view) {
        mark = false;
        s2.setVisibility(View.VISIBLE);
        s1.setVisibility(View.GONE);
        r2.setVisibility(View.VISIBLE);
        r1.setVisibility(View.GONE);
        updateData();
    }

    public void no(View view) {
        mark = false;
        s2.setVisibility(View.VISIBLE);
        s1.setVisibility(View.GONE);
        r2.setVisibility(View.VISIBLE);
        r1.setVisibility(View.GONE);
    }

    public void updateData() {

    }

    public void loadData() {

    }

    public void add_power(View view) {
        map = new HashMap<String, String>();
        map.put("name", "势力");
        map.put("date", "日期");
        commonAdapter.mDatas.add(map);
        commonAdapter.notifyDataSetChanged();
        Toast.makeText(this, String.valueOf(commonAdapter.mDatas.size()), Toast.LENGTH_SHORT).show();
    }

}
