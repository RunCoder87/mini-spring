package com.djk.minis.beans.factory.support;

import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean工厂的简单实现类
 * 既充当工厂，又充当仓库
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    private final Map<String, Object> earlySingleton = new HashMap<>(); //存放bean的早期实例
    private List<String> beanDefinitionsNames = new ArrayList<>();

    //getBean，容器的核心方法
    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试直接从单例池获取bean实例
        Object singleton = getSingleton(beanName);
        //单例池没有，从早期bean实例中获取
        if (singleton == null) {
            singleton = earlySingleton.get(beanName);
            //早期ben实例也没有，则获取它的定义来创建实例
            if (singleton == null) {
                //获取bean定义
                BeanDefinition beanDefinition = beanDefinitions.get(beanName);
                //创建bean
                singleton = createBean(beanDefinition);
                //注册bean
                registerSingleton(beanName, singleton);
            }
        }

        if (singleton == null) {
            throw new BeansException("bean is null");
        }
        return singleton;
    }

    @Override
    public boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitions.put(beanName, beanDefinition);
        beanDefinitionsNames.add(beanName);
        //不是懒加载的bean，则立即创建bean
        if (!beanDefinition.isLazyInit()) {
            //懒加载，则直接创建bean
            getBean(beanName);
        }

    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanDefinitions.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanDefinitions.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanDefinitions.get(beanName).getClass();
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanNames.contains(beanName);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitions.remove(beanName);
        beanNames.remove(beanName);
        beanDefinitionsNames.remove(beanName);
    }

    /**
     * 包装bean的整个创建过程
     * 把容器中所有的bean实例创建出来
     *
     */
    public void refresh() {
        for (String beanName : beanDefinitionsNames) {
            getBean(beanName);
        }
    }

    /**
     * 根据bean定义创建bean实例
     * 1.处理构造器参数
     * 2.处理属性
     *
     * @param beanDefinition
     * @return
     */
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null; //bean的Class对象
        //创建bean的早期实例
        Object obj = doCreateBean(beanDefinition);
        //存放到早期bean缓存中
        earlySingleton.put(beanDefinition.getId(), obj);

        //处理属性
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        handleProperties(beanDefinition, clz, obj);
        return obj;
    }

    /**
     * 创建bean早期实例
     * 只通过反射调用构造器创建bean实例，属性未进行注入
     *
     * @param beanDefinition
     * @return
     */
    private Object doCreateBean(BeanDefinition beanDefinition) {
        Constructor<?> con = null; //获取构造器
        Object obj = null;
        Class<?> clz = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
            //从bean定义获取构造器参数
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            //有参数，就去赋值
            if (!constructorArgumentValues.isEmpty()) {
                //存储参数类型和参数值
                Class<?>[] paramTypes = new Class[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                //对每一个参数，按照数据类型进行处理
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgument = constructorArgumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(constructorArgument.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgument.getValue();
                    } else if ("Integer".equals(constructorArgument.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.parseInt(constructorArgument.getValue().toString());
                    } else if ("Boolean".equals(constructorArgument.getType())) {
                        paramTypes[i] = Boolean.class;
                        paramValues[i] = Boolean.parseBoolean(constructorArgument.getValue().toString());
                    } else if ("int".equals(constructorArgument.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf(constructorArgument.getValue().toString());
                    } else {
                        //默认为String
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgument.getValue();
                    }
                }
                //通过反射拿到构造器，创建实例
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);
            } else {
                //没有参数，直接创建
                obj = clz.newInstance();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(beanDefinition.getId() + " bean created. " + beanDefinition.getClassName() + " : " + obj.toString());
        return obj;
    }

    /**
     * 处理属性，对属性进行注入
     *
     * @param beanDefinition
     * @param clz
     * @param obj
     */
    private void handleProperties(BeanDefinition beanDefinition, Class<?> clz, Object obj) {
        //处理属性
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                //每一个属性，按数据类型分出处理
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                String type = propertyValue.getType();
                boolean isRef = propertyValue.isRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                //如果属性不是引用类型
                if (!isRef) {
                    if ("String".equals(type) || "java.lang.String".equals(type)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(type)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(type)) {
                        paramTypes[0] = int.class;
                    } else {
                        //默认为String
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = value.toString();
                } else {
                    //属性是引用类型，就先去创建所依赖的那个bean(调用getBean方法)
                    try {
                        paramTypes[0] = Class.forName(type);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    paramValues[0] = getBean(value.toString());
                }

                //拿到属性的setter方法，调用setter方法为对象设置属性值
                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerBean(String beanName, Object obj) {
        registerSingleton(beanName, obj);
    }
}
