package com.example.locationword.locationword;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        addListener();
        addCallReceive();

    }
    public void initView(){
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
    public void onDestroy(){
       super.onDestroy();
        EMClient.getInstance().logout(true);
    }
    private void addCallReceive(){
        callReceiver=new CallReceiver();
       // String id=JPushInterface.getRegistrationID(this);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(callReceiver, callFilter);
    }
}
