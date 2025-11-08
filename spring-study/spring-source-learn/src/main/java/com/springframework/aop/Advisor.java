package com.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 *
 *
 * @since 2022-06-24
 */
public interface Advisor {

    Advice getAdvice();

}
