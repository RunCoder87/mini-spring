package com.djk.minis.context;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的容器事件发布者
 *
 */
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {
    //存放监听器
    List<ApplicationListener> listeners = new ArrayList<>();

    // 向监听器发布事件
    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    // 添加监听器
    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.listeners.add(listener);
    }


}
