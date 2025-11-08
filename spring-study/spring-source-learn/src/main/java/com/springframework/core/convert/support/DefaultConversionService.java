package com.springframework.core.convert.support;

import com.springframework.core.convert.converter.ConverterRegistry;

/**
 *
 *
 * @since 2022-06-24
 */
public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
        // TODO 添加其他ConverterFactory
    }
}

