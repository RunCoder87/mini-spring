package com.djk.minis.test;

import com.djk.minis.context.ClassPathXmlApplicationContext;
import com.djk.minis.test.service.AService;
import com.djk.minis.test.service.impl.AServiceImpl;

public class Test1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        AServiceImpl aservice = null;
        try {
            aservice = (AServiceImpl) context.getBean("bservice");
            aservice.sayHello();

            System.out.println("property1："+aservice.getProperty1());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
