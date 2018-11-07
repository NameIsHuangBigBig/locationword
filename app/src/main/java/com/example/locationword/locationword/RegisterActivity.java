package com.example.locationword.locationword;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



import com.example.locationword.locationword.app.AppManager;
import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.example.locationword.locationword.tool.StringUtil;
import com.google.gson.JsonObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG="RegisterActivity";
    private Context context = RegisterActivity.this;
    private EditText edRegisterPhone;
    private EditText edRegisterPassword;
    private EditText edRegisterName;
    private EditText edRegisterCode;
    private Button btnGetCode;
    private RadioGroup rag;
    private RadioButton rgRegisterBoy;
    private RadioButton rgRegisterGirl;
    private Button btnRegister;
    private EventHandler eh;
    private LoadingDialog loadingDialog;
    private ImageView bReturn;


    String userphone;
    String userpassword ;
    String RealName ;
    String Code ;
    String sex;
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

                    if(result.equals("注册成功")){
                        final String userId = jo.get("userId").getAsString();
                        try {
                            EMClient.getInstance().createAccount(userId, "123456");//同步方法
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG,"finish");
                        RegisterActivity.this.finish();
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

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        iniView();
        loadingDialog=new LoadingDialog(context,"注册中...");

        addListener();
        addMOBSDK();
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
                         registerUser();

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
                                 ShowUtil.showText(context,"验证码获取失败");
                             }
                         });
                     }else{
                         registerUser();
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 loadingDialog.close();
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
    protected void addListener(){
        btnGetCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        bReturn.setOnClickListener(this);
    }
    protected  void iniView(){
        edRegisterPhone = (EditText) findViewById(R.id.ed_register_phone);
        edRegisterPassword = (EditText) findViewById(R.id.ed_register_password);
        edRegisterName = (EditText) findViewById(R.id.ed_register_name);
        edRegisterCode = (EditText) findViewById(R.id.ed_register_code);
        btnGetCode = (Button) findViewById(R.id.btn_getCode);
        rag = (RadioGroup) findViewById(R.id.rag);
        rgRegisterBoy = (RadioButton) findViewById(R.id.rg_register_boy);
        rgRegisterGirl = (RadioButton) findViewById(R.id.rg_register_girl);
        btnRegister = (Button) findViewById(R.id.btn_register);
        bReturn = (ImageView) findViewById(R.id.b_return);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                 userphone = edRegisterPhone.getText().toString();
                 userpassword = edRegisterPassword.getText().toString();
                 RealName = edRegisterName.getText().toString();
                 Code = edRegisterCode.getText().toString();
                 sex="";
                if(rag.getCheckedRadioButtonId()==R.id.rg_register_boy){
                    sex="男";
                }else{
                    sex="女";
                }
                if(userphone.equals("")){
                    ShowUtil.showText(context,"请输入手机号");
                }else if(userpassword.equals("")){
                    ShowUtil.showText(context,"请输入密码");
                }else if(RealName.equals("")){
                    ShowUtil.showText(context,"请输入姓名");
                }else if(Code.equals("")){
                    ShowUtil.showText(context,"请输入验证码");
                }else if(userpassword.length()<6||userpassword.length()>16){
                    ShowUtil.showText(context,"密码格式出错");
                }else {
                    loadingDialog.show();
                    // 提交验证码，其中的code表示验证码，如“1357”
                    SMSSDK.submitVerificationCode("86", userphone, Code);

                }
                Log.i(TAG,"userpassword"+userpassword);
                Log.i(TAG,"userphone"+userphone);
                Log.i(TAG,"RealName"+RealName);
                Log.i(TAG,"Code"+Code);
                Log.i(TAG,"sex"+sex);
                break;
            case R.id.btn_getCode:
                String phone=edRegisterPhone.getText().toString();
                if(StringUtil.isMobileNO(phone)){
                    SMSSDK.getVerificationCode("86", phone);
                    BtnThreadstart();
                    btnGetCode.setClickable(false);
                    btnGetCode.setBackgroundResource(R.drawable.button_shape_f);
                }else{
                    ShowUtil.showText(context,"请输入正确的手机号");
                }

                break;
            case R.id.b_return:
                RegisterActivity.this.finish();
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
    protected void registerUser(){
        HashMap<String,String> m = new HashMap<>();
        m.put("phone",userphone);
        m.put("password",userpassword);
        m.put("sex",sex);
        m.put("realName",RealName);
        HttpUtil.getInstence().doPost(API.registerUser,m,handler);
    }
    protected void onDestroy(){
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh); //注册短信回调
    }
}
