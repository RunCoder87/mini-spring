package com.djk.minis.context;

import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
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
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    DefaultListableBeanFactory beanFactory;
    // bean工厂后处理器
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new
            ArrayList<>();

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        // 加载配置文件
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory = new
                DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new
                XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);

        //创建bean工厂
        this.beanFactory = beanFactory;
        if (isRefresh) {
            try {
                /**
                 * 刷新容器
                 * 1.postProcessBeanFactory
                 * 2.registerBeanPostProcessors
                 * 3.initApplicationEventPublisher
                 * 4.onRefresh
                 * 5.registerListener
                 * 6.finishRefresh
                 */
                refresh();
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    // 注册监听器
    @Override
    void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    // 初始化事件发布者
    @Override
    void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    // bean工厂后处理器
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
    void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
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