package com.djk.minis.beans;

/**
 * 存放BeanDefinition的仓库
 *
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(BeanDefinition beanDefinition);
    BeanDefinition getBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);
    void removeBeanDefinition(String beanName);
}
