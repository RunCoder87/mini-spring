package com.djk.minis.beans.factory.config;

import com.djk.minis.beans.BeansException;

/**
 * bean实例化之后，注入未完成时的处理器
 *
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
