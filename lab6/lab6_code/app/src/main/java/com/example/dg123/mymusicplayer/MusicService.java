package com.example.dg123.mymusicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
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
            if (code == 101){
                if (mp.isPlaying())
                    mp.pause();
                else
                    mp.start();
            }
            else if (code == 102){
                mp.stop();
                try {
                    mp.prepare();
                    mp.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (code == 103)
                mp.release();
            else if (code == 104)
                reply.writeInt(mp.getCurrentPosition());
            else if (code == 105)
                mp.seekTo(data.readInt());
            else{
                try{
                    AssetManager am = getAssets();
                    AssetFileDescriptor afd = am.openFd("melt.mp3");
                    mp.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getLength());
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

