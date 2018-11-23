package com.hyphenate.easeui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.easeui.ui.VideoCallActivity;
import com.hyphenate.easeui.ui.VoiceCallActivity;


public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 拨打方username
        String from = intent.getStringExtra("from");
        // call type
        String type = intent.getStringExtra("type");
        //跳转到通话页面
        if("video".equals(type)){ //video call
            context.startActivity(new Intent(context, VideoCallActivity.class).
                    putExtra("username", from)
                    .putExtra("nickname","来电")
                    .putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else{ //voice call
            context.startActivity(new Intent(context, VoiceCallActivity.class)
                    .putExtra("nickname","来电")
                    .putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}