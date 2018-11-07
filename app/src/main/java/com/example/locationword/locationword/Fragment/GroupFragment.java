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
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;

import java.util.HashMap;
import java.util.Map;

public class GroupFragment extends Fragment {
    EaseContactListFragment contactListFragment;
    public View onCreateView(LayoutInflater layoutinflater, ViewGroup vg, Bundle bundle){
        View v= View.inflate(getContext(), R.layout.group_fragment,null);
        initView();
        return v;
    }
    public void initView(){
        contactListFragment= new EaseContactListFragment();
//需要设置联系人列表才能启动fragment
        Map<String,EaseUser> map = new HashMap<>();
        map.put("志愿者交流群",new EaseUser("志愿者交流群"));
        map.put("佛山志愿者交流群",new EaseUser("佛山志愿者交流群"));
        map.put("徒步志愿者交流群",new EaseUser("徒步志愿者交流群"));

        contactListFragment.setContactsMap(map);
//设置item点击事件
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(getContext(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_content, contactListFragment).commit();

    }

}
