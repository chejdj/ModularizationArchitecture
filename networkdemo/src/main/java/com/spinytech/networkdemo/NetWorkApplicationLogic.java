package com.spinytech.networkdemo;

import com.spinytech.macore.multiprocess.BaseApplicationLogic;
import com.spinytech.macore.router.LocalRouter;

/**
 * @author zhuyangyang on 12/16/20
 **/
public class NetWorkApplicationLogic extends BaseApplicationLogic {

    @Override
    public void onCreate() {
        super.onCreate();
        LocalRouter.getInstance(mApplication).registerProvider("network", new NetWorkProvider());
    }
}
