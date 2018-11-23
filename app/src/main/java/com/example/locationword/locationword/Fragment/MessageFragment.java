package com.example.locationword.locationword.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationword.locationword.ChatActivity;
import com.example.locationword.locationword.MainActivity;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.event.MessageUpdateEvent;
import com.example.locationword.locationword.tool.Constant;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MessageFragment extends Fragment {
    EaseConversationListFragment conversationListFragment;
    EMMessageListener msgListener;
    public View onCreateView(LayoutInflater layoutinflater, ViewGroup vg, Bundle bundle){
        View v= View.inflate(getContext(), R.layout.message_fragment,null);
        iniView();
        addListenr();
        return v;
    }

    public void addListenr(){

         msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                conversationListFragment.refresh();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    public void iniView(){
        conversationListFragment = new EaseConversationListFragment();
       // conversationListFragment.refresh();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                List<EMMessage> s=conversation.getAllMessages();

                EMGroup group = EMClient.getInstance().groupManager().getGroup(conversation.conversationId());
               if (group != null){
                 //  Log.i("ckucj","group");
                   startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId())
                           .putExtra(Constant.EaseGroupId,conversation.conversationId())
                           .putExtra(Constant.EaseChattype, "Group"));
               }else{
                  // Log.i("ckucj","single"+EaseConstant.CHATTYPE_SINGLE+"\ts\t"+Constant.EaseChattype);
                   startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId())
                           .putExtra(Constant.EaseChattype, "Single")
                           .putExtra(Constant.EaseGroupId,conversation.conversationId()));

               }
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, conversationListFragment).commit();
    }

    public void onCreate(Bundle b) {
        super.onCreate(b);
        if (EventBus.getDefault() != null) {
            EventBus.getDefault().register(this);//订阅
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EventBus.getDefault().unregister(this);//订阅
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(MessageUpdateEvent event) {
        Log.i("event","UPDATEMESSAGE");
        conversationListFragment.refresh();
    }
}
