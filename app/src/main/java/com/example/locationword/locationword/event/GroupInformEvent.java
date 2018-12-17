package com.example.locationword.locationword.event;

public class GroupInformEvent {
    String content;
    public GroupInformEvent(String content){
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
