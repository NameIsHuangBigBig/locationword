package com.hyphenate.easeui.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hyphenate.easeui.API;
import com.hyphenate.easeui.HttpUtil;
import com.hyphenate.easeui.JSONChange;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.SystemMessageAdapter;
import com.hyphenate.easeui.domain.SystemMessage;
import com.hyphenate.easeui.utils.Constant;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SystemMessageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgGruopReturn;
    private ListView lvMessage;
    private SpringView springView;
    private Context context = SystemMessageActivity.this;
    private String TAG = "SystemMessageActivity";
    private String userId;
    private SystemMessageAdapter sma ;
    private List<SystemMessage> data = new ArrayList<>();
    private int page= 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    JsonArray ja  = JSONChange.StringToJsonArrary(result);
                    analysisData(ja);

                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"网络异常！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initView();
        addListener();
        requestData();
    }
    protected void requestData(){
        HttpUtil.getInstence().doGet(API.getSystemMessage+"?userId="+userId+"&page="+page,handler);
    }
    protected void addListener(){
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                page=1;
                requestData();
            }

            @Override
            public void onLoadmore() {
                page++;
                requestData();
            }
        });
        imgGruopReturn.setOnClickListener(this);
    }
    protected void initView(){
        userId = getSharedPreferences("Login",MODE_PRIVATE).getString("userid","");
        imgGruopReturn = (ImageView) findViewById(R.id.img_gruop_return);
        lvMessage = (ListView) findViewById(R.id.lv_message);
        sma = new SystemMessageAdapter(data,context);
        lvMessage.setAdapter(sma);
        springView = (SpringView) findViewById(R.id.springview);
        springView.setHeader(new DefaultFooter(context));
        springView.setFooter(new DefaultHeader(context));

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_gruop_return) {
            SystemMessageActivity.this.finish();

        }
    }
    protected void analysisData(JsonArray ja ){
        for (int i = 0 ; i<ja.size();i++){
            JsonObject jo = ja.get(i).getAsJsonObject();
            String Content = jo.get("Content").getAsString();
            String type= jo.get("type").getAsString();
            String GroupId="";
            String GroupName="";
            String sendId="";
            String receiverId="";
            String sendName="";
            String receiverName="";
            String status="";
            String s_id = "";
            if (type.equals("1")){
                 GroupId= jo.get("GroupId").getAsString();
                 GroupName= jo.get("GroupName").getAsString();
                 sendId= jo.get("sendId").getAsString();
                 receiverId= jo.get("receiverId").getAsString();
                 sendName= jo.get("sendName").getAsString();
                 receiverName= jo.get("receiverName").getAsString();
                status = jo.get("status").getAsString();
                s_id = jo.get("s_id").getAsString();
            }

            String time= jo.get("time").getAsString();
            SystemMessage sm = new SystemMessage(Content,type,GroupId,GroupName,sendId,receiverId,sendName,receiverName,time,status,s_id);
            data.add(sm);

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                springView.onFinishFreshAndLoad();
                Log.i(TAG,"size:"+data.size());
                sma.notifyDataSetChanged();
            }
        });

    }
}
