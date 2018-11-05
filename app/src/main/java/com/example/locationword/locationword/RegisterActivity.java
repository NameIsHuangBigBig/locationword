package com.example.locationword.locationword;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.locationword.locationword.app.AppManager;
import com.example.locationword.locationword.event.MessageEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity{
    private String TAG="RegisterActivity";
    private Context context = RegisterActivity.this;
    private Handler handler=new Handler(){
        public void  handleMessage(Message m){
            switch (m.what){
                case 1000:
                    String s = (String)m.obj;
                    JsonObject jo = JSONChange.StringToJsonObject(s);
                    final String result=jo.get("message").getAsString();
                    final String userId=jo.get("userId").getAsString();
                    Log.i(TAG,result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context,result);
                        }
                    });
                    if(result.equals("注册成功")){
                        registerEMClient(userId);
                    }
                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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


    }
    protected  void registerEMClient(final String userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(userId, "123456");
                    AppManager.getAppManager().finishActivity();
                } catch (final EaseMobException e) {
                    //注册失败
                    int errorCode = e.getErrorCode();
                    if (errorCode == EMError.NONETWORK_ERROR) {
                        Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                        Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
                    } else if (errorCode == EMError.UNAUTHORIZED) {
                        Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).start();
    }
}
