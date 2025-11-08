package com.springframework.test.aop;

import com.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.springframework.test.service.HelloService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 *
 *
 * @since 2022-06-24
 */
public class PointcutExpressionTest {

    @Test
    public void testPointcutExpression() throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.springframework.test.service.HelloService.*(..))");
        Class<HelloService> clazz = HelloService.class;
        Method method = clazz.getDeclaredMethod("sayHello");
        Assert.assertTrue(pointcut.matches(clazz));
        Assert.assertTrue(pointcut.matches(method, clazz));
    }
}
