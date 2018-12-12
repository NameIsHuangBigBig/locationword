package com.hyphenate.easeui.domain;



public class SystemMessage {
// {
//	"Content": "黄大大申请加入群3232",
//	"type": "1",
//	"GroupId": "67094463971329",
//	"GroupName": "3232",
//	"sendId": "35",
//	"receiverId": "40",
//	"sendName": "黄大大",
//	"receiverName": "虎牙小海豚",
//	"time": "123",
 //   "status":"1"
//},
    String Content;
    String type;
    String GroupId;
    String GroupName;
    String sendId;
    String receiverId;
    String sendName;
    String receiverName;
    String time;
    String status;
    String s_id;
    public SystemMessage(String content, String type, String groupId, String groupName, String sendId, String receiverId, String sendName, String receiverName, String time,String status,String s_id) {
        Content = content;
        this.type = type;
        GroupId = groupId;
        GroupName = groupName;
        this.sendId = sendId;
        this.receiverId = receiverId;
        this.sendName = sendName;
        this.receiverName = receiverName;
        this.time = time;
        this.status = status;
        this.s_id = s_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }
}
