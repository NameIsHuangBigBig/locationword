package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyPhoneActivity extends AppCompatActivity implements View.OnClickListener{

    EditText e_modify_phone;
    Button b_modify_getCode,b_modify_phone;
    ImageView iv_modify_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);

        e_modify_phone = (EditText)findViewById(R.id.ed_modify_phone);
        b_modify_getCode = (Button)findViewById(R.id.btn_modify_getCode);
        b_modify_phone = (Button)findViewById(R.id.btn_modify_phone);
        iv_modify_phone = (ImageView)findViewById(R.id.phone_back_mine);

        b_modify_getCode.setOnClickListener(this);
        b_modify_phone.setOnClickListener(this);
        iv_modify_phone.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_modify_getCode:
                //获取验证码
                break;
            case R.id.btn_modify_phone:
                //确定修改电话
                break;
            case R.id.phone_back_mine:
                //返回
                break;
        }
    }
}