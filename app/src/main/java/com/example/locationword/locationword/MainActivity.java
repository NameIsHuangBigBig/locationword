package com.example.locationword.locationword;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;

//
//import com.easemob.easeui.EaseConstant;
//import com.easemob.easeui.ui.EaseChatFragment;
//import com.easemob.easeui.ui.EaseConversationListFragment;
import com.example.locationword.locationword.Adapter.FragmentAdapter;
import com.example.locationword.locationword.Fragment.GroupFragment;
import com.example.locationword.locationword.Fragment.MeFragment;
import com.example.locationword.locationword.Fragment.MessageFragment;
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
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.CallReceiver;
import com.mob.tools.gui.ViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        addCallReceive();
        bdl=new BDLocation(getApplicationContext());//直接一次定位
    }
    public void initView(){
        userId = getSharedPreferences(Constant.logindata,MODE_PRIVATE
        ).getString(Constant.UserId,"");
        tv_add=findViewById(R.id.tv_add);
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
        tv_add.setOnClickListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new MyBottomBarListenr());
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add:
                SkipUtils.skipActivity(MainActivity.this,AddGroupActivity.class);
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
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
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    class MyBottomBarListenr implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            return false;
        }
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
}
