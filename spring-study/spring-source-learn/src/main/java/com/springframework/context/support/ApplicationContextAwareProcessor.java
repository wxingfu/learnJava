package com.springframework.context.support;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.config.BeanPostProcessor;
import com.springframework.context.ApplicationContext;
import com.springframework.context.ApplicationContextAware;

/**
 *
 *
 * @since 2022-06-23
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
