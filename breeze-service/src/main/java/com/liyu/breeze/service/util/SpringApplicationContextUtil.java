package com.liyu.breeze.service.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring 应用上下文工具类，获取applicationContext
 *
 * @author gleiyu
 * @version 1.0
 * @date 2020-03-03 11:31:37
 */
@Component
public class SpringApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取spring applicationContext对象
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过名称获取bean对象
     *
     * @param name bean名称
     * @param <T>  泛型对象
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        if (applicationContext == null) {
            return null;
        } else {
            return (T) applicationContext.getBean(name);
        }
    }

    /**
     * 按类型获取对象
     *
     * @param clazz 对象类型
     * @param <T>   泛型对象
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
