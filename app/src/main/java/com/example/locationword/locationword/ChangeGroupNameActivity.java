package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.locationword.locationword.event.ChatActivityUpdateEvent;
import com.example.locationword.locationword.event.GroupManagerEvent;
import com.example.locationword.locationword.event.GroupUpdateEvent;
import com.example.locationword.locationword.tool.ShowUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChangeGroupNameActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backUpdateGroupNmae;
    private TextView tvFinish;
    private EditText etName;
    private  String GroupId;
    private String nowGroupName;


    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.updategroupname);
        initView();
        addListener();
    }
    protected void initView(){
        GroupId=getIntent().getStringExtra("GroupId");
        nowGroupName=getIntent().getStringExtra("nowGroupName");

        backUpdateGroupNmae = (ImageView) findViewById(R.id.back_updateGroupNmae);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        etName = (EditText) findViewById(R.id.et_name);
        etName.setText(nowGroupName);
    }
    protected  void  addListener(){
        backUpdateGroupNmae.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_updateGroupNmae:
                ChangeGroupNameActivity.this.finish();
                break;
            case R.id.tv_finish:
                final String name=etName.getText().toString();
                if(name.equals("")){
                    ShowUtil.showText(ChangeGroupNameActivity.this,"请输入新昵称");
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().changeGroupName(GroupId,name);//需异步处理
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(new GroupUpdateEvent(true));
                                        EventBus.getDefault().post(new ChatActivityUpdateEvent(name));
                                        EventBus.getDefault().post(new GroupManagerEvent("groupname",name));
                                        ChangeGroupNameActivity.this.finish();
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
                break;
        }
    }

}
