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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.bean.User;
import com.example.locationword.locationword.http.API;

import java.util.ArrayList;
import java.util.List;

public class DeleteGroupManAdapter extends BaseAdapter {
    List<User> data = new ArrayList<>();
    Context context ;
    List<Integer> indexArray = new ArrayList<>();
    List<RadioButton> rblist = new ArrayList<>();
    String newOwnerId="";
    public DeleteGroupManAdapter(List<User> data,Context context){
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

    public String getNewOwnerId(){
        return newOwnerId;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = View.inflate(context, R.layout.owner_manager_item,null);
        }
        ImageView ivGroupmanagerItemOne = (ImageView)view. findViewById(R.id.iv_groupmanager_item_one);
        TextView tvGroupmanagerItemOne = (TextView) view.findViewById(R.id.tv_groupmanager_item_one);
        RadioButton rb = (RadioButton)view.findViewById(R.id.rb);
        RelativeLayout rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
        final User itemdata=data.get(i);
        Glide.with(context).load(API.BASEURL+itemdata.getUserAvarl())
                .placeholder(R.drawable.groupmanager_icon_one).into(ivGroupmanagerItemOne);
        tvGroupmanagerItemOne.setText(itemdata.getNickName());
        rb.setClickable(false);
        if (itemdata.isSelectDelete()){
            rb.setChecked(true);
        }else{
            rb.setChecked(false);
        }
        if (itemdata.getisOwner()){
            rb.setVisibility(View.GONE);
            tvGroupmanagerItemOne.setText(itemdata.getNickName()+"(群主)");
        }else{
            rb.setVisibility(View.VISIBLE);
            tvGroupmanagerItemOne.setText(itemdata.getNickName());
        }

        return view;
    }
    public List<Integer> getIndexArray(){
        return this.indexArray;
    }
}
