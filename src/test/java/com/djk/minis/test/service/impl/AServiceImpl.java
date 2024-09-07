package com.djk.minis.test.service.impl;


import com.djk.minis.test.service.AService;

public class AServiceImpl implements AService {
    private String property1;
    private String property2;
    private String name;
    private int level;
    private boolean isRef;

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



    public String getProperty2() {
        return property2;
    }

}
