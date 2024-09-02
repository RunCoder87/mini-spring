package com.djk.minis.beans.factory.config;

import com.djk.minis.beans.factory.BeanFactory;

/**
 *  可配置的Bean工厂（配置管理）
 *  主要定义beanFactory的扩展功能
 *  在原有的beanFactory基础上，添加一些方法来扩展功能
 *                                  比如控制单例bean、声明周期等
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);

}
