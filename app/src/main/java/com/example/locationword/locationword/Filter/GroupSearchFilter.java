package com.example.locationword.locationword.Filter;

import android.util.Log;
import android.widget.Filter;

import com.example.locationword.locationword.event.GroupSearchEvent;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class GroupSearchFilter extends Filter {
    List<EMGroupInfo> data ;
    List<EMGroupInfo> resultData;
    public GroupSearchFilter(List<EMGroupInfo> data){
        this.data=data;
    }
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults fr = new FilterResults();
        String content=charSequence.toString();
        List<EMGroupInfo> da=new ArrayList<>();
        for (int i =0; i<data.size();i++){
            if(data.get(i).getGroupName().startsWith(content)){
                da.add(data.get(i));
            }
        }
        fr.count=da.size();
        fr.values=da;
        return fr;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        Log.i("FILTER","C:"+charSequence.toString());
        Log.i("FILTER","fr:"+filterResults.count);
        resultData=(List<EMGroupInfo>) filterResults.values;
        for (int i=0;i<resultData.size();i++){
            Log.i("FILTER","RESULT:"+resultData.get(i).getGroupName());
        }
        EventBus.getDefault().post(new GroupSearchEvent());
    }
    public  List<EMGroupInfo> getResultData(){
        return resultData;
    }
}
