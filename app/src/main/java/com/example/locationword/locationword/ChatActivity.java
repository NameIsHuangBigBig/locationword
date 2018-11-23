package com.example.locationword.locationword;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.locationword.locationword.event.ChatActivityUpdateEvent;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.API;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.HttpUtil;
import com.hyphenate.easeui.JSONChange;
import com.hyphenate.easeui.ui.EaseChatFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    private EaseChatFragment chatFragment;
    private ImageView iv_manager;
    private String GroupId;
    private String ChatTpye;
    private String nickName;
    protected Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    final JsonObject jo = JSONChange.StringToJsonObject(result);
                    nickName=jo.get("NickName").getAsString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("chat","single");
                            Bundle b= new Bundle();
                            // 传群ID或者用户Id
                            b.putString(EaseConstant.EXTRA_USER_ID,GroupId);
                            b.putInt(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_SINGLE);
                            b.putString("nickName",nickName);
                            b.putString("HeadImg",API.BASEURL+jo.get("UserAvarl").getAsString());
                            chatFragment.setArguments(b);
                            getSupportFragmentManager().beginTransaction().add(R.id.fl_content, chatFragment).commit();

                        }
                    });
                    break;
            }
        }
    };
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.chatroom_activity);
        initView();
        addListenr();
    }
    public void addListenr(){
        iv_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object>m=new HashMap<>();
                m.put(Constant.EaseGroupId,GroupId);
                SkipUtils.skipActivityWithParameter(ChatActivity.this,GroupManagerActivity.class,m);
            }
        });
    }
    public void initView(){
        chatFragment =new EaseChatFragment();
        iv_manager=findViewById(R.id.iv_manager);
        chatFragment.setArguments(getIntent().getExtras());
        GroupId=getIntent().getStringExtra(Constant.EaseGroupId);
        ChatTpye=getIntent().getStringExtra(Constant.EaseChattype);
        Log.i("chat",Constant.EaseChattype+"sdsd"+ChatTpye+"\t d"+GroupId);
        if(ChatTpye!=null){

            // 传群ID或者用户Id

            if (ChatTpye.equals("Group")){
                Log.i("chat","group");
                Bundle b= new Bundle();
                b.putString(EaseConstant.EXTRA_USER_ID,GroupId);
                b.putInt(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                chatFragment.setArguments(b);
                getSupportFragmentManager().beginTransaction().add(R.id.fl_content, chatFragment).commit();

            }else{
                HttpUtil.getInstence().doGet(API.getUserDetail+"?userId="+GroupId,handler);
                iv_manager.setVisibility(View.GONE);
            }
          //  chatFragment.setArguments(b);
        }
      //  getSupportFragmentManager().beginTransaction().add(R.id.fl_content, chatFragment).commit();
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
    public void onDataSynEvent(ChatActivityUpdateEvent event) {
        Log.i("event1",event.getResult() );
        chatFragment.getTitleBar().setTitle(event.getResult());
    }
}
