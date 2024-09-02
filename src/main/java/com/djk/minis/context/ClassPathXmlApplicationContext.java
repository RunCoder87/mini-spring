package com.djk.minis.context;

import com.djk.minis.beans.ApplicationEvent;
import com.djk.minis.beans.ApplicationEventPublisher;
import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.BeanFactory;
import com.djk.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.djk.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.djk.minis.beans.factory.config.BeanDefinition;
import com.djk.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.djk.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.djk.minis.beans.factory.support.DefaultListableBeanFactory;
import com.djk.minis.core.ClassPathXmlResource;
import com.djk.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.djk.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * spring容器，负责整合整个启动流程
 *
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
    DefaultListableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new
            ArrayList<>();
    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory = new
                DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new
                XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            try {
                refresh();
            }catch (BeansException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }
    @Override
    void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }
    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }
    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }
    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor
                                                    postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }
    @Override
    void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory)
    {
        this.beanFactory.addBeanPostProcessor(new
                AutowiredAnnotationBeanPostProcessor());
    }
    @Override
    void onRefresh() {
        this.beanFactory.refresh();
    }
    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws
            IllegalStateException {
        return this.beanFactory;
    }
    @Override
    void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }
}