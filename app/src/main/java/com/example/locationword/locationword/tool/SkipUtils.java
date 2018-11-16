package com.example.locationword.locationword.tool;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

public class SkipUtils {
    public static void skipActivity(Context context,Class cls){
        Intent intent=new Intent(context,cls);
        context.startActivity(intent);
    }
    public static void skipActivityWithParameter(Context context,Class cls,HashMap<String,Object> map){
        Intent intent=new Intent(context,cls);
        for (String key : map.keySet()) {
            Object value=map.get(key);
            if(value instanceof String){
                intent.putExtra(key,(String)value);
            }
            else if(value instanceof Integer){
                intent.putExtra(key,(int)value);
            }else if (value instanceof String[]){
                intent.putExtra(key,(String[]) value);

            }
        }
        context.startActivity(intent);
    }
}
