package com.spinytech.macore.net;

import android.content.Context;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author zhuyangyang on 12/21/20
 **/
class BaiduHttpServer extends HttpServer {
    private final static String HOST = "https://www.baidu.com";

    @Override
    protected Retrofit onCreateRetrofit(@NonNull OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(HOST).addConverterFactory(
                StringConverterFactory.create()).build();
    }

    @Override
    protected String onInit(@NonNull Context context) {
        return HOST;
    }
}
