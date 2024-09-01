package com.djk.minis.beans.factory.annotation;

import com.djk.minis.beans.BeansException;
import com.djk.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.djk.minis.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * 处理@Autowired注解的后置处理器
 * 换句话说，就是解释@Autowired注解
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private AutowireCapableBeanFactory beanFactory;

    /**
     * 创建bean后的前置处理器
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                //对每一个属性进行判断，如果带有@Autowired注解，则进行注入
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if (isAutowired) {
                    //根据属性名获取同名的 bean
                    String fieldName = field.getName();
                    Object autowireObj = getBeanFactory().getBean(fieldName);
                    //设置属性值
                    try {
                        field.setAccessible(true);
                        field.set(bean, autowireObj);
                        System.out.println("autowire " + fieldName + " for bean " + beanName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return result;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
