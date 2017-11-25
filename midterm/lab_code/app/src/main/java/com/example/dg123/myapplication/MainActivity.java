package com.example.dg123.myapplication;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int[] option;
    private int[] answer;
    private int now;
    private int right;
    private int wrong;
    private int total;
    private final int RIGHT = 101;
    private final int WRONG = 102;
    private final int NEXT = 103;
    private int id;
    private Thread mThread;
    private int nnow;
    private int nright;
    private int nwrong;
    private Handler mHandler;
    private Handler tHandler;
    private Thread timer;
    private int second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        total = 1000;
        answer = new int[total];
        option = new int[8];
        option[0] = R.id.itemA; option[1] = R.id.descriptionA;
        option[2] = R.id.itemB; option[3] = R.id.descriptionB;
        option[4] = R.id.itemC; option[5] = R.id.descriptionC;
        option[6] = R.id.itemD; option[7] = R.id.descriptionD;
        init();
        mHandler = new Handler() {
            private TextView tv;
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == RIGHT || msg.what == WRONG) {
                    int ans = answer[now];
                    tv = (TextView) findViewById(option[ans*2]);
                    tv.setBackgroundResource(R.drawable.right);
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tv = (TextView) findViewById(option[ans*2+1]);
                    tv.setTextColor(getResources().getColor(R.color.green));
                    if (msg.what == WRONG) {
                        for (int i=0;i<8;++i) if (option[i] == id) {
                            int j = i;
                            if (j % 2 == 1) --j;
                            tv = (TextView) findViewById(option[j]);
                            tv.setBackgroundResource(R.drawable.wrong);
                            tv.setTextColor(getResources().getColor(R.color.white));
                            tv = (TextView) findViewById(option[j+1]);
                            tv.setTextColor(getResources().getColor(R.color.red));
                            break;
                        }
                    }
                } else {
                    for (int i=0;i<8;++i) {
                        tv = (TextView) findViewById(option[i]);
                        if (i % 2 == 0) {
                            tv.setTextColor(getResources().getColor(R.color.gray));
                            tv.setBackgroundResource(R.drawable.option_background);
                        }
                        else {
                            tv.setTextColor(getResources().getColor(R.color.black));
                        }
                    }

                    tv = (TextView) findViewById(R.id.right);
                    tv.setText(String.valueOf(right));
                    tv = (TextView) findViewById(R.id.wrong);
                    tv.setText(String.valueOf(wrong));
                    tv = (TextView) findViewById(R.id.solved);
                    tv.setText(String.valueOf(now));
                }
            }
        };

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    mHandler.obtainMessage(NEXT).sendToTarget();
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (nright < right) {
                        nright = right;
                        mHandler.obtainMessage(RIGHT).sendToTarget();
                        try{
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ++nnow;
                    }
                    else if (nwrong < wrong) {
                        nwrong = wrong;
                        mHandler.obtainMessage(WRONG).sendToTarget();
                        try{
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ++nnow;
                    }
                }
            }
        });
        mThread.start();

        tHandler = new Handler(){
            private TextView tv;
            private String s;
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == NEXT){
                    tv = (TextView) findViewById(R.id.time);
                    s = String.valueOf(second);
                    if (s.length() == 1) s = "0" + s;
                    tv.setText("倒计时: 00:" + s);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("您的成绩是" + String.valueOf(right) + "题");
                    builder.setNegativeButton("回到界面", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            init();
                        }
                    });
                    builder.create().show();
                }
            }
        };
        timer = new Thread(new Runnable() {
            private TextView tv;
            private boolean mark;
            @Override
            public void run() {
                mark = true;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (second <= 0 && mark){
                        tHandler.obtainMessage(RIGHT).sendToTarget();
                        mark = false;
                    }
                    else if (second > 0){
                        --second;
                        tHandler.obtainMessage(NEXT).sendToTarget();
                        mark = true;
                    }
                }
            }
        });
        timer.start();
    }
    public void init() {
        for (int i=0;i<total;++i) answer[i] = 0;
        now = right = wrong  = nnow = nright = nwrong = 0;
        second = 60;
    }
    public void select(View v) {
        if (now == nnow){
            id = v.getId();
            int ans = answer[now % 10];
            if (id != option[ans*2] && id != option[ans*2+1])
                ++wrong;
            else
                ++right;
            ++now;
        }
        if (wrong == 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("您的成绩是" + String.valueOf(right) + "题");
            builder.setNegativeButton("回到界面", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    init();
                }
            });
            builder.create().show();
        }
    }
    public void reset(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("开始新游戏");
        builder.setMessage("超时或错5题判输");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                init();
            }
        });
        builder.create().show();
    }

}
