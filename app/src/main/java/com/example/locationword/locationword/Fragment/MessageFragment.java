package com.example.locationword.locationword.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationword.locationword.ChatActivity;
import com.example.locationword.locationword.MainActivity;
import com.example.locationword.locationword.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

public class MessageFragment extends Fragment {
    EaseConversationListFragment conversationListFragment;
    public View onCreateView(LayoutInflater layoutinflater, ViewGroup vg, Bundle bundle){
        View v= View.inflate(getContext(), R.layout.message_fragment,null);
        iniView();
        return v;
    }
    public void iniView(){
        conversationListFragment = new EaseConversationListFragment();
       // conversationListFragment.refresh();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId()));
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, conversationListFragment).commit();
    }
}
