package com.spinytech.networkdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author zhuyangyang on 12/21/20
 **/
interface BaiduApiServer {
    @GET("/")
    Call<String> getBaiduWebPage();

    @GET("/")
    Call<String> queryFromBaidu(@Query("wd") String keyWords);
}
