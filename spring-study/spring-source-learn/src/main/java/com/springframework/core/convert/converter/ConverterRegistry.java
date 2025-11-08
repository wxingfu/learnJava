package com.springframework.core.convert.converter;

/**
 * 类型转换器注册接口
 *
 * @since 2022-06-24
 */
public interface ConverterRegistry {

    void addConverter(Converter<?, ?> converter);

    void addConverterFactory(ConverterFactory<?, ?> converterFactory);

    void addConverter(GenericConverter converter);
}
