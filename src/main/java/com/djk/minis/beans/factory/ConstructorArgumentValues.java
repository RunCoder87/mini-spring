package com.djk.minis.beans.factory;
/**
 * 存储xml里所有的<constructor-arg>实体类的信息
 *
 */

import java.util.*;

public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> constructorArgumentList = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue constructorArgument) {
        this.constructorArgumentList.add(constructorArgument);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        ConstructorArgumentValue constructorArgument = this.constructorArgumentList.get(index);
        return constructorArgument;
    }

    public int getArgumentCount() {
        return (this.constructorArgumentList.size());
    }

    public boolean isEmpty() {
        return (this.constructorArgumentList.isEmpty());
    }
}
