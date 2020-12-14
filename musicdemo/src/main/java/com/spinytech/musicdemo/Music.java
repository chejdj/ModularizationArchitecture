package com.spinytech.musicdemo;

import android.content.Context;
import android.media.MediaPlayer;

import com.spinytech.macore.tools.Logger;

/**
 * Created by wanglei on 2016/12/27.
 */
public class Music {

    private static final String TAG = "Music";

    MediaPlayer mp;

    public Music(Context context) {
        mp = MediaPlayer.create(context, R.raw.music);
    }

    public void play() {
        Logger.i(TAG, "play music");
        try {
            if (mp != null) {
                mp.stop();
            }
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        Logger.i(TAG, "stop music");
        try {
            if (mp != null) {
                mp.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}