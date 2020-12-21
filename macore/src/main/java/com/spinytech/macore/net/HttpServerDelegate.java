package com.spinytech.macore.net;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author zhuyangyang on 12/21/20
 **/
public class HttpServerDelegate {
    private static HttpServerDelegate sInstance;

    public static HttpServerDelegate getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HttpServerDelegate(BaiduHttpServer.class, context);
        }
        return sInstance;
    }

    private HttpServer mDefaultServer;
    private final ApiProvider mApiProvider;
    private final Set<HttpServer> mServers = Collections.newSetFromMap(
            new WeakHashMap<HttpServer, Boolean>());

    private HttpServerDelegate(Class<? extends HttpServer> defaultServer, Context context) {
        try {
            mDefaultServer = defaultServer.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        mApiProvider = new ApiProvider();
        if (mDefaultServer != null) {
            mDefaultServer.initIfNeed(context);
        }
        mServers.add(mDefaultServer);
    }

    private HttpServer getDefaultServer() {
        return mDefaultServer;
    }

    public <T> T getServer(@Nullable HttpServer server, Class<T> cls) {
        if (server == null) {
            server = getDefaultServer();
        }
        return mApiProvider.obtain(server, cls);
    }
}
