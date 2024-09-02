package com.djk.minis.beans.factory.config;

import com.djk.minis.beans.BeansException;

/**
 * 在bean生命周期的关键点执行
 * bean实例化之后，属性注入时进行操作
 *
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

    void setBeanFactory(AbstractAutowireCapableBeanFactory abstractAutowireCapableBeanFactory);
}
