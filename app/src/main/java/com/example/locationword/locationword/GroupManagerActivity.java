package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.SkipUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupManagerActivity extends AppCompatActivity {
    private ImageView back_groupManager;
    private ImageView iv_map;
    private String GroupId;
    List<String> memberList;//成员列表
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.group_manager_activity);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getGroupMan();
            }
        }).start();

    }
    public void getGroupMan()  {
        memberList = new ArrayList<>();
        EMCursorResult<String> result = null;
        final int pageSize = 20;
        do {
            try {
                result = EMClient.getInstance().groupManager().fetchGroupMembers(GroupId,
                        result != null ? result.getCursor() : "", pageSize);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            memberList.addAll(result.getData());
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
        for (int i=0;i<memberList.size();i++){
            Log.i("group","list"+memberList.get(i));
        }
    }
    public void initView(){
        GroupId=getIntent().getStringExtra(Constant.EaseGroupId);
        Log.i("group","Manage:当前群ID"+GroupId);
        iv_map=findViewById(R.id.iv_map);
        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkipUtils.skipActivity(GroupManagerActivity.this,MapActivity.class);
            }
        });
        back_groupManager=findViewById(R.id.back_groupManager);
        back_groupManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupManagerActivity.this.finish();
            }
        });
    }
}
