package com.spinytech.macore.router;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.spinytech.macore.ILocalRouterAIDL;
import com.spinytech.macore.MaActionResult;
import com.spinytech.macore.MaApplication;
import com.spinytech.macore.tools.Logger;

/**
 * Created by wanglei on 2016/11/29.
 */

public class LocalRouterConnectService extends Service {

    private static final String TAG = "LocalRouter";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.i(TAG, "onBind");
        return stub;
    }

    ILocalRouterAIDL.Stub stub = new ILocalRouterAIDL.Stub() {

        @Override
        public boolean checkResponseAsync(String routerRequest) {
            return LocalRouter.getInstance(MaApplication.getMaApplication()).
                    answerWiderAsync(new RouterRequest
                            .Builder(getApplicationContext())
                            .json(routerRequest)
                            .build());
        }

        @Override
        public String route(String routerRequest) {
            try {
                //TODO 优化:跨进程之间传输涉及到了4次JSON的序列化和反序列化
                LocalRouter localRouter = LocalRouter.getInstance(MaApplication.getMaApplication());
                RouterRequest routerRequest1 = new RouterRequest
                        .Builder(getApplicationContext())
                        .json(routerRequest)
                        .build();
                RouterResponse routerResponse = localRouter
                        .route(LocalRouterConnectService.this, routerRequest1);
                return routerResponse.get();
            } catch (Exception e) {
                e.printStackTrace();
                return new MaActionResult.Builder().msg(e.getMessage()).build().toString();
            }
        }

        @Override
        public boolean stopWideRouter() throws RemoteException {
            LocalRouter.getInstance(MaApplication.getMaApplication()).disconnectWideRouter();
            return true;
        }

        @Override
        public void connectWideRouter() throws RemoteException {
            LocalRouter
                    .getInstance(MaApplication.getMaApplication())
                    .connectWideRouter();
        }
    };
}
