package com.example.locationword.locationword.tool;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;



import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus- on 2018/4/12.
 */

public class StringUtils {

    private static final String REGEX_PHONE_NUMBER="^((13[0-9])|(14[5,9])|(15[^4,\\D])|(17[0,3,5-8])|(18[0,5-9]))\\d{8}$";
    private static final String REGEX_DATE="^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
    public static String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    public static boolean checkEmail(String email){
        return email.matches(REGEX_EMAIL);
    }
    public static boolean isPhoneNumber(String number) {
        Pattern p = Pattern.compile(REGEX_PHONE_NUMBER);
        Matcher m = p.matcher(number);
        return m.matches();
    }
    private static final String REGEX_PASSWORD="[a-z0-9]{6,16}";
    public static boolean isVailedPassword(String password) {
        return password.matches(REGEX_PASSWORD);
    }



    public static boolean isVailedDate(String date) {
        return date.matches(REGEX_DATE);
    }
//返回字典排序后的字符串

    /*public static String sort(AjaxParams params){
        Log.i("StringUtils",params.getParamString());
        try {
            byte[] b = params.getParamString().getBytes("ISO-8859-1");
            String output = new String(b,"UTF-8");
            Log.i("StringUtils",output);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<String, String>();
        if (params.getParamString().length()!=0){
            String[]stringParams=params.getParamString().split("&");
            for (String str:stringParams){
                //Log.i("params", "post: "+  str);
                String[] secondStr=str.split("=");
                map.put(secondStr[0],secondStr[1]);
            }
        }
        map.put("appkey", Constant.appkey);
        Collection<String> keyset = map.keySet();
        System.out.println("timestrap="+System.currentTimeMillis());
        List list = new ArrayList<String>(keyset);
        Collections.sort(list);
//这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i) + "=" + map.get(list.get(i)));
            sb. append(list.get(i) + "=" + map.get(list.get(i))+"&");
        }
        String newStr = sb.toString();
        String str3 =newStr.substring(0, newStr.length() - 1);
        return str3;
    }*/
//返回字典排序后的字符串
    public static String sort(HashMap<String,Object> map){
        //map.put("appkey", Constant.appkey);
        List list = new ArrayList<String>();
        //遍历去掉file排序
        for (String key : map.keySet()) {
            Object value=map.get(key);
            if (!(value instanceof File)){
                list.add(key);
            }
        }
        //Collection<String> keyset = map.keySet();
        System.out.println("timestrap="+System.currentTimeMillis());
        Collections.sort(list);
//这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i) + "=" + map.get(list.get(i)));
            sb.append(list.get(i) + "=" + map.get(list.get(i))+"&");
        }
        String newStr = sb.toString();
        String str3 =newStr.substring(0, newStr.length() - 1);
        Log.i("sort",str3);
        return str3;
    }
    public static boolean isEmojiString(String s){//判断是否为表情包
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }



}
