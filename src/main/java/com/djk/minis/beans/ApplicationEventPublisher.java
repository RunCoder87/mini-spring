package com.djk.minis.beans;

import com.djk.minis.context.ApplicationListener;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);

    void addApplicationListener(ApplicationListener listener);

}
