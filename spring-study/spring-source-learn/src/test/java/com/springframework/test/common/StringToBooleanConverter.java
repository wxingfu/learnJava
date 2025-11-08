package com.springframework.test.common;

import com.springframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 *
 *
 * @since 2022-06-24
 */
public class StringToBooleanConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Boolean.class));
    }

    @Override
    public Object convert(Object source, Class sourceType, Class targetType) {
        return Boolean.valueOf((String) source);
    }
}
