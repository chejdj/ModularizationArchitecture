package com.spinytech.macore.net;

import java.util.Objects;
import java.util.WeakHashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author zhuyangyang on 12/21/20
 **/
public class ApiProvider {
    private final WeakHashMap<String, Retrofit> mRetrofitMap;

    public ApiProvider() {
        mRetrofitMap = new WeakHashMap<>();
    }

    public <T> T obtain(HttpServer server, Class<T> cls) {
        if (!mRetrofitMap.containsKey(server.getServerUUID())) {
            OkHttpClient client = server.onCreateHttpClient();
            mRetrofitMap.put(server.getServerUUID(), server.onCreateRetrofit(client));
        }
        Retrofit retrofit = mRetrofitMap.get(server.getServerUUID());
        return Objects.requireNonNull(retrofit).create(cls);
    }
}
