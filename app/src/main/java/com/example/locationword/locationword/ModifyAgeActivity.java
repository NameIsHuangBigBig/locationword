package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyAgeActivity extends AppCompatActivity implements View.OnClickListener{

    EditText e_modify_age;
    Button b_modify_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_age);

        e_modify_age = (EditText)findViewById(R.id.ed_modify_age);
        b_modify_age = (Button)findViewById(R.id.btn_modify_age);

        b_modify_age.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_modify_age:
                //确定修改年龄
                break;
        }
    }
}
