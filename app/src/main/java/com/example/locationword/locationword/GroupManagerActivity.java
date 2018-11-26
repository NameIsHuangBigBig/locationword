package com.example.locationword.locationword;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.locationword.locationword.event.GroupManagerEvent;
import com.example.locationword.locationword.event.GroupUpdateEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.GroupManImage;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonArray;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import org.apmem.tools.layouts.FlowLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupManagerActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back_groupManager;
    private ImageView iv_map;
    private String GroupId;
    private FlowLayout flowLayout;
    private List<String> memberList;//成员列表
    private StringBuffer userIdArray=new StringBuffer();
    private String TAG = "GroupManagerActivity";
    private ProgressBar loading;
    private RelativeLayout relaTitleGroupManagerTwoOne;
    private RelativeLayout relaTitleGroupManagerTwoTwo;
    private ImageView ivZr;
    private ImageView ivJj;
    private ImageView ivFx;
    private ImageView ivYc;
    private ImageView ivInvite;
    private Button btnTuic;
    private TextView tvGroupname;
    private TextView tvTs;
    private String OwnerId;
    private String UserId;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Map<String,String> m=new HashMap<String,String>();
                    m.put("userIdarray",userIdArray.toString());
                    Log.i(TAG,"userIdarray"+userIdArray.toString());
                    HttpUtil.getInstence().doPost(API.getGroupUser,m,handler);
                    break;
                case 1000:
                    String result = (String)msg.obj;
                    Log.i(TAG,"返回结果："+result);
                    final JsonArray ja=JSONChange.StringToJsonArrary(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivZr.setClickable(true);
                            ivYc.setClickable(true);
                            iv_map.setClickable(true);
                            ivInvite.setVisibility(View.VISIBLE);
                            addImageView(ja);
                        }
                    });
                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
                            ShowUtil.showText(GroupManagerActivity.this,"网络异常");
                        }
                    });
                    break;
            }
        }
    };
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.group_manager_activity);
        initView();
        addListener();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getGroupMan();
            }
        }).start();
    }
    protected void addListener(){
        iv_map.setOnClickListener(this);
        back_groupManager.setOnClickListener(this);
        relaTitleGroupManagerTwoOne .setOnClickListener(this);
        relaTitleGroupManagerTwoTwo .setOnClickListener(this);
        ivZr .setOnClickListener(this);
        ivZr.setClickable(false);
        ivJj .setOnClickListener(this);
        ivFx .setOnClickListener(this);
        ivYc .setOnClickListener(this);
        btnTuic .setOnClickListener(this);
        ivInvite.setOnClickListener(this);
    }
    public void getGroupMan()  {
        memberList = new ArrayList<>();
        EMCursorResult<String> result = null;
        final int pageSize = 999;
        do {
            try {
                result = EMClient.getInstance().groupManager().fetchGroupMembers(GroupId,
                        result != null ? result.getCursor() : "", pageSize);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            memberList.addAll(result.getData());
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
        getGroupBoss();
        userIdArray=new StringBuffer();
        userIdArray.append("[");
        for (int i=0;i<memberList.size();i++){

            if(i==memberList.size()-1){
                userIdArray.append(memberList.get(i)+"]");
            }else{
                userIdArray.append(memberList.get(i)+",");
            }

            Log.i("group","list"+memberList.get(i));
        }
        handler.sendEmptyMessage(1);
    }
    public void initView(){
        flowLayout= findViewById(R.id.fly);
        loading= findViewById(R.id.loading);
        GroupId=getIntent().getStringExtra(Constant.EaseGroupId);
        UserId=getSharedPreferences(Constant.logindata,MODE_PRIVATE).getString(Constant.UserId,"");
        Log.i("group","Manage:当前群ID"+GroupId);
        iv_map=findViewById(R.id.iv_map);
        back_groupManager=findViewById(R.id.back_groupManager);
        relaTitleGroupManagerTwoOne = (RelativeLayout) findViewById(R.id.rela_title_groupManager_two_one);
        relaTitleGroupManagerTwoTwo = (RelativeLayout) findViewById(R.id.rela_title_groupManager_two_two);
        ivZr = (ImageView) findViewById(R.id.iv_zr);
        ivJj = (ImageView) findViewById(R.id.iv_jj);
        ivFx = (ImageView) findViewById(R.id.iv_fx);
        ivYc = (ImageView) findViewById(R.id.iv_yc);
        ivYc.setClickable(false);
        iv_map.setClickable(false);
        btnTuic = (Button) findViewById(R.id.btn_tuic);
        ivInvite = (ImageView) findViewById(R.id.iv_invite);
        ivInvite.setVisibility(View.INVISIBLE);
        tvGroupname = (TextView) findViewById(R.id.tv_groupname);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(GroupId);
        if (group != null){
            tvGroupname.setText(group.getGroupName());
        }
        tvTs = (TextView) findViewById(R.id.tv_ts);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_map:
                HashMap<String,Object>mapmap=new HashMap<>();
               // map.put("GroupId",GroupId);
                mapmap.put("InGroupMan",userIdArray.toString());
                SkipUtils.skipActivityWithParameter(GroupManagerActivity.this,MapActivity.class,mapmap);
                break;
            case R.id.back_groupManager:
                GroupManagerActivity.this.finish();
                break;
            case R.id.rela_title_groupManager_two_one:
                String s="";
                HashMap<String,Object>m=new HashMap<>();
                m.put("GroupId",GroupId);
                EMGroup group = EMClient.getInstance().groupManager().getGroup(GroupId);
                if (group != null){
                     s=group.getGroupName();
                }
                m.put("nowGroupName",s);
                SkipUtils.skipActivityWithParameter(GroupManagerActivity.this,ChangeGroupNameActivity.class,m);
                break;
            case R.id.iv_invite:
                HashMap<String,Object>map=new HashMap<>();
                map.put("GroupId",GroupId);
                map.put("InGroupMan",userIdArray.toString());
                SkipUtils.skipActivityWithParameter(GroupManagerActivity.this,InviteGroupManActivity.class,map);
                break;
            case R.id.rela_title_groupManager_two_two:
                break;
            case R.id.iv_zr:
                Log.i(TAG,OwnerId+"\t213213"+UserId);
                if(OwnerId.equals(UserId)){
                    HashMap<String,Object>mapzr=new HashMap<>();
                    mapzr.put("GroupId",GroupId);
                    mapzr.put("nowOwner",OwnerId);
                    mapzr.put("InGroupMan",userIdArray.toString());
                    SkipUtils.skipActivityWithParameter(GroupManagerActivity.this,ChangeGroupOwnerActivity.class,mapzr);
                }else{
                    ShowUtil.showText(GroupManagerActivity.this,"只有会长可以使用此功能！");
                }
                break;
            case R.id.iv_jj:
                break;
            case R.id.iv_fx:
                break;
            case R.id.iv_yc:
                if(OwnerId.equals(UserId)){
                    HashMap<String,Object>mapzr=new HashMap<>();
                    mapzr.put("GroupId",GroupId);
                    mapzr.put("nowOwner",OwnerId);
                    mapzr.put("InGroupMan",userIdArray.toString());
                    SkipUtils.skipActivityWithParameter(GroupManagerActivity.this,DeleteGroupManActivity.class,mapzr);
                }else{
                    ShowUtil.showText(GroupManagerActivity.this,"只有会长可以使用此功能！");
                }
                break;
            case R.id.btn_tuic:
                break;
        }
    }
    protected  void addImageView(JsonArray ja){
        loading.setVisibility(View.GONE);
        for(int i=0;i<ja.size();i++){
            GroupManImage gmi = new GroupManImage(GroupManagerActivity.this);
            gmi.setIndex(Integer.parseInt(ja.get(i).getAsJsonObject().get("UserId").getAsString()));
            gmi.setClickListener(new GroupManImage.ClickListener() {
                @Override
                public void clickLayout(int Id) {
                    Log.i("detail",Id+"");
                    HashMap<String,Object> m =new HashMap<>();
                    m.put("UserId",Id+"");
                    SkipUtils.skipActivityWithParameter(GroupManagerActivity.this,GroupManDetailActivity.class,m);
                }
            });
            if (ja.get(i).getAsJsonObject().get("UserId").getAsString().equals(OwnerId)){
                gmi.isMan();
            }else{
                gmi.noIsMan();
            }
            gmi.setImagerView(API.BASEURL+ja.get(i).getAsJsonObject().get("UserAvarl").getAsString());
            gmi.setTvNickname(ja.get(i).getAsJsonObject().get("NickName").getAsString());
            flowLayout.addView(gmi,0);
        }
    }
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//订阅
        }
    }
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//订阅
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(GroupManagerEvent event) {
        if(event.getResult().equals("groupname")){
            tvGroupname.setText(event.getContent());
        }else if (event.getResult().equals("inviteman")
                || event.getResult().equals("changeman")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            flowLayout.removeAllViews();
                            flowLayout.addView(ivInvite);
                            ivInvite.setVisibility(View.INVISIBLE);
                            loading.setVisibility(View.VISIBLE);
                        }
                    });

                    getGroupMan();
                }
            }).start();
        }

    }
    public void getGroupBoss(){
        EMGroup group = null;

        try {
            group = EMClient.getInstance().groupManager().getGroupFromServer(GroupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        OwnerId=group.getOwner();
        memberList.add(group.getOwner());
    }
}
