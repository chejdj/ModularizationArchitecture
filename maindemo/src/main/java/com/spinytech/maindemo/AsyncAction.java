package com.spinytech.maindemo;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;

import com.spinytech.macore.MaAction;
import com.spinytech.macore.MaActionResult;
import com.spinytech.macore.tools.Logger;

/**
 * Created by wanglei on 2016/12/28.
 */

public class AsyncAction extends MaAction {

    @Override
    public boolean isAsync(Context context, HashMap<String, String> requestData) {
        return true;
    }

    @Override
    public MaActionResult invoke(Context context, HashMap<String, String> requestData) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String temp = "";
        if (!TextUtils.isEmpty(requestData.get("1"))) {
            temp += requestData.get("1");
        }
        if (!TextUtils.isEmpty(requestData.get("2"))) {
            temp += requestData.get("2");
        }
        Logger.i("AsyncAction", temp);
        MaActionResult result = new MaActionResult.Builder()
                .code(MaActionResult.CODE_SUCCESS)
                .msg("success")
                .data(temp)
                .object(null)
                .build();
        return result;
    }
}
