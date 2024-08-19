package com.djk.minis.beans;

/**
 * 角色分离：
 *  这里充当仓库的角色
 *  用来管理单例bean的
 *  定义单例bean应有的方法
 */
public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletonObject);
    Object getSingleton(String beanName);
    boolean containsSingleton(String beanName);
    String[] getSingletonNames();

}
