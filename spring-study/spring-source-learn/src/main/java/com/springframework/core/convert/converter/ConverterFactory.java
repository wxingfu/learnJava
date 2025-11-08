package com.springframework.core.convert.converter;

/**
 * 类型转换工厂
 *
 * @since 2022-06-24
 */
public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
