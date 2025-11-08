package com.springframework.aop;

/**
 *
 *
 * @since 2022-06-24
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);
}
