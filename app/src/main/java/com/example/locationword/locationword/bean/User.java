package com.example.locationword.locationword.bean;
public class User {
    String UserId;
    String UserPhone;
    String NickName;
    String RealName;
    String Password;
    String UserAvarl;
    String UserSex;
    int IsBreak;

    boolean isJointhisGroup=false;

    public User(String userId, String userPhone, String nickName, String realName, String password, String userAvarl,
                String UserSex,int isBreak) {
        super();
        UserId = userId;
        UserPhone = userPhone;
        NickName = nickName;
        RealName = realName;
        Password = password;
        UserAvarl = userAvarl;
        this.UserSex=UserSex;
        IsBreak = isBreak;
    }
    public User(String userId, String userPhone, String nickName, String realName, String userAvarl,
                String UserSex) {
        super();
        UserId = userId;
        UserPhone = userPhone;
        NickName = nickName;
        RealName = realName;
        UserAvarl = userAvarl;
        this.UserSex=UserSex;
    }
    public String getUserId() {
        return UserId;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
    public String getUserPhone() {
        return UserPhone;
    }
    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
    public String getNickName() {
        return NickName;
    }
    public void setNickName(String nickName) {
        NickName = nickName;
    }
    public String getRealName() {
        return RealName;
    }
    public void setRealName(String realName) {
        RealName = realName;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getUserAvarl() {
        return UserAvarl;
    }
    public void setUserAvarl(String userAvarl) {
        UserAvarl = userAvarl;
    }
    public int getIsBreak() {
        return IsBreak;
    }
    public void setIsBreak(int isBreak) {
        IsBreak = isBreak;
    }

    public boolean getisJointhisGroup() {
        return isJointhisGroup;
    }

    public void setJointhisGroup(boolean jointhisGroup) {
        isJointhisGroup = jointhisGroup;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String userSex) {
        UserSex = userSex;
    }
}
