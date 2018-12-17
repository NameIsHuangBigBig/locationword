package com.example.locationword.locationword;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//
//import com.easemob.easeui.EaseConstant;
//import com.easemob.easeui.ui.EaseChatFragment;
//import com.easemob.easeui.ui.EaseConversationListFragment;
import com.example.locationword.locationword.Adapter.FragmentAdapter;
import com.example.locationword.locationword.Fragment.GroupFragment;
import com.example.locationword.locationword.Fragment.MeFragment;
import com.example.locationword.locationword.Fragment.MessageFragment;
import com.example.locationword.locationword.Receiver.JPushReceiver;
import com.example.locationword.locationword.app.AppManager;
import com.example.locationword.locationword.event.GroupUpdateEvent;
import com.example.locationword.locationword.event.LocationEvent;
import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.BDLocation;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.SystemMessageActivity;
import com.hyphenate.easeui.utils.CallReceiver;
import com.mob.tools.gui.ViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private BottomNavigationView bottomNavigationView;
    private FragmentAdapter fa;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private TextView tv_title;
    TextView tv_add;
    private CallReceiver callReceiver;
    private String TAG="MainActivity";
    private Context context = MainActivity.this;
    private String userId;
    private BDLocation bdl;
    private SharedPreferences s;
    private ImageView ivSearch;
    private String[] premission;
    private Boolean connectB = true;
    private EMConnectionListener connectionListener;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
//                    String result = (String)msg.obj;
//                    JsonObject jo =JSONChange.StringToJsonObject(result);
//                    Log.i(TAG,jo.get("message").getAsString());
                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context,"网络异常！");
                        }
                    });
                    break;
                case 50:
                    setJPush();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        AppManager.getAppManager().addActivity(this);
        initView();
        addListener();
        if (Build.VERSION.SDK_INT >= 23) {
            AppManager.requestRuntimePermissions(premission,MainActivity.this);
        }
        addCallReceive();
        addJpushReceive();
        bdl=new BDLocation(getApplicationContext());//直接一次定位
        setJPush();
    }
    public void initView(){
        premission= new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA
        ,Manifest.permission.READ_CONTACTS,Manifest.permission.RECEIVE_SMS,Manifest.permission.MANAGE_DOCUMENTS };
        s = getSharedPreferences(Constant.logindata,MODE_PRIVATE);
        userId = getSharedPreferences(Constant.logindata,MODE_PRIVATE
        ).getString(Constant.UserId,"");
        tv_add=findViewById(R.id.tv_add);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        //  BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager = (ViewPager) findViewById(R.id.vp);
        List<Fragment> data = new ArrayList<>();
        data.add(new MessageFragment());
        data.add(new GroupFragment());
        data.add(new MeFragment());
        fa =new FragmentAdapter(getSupportFragmentManager(),data);
        viewPager.setAdapter(fa);
        tv_title=findViewById(R.id.tv_title);
      }
    public void addListener(){
        connectionListener = new EMConnectionListener() {
            @Override
            public void onConnected() {
                Log.i("tttt","dsdsd111");
            }

            @Override
            public void onDisconnected(int errorCode) {
                connectB=false;

                showDisConnectDialog();
                Log.i("tttt","dsdsd");
            }
        };
        tv_add.setOnClickListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new MyBottomBarListenr());
        viewPager.addOnPageChangeListener(this);
        ivSearch.setOnClickListener(this);

        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    @Override
    public void onClick(View v) {
        if (connectB){
            switch (v.getId()){
                case R.id.tv_add:
                    SkipUtils.skipActivity(MainActivity.this,AddGroupActivity.class);
                    break;
                case R.id.iv_search:
                    SkipUtils.skipActivity(MainActivity.this,SearchGroupActivity.class);
                    break;
            }
        }else {
            showDisConnectDialog();
        }

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
    if (connectB){

        if (menuItem != null) {
            menuItem.setChecked(false);
        } else {
            bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        menuItem = bottomNavigationView.getMenu().getItem(i);
        menuItem.setChecked(true);
        switch (i){
            case 0:
                tv_title.setText("消息");
                break;
            case 1:
                tv_title.setText("群组");
                break;
            case 2:
                tv_title.setText("我的");
                break;
        }
    }else {
        showDisConnectDialog();
    }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    class MyBottomBarListenr implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (connectB){
                menuItem = item;
                switch (item.getItemId()) {
                    case R.id.navigation_msg:
                        tv_title.setText("消息");
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_group:
                        tv_title.setText("群组");
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_me:
                        tv_title.setText("我的");
                        viewPager.setCurrentItem(2);
                        return true;
                }
            }else {
                showDisConnectDialog();
            }

            return false;
        }
    }
    protected void addJpushReceive(){
        JPushReceiver jpr = new JPushReceiver();
        // String id=JPushInterface.getRegistrationID(this);
        IntentFilter callFilter = new IntentFilter("cn.jpush.android.intent.REGISTRATION");
        registerReceiver(jpr, callFilter);
    }
    private void addCallReceive(){
        callReceiver=new CallReceiver();
       // String id=JPushInterface.getRegistrationID(this);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(callReceiver, callFilter);
    }
    public void addLocationData(double lat,double lon){
        HashMap<String,String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("lat",lat+"");
        map.put("lon",lon+"");
        HttpUtil.getInstence().doPost(API.addUserLocation,map,handler);
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//订阅
        }
    }

    public void onDestroy() {
        super.onDestroy();
        LoginOutLocation();
       // EMClient.getInstance().logout(true);
        bdl.stop();
        AppManager.getAppManager().removeActivity(this);
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EventBus.getDefault().unregister(this);//订阅
        Log.i("application","logout");
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(LocationEvent event) {
        Log.i(TAG,event.getLat()+","+event.getLon());
        addLocationData(event.getLat(),event.getLon());
    }
    public void LoginOutLocation(){
        HttpUtil.getInstence().doGet(API.changeLocationState+"?userId="+getSharedPreferences(Constant.logindata,MODE_PRIVATE
        ).getString(Constant.UserId,"")+"&isOnline=0",handler);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

                new AlertDialog.Builder(context)
                        .setTitle("确定退出程序")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        })
                        .create().show();


            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context,"推送服务繁忙，正在重新连接");
                        }
                    });
                    handler.sendEmptyMessageDelayed(50,1000*60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
          //  ExampleUtil.showToast(logs, getApplicationContext());
        }
    };

    protected void showDisConnectDialog(){
        handler.post(new Runnable() {
            @Override
            public void run() {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("你的账号在别处登录！")
                        .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences s=getSharedPreferences(Constant.logindata,MODE_PRIVATE);
                                s.edit().putString(Constant.UserId,userId).commit();
                                AppManager.getAppManager().finishActivity(MainActivity.class);
                                SkipUtils.skipActivity(MainActivity.this,LoginActivity.class);
                            }
                        }).setNegativeButton("退出登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences s=getSharedPreferences(Constant.logindata,MODE_PRIVATE);
                        s.edit().putString(Constant.UserId,"").commit();
                        AppManager.getAppManager().finishActivity(MainActivity.class);
                        SkipUtils.skipActivity(MainActivity.this,LoginActivity.class);
                    }
                }).create().show();
            }
        });
    }
    protected void setJPush(){
        JPushInterface.setAliasAndTags(getApplicationContext(),
                userId,
                null,
                mAliasCallback);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
        builder.statusBarDrawable = R.mipmap.location_logo;
        builder.notificationFlags =  Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
    }
}
