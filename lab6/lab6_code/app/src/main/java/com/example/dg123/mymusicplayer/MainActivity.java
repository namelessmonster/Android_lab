package com.example.dg123.mymusicplayer;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public ImageView image;
    public Button play;
    public Button stop;
    public Button quit;
    public TextView status;
    public TextView time1;
    public TextView time2;
    public SeekBar seekBar;
    public int mark;
    public SimpleDateFormat time;
    public ObjectAnimator mMusicAnimation;
    private IBinder mBinder;
    private ServiceConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        quit = (Button) findViewById(R.id.quit);
        status = (TextView) findViewById(R.id.status);
        time1 = (TextView) findViewById(R.id.time1);
        time2 = (TextView) findViewById(R.id.time2);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        image = (ImageView) findViewById(R.id.image);
        mark = -1;
        time = new SimpleDateFormat("mm:ss");
        mMusicAnimation =ObjectAnimator.ofFloat(image, "rotation", 0f,360f);
        mMusicAnimation.setDuration(15000);
        mMusicAnimation.setInterpolator(new LinearInterpolator());
        mMusicAnimation.setRepeatCount(-1);

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = service;
                try {
                    int code = 110;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                    seekBar.setMax(reply.readInt());
                    seekBar.setProgress(reply.readInt());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mConnection = null;
            }
        };
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    try {
                        int code = 105;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        data.writeInt(progress);
                        mBinder.transact(code, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if (msg.what == 101 || msg.what == 102) {
                    if (msg.what == 102) {
                        play.setText("PLAY");
                        status.setText("Paused");
                        mMusicAnimation.pause();
                    }
                    else {
                        play.setText("PAUSED");
                        status.setText("Playing");
                        if (mMusicAnimation.isPaused()) mMusicAnimation.resume();
                        else mMusicAnimation.start();
                    }
                    try {
                        int code = 101;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        mBinder.transact(code, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (msg.what == 103){
                    seekBar.setProgress(0);
                    play.setText("PLAY");
                    status.setText("Stopped");
                    mMusicAnimation.setCurrentPlayTime(0);
                    mMusicAnimation.cancel();
                    try {
                        int code = 102;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        mBinder.transact(code, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (msg.what == 104) {
                    try {
                        int code = 103;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        mBinder.transact(code, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    unbindService(mConnection);
                    mConnection = null;
                    try{
                        MainActivity.this.finish();
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mark = -1;
                try {
                    int code = 104;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                    int progress = reply.readInt();
                    seekBar.setProgress(progress);
                    time1.setText(time.format(progress));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mThread = new Thread(){
            @Override
            public void run(){
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.obtainMessage(mark).sendToTarget();
                }
            }
        };
        mThread.start();
    }

    public void onClick(View v){
        if (v.getId() == R.id.play){
            if (status.getText().toString().equals("Playing"))
                mark = 102;
            else
                mark = 101;
        }
        else if (v.getId() == R.id.stop)
            mark = 103;
        else
            mark = 104;
    }
}
