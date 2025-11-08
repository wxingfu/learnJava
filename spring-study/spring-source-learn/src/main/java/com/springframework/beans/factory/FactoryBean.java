package com.springframework.beans.factory;

/**
 *
 *
 * @since 2022-06-23
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    boolean isSingleton();
}
