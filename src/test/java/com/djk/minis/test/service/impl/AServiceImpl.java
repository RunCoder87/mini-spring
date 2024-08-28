package com.djk.minis.test.service.impl;


import com.djk.minis.test.service.AService;

public class AServiceImpl implements AService {
    private final String property1;
    private final String property2;
    private final String name;
    private int level;
    private final boolean isRef;

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println("name："+name+" ，level："+level);
    }


    @Override
    public void sayHello() {
        System.out.println(this.property1 + "," + this.property2);
    }


    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }
}
