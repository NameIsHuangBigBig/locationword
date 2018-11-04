package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyNameActivity extends AppCompatActivity implements View.OnClickListener{

    EditText e_modify_name;
    Button b_modify_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);

        e_modify_name = (EditText)findViewById(R.id.ed_modify_name);
        b_modify_name = (Button)findViewById(R.id.btn_modify_name);

        b_modify_name.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_modify_name:
                //确定修改昵称
                break;
        }
    }
}
