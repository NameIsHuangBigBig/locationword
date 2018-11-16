package com.example.locationword.locationword.event;

public class ChatActivityUpdateEvent {
    String result;
    public ChatActivityUpdateEvent(String s) {
        this.result=s;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
