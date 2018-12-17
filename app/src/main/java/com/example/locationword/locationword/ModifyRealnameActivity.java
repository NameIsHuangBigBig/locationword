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

public class ModifyRealnameActivity extends AppCompatActivity implements View.OnClickListener{

    EditText e_modify_realname;
    Button b_modify_realname;
    ImageView iv_modify_realname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_realname);

        e_modify_realname = (EditText)findViewById(R.id.ed_modify_realname);
        b_modify_realname = (Button)findViewById(R.id.btn_modify_realname);
        iv_modify_realname = findViewById(R.id.realname_back_mine);


        b_modify_realname.setOnClickListener(this);
        iv_modify_realname.setOnClickListener(this);

    }
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_modify_realname:
                //确定修改真实姓名
                if ( e_modify_realname.getText().toString()==null || e_modify_realname.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"真实姓名不可为空",Toast.LENGTH_SHORT).show();
                    Log.i("555","wos");
                }else{
                    Intent intent = getIntent();
                    intent.putExtra("text", e_modify_realname.getText().toString());
                    setResult(3, intent);
                    finish();
                }
                break
                        ;
            case R.id.realname_back_mine:
                finish();
                break;
        }
    }
}
