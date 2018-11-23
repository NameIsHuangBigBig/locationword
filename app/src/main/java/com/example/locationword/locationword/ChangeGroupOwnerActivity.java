package com.example.locationword.locationword;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.locationword.locationword.Adapter.ChangeOwnerAdapter;
import com.example.locationword.locationword.bean.User;
import com.example.locationword.locationword.event.GroupManagerEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.StringUtil;
import com.google.gson.JsonArray;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeGroupOwnerActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ImageView back;
    private TextView tvFinish;
    private ListView lvMain;
    private String GroupId;
    private String nowOwner;
    private String InGroupMan;
    private String newOwnerId;
    private List<User> data=new ArrayList<>();
    private String TAG="ChangeGroupOwnerActivity";
    private Context context=ChangeGroupOwnerActivity.this;
    private ChangeOwnerAdapter coa;
    private LoadingDialog loadingDialog;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    Log.i(TAG,"返回结果："+result);
                    final JsonArray ja= JSONChange.StringToJsonArrary(result);
                    analysis(ja);
                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context,"网络异常");
                        }
                    });
                    break;
            }
        }
    };
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.tansformgroup);
        initView();
        addListener();
        requestData();
    }
    public void requestData(){
        Map<String,String> m=new HashMap<String,String>();
        m.put("userIdarray",InGroupMan);
        HttpUtil.getInstence().doPost(API.getGroupUser,m,handler);
    }
    public void initView(){
        loadingDialog = new LoadingDialog(context,"转让中...");
        back = (ImageView) findViewById(R.id.back);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        lvMain = (ListView) findViewById(R.id.lv_main);
        coa = new ChangeOwnerAdapter(data,context);
        lvMain.setAdapter(coa);
        GroupId = getIntent().getStringExtra("GroupId");
        nowOwner = getIntent().getStringExtra("nowOwner");
        InGroupMan = getIntent().getStringExtra("InGroupMan");
    }

    private void analysis(JsonArray ja){
        for (int i=0;i<ja.size();i++) {
            String UserId=ja.get(i).getAsJsonObject().get("UserId").getAsString();
            String NickName=ja.get(i).getAsJsonObject().get("NickName").getAsString();
            String RealName=ja.get(i).getAsJsonObject().get("RealName").getAsString();
            String UserAvarl=ja.get(i).getAsJsonObject().get("UserAvarl").getAsString();
            String UserSex = ja.get(i).getAsJsonObject().get("UserSex").getAsString();
            String UserPhone = ja.get(i).getAsJsonObject().get("UserPhone").getAsString();
            User u =new User(UserId,UserPhone,NickName,RealName,UserAvarl,UserSex);
            if (UserId.equals(nowOwner)){
                u.setOwner(true);
            }
            data.add(u);
        }

    }
    public void addListener(){
        back.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        lvMain.setOnItemClickListener(this);
    }
    public void changeOwner(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG,GroupId+"/NEW/"+newOwnerId);
                    EMClient.getInstance().groupManager().changeOwner(GroupId, newOwnerId);//需异部处理
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new GroupManagerEvent("changeman",""));
                        ChangeGroupOwnerActivity.this.finish();
                        ShowUtil.showText(context,"转让成功!");
                        loadingDialog.close();
                    }
                });
            }
        }).start();

    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                ChangeGroupOwnerActivity.this.finish();
                break;
            case R.id.tv_finish:
                new AlertDialog.Builder(context)
                        .setTitle("确定后你将不是会长")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadingDialog.show();
                                changeOwner();
                            }
                        })
                        .create().show();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG,"index"+i);
        for (int q=0;q<data.size();q++){
            data.get(q).setSelectOwner(false);
        }
        data.get(i).setSelectOwner(true);
        newOwnerId=String.valueOf(data.get(i).getUserId());
        coa.notifyDataSetChanged();
    }
}
