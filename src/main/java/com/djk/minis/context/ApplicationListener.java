package com.djk.minis.context;

import com.djk.minis.beans.ApplicationEvent;

import java.util.EventListener;

public class ApplicationListener implements EventListener {
    void onApplicationEvent(ApplicationEvent event) {
        System.out.println(event.toString());
    }
}
