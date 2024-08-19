package com.djk.minis.context;

import com.djk.minis.beans.BeanDefinition;
import com.djk.minis.beans.BeanFactory;
import com.djk.minis.core.ClassPathXmlResource;
import com.djk.minis.beans.SimpleBeanFactory;
import com.djk.minis.beans.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext implements BeanFactory {
    SimpleBeanFactory beanFactory;

    //context负责整合容器的启动过程，读取外部配置，解析bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName) {
        ClassPathXmlResource resource = new ClassPathXmlResource(fileName);
        this.beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
    }

    @Override
    public Object getBean(String beanName){
        return beanFactory.getBean(beanName);
    }


    @Override
    public Boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        beanFactory.registerBean(beanName, obj);
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
