package com.djk.minis.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;

/**
 * 核心功能：
 *      1.加载xml文件
 *      2.解析xml文件
 *      3.将解析后的xml文件封装成BeanDefinition
 *
 */
public class ClassPathXmlResource implements Resource {
    Document document;
    Element rootElement;
    Iterator<Element> elementIterator;

    public ClassPathXmlResource(String fileName) {
        URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
        //读取xml文件
        SAXReader saxReader = new SAXReader();
        //将配置文件装载进来，生成一个迭代器，用来遍历
        try {
            this.document = saxReader.read(xmlPath);
            this.rootElement = this.document.getRootElement();
            this.elementIterator = this.rootElement.elementIterator();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean hasNext() {
        return this.elementIterator.hasNext();
    }
    public Object next() {
        return this.elementIterator.next();
    }

}
