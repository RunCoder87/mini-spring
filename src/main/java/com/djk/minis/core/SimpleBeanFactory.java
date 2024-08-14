package com.djk.minis.core;

import com.djk.minis.BeanDefinition;
import com.djk.minis.exceptions.BeansException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBeanFactory implements BeanFactory{
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private List<String> beanNames = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    //getBean，容器的核心方法
    @Override
    public Object getBean(String beanName) throws Exception {
        //尝试直接获取bean
        Object singleton = singletons.get(beanName);
        //如果没有，则创建
        if (singleton == null) {
            int index = beanNames.indexOf(beanName);
            if (index == -1) {
                throw new BeansException("beanName not found");
            }
            //获取bean定义
            BeanDefinition beanDefinition = beanDefinitions.get(index);
            //创建bean
            singleton = Class.forName(beanDefinition.getClassName()).newInstance();
            //注册bean实例
            singletons.put(beanDefinition.getId(), singleton);

        }
        return singleton;
    }

    //注册bean
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {

    }
}
