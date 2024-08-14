package com.djk.minis.test;

import com.djk.minis.ClassPathXmlApplicationContext;
import com.djk.minis.test.service.AService;

public class Test1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        AService aservice = (AService) context.getBean("aservice");
        aservice.sayHello();
    }
}
