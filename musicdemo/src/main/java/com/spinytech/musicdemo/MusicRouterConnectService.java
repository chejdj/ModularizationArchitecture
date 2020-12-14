package com.spinytech.musicdemo;

import android.content.Intent;

import com.spinytech.macore.router.LocalRouterConnectService;
import com.spinytech.macore.tools.Logger;

/**
 * Created by wanglei on 2016/12/28.
 */

public class MusicRouterConnectService extends LocalRouterConnectService {

    private static final String TAG = "MRCS";

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy");
    }
}
