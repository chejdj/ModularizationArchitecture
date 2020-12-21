package com.spinytech.networkdemo;

import android.content.Context;

import com.spinytech.macore.MaAction;
import com.spinytech.macore.MaActionResult;
import com.spinytech.macore.net.HttpServerDelegate;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author zhuyangyang on 12/16/20
 **/
class BaiduQueryAction extends MaAction {
    public static String KEY_WORDS = "key_words";

    @Override
    public boolean isAsync(Context context, HashMap<String, String> requestData) {
        return true;
    }

    @Override
    public MaActionResult invoke(Context context, HashMap<String, String> requestData) {
        BaiduApiServer apiServer = HttpServerDelegate.getInstance(context).getServer(null,
                BaiduApiServer.class);
        String keyWords = requestData.get(KEY_WORDS);
        try {
            String data = apiServer.queryFromBaidu(keyWords).execute().body();
            return new MaActionResult.Builder().code(MaActionResult.CODE_SUCCESS).data(
                    data).build();
        } catch (IOException e) {
            e.printStackTrace();
            return new MaActionResult.Builder().code(MaActionResult.CODE_ERROR).data(
                    e.getLocalizedMessage()).build();
        }
    }
}
