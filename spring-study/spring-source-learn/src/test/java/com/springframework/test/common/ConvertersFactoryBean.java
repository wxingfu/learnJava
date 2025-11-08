package com.springframework.test.common;

import com.springframework.beans.factory.FactoryBean;

import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * @since 2022-06-24
 */
public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    @Override
    public Set<?> getObject() throws Exception {
        HashSet<Object> converters = new HashSet<>();
        StringToLocalDateConverter stringToLocalDateConverter =
                new StringToLocalDateConverter("yyyy-MM-dd");
        converters.add(stringToLocalDateConverter);
        return converters;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

