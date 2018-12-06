package com.example.locationword.locationword.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.Filter.GroupSearchFilter;
import com.example.locationword.locationword.GroupDetailActivity;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.tool.SkipUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;

public class SearchGroupAdapter extends BaseAdapter {
    List<EMGroupInfo> data;
    Context context;
    Handler handler = new Handler();
    GroupSearchFilter gsf ;
    public SearchGroupAdapter(List<EMGroupInfo>data, Context context){
        this.data=data;
        this.context=context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final EMGroupInfo itemData=data.get(i);
        Log.i("ADAPTER", itemData.getGroupName()+":::::i");
        if (view==null){
            view = View.inflate(context, R.layout.search_group_item,null);
        }
      //  ViewHolder holder =(ViewHolder) view.getTag();
       // if (holder==null){
           // holder=new ViewHolder();
        LinearLayout llMain = (LinearLayout) view.findViewById(R.id.ll_main);
        ImageView ivHead = (ImageView) view.findViewById(R.id.iv_head);
        final TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        final ImageView ivGroupMan = (ImageView) view.findViewById(R.id.iv_group_man);
        final TextView tvMan = (TextView) view.findViewById(R.id.tv_man);
        final TextView tvNum = (TextView) view.findViewById(R.id.tv_num);
        tvName.setText(itemData.getGroupName());
      //      view.setTag(holder);
      //  }
     //   final ViewHolder finalHolder = holder;
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMGroup group = null;
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(itemData.getGroupId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                if(group!=null){
                    final String manId = group.getOwner();//获取群主
                    HttpUtil.getInstence().doGet(API.getUserDetail+"?userId="+manId,handler,tvMan);
                    final int  member = group.getMemberCount();
                    //        http://localhost:8082/locationword/image/1head1.jpg
                   handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(context).load(API.BASEURL+"/image/"+manId+"head"+manId+".jpg")
                                    .placeholder(R.drawable.ease_default_avatar).into(ivGroupMan);
                            tvNum.setText("人数："+member+"人");
                        }
                    });

                }
            }
        }).start();
        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> m = new HashMap<>();
                m.put("GroupId",itemData.getGroupId());
                SkipUtils.skipActivityWithParameter(context, GroupDetailActivity.class,m);
            }
        });
        return view;
    }
    public static class ViewHolder{
        private ImageView ivHead;
        private TextView tvName;
        private ImageView ivGroupMan;
        private TextView tvMan;
        private TextView tvNum;
    }
    protected void setFilter(GroupSearchFilter gsf){
        this.gsf=gsf;
    }
}
