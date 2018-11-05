package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

public class AddGroupActivity extends AppCompatActivity{
    EditText et_groupname;
    EditText et_distribue;
    EditText et_num;
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.addgroun_activity);
        et_groupname=findViewById(R.id.et_groupname);
        et_distribue=findViewById(R.id.et_distribue);
        et_num=findViewById(R.id.et_num);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().createPublicGroup(et_groupname.getText().toString(), et_distribue.getText().toString(), null,true,Integer.parseInt(et_num.getText().toString()));//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
