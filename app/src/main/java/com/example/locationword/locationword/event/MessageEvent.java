package com.example.locationword.locationword.event;

public class MessageEvent {
    String s;
    String type;
    public MessageEvent(String s,String type) {
        this.s = s;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public String getS() {
        return s;
    }
}
