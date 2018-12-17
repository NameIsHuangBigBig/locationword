package com.example.locationword.locationword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.locationword.locationword.Fragment.MeFragment;

public class ModifyNameActivity extends AppCompatActivity implements View.OnClickListener{

    EditText e_modify_name;//我的界面的更改姓名的编辑框
    Button b_modify_name;//我的界面的更改姓名的确定键
    ImageView iv_modify_name;//我的界面的更改姓名的返回键

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);

        e_modify_name = (EditText)findViewById(R.id.ed_modify_name);
        b_modify_name = (Button)findViewById(R.id.btn_modify_name);
        iv_modify_name = (ImageView)findViewById(R.id.name_back_mine) ;

        b_modify_name.setOnClickListener(this);
        iv_modify_name.setOnClickListener(this);
    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_modify_name:
                if ( e_modify_name.getText().toString()==null || e_modify_name.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"昵称不可为空",Toast.LENGTH_SHORT).show();
                }else{
                    //确定修改昵称
                    Intent intent = getIntent();
                    intent.putExtra("text", e_modify_name.getText().toString());
                    setResult(1, intent);
                    finish();
                }
                break;
            case R.id.name_back_mine:
                finish();
                break;
        }
    }

}
