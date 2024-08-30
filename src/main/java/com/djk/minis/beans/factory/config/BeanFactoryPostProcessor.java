package com.djk.minis.beans.factory.config;

import com.djk.minis.beans.BeansException;

/**
 * 在bean定义加载解析后，进行实例化之前的操作
 */
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(AutowireCapableBeanFactory beanFactory) throws BeansException;
}
