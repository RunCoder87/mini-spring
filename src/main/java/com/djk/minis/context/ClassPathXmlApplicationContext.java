package com.djk.minis.context;

import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.BeanFactory;
import com.djk.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.djk.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.djk.minis.beans.factory.config.BeanDefinition;
import com.djk.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.djk.minis.core.ClassPathXmlResource;
import com.djk.minis.beans.factory.support.SimpleBeanFactory;
import com.djk.minis.beans.factory.xml.XmlBeanDefinitionReader;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext implements BeanFactory {
    AutowireCapableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
            new ArrayList<BeanFactoryPostProcessor>();

    //context负责整合容器的启动过程，读取外部配置，解析bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        this.beanFactory = new AutowireCapableBeanFactory();

        //解析外部资源
        ClassPathXmlResource resource = new ClassPathXmlResource(fileName);
        //加载bean定义到内存
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        //刷新容器
        if (isRefresh) {
            refresh();
        }
    }

    private void refresh() {
        //注册bean的后置处理器
        registerBeanPostProcessors(beanFactory);
        //刷新容器
        onRefresh();
    }

    /**
     * 注册bean的后置处理器
     * @param beanFactory
     */
    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    private void onRefresh() {
        beanFactory.refresh();
    }

    private List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }


    @Override
    public Boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }



    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerSingleton(beanName, obj);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanFactory.isSingleton(beanName);
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanFactory.isPrototype(beanName);
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanFactory.getType(beanName);
    }
}
