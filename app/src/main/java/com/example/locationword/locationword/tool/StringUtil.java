package com.example.locationword.locationword.tool;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static List<Integer> StringtoArrayList(String s){
        String index=s.substring(1,s.length()-1);
        String sarray[]=index.split(",");
        List<Integer> data = new ArrayList<>();
        for(int i=0;i<sarray.length;i++){
            data.add(Integer.parseInt(sarray[i]));
        }
        return data;
    }
}
