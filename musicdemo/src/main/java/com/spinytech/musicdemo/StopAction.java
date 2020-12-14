package com.spinytech.musicdemo;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import com.spinytech.macore.MaAction;
import com.spinytech.macore.MaActionResult;
import com.spinytech.macore.tools.Logger;

/**
 * Created by wanglei on 2016/12/28.
 */

public class StopAction extends MaAction {

    private static final String TAG = "StopAction";

    @Override
    public boolean isAsync(Context context, HashMap<String, String> requestData) {
        return false;
    }

    @Override
    public MaActionResult invoke(Context context, HashMap<String, String> requestData) {
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra("command", "stop");
        context.startService(intent);
        MaActionResult result = new MaActionResult.Builder()
                .code(MaActionResult.CODE_SUCCESS)
                .msg("stop success")
                .data("")
                .object(null)
                .build();
        Logger.i(TAG, "\nStopAction end: " + System.currentTimeMillis());
        return result;
    }
}
