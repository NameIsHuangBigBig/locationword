package com.example.locationword.locationword.app;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2017/11/2.
 */

public class AppManager {

    private static Stack<AppCompatActivity> activityStack;
    private static AppManager instance;

    private AppManager(){}


    public int getActivityStackSize()
    {
        if(activityStack==null){
            return 0;
        }
        return activityStack.size();

    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager(){
        if(instance == null){
            synchronized (AppManager.class){
                if(instance == null){
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(AppCompatActivity activity){
        if(activityStack==null){
            activityStack=new Stack<AppCompatActivity>();
        }
        activityStack.add(activity);
    }
    public void removeActivity(AppCompatActivity activity){
        if(activityStack==null){
            activityStack=new Stack<AppCompatActivity>();
        }
        activityStack.remove(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public AppCompatActivity currentActivity(){
        AppCompatActivity activity=activityStack.lastElement();
        return activity;
    }
    /**
     * 获取前一个Activity（当前Activity前一个Activity）
     */
    public AppCompatActivity beforeActivity(){
        AppCompatActivity activity=activityStack.get(activityStack.size()-2);
        return activity;
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        AppCompatActivity activity=activityStack.lastElement();
        finishActivity(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public boolean checkActivity(Class<?> cls){
        for (AppCompatActivity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                return true;
            }
        }
        return false;
    }
    public void finishActivity(Class<?> cls){
        for (AppCompatActivity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定数量的Activity
     */
    public void finishManyActivity(int count){
        for (int i = 0; i < count; i++){
            if(activityStack.isEmpty()){
                return;
            }
            try {
                AppCompatActivity activity=activityStack.pop();
                activity.finish();
                activity=null;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        if(activityStack == null){
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {	}
    }
    public static void requestRuntimePermissions(
            String[] permissions, Context context) {

        List<String> permissionList = new ArrayList<>();
        // 遍历每一个申请的权限，把没有通过的权限放在集合中
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        // 申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    permissionList.toArray(new String[permissionList.size()]), 1);
        }
    }


}

