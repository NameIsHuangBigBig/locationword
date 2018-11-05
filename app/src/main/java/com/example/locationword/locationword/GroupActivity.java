package com.example.locationword.locationword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    TextView t_add;
    ImageView i_information,i_mine;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_layout);

        t_add = (TextView)findViewById(R.id.tv_add);
        i_information = (ImageView)findViewById(R.id.img_information);
        i_mine = (ImageView)findViewById(R.id.img_mine);

        t_add.setOnClickListener(this);
        i_information.setOnClickListener(this);
        i_mine.setOnClickListener(this);
    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.tv_add:
                //点击＋号,添加群号
                break;
            case R.id.img_information:
                //跳转到信息界面
                break;
            case R.id.img_mine:
                //跳转到我的界面
                Intent intent = new Intent(this,MineActivity.class);
                startActivity(intent);
                break;
        }
    }
}
