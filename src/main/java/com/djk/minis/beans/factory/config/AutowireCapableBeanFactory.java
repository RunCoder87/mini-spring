package com.djk.minis.beans.factory.config;

import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.BeanFactory;

/**
 * 扩展BeanFactory接口
 *  主要用于实现自动装配Bean的依赖关系
 *
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
