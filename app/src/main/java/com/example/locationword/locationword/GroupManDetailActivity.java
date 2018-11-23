package com.example.locationword.locationword;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;
import com.hyphenate.easeui.API;

import java.util.HashMap;

public class GroupManDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgHeadPersondetail;
    private TextView tvNamePersondetail;
    private TextView tvPhonePersondetail;
    private TextView tvRealname;
    private ImageView ivBack;
    private TextView tvSex;
    private Button downLayoutPersondetail;
    private String UserId;
    private Context context=GroupManDetailActivity.this;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    JsonObject jo =JSONChange.StringToJsonObject(result);
                    analysisData(jo);
                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context,"网络异常!");
                        }
                    });
                    break;
            }
        }
    };
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.personnaldetail_activity);
        initView();
        addListener();
        requestData();
    }
    protected void initView(){
        ivBack = (ImageView) findViewById(R.id.iv_back);
        UserId = getIntent().getStringExtra("UserId");
        imgHeadPersondetail = (ImageView) findViewById(R.id.img_head_persondetail);
        tvNamePersondetail = (TextView) findViewById(R.id.tv_name_persondetail);
        tvPhonePersondetail = (TextView) findViewById(R.id.tv_phone_persondetail);
        tvRealname = (TextView) findViewById(R.id.tv_realname);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        downLayoutPersondetail = (Button) findViewById(R.id.down_layout_persondetail);
    }
    protected void requestData(){
        HttpUtil.getInstence().doGet(API.getUserDetail+"?userId="+UserId,handler);
    }
    protected void addListener(){
        ivBack.setOnClickListener(this);
        downLayoutPersondetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.down_layout_persondetail:
                HashMap<String,Object> map = new HashMap<>();
                map.put(Constant.EaseGroupId,UserId);
                map.put(Constant.EaseChattype,"Single");
                SkipUtils.skipActivityWithParameter(context,ChatActivity.class,map);
                break;
            case R.id.iv_back:
                GroupManDetailActivity.this.finish();
                break;
        }
    }
    protected void analysisData(final JsonObject jo){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(context).load(API.BASEURL+jo.get("UserAvarl").getAsString())
                        .placeholder(R.drawable.groupmanager_icon_one).into(imgHeadPersondetail);
                tvNamePersondetail.setText(jo.get("NickName").getAsString());
                tvPhonePersondetail.setText(jo.get("UserPhone").getAsString());
                tvRealname.setText(jo.get("RealName").getAsString());
                tvSex.setText(jo.get("UserSex").getAsString());
            }
        });
    }
}
