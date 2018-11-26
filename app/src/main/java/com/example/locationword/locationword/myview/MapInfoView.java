package com.example.locationword.locationword.myview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.ChatActivity;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.Thread.GetLocationThread;
import com.example.locationword.locationword.event.MapEvent;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.google.gson.JsonObject;
import com.hyphenate.easeui.API;
import com.hyphenate.easeui.EaseConstant;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MapInfoView extends LinearLayout implements View.OnClickListener {
    private ImageView ivClose;
    private ImageView imgHeadPersondetail;
    private TextView tvNamePersondetail;
    private TextView tvPhonePersondetail;
    private ImageView ivPhone;
    private TextView tvRealname;
    private TextView tvSex;
    private Button downLayoutPersondetail;
    private String userId;
    private Context context;
    private Button btnBx;
    private Button btnQx;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    String result = (String) msg.obj;
                    JsonObject jo = JSONChange.StringToJsonObject(result);
                    analysis(jo);
                    break;
                case 1001:
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context, "网络异常");
                        }
                    });
                    break;
            }
        }
    };

    public MapInfoView(Context context, String userId) {
        super(context);
        this.context = context;
        this.userId = userId;
        LayoutInflater.from(context).inflate(R.layout.mapinfo_view, this);
        initView();
        addListener();
        requsetData();
        this.userId = userId;
    }

    public MapInfoView(Context context, String userId, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.userId = userId;
        LayoutInflater.from(context).inflate(R.layout.mapinfo_view, this);
        initView();
        addListener();
        requsetData();
        this.userId = userId;
    }

    protected void requsetData() {
        HttpUtil.getInstence().doGet(API.getUserDetail + "?userId=" + userId, handler);
    }

    public void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        imgHeadPersondetail = (ImageView) findViewById(R.id.img_head_persondetail);
        tvNamePersondetail = (TextView) findViewById(R.id.tv_name_persondetail);
        tvPhonePersondetail = (TextView) findViewById(R.id.tv_phone_persondetail);
        ivPhone = (ImageView) findViewById(R.id.iv_phone);
        tvRealname = (TextView) findViewById(R.id.tv_realname);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        downLayoutPersondetail = (Button) findViewById(R.id.down_layout_persondetail);
        btnBx = (Button) findViewById(R.id.btn_bx);
        btnQx = (Button) findViewById(R.id.btn_qx);
    }

    public void addListener() {
        ivClose.setOnClickListener(this);
        imgHeadPersondetail.setOnClickListener(this);
        ivPhone.setOnClickListener(this);
        downLayoutPersondetail.setOnClickListener(this);
        btnBx.setOnClickListener(this);
        btnQx.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                EventBus.getDefault().post(new MapEvent(true));
                break;
            case R.id.img_head_persondetail:
                break;
            case R.id.iv_phone:
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + tvPhonePersondetail.getText().toString());
                intent.setData(data);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
                break;
            case R.id.down_layout_persondetail:
                context.startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,userId)
                        .putExtra(Constant.EaseChattype, "Single")
                        .putExtra(Constant.EaseGroupId,userId));
                break;
            case R.id.btn_bx:
                break;
            case R.id.btn_qx:
                break;
        }

    }
    public void analysis(final JsonObject jo){
        handler.post(new Runnable() {
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
