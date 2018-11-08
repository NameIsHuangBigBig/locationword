package com.example.locationword.locationword.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.locationword.locationword.ChatActivity;
import com.example.locationword.locationword.MainActivity;
import com.example.locationword.locationword.R;
import com.example.locationword.locationword.event.GroupUpdateEvent;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupFragment extends Fragment {
    EaseContactListFragment contactListFragment;
    ProgressBar loading ;
    Map<String, EaseUser> m = new HashMap<String, EaseUser>();
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i("event","set");

                    loading.setVisibility(View.GONE);
                    contactListFragment.refresh();
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup vg, Bundle bundle) {
        View v = View.inflate(getContext(), R.layout.group_fragment, null);
        initView(v);
        getGroupList();
        return v;
    }

    public void initView(View v) {
        loading= v.findViewById(R.id.loading);

        contactListFragment = new EaseContactListFragment();
        contactListFragment.setContactsMap(m);
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_content, contactListFragment).commit();
        contactListFragment.setContactsMap(m);
    }

    public void getGroupList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    for (int i = 0; i < grouplist.size(); i++) {
                        EaseUser eu = new EaseUser(grouplist.get(i).getGroupName());
                        m.put(grouplist.get(i).getGroupId(), eu);
                    }
                    h.sendEmptyMessage(1);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void onCreate(Bundle b) {
        super.onCreate(b);
        if (EventBus.getDefault() != null) {
            EventBus.getDefault().register(this);//订阅
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//订阅
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(GroupUpdateEvent event) {
        Log.i("event","UPDATEGROUP");
        m.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    for (int i = 0; i < grouplist.size(); i++) {
                        EaseUser eu = new EaseUser(grouplist.get(i).getGroupName());
                        m.put(grouplist.get(i).getGroupId(), eu);
                        h.sendEmptyMessage(1);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
