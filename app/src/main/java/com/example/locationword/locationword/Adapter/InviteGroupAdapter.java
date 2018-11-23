package com.example.locationword.locationword.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.bean.User;
import com.example.locationword.locationword.http.API;

import java.util.ArrayList;
import java.util.List;

public class InviteGroupAdapter extends BaseAdapter {
    List<User> data = new ArrayList<>();
    Context context ;
    List<Integer> indexArray = new ArrayList<>();
    public InviteGroupAdapter(List<User> data,Context context){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        if (data!=null){
            return data.size();
        }
        return 0;
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
        if (view == null){
            view = View.inflate(context, R.layout.invite_group_item,null);
        }

        ImageView ivGroupmanagerItemOne = (ImageView)view. findViewById(R.id.iv_groupmanager_item_one);
        TextView tvGroupmanagerItemOne = (TextView) view.findViewById(R.id.tv_groupmanager_item_one);
        CheckBox rb = (CheckBox)view.findViewById(R.id.rb);
        RelativeLayout rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
        final User itemdata=data.get(i);
        Glide.with(context).load(API.BASEURL+itemdata.getUserAvarl())
                .placeholder(R.drawable.groupmanager_icon_one).into(ivGroupmanagerItemOne);
        tvGroupmanagerItemOne.setText(itemdata.getNickName());
        if (itemdata.getisJointhisGroup()){
            rb.setVisibility(View.GONE);
            rlMain.setBackgroundColor(Color.rgb(229,229,229));
        }else{
            rb.setVisibility(View.VISIBLE);
            rlMain.setBackgroundColor(Color.WHITE);
        }
        rb.setChecked(false);
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    indexArray.add(i);
                }else{
                    Integer index=new Integer(i);
                    indexArray.remove(index);
                }
                for (int q=0;q<indexArray.size();q++){
                    Log.i("InviteGroupA",indexArray.get(q)+"");
                }
            }
        });

        return view;
    }
    public List<Integer> getIndexArray(){
        return this.indexArray;
    }
}
