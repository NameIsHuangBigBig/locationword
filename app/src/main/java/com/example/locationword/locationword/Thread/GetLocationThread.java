package com.example.locationword.locationword.Thread;

import android.os.Handler;
import android.util.Log;

import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.google.gson.JsonArray;

import java.util.HashMap;

public class GetLocationThread extends Thread {
    private JsonArray ja;
    private boolean isstoplocation=false;
    private boolean isLocation=true;
    private Handler handler;
    private String myselfId;
    private HashMap<String,String> map = new HashMap<>();
    private StringBuffer sb=new StringBuffer();
    public GetLocationThread(JsonArray ja, Handler handler, String myselfId){
        this.ja=ja;
        this.handler=handler;
        this.myselfId=myselfId;
        sb.append("[");
        for(int i = 0 ; i< ja.size();i++){
                if(i!=ja.size()-1){
                    sb.append(ja.get(i).getAsString()+",");
                }else {
                    sb.append(ja.get(i).getAsString());
                }
        }
        sb.append("]");
        Log.i("ssss",sb.toString());
        map.put("userIdArray",sb.toString());
        this.start();
    }
    public void run(){
        try {
            while (isLocation){
                while(!isstoplocation){
                    HttpUtil.getInstence().doPost(API.getUserLocationA, map,handler,500);
                    Log.i("httpGet",API.getUserLocationA + "?userIdArray="+sb.toString());
                    Thread.sleep(3000);
                }


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean getLocation(){
        return isLocation;
    }
    public void startLocation(){
        isLocation=true;
    }
    public void setIsstoplocation(boolean b){
        this.isstoplocation=b;
    }
    public boolean getIsstoplocation(){
        return this.isstoplocation;
    }
    public void stopLocation(){
        isLocation=false;
    }
}
