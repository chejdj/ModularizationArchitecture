package com.spinytech.macore.router;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.spinytech.macore.ErrorAction;
import com.spinytech.macore.IWideRouterAIDL;
import com.spinytech.macore.MaAction;
import com.spinytech.macore.MaActionResult;
import com.spinytech.macore.MaApplication;
import com.spinytech.macore.MaProvider;
import com.spinytech.macore.tools.Logger;
import com.spinytech.macore.tools.ProcessUtil;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Local Router
 */

public class LocalRouter {

    private static final String TAG = "LocalRouter";

    private String mProcessName;

    private static LocalRouter sInstance = null;

    // Action container
    private HashMap<String, MaProvider> mProviders;

    private MaApplication mApplication;

    private IWideRouterAIDL mWideRouterAIDL;

    private static ExecutorService threadPool = null;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWideRouterAIDL = IWideRouterAIDL.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWideRouterAIDL = null;
        }
    };

    private LocalRouter(MaApplication context) {
        mApplication = context;
        mProcessName = ProcessUtil.getProcessName(context, ProcessUtil.getMyProcessId());
        mProviders = new HashMap<>();
        if (mApplication.needMultipleProcess() && !WideRouter.PROCESS_NAME.equals(mProcessName)) {
            connectWideRouter();
        }
    }

    public static synchronized LocalRouter getInstance(@NonNull MaApplication context) {
        if (sInstance == null) {
            sInstance = new LocalRouter(context);
        }
        return sInstance;
    }

    private static synchronized ExecutorService getThreadPool() {
        if (null == threadPool) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    public void connectWideRouter() {
        Intent binderIntent = new Intent(mApplication, WideRouterConnectService.class);
        binderIntent.putExtra("domain", mProcessName);
        mApplication.bindService(binderIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void disconnectWideRouter() {
        if (null == mServiceConnection) {
            return;
        }
        mApplication.unbindService(mServiceConnection);
        mWideRouterAIDL = null;
    }

    public void registerProvider(String providerName, MaProvider provider) {
        mProviders.put(providerName, provider);
    }

    public boolean checkWideRouterConnection() {
        boolean result = false;
        if (mWideRouterAIDL != null) {
            result = true;
        }
        return result;
    }

    boolean answerWiderAsync(@NonNull RouterRequest routerRequest) {
        if (mProcessName.equals(routerRequest.getDomain()) && checkWideRouterConnection()) {
            return findRequestAction(routerRequest).isAsync(mApplication, routerRequest.getData());
        } else {
            return true;
        }
    }

    public RouterResponse route(Context context, @NonNull RouterRequest routerRequest)
            throws Exception {
        Logger.i(TAG,
                "Process:" + mProcessName + "\nLocal route start: " + System.currentTimeMillis());
        RouterResponse routerResponse = new RouterResponse();
        // Local request
        if (mProcessName.equals(routerRequest.getDomain())) {
            Object attachment = routerRequest.getAndClearObject();
            HashMap<String, String> params = new HashMap<>(routerRequest.getData());
            Logger.i(TAG, "Process:" + mProcessName + "\nLocal find action start: "
                    + System.currentTimeMillis());
            MaAction targetAction = findRequestAction(routerRequest);
            routerRequest.isIdle.set(true);
            Logger.i(TAG, "Process:" + mProcessName + "\nLocal find action end: "
                    + System.currentTimeMillis());
            routerResponse.mIsAsync = attachment == null ? targetAction.isAsync(context, params)
                    : targetAction.isAsync(context, params, attachment);
            // Sync result, return the result immediately.
            if (!routerResponse.mIsAsync) {
                MaActionResult result = attachment == null ? targetAction.invoke(context, params)
                        : targetAction.invoke(context, params, attachment);
                routerResponse.mResultString = result.toString();
                routerResponse.mObject = result.getObject();
                Logger.i(TAG, "Process:" + mProcessName + "\nLocal sync end: "
                        + System.currentTimeMillis());
            }
            // Async result, use the thread pool to execute the task.
            else {
                LocalTask task = new LocalTask(routerResponse, params, attachment, context,
                        targetAction);
                routerResponse.mAsyncResponse = getThreadPool().submit(task);
            }
        } else if (!mApplication.needMultipleProcess()) {
            throw new Exception(
                    "Please make sure the returned value of needMultipleProcess in MaApplication "
                            + "is true, so that you can invoke other process action.");
        }
        // IPC request
        else {
            String domain = routerRequest.getDomain();
            String routerRequestString = routerRequest.toString();
            routerRequest.isIdle.set(true);
            if (checkWideRouterConnection()) {
                Logger.i(TAG, "Process:" + mProcessName + "\nWide async check start: "
                        + System.currentTimeMillis());
                //If you don't need wide async check, use "routerResponse.mIsAsync = false;"
                // replace the next line to improve performance.
                routerResponse.mIsAsync =
                        mWideRouterAIDL.checkResponseAsync(domain, routerRequestString);
                Logger.i(TAG, "Process:" + mProcessName + "\nWide async check end: "
                        + System.currentTimeMillis());
            }
            // Has not connected with the wide router.
            else {
                routerResponse.mIsAsync = true;
                ConnectWideTask task = new ConnectWideTask(routerResponse, domain,
                        routerRequestString);
                routerResponse.mAsyncResponse = getThreadPool().submit(task);
                return routerResponse;
            }
            if (!routerResponse.mIsAsync) {
                routerResponse.mResultString = mWideRouterAIDL.route(domain, routerRequestString);
                Logger.i(TAG, "Process:" + mProcessName + "\nWide sync end: "
                        + System.currentTimeMillis());
            }
            // Async result, use the thread pool to execute the task.
            else {
                WideTask task = new WideTask(domain, routerRequestString);
                routerResponse.mAsyncResponse = getThreadPool().submit(task);
            }
        }
        return routerResponse;
    }

    public boolean stopSelf(Class<? extends LocalRouterConnectService> clazz) {
        if (checkWideRouterConnection()) {
            try {
                return mWideRouterAIDL.stopRouter(mProcessName);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            mApplication.stopService(new Intent(mApplication, clazz));
            return true;
        }
    }

    public void stopWideRouter() {
        if (checkWideRouterConnection()) {
            try {
                mWideRouterAIDL.stopRouter(WideRouter.PROCESS_NAME);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logger.e(TAG, "This local router hasn't connected the wide router.");
        }
    }

    private MaAction findRequestAction(RouterRequest routerRequest) {
        MaProvider targetProvider = mProviders.get(routerRequest.getProvider());
        ErrorAction defaultNotFoundAction = new ErrorAction(false, MaActionResult.CODE_NOT_FOUND,
                "Not found the action.");
        if (null == targetProvider) {
            return defaultNotFoundAction;
        } else {
            MaAction targetAction = targetProvider.findAction(routerRequest.getAction());
            if (null == targetAction) {
                return defaultNotFoundAction;
            } else {
                return targetAction;
            }
        }
    }

    private class LocalTask implements Callable<String> {

        private final RouterResponse mResponse;

        private final HashMap<String, String> mRequestData;

        private final Context mContext;

        private final MaAction mAction;

        private final Object mObject;

        public LocalTask(RouterResponse routerResponse, HashMap<String, String> requestData,
                Object object, Context context, MaAction maAction) {
            this.mContext = context;
            this.mResponse = routerResponse;
            this.mRequestData = requestData;
            this.mAction = maAction;
            this.mObject = object;
        }

        @Override
        public String call() {
            MaActionResult result = mObject == null ? mAction.invoke(mContext, mRequestData)
                    : mAction.invoke(mContext, mRequestData, mObject);
            mResponse.mObject = result.getObject();
            Logger.i(TAG, "Process:" + mProcessName + "\nLocal async end: "
                    + System.currentTimeMillis());
            return result.toString();
        }
    }

    private class WideTask implements Callable<String> {

        private final String mDomain;

        private final String mRequestString;

        public WideTask(String domain, String requestString) {
            this.mDomain = domain;
            this.mRequestString = requestString;
        }

        @Override
        public String call() throws RemoteException {
            Logger.i(TAG, "Process:" + mProcessName + "\nWide async start: "
                    + System.currentTimeMillis());
            String result = mWideRouterAIDL.route(mDomain, mRequestString);
            Logger.i(TAG, "Process:" + mProcessName + "\nWide async end: "
                    + System.currentTimeMillis());
            return result;
        }
    }

    private class ConnectWideTask implements Callable<String> {

        private final RouterResponse mResponse;

        private final String mDomain;

        private final String mRequestString;

        public ConnectWideTask(RouterResponse routerResponse, String domain, String requestString) {
            this.mResponse = routerResponse;
            this.mDomain = domain;
            this.mRequestString = requestString;
        }

        @Override
        public String call() throws RemoteException {
            Logger.i(TAG, "Process:" + mProcessName + "\nBind wide router start: "
                    + System.currentTimeMillis());
            connectWideRouter();
            int time = 0;
            while (true) {
                if (null == mWideRouterAIDL) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time++;
                } else {
                    break;
                }
                if (time >= 600) {
                    ErrorAction defaultNotFoundAction = new ErrorAction(true,
                            MaActionResult.CODE_CANNOT_BIND_WIDE,
                            "Bind wide router time out. Can not bind wide router.");
                    MaActionResult result = defaultNotFoundAction.invoke(mApplication,
                            new HashMap<String, String>());
                    mResponse.mResultString = result.toString();
                    return result.toString();
                }
            }
            Logger.i(TAG, "Process:" + mProcessName + "\nBind wide router end: "
                    + System.currentTimeMillis());
            String result = mWideRouterAIDL.route(mDomain, mRequestString);
            Logger.i(TAG, "Process:" + mProcessName + "\nWide async end: "
                    + System.currentTimeMillis());
            return result;
        }
    }
}
