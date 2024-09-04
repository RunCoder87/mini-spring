package com.djk.minis.context;

/**
 * 容器事件发布者
 *      负责容器的事件发布
 */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);

    void addApplicationListener(ApplicationListener listener);

}
