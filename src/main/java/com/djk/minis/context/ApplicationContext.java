package com.djk.minis.context;

import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.djk.minis.beans.factory.config.ConfigurableBeanFactory;
import com.djk.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.djk.minis.beans.factory.xml.ListableBeanFactory;
import com.djk.minis.core.env.Environment;
import com.djk.minis.core.env.EnvironmentCapable;

/**
 * 支持上下文环境和事件发布
 *
 */
public interface ApplicationContext
        extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {
    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();

}
