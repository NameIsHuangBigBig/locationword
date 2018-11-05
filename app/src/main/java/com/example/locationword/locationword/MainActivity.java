package com.example.locationword.locationword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.easemob.chat.EMConversation;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.ui.EaseConversationListFragment;
import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EaseConversationListFragment conversationListFragment;
    TextView tv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        addListener();
        conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(MainActivity.this, ChatRoomActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()).putExtra("username","sdsd"));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.main_fl, conversationListFragment).commit();

    }
    public void initView(){
        tv_add=findViewById(R.id.tv_add);
    }
    public void addListener(){
        tv_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add:
                SkipUtils.skipActivity(MainActivity.this,AddGroupActivity.class);
                break;
        }
    }
}
