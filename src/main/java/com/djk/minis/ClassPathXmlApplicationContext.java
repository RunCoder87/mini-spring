package com.djk.minis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContext(String fileName) {
        this.readXml(fileName);
        this.instanceBean();
    }

    // 读取xml文件
    private void readXml(String fileName) {
        // 创建SAXReader对象
        SAXReader saxReader = new SAXReader();
        //根据文件名获取类路径下的xml文件资源
        URL xmlPath =this.getClass().getClassLoader().getResource(fileName);
        try {
            // 获取document对象
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            // 获取根元素下的子元素(也就是定义每一个bean)
            for (Element element : (List<Element>) rootElement.elements()) {
                // 获取bean的id和className
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(id, className);
                // 将beanDefinition放入beanDefinitions中
                beanDefinitions.add(beanDefinition);
            }

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    // 利用反射创建bean实例，并将实例放入singletons中
    private void instanceBean() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                singletons.put(beanDefinition.getId(), Class.forName(beanDefinition.getClassName()).newInstance());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //外部程序从容器中获取bean实例
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}
