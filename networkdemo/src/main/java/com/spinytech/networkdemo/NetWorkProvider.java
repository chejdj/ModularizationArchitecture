package com.spinytech.networkdemo;

import com.spinytech.macore.MaProvider;

/**
 * @author zhuyangyang on 12/16/20
 **/
class NetWorkProvider extends MaProvider {

    @Override
    protected void registerActions() {
        registerAction("get", new BaiduGetAction());
        registerAction("query", new BaiduQueryAction());
    }
}
