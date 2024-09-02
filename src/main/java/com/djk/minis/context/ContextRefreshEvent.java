package com.djk.minis.context;

import com.djk.minis.beans.ApplicationEvent;

public class ContextRefreshEvent extends ApplicationEvent{

    private static final long serialVersionUID = 1L;

    public ContextRefreshEvent(Object arg0) {
        super(arg0);
    }

    @Override
    public String toString() {
        return "ContextRefreshEvent{" +
                "source=" + source +
                '}';
    }
}
