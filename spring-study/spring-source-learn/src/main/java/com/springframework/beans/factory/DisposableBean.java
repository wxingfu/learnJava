package com.springframework.beans.factory;

/**
 *
 *
 * @since 2022-06-23
 */
public interface DisposableBean {

    void destroy() throws Exception;
}
