package com.djk.minis.beans.factory;

/**
 * 对应xml中的<property>标签
 */
public class PropertyValue {
    private final String type;
    private final String name;
    private final Object value;
    private final boolean isRef; //判断是普通值还是引用的bean

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public String getType() {
        return type;
    }



    public String getName() {
        return name;
    }


    public Object getValue() {
        return value;
    }

    public boolean isRef() {
        return isRef;
    }
}
