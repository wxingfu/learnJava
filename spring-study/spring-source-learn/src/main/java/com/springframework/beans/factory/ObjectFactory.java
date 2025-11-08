package com.springframework.beans.factory;

import com.springframework.beans.BeansException;

/**
 *
 *
 * @since 2022-06-23
 */
public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
