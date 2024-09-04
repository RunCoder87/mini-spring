package com.djk.minis.beans.factory;

/**
 * 对应xml中的<constructor-arg>标签
 */
public class ConstructorArgumentValue {
    private String type;
    private String name;
    private Object value;

    public ConstructorArgumentValue(Object value, String type) {
        this.value = value;
        this.type = type;
    }

    public ConstructorArgumentValue(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
