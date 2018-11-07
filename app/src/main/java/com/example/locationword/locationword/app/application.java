package com.example.locationword.locationword.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;


import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.mob.MobSDK;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.Iterator;
import java.util.List;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class application extends Application {
    String TAG="app";
    Context appContext;
    public void onCreate(){
        super.onCreate();
        EaseClientInit();
        UMengInit();
        BaiduInit();
        MobSDK.init(this);
    }
    public void BaiduInit(){

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

}
