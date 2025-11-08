package com.springframework.core.io;

/**
 *
 *
 * @since 2022-06-23
 */
public interface ResourceLoader {

    Resource getResource(String location);
}

