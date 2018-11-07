package com.example.locationword.locationword;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.StringUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView bReturn;
    private EditText edRegisterPhone;
    private EditText edRegisterPassword;
    private EditText edagainPassword;
    private EditText edRegisterCode;
    private Button btnGetCode;
    private Button btnForgetRegister;
    private Context context=ForgetPasswordActivity.this;
    private EventHandler eh;
    private String TAG="ForgetPasswordActivity";
    private String userPhone;
    private String userPassword;
    private String code;
    private String useragainPassword;
    private LoadingDialog loadingDialog;
    private Handler handler=new Handler(){
        public void  handleMessage(Message m){
            switch (m.what){
                case 1000:
                    String s = (String)m.obj;
                    JsonObject jo = JSONChange.StringToJsonObject(s);
                    final String result=jo.get("message").getAsString();
                    Log.i(TAG,result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.close();
                            ShowUtil.showText(context,result);
                        }
                    });

                    if(result.equals("修改成功")){
                        final String userId = jo.get("userid").getAsString();
                        Log.i(TAG,"finish");
                        ForgetPasswordActivity.this.finish();
                    }

                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.close();
                            ShowUtil.showText(context,"网络异常");
                        }
                    });
                   //
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_layout);
        initView();
        addListener();
        addMOBSDK();
    }
    protected  void initView(){
        bReturn = (ImageView) findViewById(R.id.b_return);
        edRegisterPhone = (EditText) findViewById(R.id.ed_register_phone);
        edRegisterPassword = (EditText) findViewById(R.id.ed_register_password);
        edagainPassword = (EditText) findViewById(R.id.ed_register_name);
        edRegisterCode = (EditText) findViewById(R.id.ed_register_code);
        btnGetCode = (Button) findViewById(R.id.btn_getCode);
        btnForgetRegister = (Button) findViewById(R.id.btn_forget_register);
        loadingDialog=new LoadingDialog(context,"重置中...");
    }
    protected void  addListener(){
        bReturn.setOnClickListener(this);
        btnGetCode.setOnClickListener(this);
        btnForgetRegister.setOnClickListener(this);
    }
    public void onClick(View v){

        switch (v.getId()){
            case R.id.b_return:
                ForgetPasswordActivity.this.finish();
                break;
            case R.id.btn_getCode:
                String phone=edRegisterPhone.getText().toString();
                Log.i(TAG,phone);
                if(StringUtil.isMobileNO(phone)){
                    SMSSDK.getVerificationCode("86", phone);
                    BtnThreadstart();
                    btnGetCode.setClickable(false);
                    btnGetCode.setBackgroundResource(R.drawable.button_shape_f);
                }else{
                    ShowUtil.showText(context,"请输入正确的手机号");
                }
                break;
            case R.id.btn_forget_register:

                userPhone = edRegisterPhone.getText().toString();
                userPassword = edRegisterPassword.getText().toString();
                useragainPassword = edagainPassword.getText().toString();
                code = edRegisterCode.getText().toString();
                if(userPhone.equals("")){
                    ShowUtil.showText(context,"请输入手机号");
                }else if(userPassword.equals("")){
                    ShowUtil.showText(context,"请输入密码");
                }else if(useragainPassword.equals("")){
                    ShowUtil.showText(context,"请输入密码");
                }else if(code.equals("")){
                    ShowUtil.showText(context,"请输入验证码");
                }else if(userPassword.length()<6||userPassword.length()>16){
                    ShowUtil.showText(context,"密码格式出错");
                }else if(!userPassword.equals(useragainPassword)){
                    ShowUtil.showText(context,"密码不一致");
                }
                else {
                    loadingDialog.show();
                    // 提交验证码，其中的code表示验证码，如“1357”
                    SMSSDK.submitVerificationCode("86", userPhone, code);

                }
                break;
        }
    }

    protected void addMOBSDK(){
        eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.i(TAG,"event"+event);
                Log.i(TAG,"result"+result);
                Log.i(TAG,"data"+data.toString());
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        resetPassword();

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功

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
                                ShowUtil.showText(context,"短信条数达到上限");
                            }
                        });
                    }else{
                        resetPassword();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowUtil.showText(context,"验证码错误");
                            }
                        });
                    }


                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    protected void resetPassword(){
        HashMap<String,String>map = new HashMap<>();
        map.put("phone",userPhone);
        map.put("newPassword",userPassword);
        HttpUtil.getInstence().doPost(API.resetPassword,map,handler);
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
                                btnGetCode.setText(index+"");
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnGetCode.setClickable(true);
                        btnGetCode.setBackgroundResource(R.drawable.button_shape);
                        btnGetCode.setText("获取验证码");
                    }
                });
            }
        }).start();
    }


    protected void onDestroy(){
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh); //注册短信回调
    }
}
