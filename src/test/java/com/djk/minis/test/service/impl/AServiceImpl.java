package com.djk.minis.test.service.impl;


import com.djk.minis.test.service.AService;

public class AServiceImpl implements AService {
    private String property1;

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty1() {
        return property1;
    }

    @Override
    public void sayHello() {
        System.out.println("aservice say hello");
    }

}
