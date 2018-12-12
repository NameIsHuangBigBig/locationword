package com.example.locationword.locationword;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.locationword.locationword.Adapter.ChangeOwnerAdapter;
import com.example.locationword.locationword.Adapter.DeleteGroupManAdapter;
import com.example.locationword.locationword.bean.User;
import com.example.locationword.locationword.event.GroupManagerEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.StringUtil;
import com.google.gson.JsonArray;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteGroupManActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ImageView back;
    private TextView tvFinish;
    private ListView lvMain;
    private String GroupId;
    private String GroupName;
    private String userId;
    private String nowOwner;
    private String InGroupMan;
    private String newOwnerId;
    private List<User> data=new ArrayList<>();
    private String TAG="ChangeGroupOwnerActivity";
    private Context context=DeleteGroupManActivity.this;
    private DeleteGroupManAdapter dgm;
    private LoadingDialog loadingDialog;
    private List<String> DeleteMan = new ArrayList<>();
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    Log.i(TAG,"返回结果："+result);
                    final JsonArray ja= JSONChange.StringToJsonArrary(result);
                    analysis(ja);
                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context,"网络异常");
                        }
                    });
                    break;
                case 500:
                    String resultts = (String)msg.obj;
                    Log.i("tuis","返回结果："+resultts);

                    break;
            }
        }
    };
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.deletegroup_man_activity);
        initView();
        addListener();
        requestData();
    }
    public void requestData(){
        Map<String,String> m=new HashMap<String,String>();
        m.put("userIdarray",InGroupMan);
        HttpUtil.getInstence().doPost(API.getGroupUser,m,handler);
    }
    public void initView(){
        GroupName = getIntent().getStringExtra("GroupName");
        userId = getIntent().getStringExtra("userId");
        loadingDialog = new LoadingDialog(context,"移除中...");
        back = (ImageView) findViewById(R.id.back);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        lvMain = (ListView) findViewById(R.id.lv_main);
        dgm = new DeleteGroupManAdapter(data,context);
        lvMain.setAdapter(dgm);
        GroupId = getIntent().getStringExtra("GroupId");
        nowOwner = getIntent().getStringExtra("nowOwner");
        InGroupMan = getIntent().getStringExtra("InGroupMan");
    }

    private void analysis(JsonArray ja){
        for (int i=0;i<ja.size();i++) {
            String UserId=ja.get(i).getAsJsonObject().get("UserId").getAsString();
            String NickName=ja.get(i).getAsJsonObject().get("NickName").getAsString();
            String RealName=ja.get(i).getAsJsonObject().get("RealName").getAsString();
            String UserAvarl=ja.get(i).getAsJsonObject().get("UserAvarl").getAsString();
            String UserSex = ja.get(i).getAsJsonObject().get("UserSex").getAsString();
            String UserPhone = ja.get(i).getAsJsonObject().get("UserPhone").getAsString();
            User u =new User(UserId,UserPhone,NickName,RealName,UserAvarl,UserSex);
            if (UserId.equals(nowOwner)){
                u.setOwner(true);
            }
            data.add(u);
        }

    }
    public void addListener(){
        back.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        lvMain.setOnItemClickListener(this);
    }
    public void deleteman(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer deleteAr = new StringBuffer();
                try {

                    deleteAr.append("[");
                    Log.i(TAG,GroupId+"/delete/"+newOwnerId);
                    for (int i =0;i<DeleteMan.size();i++){
                        if (i!=(DeleteMan.size()-1)){
                            deleteAr.append(DeleteMan.get(i)+",");
                        }else{
                            deleteAr.append(DeleteMan);
                        }
                        EMClient.getInstance().groupManager().removeUserFromGroup(GroupId, DeleteMan.get(i));//需异步处理
                    }
                    deleteAr.append("]");
                     } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("deleteAr",deleteAr.toString());
                        HashMap<String,String> map = new HashMap<>();
                        map.put("userId",userId);
                        map.put("receiverIdArray",deleteAr.toString());
                        map.put("GroupName",GroupName);
                        map.put("type","2");
                        HttpUtil.getInstence().doPost(API.userJPush,map,handler,500);
                        EventBus.getDefault().post(new GroupManagerEvent("changeman",""));
                        DeleteGroupManActivity.this.finish();
                        ShowUtil.showText(context,"移除成功!");
                        loadingDialog.close();
                    }
                });
            }
        }).start();

    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                DeleteGroupManActivity.this.finish();
                break;
            case R.id.tv_finish:
                new AlertDialog.Builder(context)
                        .setTitle("确定后将移除成员")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadingDialog.show();
                                deleteman();
                            }
                        })
                        .create().show();

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG,"index"+i);
        if(data.get(i).isSelectDelete()){
            data.get(i).setSelectDelete(false);
            DeleteMan.remove(data.get(i).getUserId());
        }else{
            data.get(i).setSelectDelete(true);
            DeleteMan.add(data.get(i).getUserId());
        }
        dgm.notifyDataSetChanged();
    }
}
