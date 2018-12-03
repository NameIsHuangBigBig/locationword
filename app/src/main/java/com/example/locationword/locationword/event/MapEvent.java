package com.example.locationword.locationword.event;

public class MapEvent {
    String close;

    public MapEvent(String close) {
        this.close=close;
    }

    public String isClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}
