package com.springframework.beans.factory.config;

import com.springframework.beans.factory.HierarchicalBeanFactory;
import com.springframework.core.convert.ConversionService;
import com.springframework.util.StringValueResolver;

/**
 *
 *
 * @since 2022-06-23
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁单例bean
     */
    void destroySingletons();

    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);

    ConversionService getConversionService();

    void setConversionService(ConversionService conversionService);

}
