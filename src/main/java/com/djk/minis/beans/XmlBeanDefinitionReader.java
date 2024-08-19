package com.djk.minis.beans;

import com.djk.minis.core.Resource;
import org.dom4j.Element;

/**
 * 将解析好的 XML 转换成我们需要的 BeanDefinition
 */
public class XmlBeanDefinitionReader {
    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    //从文件中读取bean定义
    //将bean定义注册到bean工厂中
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
