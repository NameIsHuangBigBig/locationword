package com.example.locationword.locationword;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.locationword.locationword.Adapter.SearchGroupAdapter;
import com.example.locationword.locationword.Filter.GroupSearchFilter;
import com.example.locationword.locationword.bean.Group;
import com.example.locationword.locationword.event.GroupSearchEvent;
import com.example.locationword.locationword.event.GroupUpdateEvent;
import com.example.locationword.locationword.myview.LoadingDialog;
import com.example.locationword.locationword.tool.ShowUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private SearchView scv;
    private SpringView springview;
    private ListView listMan;
    private String TAG = "SearchGroupActivity";
    private EMCursorResult<EMGroupInfo> result;
    private boolean istop=false;
    private SearchGroupAdapter sga;
    private String cur="";
    private GroupSearchFilter gsf;
    private LoadingDialog loadingDialog;
    private List<EMGroupInfo> groupCopyList= new ArrayList<>();
    private List<EMGroupInfo> groupsList=new ArrayList<>();

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.serach_group_activity);
        initView();
        addListener();
        getGroupData("0");
        getGroupCopyData();
    }
    protected void getGroupCopyData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMCursorResult<EMGroupInfo> result1= EMClient.getInstance().groupManager().getPublicGroupsFromServer(99999,"0");
                    groupCopyList=result1.getData();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gsf=new GroupSearchFilter(groupCopyList);
                            loadingDialog.close();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    protected void getGroupData(String cusror){
        Log.i("cur",cur+"\t"+cusror);
        if(cusror.equals("")){
            ShowUtil.showText(SearchGroupActivity.this,"亲，数据加载完毕咯！");
            springview.onFinishFreshAndLoad();
        }else{
            final String finalcusror = cusror;
            Log.i(TAG,"CURSOR："+cusror);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(10,finalcusror);
                        List<EMGroupInfo> da=result.getData();
                        for (int i =0;i<da.size();i++){
                            Log.i(TAG,"NAME"+da.get(i).getGroupName());
                        }
                        if (istop){
                            groupsList.addAll(0,result.getData());
                        }else{
                            groupsList.addAll(result.getData());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sga.notifyDataSetChanged();
                                springview.onFinishFreshAndLoad();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        cur=cusror;

        //获取公开群列表
//pageSize为要取到的群组的数量，cursor用于告诉服务器从哪里开始取

    }
    protected void initView(){
        loadingDialog = new LoadingDialog(SearchGroupActivity.this,"加载中...");
        loadingDialog.show();
        back = (ImageView) findViewById(R.id.back);
        scv = (SearchView) findViewById(R.id.scv);
        springview = (SpringView) findViewById(R.id.springview);
        springview.setHeader(new DefaultHeader(this));
        springview.setFooter(new DefaultFooter(this));
        listMan = (ListView) findViewById(R.id.list_man);
        sga = new SearchGroupAdapter(groupsList,SearchGroupActivity.this);
        listMan.setAdapter(sga);
       // listMan.setTextFilterEnabled(true);
    }
    protected void addListener(){
        back.setOnClickListener(this);
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"刷新");
                istop=true;
                String cursor = result.getCursor();

                Log.i(TAG,"refresh:"+cursor);
                getGroupData(cursor);

            }
            @Override
            public void onLoadmore() {
                istop=false;
                Log.i(TAG,"加载");
                String cursor = result.getCursor();
                Log.i(TAG,"refresh:"+cursor);
                getGroupData(cursor);
            }
        });
        scv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.equals("")){
                    groupsList.clear();
                    getGroupData("0");
                }else{
                    gsf.filter(s);
                }

                //refreshUI();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    groupsList.clear();
                    getGroupData("0");
                }else{
                    gsf.filter(s);
                }

               // refreshUI();
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                SearchGroupActivity.this.finish();
                break;
        }
    }

    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//订阅
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//订阅
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(GroupSearchEvent event) {
        Log.i("event","UPDATEGROUP");
        refreshUI();
    }
    protected void refreshUI(){
        groupsList.clear();
        groupsList.addAll(gsf.getResultData());
        for (int i=0;i<groupsList.size();i++){
            Log.i("Filter",groupsList.get(i).getGroupName());
        }
        sga.notifyDataSetChanged();
    }
}
