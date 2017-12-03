package com.example.dg123.mymusicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;

import java.nio.charset.MalformedInputException;


public class MusicService extends Service{
    private IBinder mBinder = new MyBinder();
    private MediaPlayer mp = new MediaPlayer();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException{
            if (code == 101){   //播放/暂停音乐
                if (mp.isPlaying())
                    mp.pause();
                else
                    mp.start();
            }
            else if (code == 102){  //停止音乐
                mp.stop();
                try {
                    mp.prepare();
                    mp.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (code == 103)   //退出应用
                mp.release();
            else if (code == 104)   //更新进度条
                reply.writeInt(mp.getCurrentPosition());
            else if (code == 105)   //修改音乐播放进度
                mp.seekTo(data.readInt());
            else{                  //初始化音乐播放器
                try{
                    /*
                    AssetManager am = getAssets();
                    AssetFileDescriptor afd = am.openFd("melt.mp3");
                    mp.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getLength());
                    */
                    mp.setDataSource(Environment.getExternalStorageDirectory() + "/melt.mp3");
                    mp.prepare();
                    mp.setLooping(true);
                    reply.writeInt(mp.getDuration());
                    reply.writeInt(mp.getCurrentPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

}


