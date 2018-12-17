package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.locationword.locationword.event.GroupInformEvent;
import com.example.locationword.locationword.event.GroupManagerEvent;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.google.gson.JsonArray;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_GROUP;

public class SendGroupInformActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private TextView tvOk;
    private EditText etContent;
    private String GroupId;
    private String InGroupMan;
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.send_group_inform_);
        initView();
        addListener();
    }
    protected void initView(){
        GroupId=getIntent().getStringExtra("GroupId");
        InGroupMan=getIntent().getStringExtra("InGroupMan");
        back = (ImageView) findViewById(R.id.back);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        etContent = (EditText) findViewById(R.id.et_content);

    }
    protected void addListener(){
        back.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                SendGroupInformActivity.this.finish();
                break;
            case R.id.tv_ok:
                if (etContent.getText().toString().equals("")){
                    ShowUtil.showText(SendGroupInformActivity.this,"亲，你未输入内容！");
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().updateGroupAnnouncement(GroupId, etContent.getText().toString());
                                JsonArray ja =JSONChange.StringToJsonArrary(InGroupMan);
                                for (int i =0;i<ja.size();i++){
                                    EMMessage message = EMMessage.createTxtSendMessage(etContent.getText().toString(), ja.get(i).getAsString());
                                    EMClient.getInstance().chatManager().sendMessage(message);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(new GroupManagerEvent("inform","inform"));

                                         ShowUtil.showText(SendGroupInformActivity.this,"发布成功！");
                                        SendGroupInformActivity.this.finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShowUtil.showText(SendGroupInformActivity.this,"发布失败！服务器异常");
                                    }
                                });
                                }
                        }
                    }).start();


                    //EventBus.getDefault().post(new GroupInformEvent(etContent.getText().toString()));
                }
                break;
        }
    }
}
