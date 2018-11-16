package com.example.locationword.locationword;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.locationword.locationword.Adapter.InviteGroupAdapter;
import com.example.locationword.locationword.bean.User;
import com.example.locationword.locationword.event.GroupManagerEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.example.locationword.locationword.tool.StringUtil;
import com.google.gson.JsonArray;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class InviteGroupManActivity extends AppCompatActivity implements View.OnClickListener,SearchView.OnQueryTextListener{
    private ImageView back;
    private SearchView scv;
    private ListView listMan;
    private Context context=InviteGroupManActivity.this;
    private int page=1;
    protected String TAG="InviteGroupManActivity";
    private SpringView springView;
    private String userId;
    private InviteGroupAdapter iga;
    private TextView tvOk;
    private List<User> data=new ArrayList<>();
    private String GroupId;
    private String InGroupMan;
    private LoadingDialog loadingDialog ;
    private Handler handler= new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case 1000:
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          springView.onFinishFreshAndLoad();
                      }
                  });
                  String s= (String)msg.obj;
                  Log.i(TAG,"返回结果："+s);
                  JsonArray ja = JSONChange.StringToJsonArrary(s);
                  if(ja.size()>0){
                      analysis(ja);
                  }else{
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              ShowUtil.showText(context,"亲，已经全部加载完了！");
                          }
                      });
                  }

                  break;
              case 500:
                  String s1= (String)msg.obj;
                  Log.i(TAG,"返回结果："+s1);
                  JsonArray ja1 = JSONChange.StringToJsonArrary(s1);
                  if(ja1.size()>0){
                      searchanalysis(ja1);
                  }else{
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              ShowUtil.showText(context,"查无此人！");
                          }
                      });
                  }
                  break;
              case 1001:
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          ShowUtil.showText(context,"网络异常");
                      }
                  });
                  break;
          }
      }
    };

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.invite_group);
        initView();
        addListener();
        requestData();
    }
    public void initView(){
        loadingDialog = new LoadingDialog(context,"请稍候...");
        tvOk = (TextView) findViewById(R.id.tv_ok);
        userId = getSharedPreferences(Constant.logindata,MODE_PRIVATE).getString(Constant.UserId,"1");
        back = (ImageView) findViewById(R.id.back);
        scv = (SearchView) findViewById(R.id.scv);
        scv.setSubmitButtonEnabled(true);
        listMan = (ListView) findViewById(R.id.list_man);
        iga = new InviteGroupAdapter(data,context);
        listMan.setAdapter(iga);
        springView = (SpringView) findViewById(R.id.springview);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        GroupId = getIntent().getStringExtra("GroupId");
        InGroupMan = getIntent().getStringExtra("InGroupMan");

    }
    public void addListener(){
        back.setOnClickListener(this);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"刷新");
                page++;
                requestData();
            }
            @Override
            public void onLoadmore() {
                Log.i(TAG,"加载");
                page++;
                requestData();
            }
        });
        scv.setOnQueryTextListener(this);
        tvOk.setOnClickListener(this);
    }
    public void requestData(){
        Log.i(TAG,"PAGE:"+page);
        HttpUtil.getInstence().doGet(API.getSearchMan+"?userId="+userId+"&page="+page,handler);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                InviteGroupManActivity.this.finish();
                break;
            case R.id.tv_ok:

                if (iga.getIndexArray().size()>0){
                    loadingDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<Integer> dataindex=iga.getIndexArray();
                                String s[]=new String[dataindex.size()];
                                for(int i =0;i<dataindex.size();i++){
                                    Log.i(TAG,"SELECTMAN:"+data.get(dataindex.get(i)).getUserId());
                                    s[i]=String.valueOf(data.get(dataindex.get(i)).getUserId());
                                }
                                try {
                                    getGroupMan(s);
                                } catch (HyphenateException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShowUtil.showText(context,"服务器异常！");
                                            loadingDialog.close();
                                        }
                                    });

                                    e.printStackTrace();
                                }
                        }
                    }).start();
                }else{
                    ShowUtil.showText(context,"请勾选成员!");
                }

                break;
        }
    }
    private void getGroupMan(String s[]) throws HyphenateException {
        EMGroup group = null;

        try {
            group = EMClient.getInstance().groupManager().getGroupFromServer(GroupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        String UserId = getSharedPreferences(Constant.logindata,MODE_PRIVATE).getString(Constant.UserId,"");
        if(UserId.equals(group.getOwner())){
            EMClient.getInstance().groupManager().addUsersToGroup(GroupId,s);//需异步处理
        }else{
            EMClient.getInstance().groupManager().inviteUser(GroupId, s, null);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.close();
                InviteGroupManActivity.this.finish();
                EventBus.getDefault().post(new GroupManagerEvent("inviteman","update"));
                ShowUtil.showText(context,"邀请成功");
            }
        });

    }
    private void searchanalysis(JsonArray ja){
        data.clear();
        List<Integer>dataarray = StringUtil.StringtoArrayList(InGroupMan);
        for (int i=0;i<ja.size();i++){
            Log.i(TAG,ja.get(i).getAsJsonObject().get("RealName").getAsString());
            String UserId=ja.get(i).getAsJsonObject().get("UserId").getAsString();
            String NickName=ja.get(i).getAsJsonObject().get("NickName").getAsString();
            String RealName=ja.get(i).getAsJsonObject().get("RealName").getAsString();
            String UserAvarl=ja.get(i).getAsJsonObject().get("UserAvarl").getAsString();
            String UserSex = ja.get(i).getAsJsonObject().get("UserSex").getAsString();
            String UserPhone = ja.get(i).getAsJsonObject().get("UserPhone").getAsString();
            User u =new User(UserId,UserPhone,NickName,RealName,UserAvarl,UserSex);
            for(int q=0;q<dataarray.size();q++){
                if(ja.get(i).getAsJsonObject().get("UserId").getAsString().equals(String.valueOf(dataarray.get(q)))){
                    u.setJointhisGroup(true);
                    break;
                }
            }
            data.add(u);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iga.notifyDataSetChanged();
            }
        });
    }
    private void analysis(JsonArray ja){
        Log.i(TAG,InGroupMan);
        List<Integer>dataarray = StringUtil.StringtoArrayList(InGroupMan);
        for (int i=0;i<ja.size();i++){
            boolean b= true;
            for(int q=0;q<dataarray.size();q++){
                if(ja.get(i).getAsJsonObject().get("UserId").getAsString().equals(String.valueOf(dataarray.get(q)))){
                  //  Log.i(TAG,"NUM:"+String.valueOf(dataarray.get(q)));
                    b=false;
                    break;
                }
            }
            if(!b){
                continue;
            }
            Log.i(TAG,ja.get(i).getAsJsonObject().get("RealName").getAsString());
            String UserId=ja.get(i).getAsJsonObject().get("UserId").getAsString();
            String NickName=ja.get(i).getAsJsonObject().get("NickName").getAsString();
            String RealName=ja.get(i).getAsJsonObject().get("RealName").getAsString();
            String UserAvarl=ja.get(i).getAsJsonObject().get("UserAvarl").getAsString();
            String UserSex = ja.get(i).getAsJsonObject().get("UserSex").getAsString();
            String UserPhone = ja.get(i).getAsJsonObject().get("UserPhone").getAsString();
            User u =new User(UserId,UserPhone,NickName,RealName,UserAvarl,UserSex);

            data.add(u);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iga.notifyDataSetChanged();
            }
        });

    }
    public void onDestroy(){
        super.onDestroy();
        loadingDialog.close();
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        HttpUtil.getInstence().doGet(API.SearchMantoInvite+"?userName="+s,handler,500);
        return true;
    }
}
