package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatGroupActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView i_group_return,i_group_head;
    TextView t_group_add;
    EditText e_group_name,e_group_synopsis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_group_layout);

        i_group_return = (ImageView)findViewById(R.id.img_gruop_return);
        i_group_head = (ImageView)findViewById(R.id.img_group_head);

        t_group_add = (TextView)findViewById(R.id.tv_group_add);

        e_group_name = (EditText)findViewById(R.id.ed_group_name);
        e_group_synopsis = (EditText)findViewById(R.id.ed_group_synopsis);

        i_group_return.setOnClickListener(this);
        i_group_head.setOnClickListener(this);
        t_group_add.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.img_gruop_return:
                //点击返回
                break;
            case R.id.img_group_head:
                //点击更换头像
                break;
            case R.id.tv_group_add:
                //点击确定
                break;
        }
    }
}
