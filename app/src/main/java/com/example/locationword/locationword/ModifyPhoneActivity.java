package com.example.locationword.locationword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.StringUtil;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ModifyPhoneActivity extends AppCompatActivity implements View.OnClickListener{

    EditText e_modify_phone,ed_modify_code;
    Button b_modify_getCode,b_modify_phone;
    ImageView iv_modify_phone;
    private EventHandler eh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        addMOBSDK();
        e_modify_phone = (EditText)findViewById(R.id.ed_modify_phone);
        b_modify_getCode = (Button)findViewById(R.id.btn_modify_getCode);
        b_modify_phone = (Button)findViewById(R.id.btn_modify_phone);
        iv_modify_phone = (ImageView)findViewById(R.id.phone_back_mine);
        ed_modify_code = (EditText) findViewById(R.id.ed_modify_code);


        b_modify_getCode.setOnClickListener(this);
        b_modify_phone.setOnClickListener(this);
        iv_modify_phone.setOnClickListener(this);

    }
    public void onClick(View v){
        String phone=e_modify_phone.getText().toString();
        String  code  = ed_modify_code.getText().toString();
        int id = v.getId();
        switch (id){
            case R.id.btn_modify_getCode:
                //获取验证码
                if(StringUtil.isMobileNO(phone)){
                    SMSSDK.getVerificationCode("86", phone);
                    BtnThreadstart();
                    b_modify_getCode.setClickable(false);
                    b_modify_getCode.setBackgroundResource(R.drawable.button_shape_f);
                }else{
                    ShowUtil.showText(getApplication(),"请输入正确的手机号");
                }
                break;
            case R.id.btn_modify_phone:
//                if(SMSSDK.getVerificationCode("86", phone, code)){
//
//                }
                SMSSDK.submitVerificationCode("86", phone, code);

                //   finish();

                //确定修改电话
                break;
            case R.id.phone_back_mine:
                finish();
                //返回
                break;
        }
    }
    protected void BtnThreadstart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i =60;i>0;i--){
                    final String index=i+"";
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                b_modify_getCode.setText(index+"");
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        b_modify_getCode.setClickable(true);
                        b_modify_getCode.setBackgroundResource(R.drawable.button_shape);
                        b_modify_getCode.setText("获取验证码");
                    }
                });
            }
        }).start();
    }
    protected void addMOBSDK(){
        Log.i("222","32122");
        eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.i("222","321");
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Log.i("222","12");
                        //提交验证码成功
                        Intent intent = getIntent();
                        intent.putExtra("text", e_modify_phone.getText().toString());
                        setResult(2, intent);
                        finish();
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        Log.i("222","22");
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtil.showText(getApplication(),"验证码发送成功！");
                            }
                        });
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    //测试
                    if(event == 2){
                        //  registerUser();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtil.showText(getApplication(),"验证码获取失败");
                            }
                        });
                    }else{
                        Log.i("222","333");
//                        Intent intent = getIntent();
//                        intent.putExtra("text", e_modify_phone.getText().toString());
//                        setResult(2, intent);
//                        finish();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtil.showText(getApplication(),"验证码错误");
                            }
                        });
                    }
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
}