package com.example.locationword.locationword.event;

public class GroupManagerEvent {
    String result;
    String content;
    public GroupManagerEvent(String s,String content) {
        this.result=s;
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
