package com.hyphenate.easeui;

/**
 * Created by Administrator on 2017/10/16.
 */

public class API {
    /**
     * 基本路径
     */
    public static final String BASEURL = "http://172.17.146.156:8082/locationword/";
    //获取昵称或者头像
    public static final  String getUserDetail = BASEURL+"/User/getUserDetail";
    //获取系统消息及群推送
    public static final  String getSystemMessage = BASEURL+"/JPush/getSystemMessage";
    //修改群推送状态 0已取消  1 以同意， -1 未处理
    public static final  String changeMessageStatus = BASEURL+"/JPush/changeMessageStatus";
}


















































