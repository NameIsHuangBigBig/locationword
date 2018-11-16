package com.example.locationword.locationword.http;

/**
 * Created by Administrator on 2017/10/16.
 */

public class API {
    /**
     * 基本路径
     */
    public static final String BASEURL = "http://172.17.145.86:8082/locationword/";
    //检查登录
    public static final String checkLogin = BASEURL+"/Login/checklogin";
    //注册用户
    public static final String registerUser = BASEURL+"/Register/addRegister";
    //重置密码
    public static final String resetPassword = BASEURL+"/Login/resetPassword";
    //获取当前群组的用户信息
    public static final String getGroupUser = BASEURL+"/User/getGroupUser";
    //获取邀请进入群的用户列表
    public static final String getSearchMan = BASEURL+"/User/getSearchUsers";
    //模糊搜索用户列表
    public static final String SearchMantoInvite = BASEURL+"/User/selectUser";
}


















































