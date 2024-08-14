package com.djk.minis.core;

import com.djk.minis.BeanDefinition;

/**
 * bean工厂
 */
public interface BeanFactory {
    // 获取bean
    Object getBean(String beanName) throws Exception;
    // 注册bean
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;
}
