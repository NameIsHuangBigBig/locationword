package com.example.locationword.locationword.event;

public class MapEvent {
    boolean close ;
    public MapEvent(boolean close) {
        this.close=close;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}
