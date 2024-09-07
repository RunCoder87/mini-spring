package com.djk.minis.test;

import com.djk.minis.web.RequestMapping;

public class HelloWordBean {
    @RequestMapping("/test")
    public String doGetTest()
    {
        return "hello world!";
    }
    public String doPost()
    {
        return "hello world!";
    }
}
