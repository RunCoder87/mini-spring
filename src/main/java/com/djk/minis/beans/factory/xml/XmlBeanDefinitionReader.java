package com.djk.minis.beans.factory.xml;

import com.djk.minis.beans.factory.config.PropertyValue;
import com.djk.minis.beans.factory.config.PropertyValues;
import com.djk.minis.beans.factory.config.ArgumentValue;
import com.djk.minis.beans.factory.config.ArgumentValues;
import com.djk.minis.beans.factory.config.BeanDefinition;
import com.djk.minis.beans.factory.support.SimpleBeanFactory;
import com.djk.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 将解析好的 XML 转换成我们需要的 BeanDefinition
 * 1.解析bean标签
 * 2.解析property标签
 * 3.解析constructor标签
 * 4.封装成bean并加载到内存中
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
            //获取bean标签
            Element element = (Element) resource.next();
            //解析bean的基本信息(bean标签)
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            simpleBeanFactory.registerBeanDefinition(beanDefinition);

            //获取bean标签下的constructor标签
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues argumentValues = new ArgumentValues();
            //解析constructor标签
            for (Element constructorElement : constructorElements) {
                String type = constructorElement.attributeValue("type");
                String name = constructorElement.attributeValue("name");
                String argumentValue = constructorElement.attributeValue("value");
                //创建属性对象
                ArgumentValue AV = new ArgumentValue(type, name, argumentValue);
                argumentValues.addArgumentValue(AV);
            }
            beanDefinition.setConstructorArgumentValues(argumentValues);

            //获取bean标签下的property标签
            List<Element> propertyElements = element.elements("property");
            PropertyValues propertyValues = new PropertyValues();
            List<String> refs = new ArrayList<>();
            //解析property标签
            for (Element propertyElement : propertyElements) {
                String name = propertyElement.attributeValue("name");
                String value = propertyElement.attributeValue("value");
                String ref = propertyElement.attributeValue("ref");
                String type = propertyElement.attributeValue("type");
                boolean isRef = false;
                String pV = "";
                //判断属性是普通值还是引用的bean
                if (value != null && !value.equals("")) {
                    isRef = false;
                    pV = value;
                } else if (ref != null && !ref.equals("")) {
                    isRef = true;
                    pV = ref;
                    refs.add(ref);
                }
                //创建属性对象
                PropertyValue PV = new PropertyValue(type, name, pV, isRef);
                propertyValues.addPropertyValue(PV);
            }
            //给beanDefinition设置属性
            beanDefinition.setPropertyValues(propertyValues);
            String[] refArray = refs.toArray(new String[refs.size()]);
            beanDefinition.setDependsOn(refArray);

            //注册beanDefinition
            simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
