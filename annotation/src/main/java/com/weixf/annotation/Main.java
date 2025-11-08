package com.weixf.annotation;

import java.lang.reflect.Method;


public class Main {

    public static void main(String[] args) throws NoSuchMethodException {
        Class<?> clazz = TestAnnotation.class;
        // System.out.println(Arrays.toString(clazz.getAnnotations()));
        // System.out.println("------------------");
        // System.out.println(clazz.getAnnotation(A.class));
        // System.out.println("------------------");
        // System.out.println(Arrays.toString(clazz.getDeclaredAnnotations()));

        // System.out.println();

        Method method = clazz.getMethod("sayHello", null);
        // System.out.println(Arrays.toString(method.getAnnotations()));
        // System.out.println("------------------");
        // System.out.println(method.getAnnotation(B.class));
        // System.out.println("------------------");
        // System.out.println(Arrays.toString(method.getDeclaredAnnotations()));
        B annotation = method.getAnnotation(B.class);

        String value = annotation.value();
        System.out.println(value);
    }
}
