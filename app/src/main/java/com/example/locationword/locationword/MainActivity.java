package com.example.locationword.locationword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.http.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

       // HttpUtil.getInstence().doPost("http://172.17.146.136:8082/MVCl_w/Login/checklogin",map);
    }
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        
    };

}
