package com.djk.minis.beans.factory.support;

import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.BeanFactory;
import com.djk.minis.beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于代码复用、解耦的原则
 * 对BeanFactory通用的部分代码进行抽象
 * 提供一个具有基本功能的BeanFactory实现，方便扩展
 *
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    public Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    protected List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    public AbstractBeanFactory() {
    }

    public abstract Object applyBeanPostProcessorsBeforeInitialization(Object singleton, String beanName);
    public abstract Object applyBeanPostProcessorsAfterInitialization(Object singleton, String beanName);

    public void refresh(){
        for (String beanName : beanDefinitionNames) {
            getBean(beanName);
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        //尝试直接从单例池获取bean实例
        Object singleton = getSingleton(beanName);
        //单例池没有，从早期bean实例中获取
        if (singleton == null) {
            singleton = earlySingletonObjects.get(beanName);
            //早期ben实例也没有，则获取它的定义来创建实例
            if (singleton == null) {
                //获取bean定义
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                //创建bean
                singleton = createBean(beanDefinition);
                //注册这个单例bean
                registerBean(beanName, singleton);

                //进行beanPostProcessor的前置处理
                applyBeanPostProcessorsBeforeInitialization(singleton, beanName);
                //init-method
                if (beanDefinition.getInitMethodName() != null && !beanDefinition.equals("")) {
                    invokeInitMethod(beanDefinition, singleton);
                }
                //beanPostProcessor的后置处理
                applyBeanPostProcessorsAfterInitialization(singleton, beanName);
            }
        }

        if (singleton == null) {
            throw new BeansException("bean is null");
        }
        return singleton;
    }

    /**
     *
     * @param beanDefinition
     * @param singleton
     */
    private void invokeInitMethod(BeanDefinition beanDefinition, Object singleton) {
        Class<?> clz = beanDefinition.getClass();
        try {
            Method method = clz.getMethod(beanDefinition.getInitMethodName());
            method.invoke(singleton);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void registerBean(String beanName, Object singleton) {
        registerSingleton(beanName, singleton);
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
        earlySingletonObjects.put(beanDefinition.getId(), obj);

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

    @Override
    public boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanDefinitionMap.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanDefinitionMap.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanDefinitionMap.get(beanName).getClass();
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        beanDefinitionNames.add(beanName);
        if (!beanDefinition.isLazyInit()) {
            //懒加载，则直接创建bean
            getBean(beanName);
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
        beanDefinitionNames.remove(beanName);
        removeSingleton(beanName);
    }


}
