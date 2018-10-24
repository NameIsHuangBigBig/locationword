package com.example.locationword.locationword.http;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

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
    public void doGet(String url){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("okhttp","访问异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200){
                    byte[]b=response.body().bytes();
                    String s=new String(b);
                    Log.i("okhttp","返回结果"+s);
                }
            }
        });
    }
    public void doPost(String url, Map<String,String> map){
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
                e.printStackTrace();
                Log.i("okhttp","访问异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200) {
                    byte[] b = response.body().bytes();
                    String s = new String(b);
                    Log.i("okhttp", "返回结果" + s);
                }
            }
        });
    }
}
