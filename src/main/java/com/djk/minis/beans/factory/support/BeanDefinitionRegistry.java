package com.djk.minis.beans.factory.support;

import com.djk.minis.beans.factory.BeanDefinition;

/**
 * 存放BeanDefinition的仓库
 *
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName,BeanDefinition beanDefinition);
    BeanDefinition getBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);
    void removeBeanDefinition(String beanName);
}
