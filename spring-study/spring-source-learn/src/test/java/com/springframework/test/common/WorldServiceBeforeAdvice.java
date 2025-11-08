package com.springframework.test.common;

import com.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 *
 *
 * @since 2022-06-24
 */
public class WorldServiceBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("BeforeAdvice: do something before the earth explodes");
    }
}
