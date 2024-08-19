package com.djk.minis.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean工厂的简单实现类
 * 既充当工厂，又充当仓库
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry{
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new ArrayList<>();

    //getBean，容器的核心方法
    @Override
    public Object getBean(String beanName){
        //尝试直接获取bean实例
        Object singleton = getSingleton(beanName);
        //如果没有，则获取它的定义来创建实例
        if (singleton == null) {
            //获取bean定义
            BeanDefinition beanDefinition = beanDefinitions.get(beanName);
            //注册bean
            try {
                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            //注册bean实例
            registerSingleton(beanDefinition.getId(), singleton);

        }
        return singleton;
    }

    @Override
    public Boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        registerSingleton(beanName, obj);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitions.put(beanDefinition.getId(), beanDefinition);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanDefinitions.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanDefinitions.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanDefinitions.get(beanName).getClass();
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionNames.contains(beanName);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitions.remove(beanName);
        beanDefinitionNames.remove(beanName);
    }
}
