package com.djk.minis.context;

/**
 * 自定义容器刷新事件
 *
 */
public class ContextRefreshEvent extends ApplicationEvent{

    private static final long serialVersionUID = 1L;

    public ContextRefreshEvent(Object arg0) {
        super(arg0);
    }

    @Override
    public String toString() {
        return "ContextRefreshEvent{" +
                "msg='" + msg + '\'' +
                ", source=" + source +
                '}';
    }
}
