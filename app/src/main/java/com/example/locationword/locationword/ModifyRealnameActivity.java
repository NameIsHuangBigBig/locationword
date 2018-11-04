package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyRealnameActivity extends AppCompatActivity implements View.OnClickListener{

    EditText e_modify_realname;
    Button b_modify_realname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_realname);

        e_modify_realname = (EditText)findViewById(R.id.ed_modify_realname);
        b_modify_realname = (Button)findViewById(R.id.btn_modify_realname);

        b_modify_realname.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_modify_realname:
                //确定修改真实姓名
                break;
        }
    }
}
