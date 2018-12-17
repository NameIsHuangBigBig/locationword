package com.example.locationword.locationword.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.example.locationword.locationword.MainActivity;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.tool.Constant;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.mob.MobSDK;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.Iterator;
import java.util.List;

import java.util.logging.LogRecord;

import cn.jpush.android.api.JPushInterface;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class application extends Application {
    String TAG="app";
    Context appContext;
    private Handler handler=new Handler() {
    };
    public void onCreate(){
        super.onCreate();
        EaseClientInit();
        UMengInit();
        BaiduInit();
        JPushInit();
       // LoginListener();
        MobSDK.init(this);
    }
    protected void LoginListener(){
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
                Log.i("tttt","dsdsd");
            }

            @Override
            public void onDisconnected(int errorCode) {
                Log.i("tttt","dsdsd");
            }
        });

//
    }
    protected void JPushInit(){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    public void BaiduInit(){
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。

    }
    public void UMengInit(){
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this,"5bd67c0ab465f530ac00004b"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
    }
    public void EaseClientInit(){
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        EaseUI.getInstance().init(this, options);

 }
    public void onTerminate(){
       super.onTerminate();
        Log.i("application","logout");
        LoginOutLocation();
        EMClient.getInstance().logout(true);
    }
    public void LoginOutLocation(){
        HttpUtil.getInstence().doGet(API.changeLocationState+"?userId="+getSharedPreferences(Constant.logindata,MODE_PRIVATE
        ).getString(Constant.UserId,"")+"&isOnline=0",handler);
    }

}
