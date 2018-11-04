package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText ed_phone,ed_password,ed_name,ed_code;
    Button b_getCode,b_register;
    RadioGroup radioGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        ed_phone = (EditText)findViewById(R.id.ed_register_phone);
        ed_password = (EditText)findViewById(R.id.ed_register_password);
        ed_name = (EditText)findViewById(R.id.ed_register_name);
        ed_code = (EditText)findViewById(R.id.ed_register_code);

        radioGroup = (RadioGroup)findViewById(R.id.rag);

        b_getCode = (Button)findViewById(R.id.btn_getCode);
        b_register = (Button)findViewById(R.id.btn_register);

        b_getCode.setOnClickListener(this);
        b_register.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rg_register_boy){
                    //选男孩
                }else {
                    //选女孩
                }
            }
        });
    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_getCode:
                //获取验证码
                break;
            case R.id.btn_register:
                //注册
                break;
        }
    }
}
