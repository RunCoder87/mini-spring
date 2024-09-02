package com.djk.minis.beans.factory.xml;

import com.djk.minis.beans.BeansException;

import java.util.Map;

/**
 * 对beanFactory进行扩展
 * 将内部的Bean定义当作一个集合来看待，作为一个单独的功能
 *          提供额外的方法来获取bean的信息
 */
public interface ListableBeanFactory {
    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);

    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}
