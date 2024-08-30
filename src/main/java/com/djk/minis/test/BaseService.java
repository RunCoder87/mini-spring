package com.djk.minis.test;

import com.djk.minis.beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    private BaseBaseService bbs;

    public BaseService() {
    }


    public BaseBaseService getBbs() {
        return bbs;
    }

    public void sayHello() {
        System.out.println("Base Service says Hello");
        bbs.sayHello();
    }


    public void setBbs(BaseBaseService bbs) {
        this.bbs = bbs;
    }
}
