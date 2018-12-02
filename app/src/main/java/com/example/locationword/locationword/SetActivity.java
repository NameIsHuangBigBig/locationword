package com.example.locationword.locationword;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.locationword.locationword.app.AppManager;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.hyphenate.chat.EMClient;

public class SetActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private ImageView back;
    private Switch sLocation;
    private Switch sTuis;
    private Switch sLogin;
    private RelativeLayout rlChangePhone;
    private Button btnTuic;
    private SharedPreferences s;

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.set_activity);
        initView();
        addListener();
    }
    protected void addListener(){
        back.setOnClickListener(this);
        rlChangePhone.setOnClickListener(this);
        btnTuic.setOnClickListener(this);
        sLocation.setOnCheckedChangeListener(this);
        sTuis.setOnCheckedChangeListener(this);
        sLogin.setOnCheckedChangeListener(this);
    }
    public void initView(){
        back = (ImageView) findViewById(R.id.back);
        sLocation = (Switch) findViewById(R.id.s_location);
        sTuis = (Switch) findViewById(R.id.s_tuis);
        sLogin = (Switch) findViewById(R.id.s_login);
        rlChangePhone = (RelativeLayout) findViewById(R.id.rl_changePhone);
        btnTuic = (Button) findViewById(R.id.btn_tuic);
        s = getSharedPreferences(Constant.logindata,MODE_PRIVATE);
        if (s.getBoolean(Constant.autoLogin,true)){
            sLogin.setChecked(true);
        }else{
            sLogin.setChecked(false);
        }
        if (s.getBoolean(Constant.getLocation,true)){
            sLocation.setChecked(true);
        }else{
            sLocation.setChecked(false);
        }
        if (s.getBoolean(Constant.autoTuis,true)){
            sTuis.setChecked(true);
        }else{
            sTuis.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                SetActivity.this.finish();
                break;
            case R.id.btn_tuic:
                new AlertDialog.Builder(SetActivity.this)
                        .setTitle("确定退出此账号吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences s=getSharedPreferences(Constant.logindata,MODE_PRIVATE);
                                s.edit().putString(Constant.UserId,"").commit();

                                SetActivity.this.finish();
                                AppManager.getAppManager().finishActivity(MainActivity.class);
                                SkipUtils.skipActivity(SetActivity.this,LoginActivity.class);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create().show();

                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        ShowUtil.showText(SetActivity.this,"设置成功！");
        switch (compoundButton.getId()){

            case R.id.s_location:
                if (b){
                   s.edit().putBoolean(Constant.getLocation,true).commit();
                }else{
                    s.edit().putBoolean(Constant.getLocation,false).commit();
                }
                break;
            case R.id.s_tuis:
                if (b){
                    s.edit().putBoolean(Constant.autoTuis,true).commit();
                }else{
                    s.edit().putBoolean(Constant.autoTuis,false).commit();
                }
                break;
            case R.id.s_login:
                if (b){
                    s.edit().putBoolean(Constant.autoLogin,true).commit();
                }else{
                    s.edit().putBoolean(Constant.autoLogin,false).commit();
                }
                break;
        }
    }
}
