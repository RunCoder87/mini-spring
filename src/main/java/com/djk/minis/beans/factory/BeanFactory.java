package com.djk.minis.beans.factory;

import com.djk.minis.beans.BeansException;

/**
 * bean工厂
 * 核心功能：
 *      1. 获取bean
 *      2. 注册bean
 */
public interface BeanFactory {
    // 获取bean
    Object getBean(String beanName) throws BeansException;
    // 注册bean
//    void registerBean(String beanName, Object obj);
    Boolean containsBean(String beanName);
    boolean isSingleton(String beanName);
    boolean isPrototype(String beanName);
    Class<?> getType(String beanName);


}
