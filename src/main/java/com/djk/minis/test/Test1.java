package com.djk.minis.test;

import com.djk.minis.beans.BeansException;
import com.djk.minis.context.ClassPathXmlApplicationContext;

public class Test1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService;

        aService = (AService) ctx.getBean("aservice");
        aService.sayHello();

    }
}
