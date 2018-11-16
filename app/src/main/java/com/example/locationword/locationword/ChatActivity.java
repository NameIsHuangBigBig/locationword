package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.locationword.locationword.event.ChatActivityUpdateEvent;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.SkipUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
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

        if(ChatTpye!=null){
            Bundle b= new Bundle();

            // 传群ID或者用户Id
            b.putString(EaseConstant.EXTRA_USER_ID,GroupId);
            if (ChatTpye.equals("Group")){
                b.putInt(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
            }else{
                b.putInt(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_SINGLE);
            }
            chatFragment.setArguments(b);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, chatFragment).commit();
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
