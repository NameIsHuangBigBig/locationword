package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.locationword.locationword.tool.ShowUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et_groupname;
    EditText et_distribue;
    EditText et_num;
    ImageView b_return;
    Button bt_finish;
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.addgroun_activity);
        initView();
        addListener();

    }
    public void addListener(){
        b_return.setOnClickListener(this);
        bt_finish.setOnClickListener(this);
    }
    public void initView(){
        et_groupname=findViewById(R.id.et_groupname);
        et_distribue=findViewById(R.id.et_distribue);
        et_num=findViewById(R.id.et_num);
        b_return= findViewById(R.id.b_return);
        bt_finish=findViewById(R.id.bt_finish);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_finish:
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
        EMGroupOptions option = new EMGroupOptions();
        option.maxUsers = allMembers;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
        String groupName = et_groupname .getText().toString();
        String desc = et_distribue.getText().toString();
        try {
            EMClient.getInstance().groupManager().createGroup(groupName,desc,null,"邀请你加入群",option);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }
}
