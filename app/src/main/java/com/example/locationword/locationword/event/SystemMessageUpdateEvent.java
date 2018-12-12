package com.example.locationword.locationword.event;

import com.hyphenate.easeui.domain.SystemMessage;

public class SystemMessageUpdateEvent {
    boolean b;
    public SystemMessageUpdateEvent(boolean b ){
        this.b = b ;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }
}
