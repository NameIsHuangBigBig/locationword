package com.example.locationword.locationword.http;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.tool.JSONChange;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private static HttpUtil httpUtil=null;

   // 双重加锁机制单例模式
    public static HttpUtil getInstence() {
        if (httpUtil == null){
            synchronized (HttpUtil.class) {
                if (httpUtil == null){
                    httpUtil = new HttpUtil();
                }
            }
        }
        return httpUtil;
    }
    public void doGet(String url,final Handler hcb){

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hcb.sendEmptyMessage(1001);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=new String(response.body().bytes());
                Message m= new Message();
                m.what=1000;
                m.obj=s;
                hcb.handleMessage(m);
            }
        });
    }
    public void doGet(String url,final Handler hcb,final TextView v){

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hcb.sendEmptyMessage(1001);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s=new String(response.body().bytes());
                Message m= new Message();
                m.what=1000;
                m.obj=s;
                hcb.post(new Runnable() {
                    @Override
                    public void run() {
                        v.setText(JSONChange.StringToJsonObject(s).get("NickName").getAsString());
                    }
                });

                hcb.handleMessage(m);
            }
        });
    }
    public void doGet(String url,final Handler hcb,final int i){

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hcb.sendEmptyMessage(1001);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=new String(response.body().bytes());
                Message m= new Message();
                m.what=i;
                m.obj=s;
                hcb.handleMessage(m);
            }
        });
    }
    public void doPost(String url, Map<String,String> map,final Handler hand){

        FormBody.Builder params=new FormBody.Builder();
        for(String key: map.keySet()){
            params.add(key,map.get(key));
        }
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hand.sendEmptyMessage(1001);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=new String(response.body().bytes());
                Message m= new Message();
                Log.i("LoginActivity","1000");

                m.what=1000;
                m.obj=s;
                hand.handleMessage(m);
            }
        });
    }
    public void doPost(String url, Map<String,String> map,final Handler hand,final int i){

        FormBody.Builder params=new FormBody.Builder();
        for(String key: map.keySet()){
            params.add(key,map.get(key));
        }
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hand.sendEmptyMessage(1001);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=new String(response.body().bytes());
                Message m= new Message();
                Log.i("LoginActivity","1000");

                m.what=i;
                m.obj=s;
                hand.handleMessage(m);
            }
        });
    }
}
