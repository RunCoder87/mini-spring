package com.djk.minis;

/**
 * bean定义
 */
public class BeanDefinition {
    private String id; //类的别名
    private String className; //全限定类名

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
