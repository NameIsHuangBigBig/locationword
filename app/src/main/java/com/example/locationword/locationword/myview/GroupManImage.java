package com.example.locationword.locationword.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locationword.locationword.R;

public class GroupManImage extends LinearLayout implements View.OnClickListener{
    Context context;
    private ImageView ivHead;
    private TextView tvNickname;
    private LinearLayout llMain;
    private LinearLayout llBg;
    private ClickListener clickListener;
    private int index;
    public GroupManImage(Context context) {
        super(context);
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.gourp_manager_man_layout,this);
        iniView();
        addListener();
    }
    public GroupManImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.gourp_manager_man_layout,this);
        iniView();
        addListener();
    }
    protected void iniView(){
        ivHead = (ImageView) findViewById(R.id.iv_head);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        llMain = (LinearLayout) findViewById(R.id.ll_main);
        llBg = (LinearLayout) findViewById(R.id.ll_bg);

    }
    protected void addListener(){
        llMain.setOnClickListener(this);
    }
    public void setIndex(int i ){
        index=i;
    }
    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.clickLayout(index);
    }

    public interface ClickListener{
        void clickLayout(int Id);
    }
    public void isMan(){
        llBg.setBackgroundColor(Color.RED);
    }
    public void noIsMan(){
        llBg.setBackgroundColor(Color.WHITE);
    }
    public void setImagerView(String avatrl){
        Log.i("image",avatrl);
        Glide.with(context).load(avatrl).placeholder(R.drawable.groupmanager_icon_one).into(ivHead);
    }
    public void setTvNickname(String s){
        tvNickname.setText(s);
    }
}
