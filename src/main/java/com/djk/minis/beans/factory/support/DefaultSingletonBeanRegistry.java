package com.djk.minis.beans.factory.support;

import com.djk.minis.beans.factory.SingletonBeanRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 为了避免把代码写死，这里创建一个默认的实现类
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    //容器中存放所有bean的名称的列表
    protected List<String> beanNames = new ArrayList<>();
    //容器中存放所有bean实例的map
    protected Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    //存放bean依赖关系的map
    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    protected Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (singletonObjects) {
            //判断是否已经存在
            Object oldObj = singletonObjects.get(beanName);
            //如果这个bean已经存在，抛出异常
            if (oldObj != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject + "] under bean name '" + beanName + "': there is already object [" + oldObj + "] bound");
            }
            singletonObjects.put(beanName, singletonObject);
            beanNames.add(beanName);
            System.out.println(" bean registered ....... " + beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return (String[]) beanNames.toArray();
    }

    protected void removeSingleton(String beanName) {
        synchronized (singletonObjects) {
            beanNames.remove(beanName);
            singletonObjects.remove(beanName);
        }
    }

    /**
     * 注册bean的依赖关系
     *
     * @param beanName
     * @param dependentBeanName
     */
    public void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = dependentBeanMap.get(beanName);
        //如果已经添加了这个依赖，则直接返回
        if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
            return;
        }
        //没有才去添加依赖
        synchronized (dependentBeanMap) {
            dependentBeanMap.get(beanName);
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet<>(8);
                dependentBeanMap.put(beanName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }

        synchronized (dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet<String>(8);
                dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            dependenciesForBean.add(beanName);
        }

    }
    protected boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }
    protected String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return (String[]) dependentBeans.toArray();
    }
    protected String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return (String[]) dependenciesForBean.toArray();

    }

}
