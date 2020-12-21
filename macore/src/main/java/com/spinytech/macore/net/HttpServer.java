package com.spinytech.macore.net;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author zhuyangyang on 12/17/20
 **/
public abstract class HttpServer {
    private boolean mInit;
    private Context mContext;
    private String mServerUUID;
    private OkHttpClient mOkHttpClient;

    @CallSuper
    protected void onPreRequest(@NonNull Request request) {
    }

    @CallSuper
    protected void onResponseResult(@NonNull Response resp) {
    }

    @CallSuper
    protected OkHttpClient onCreateHttpClient() {
        mOkHttpClient = new OkHttpClient.Builder().addInterceptor(
                new HttpLoggingInterceptor())
                .addNetworkInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();
                        onPreRequest(request);
                        Response response = chain.proceed(request);
                        onResponseResult(response);
                        return response;
                    }
                }).build();
        return mOkHttpClient;
    }

    protected abstract Retrofit onCreateRetrofit(@NonNull OkHttpClient okHttpClient);

    protected abstract String onInit(@NonNull Context context);

    synchronized void initIfNeed(@NonNull Context context) {
        if (mInit) {
            return;
        }
        this.mContext = context;
        String serverHome = onInit(context);
        this.mServerUUID = String.valueOf(Objects.hash(serverHome, this.getClass()));
        this.mInit = true;
    }

    public String getServerUUID() {
        return mServerUUID;
    }
}
