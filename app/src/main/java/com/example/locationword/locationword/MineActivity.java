package com.example.locationword.locationword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView i_head,i_information,i_group;
    TextView t_name,t_phone,t_realname,t_age,t_sex;
    LinearLayout layout_realname,layout_age,layout_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout);

        i_head = (ImageView)findViewById(R.id.img_head);
        i_information = (ImageView)findViewById(R.id.img_information);
        i_group = (ImageView)findViewById(R.id.img_group);

        t_name = (TextView)findViewById(R.id.tv_name);
        t_phone = (TextView)findViewById(R.id.tv_phone);
        t_realname = (TextView)findViewById(R.id.tv_realname);
        t_age = (TextView)findViewById(R.id.tv_age);
        t_sex = (TextView) findViewById(R.id.tv_sex);

        layout_realname = (LinearLayout)findViewById(R.id.realname_layout);
        layout_age = (LinearLayout)findViewById(R.id.age_layout);
        layout_sex = (LinearLayout)findViewById(R.id.sex_layout);

        i_head.setOnClickListener(this);
        t_name.setOnClickListener(this);
        t_phone.setOnClickListener(this);
        layout_realname.setOnClickListener(this);
        layout_age.setOnClickListener(this);
        layout_sex.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.img_head:
                //修改头像
                break;
            case R.id.img_information:
                //跳转到聊天页
                break;
            case R.id.img_group:
                //跳转到群组页
                Intent intent = new Intent(this,ModifyNameActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_name:
                //修改昵称
                Log.i("666","昵称");
                Intent intent1 = new Intent(this,ModifyNameActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_phone:
                //修改电话号码
                Log.i("666","号码");
                Intent intent2 = new Intent(this,ModifyPhoneActivity.class);
                startActivity(intent2);
                break;
            case R.id.realname_layout:
                //修改真实姓名
                Log.i("666","真实姓名");
                Intent intent3 = new Intent(this,ModifyRealnameActivity.class);
                startActivity(intent3);
                break;
            case R.id.age_layout:
                //修改年龄
                Log.i("666","年龄");
                Intent intent4 = new Intent(this,ModifyAgeActivity.class);
                startActivity(intent4);
                break;
            case R.id.sex_layout:
                //修改性别
                Log.i("666","性别");
                new AlertDialog.Builder(this).setTitle("请选择性别").setIcon(R.mipmap.ic_launcher).setSingleChoiceItems(new String[]{"男","女"},0,new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialogInterface,int which){
                }}).setPositiveButton("确定",null).show();
                break;
        }
    }
}
