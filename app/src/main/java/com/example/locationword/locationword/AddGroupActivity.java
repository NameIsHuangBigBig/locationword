package com.example.locationword.locationword;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.locationword.locationword.event.GroupUpdateEvent;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.ShowUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et_groupname;
    EditText et_distribue;
    EditText et_num;
    ImageView b_return;
    Button btn_finish;
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.addgroun_activity);
        initView();
        addListener();

    }
    public void addListener(){
        b_return.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
    }
    public void initView(){
        et_groupname=findViewById(R.id.et_groupname);
        et_distribue=findViewById(R.id.et_distribue);
        et_num=findViewById(R.id.et_num);
        b_return= findViewById(R.id.b_return);
        btn_finish=findViewById(R.id.btn_finish);
        try {
            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_finish:
                int allMembers=Integer.parseInt(et_num.getText().toString());
                if(allMembers<10||allMembers>999){
                    ShowUtil.showText(AddGroupActivity.this,"群人数不正确");
                }else{
                    EMCreateGroup(allMembers);
                }
                break;
            case R.id.b_return:
                AddGroupActivity.this.finish();
                break;
        }
    }
    public void EMCreateGroup(int allMembers){
        final EMGroupOptions option = new EMGroupOptions();
        option.maxUsers = allMembers;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin ;
        final String groupName = et_groupname .getText().toString();
        final String desc = et_distribue.getText().toString();
        SharedPreferences sha=getSharedPreferences(Constant.logindata,MODE_PRIVATE);
        final String userId= sha.getString("userid","");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().createGroup(groupName,desc,new String[]{userId},"邀请你加入群",option);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(new GroupUpdateEvent(true));
                                ShowUtil.showText(AddGroupActivity.this,"创建群成功");
                                AddGroupActivity.this.finish();
                            }
                        });
                    } catch (HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtil.showText(AddGroupActivity.this,"服务器出错");
                            }
                        });
                        e.printStackTrace();
                    }

                }
            }).start();


    }
}
