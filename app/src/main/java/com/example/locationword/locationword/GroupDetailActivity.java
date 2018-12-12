package com.example.locationword.locationword;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private ImageView ivImage;
    private TextView tvName;
    private TextView tvNum;
    private TextView tvDistribute;
    private Button btnRequest;
    private String groupId;
    private String TAG="GroupDetailActivity";
    private ImageView ivManhead;
    private TextView tvGroupMan;
    private String userId ;
    private String manName;
    private String manId;
    private String GroupName;
    private String GroupId;
    private String userName;
    private LinearLayout llMan;
    private LoadingDialog loadingDialog;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    final JsonObject jo =JSONChange.StringToJsonObject(result);
                    Log.i(TAG,result);
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.close();
                            manName=jo.get("NickName").getAsString();
                            tvGroupMan.setText(jo.get("NickName").getAsString());
                            //userId= jo.get("UserId").getAsString();

                            llMan.setClickable(true);

                        }
                    });
                    break;
                case 1001:
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.close();
                            ShowUtil.showText(GroupDetailActivity.this,"网络异常！");
                        }
                    });
                    break;
                case 500:
                    String resultme = (String)msg.obj;
                    final JsonObject jome =JSONChange.StringToJsonObject(resultme);
                    Log.i(TAG,resultme);
                    userName=jome.get("NickName").getAsString();
                    break;
                case 501:
                    String push = (String)msg.obj;
                    JsonObject jopush =JSONChange.StringToJsonObject(push);
                    Log.i("dddd",jopush.get("message").getAsString());
                    if (jopush.get("message").getAsString().equals("成功")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnRequest.setText("等待对方同意");
                                btnRequest.setClickable(false);
                                loadingDialog.close();
                                ShowUtil.showText(GroupDetailActivity.this,"申请成功");
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.close();
                               // btnRequest.setText("等待对方同意");
                               // btnRequest.setClickable(false);
                                ShowUtil.showText(GroupDetailActivity.this,"该用户app版本较低，无法初始化推送服务！");
                            }
                        });
                    }
                    break;
            }
        }
    };
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.group_detail_activity);
        initView();
        addListenr();
        requestData();

    }
    protected  void requestManPeople () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> memberList = new ArrayList<>();
                EMCursorResult<String> result = null;
                final int pageSize = 999;
                do {
                    try {
                        result = EMClient.getInstance().groupManager().fetchGroupMembers(GroupId,
                                result != null ? result.getCursor() : "", pageSize);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        break;
                    }
                    if (result!=null){
                        memberList.addAll(result.getData());
                    }
                } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
                for (int i =0; i<memberList.size();i++){
                    if (userId.equals(memberList.get(i))){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnRequest.setText("您已加入此群");
                                btnRequest.setClickable(false);
                            }
                        });
                        break;
                    }
                }
            }
        }).start();

    }
    protected void initView(){
        loadingDialog = new LoadingDialog(GroupDetailActivity.this,"请求中...");
        loadingDialog.show();
        groupId = getIntent().getStringExtra("GroupId");
        userId = getSharedPreferences(Constant.logindata,MODE_PRIVATE
        ).getString(Constant.UserId,"");
        Log.i(TAG,groupId+"dd");
        back = (ImageView) findViewById(R.id.back);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvDistribute = (TextView) findViewById(R.id.tv_distribute);
        btnRequest = (Button) findViewById(R.id.btn_request);

        llMan = (LinearLayout) findViewById(R.id.ll_man);
        llMan.setClickable(false);
        ivManhead = (ImageView) findViewById(R.id.iv_manhead);
        tvGroupMan = (TextView) findViewById(R.id.tv_group_man);

    }
    protected void addListenr(){
        back.setOnClickListener(this);
        llMan.setOnClickListener(this);
        btnRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                GroupDetailActivity.this.finish();
                break;
            case R.id.btn_request:
                loadingDialog.show();
                HashMap<String,String> m = new HashMap<>();
                m.put("userId",userId);
                m.put("userName",userName);
                m.put("GroupName",GroupName);
                m.put("GroupId",GroupId);
                m.put("manId",manId);
                m.put("manName",manName);
                HttpUtil.getInstence().doPost(API.addGroupPush,m,handler,501);
                // addGroup();

               break;
            case R.id.ll_man:
                HashMap<String,Object> m1 = new HashMap<>();
                m1.put("UserId",manId);
                SkipUtils.skipActivityWithParameter(GroupDetailActivity.this,GroupManDetailActivity.class,m1);
                break;
        }
    }

    protected void requestData(){
        HttpUtil.getInstence().doGet(API.getUserDetail+"?userId="+userId,handler,500);
        //根据群组ID从服务器获取群组基本信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                 //   Log.i(TAG,group.getGroupName());
                    if (group!=null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String id= group.getOwner();
                                Glide.with(GroupDetailActivity.this)
                                        .load(API.BASEURL+"/image/"+id+"head"+id+".jpg")
                                        .placeholder(R.drawable.groupmanager_icon_one).into(ivManhead);
                                Log.i(TAG,"id"+id);
                                HttpUtil.getInstence().doGet(API.getUserDetail+"?userId="+id,handler);
                                GroupName=group.getGroupName();
                                GroupId=group.getGroupId();
                                requestManPeople();
                                manId = id;
                                if(manId.equals(userId)){
                                    btnRequest.setClickable(false);
                                    btnRequest.setText("您已加入此群！");
                                }
                                tvName .setText(group.getGroupName());
                                tvNum.setText(group.getGroupId());
                                tvDistribute.setText(group.getDescription());
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

