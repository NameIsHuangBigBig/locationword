package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText ed_phone,ed_password,ed_name,ed_code;
    ImageView b_return;
    Button b_getCode,b_register;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_layout);

        ed_phone = (EditText)findViewById(R.id.ed_forget_phone);
        ed_password = (EditText)findViewById(R.id.ed_forget_password);
        ed_name = (EditText)findViewById(R.id.ed_forget_name);
        ed_code = (EditText)findViewById(R.id.ed_forget_code);

        b_return = (ImageView)findViewById(R.id.b_return);
        b_getCode = (Button)findViewById(R.id.btn_forget_getCode);
        b_register = (Button)findViewById(R.id.btn_forget_register);

        b_return.setOnClickListener(this);
        b_getCode.setOnClickListener(this);
        b_register.setOnClickListener(this);


    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.b_return:
                //返回
                break;
            case R.id.btn_forget_getCode:
                //获取验证码
                break;
            case R.id.btn_forget_register:
                //确定重置密码
                break;
        }
    }
}
