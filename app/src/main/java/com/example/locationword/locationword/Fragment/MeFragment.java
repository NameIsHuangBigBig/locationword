package com.example.locationword.locationword.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.SetActivity;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.google.gson.JsonObject;
import com.hyphenate.easeui.API;

public class MeFragment extends Fragment implements View.OnClickListener{
    private ImageView imgHead;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvRealname;
    private TextView tvSex;
    private LinearLayout shez;
    private LinearLayout realnameLayout;
    private LinearLayout sexLayout;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    final JsonObject jo =JSONChange.StringToJsonObject(result);
                    analysis(jo);
                    break;
                case 1001:
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(getContext(),"网络异常！");
                        }
                    });
                    break;
            }
        }
    };


    public View onCreateView(LayoutInflater layoutinflater, ViewGroup vg, Bundle bundle){
        View v= View.inflate(getContext(), R.layout.me_fragment,null);
        initView(v);
        addListener();
        requestData();
        return v;
    }
    protected void addListener(){
        shez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkipUtils.skipActivity(getContext(),SetActivity.class);
            }
        });
      //  tvSex.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        realnameLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
    }
    protected void requestData(){
        HttpUtil.getInstence().doGet(API.getUserDetail+"?userId="+getContext().getSharedPreferences(Constant.logindata, Context.MODE_PRIVATE)
        .getString(Constant.UserId,""),handler);
    }
    protected void initView(View v){
        imgHead = (ImageView) v.findViewById(R.id.img_head);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvPhone = (TextView) v.findViewById(R.id.tv_phone);
        tvRealname = (TextView) v.findViewById(R.id.tv_realname);
        tvSex = (TextView) v.findViewById(R.id.tv_sex);
        shez = (LinearLayout) v.findViewById(R.id.shez);
        realnameLayout = (LinearLayout) v.findViewById(R.id.realname_layout);
        sexLayout = (LinearLayout) v.findViewById(R.id.sex_layout);
    }
    protected void analysis(final JsonObject jo){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("mefargment",API.BASEURL+jo.get("UserAvarl").getAsString());
                Glide.with(getContext()).load(API.BASEURL+jo.get("UserAvarl").getAsString()).placeholder(R.drawable.groupmanager_icon_one).into(imgHead);
                tvName.setText(jo.get("NickName").getAsString());
                tvPhone.setText(jo.get("UserPhone").getAsString());
                tvRealname .setText(jo.get("RealName").getAsString());
                tvSex .setText(jo.get("UserSex").getAsString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_name:

                break;
            case R.id.tv_phone:
                break;
            case R.id.realname_layout:
                break;
            case R.id.sex_layout:
                final AlertDialog al =new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(new String[]{"男", "女"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShowUtil.showText(getContext(),"修改成功！");
                               // al.dismiss();
                            }
                        }).create();
                al.show();

                break;
        }
    }
}
