package com.spinytech.musicdemo;
/**
 * Created by wanglei on 2016/12/27.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.spinytech.macore.tools.Logger;

public class MusicService extends Service {

    private static final String TAG = "MusicService";

    public Music music;

    @Override
    public void onCreate() {
        super.onCreate();
        music = new Music(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String command = intent.getStringExtra("command");
            if ("play".equals(command)) {
                music.play();
            } else if ("stop".equals(command)) {
                music.stop();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy");
    }
}