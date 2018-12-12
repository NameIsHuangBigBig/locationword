package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.API;
import com.hyphenate.easeui.HttpUtil;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.SystemMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class SystemMessageAdapter extends BaseAdapter {
    List<SystemMessage> data ;
    Context context;
    Handler handler = new Handler(){};
    public SystemMessageAdapter(List<SystemMessage> data,Context context){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final SystemMessage viewData = data.get(i);
     //   if (view == null){
            if (viewData.getType().equals("0")||viewData.getType().equals("2")||viewData.getType().equals("3")
                    ||viewData.getType().equals("4")){
                view = View.inflate(context, R.layout.system_item,null);
                 ImageView systemItemHead;
                 TextView systemTv2;
                 TextView tvSystem;

                systemItemHead = (ImageView) view.findViewById(R.id.system_item_head);
                systemTv2 = (TextView) view.findViewById(R.id.system_tv_2);
                tvSystem = (TextView) view.findViewById(R.id.tv_system);

                if (i==0){
                    systemItemHead.setImageResource(R.drawable.location_logo);
                    tvSystem.setVisibility(View.VISIBLE);
                }else{
                    Glide.with(context).load(API.BASEURL+"/image/"+viewData.getSendId()+"head"+
                            viewData.getSendId()+".jpg").placeholder(R.drawable.ease_default_avatar).into(systemItemHead);

                    tvSystem.setVisibility(View.GONE);
                }
               systemTv2.setText(viewData.getContent());

            }else {
                view = View.inflate(context, R.layout.system_item_join, null);
                 ImageView systemItemHead;
                 TextView systemTv2;
                 final Button btnAgree;
                 final Button btnCancel;
                final TextView tvStatus;
                tvStatus = (TextView) view.findViewById(R.id.tv_status);
                systemItemHead = (ImageView) view.findViewById(R.id.system_item_head);
                systemTv2 = (TextView) view.findViewById(R.id.system_tv_2);
                btnAgree = (Button) view.findViewById(R.id.btn_agree);
                btnCancel = (Button) view.findViewById(R.id.btn_cancel);
                if (viewData.getStatus().equals("1")){
                    tvStatus.setText("已同意");
                    btnAgree.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                }else if(viewData.getStatus().equals("0")){
                    tvStatus.setText("已取消");
                    btnAgree.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                }else {
                    tvStatus.setVisibility(View.GONE);
                    btnAgree.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    final String groupId = viewData.getGroupId();
                    final String sendId = viewData.getSendId();
                    btnAgree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addGroup(groupId,sendId,viewData.getS_id(),viewData.getGroupName());
                            tvStatus.setText("已同意");
                            tvStatus.setVisibility(View.VISIBLE);
                            btnAgree.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                         //   addGroup(groupId);
                            tvStatus.setText("已取消");
                            tvStatus.setVisibility(View.VISIBLE);
                            btnAgree.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            HttpUtil.getInstence().doGet(API.changeMessageStatus+"?s_id="+viewData.getS_id()
                                    +"&status=0",handler);
                        }
                    });
                }
                Glide.with(context).load(API.BASEURL+"/image/"+viewData.getSendId()+"head"
                +viewData.getSendId()+".jpg").placeholder(R.drawable.ease_default_avatar).into(systemItemHead);

                systemTv2.setText(viewData.getContent());
            }
      //  }

        return view;
    }
    protected void addGroup(final String groupId,final String sendId,final String s_id,final String GroupName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //群主加人调用此方法
                try {
                    EMClient.getInstance().groupManager().addUsersToGroup(groupId, new String[]{sendId});//需异步处理
                    HttpUtil.getInstence().doGet(API.changeMessageStatus+"?s_id="+s_id
                            +"&status=1&GroupName="+GroupName+"&id="+sendId,handler);

                } catch (HyphenateException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"对方已加入此群！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
