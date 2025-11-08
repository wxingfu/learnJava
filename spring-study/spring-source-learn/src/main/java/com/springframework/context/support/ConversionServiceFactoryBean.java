package com.springframework.context.support;

import com.springframework.beans.factory.FactoryBean;
import com.springframework.beans.factory.InitializingBean;
import com.springframework.core.convert.ConversionService;
import com.springframework.core.convert.converter.Converter;
import com.springframework.core.convert.converter.ConverterFactory;
import com.springframework.core.convert.converter.ConverterRegistry;
import com.springframework.core.convert.converter.GenericConverter;
import com.springframework.core.convert.support.DefaultConversionService;
import com.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

/**
 *
 *
 * @since 2022-06-24
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    private Set<?> converters;

    private GenericConversionService conversionService;

    @Override
    public void afterPropertiesSet() throws Exception {
        conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}

