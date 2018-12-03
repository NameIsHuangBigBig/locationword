package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class SystemMessageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgGruopReturn;
    private ListView lvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initView();
        addListener();
    }
    protected void addListener(){
        imgGruopReturn.setOnClickListener(this);
    }
    protected void initView(){
        imgGruopReturn = (ImageView) findViewById(R.id.img_gruop_return);
        lvMessage = (ListView) findViewById(R.id.lv_message);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_gruop_return:
                SystemMessageActivity.this.finish();
                break;
        }
    }
}
