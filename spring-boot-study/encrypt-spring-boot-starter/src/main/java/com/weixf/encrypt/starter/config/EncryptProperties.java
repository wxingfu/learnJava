package com.weixf.encrypt.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 *
 * @since 2022-06-17
 */
@ConfigurationProperties(prefix = "spring.encrypt")
public class EncryptProperties {
    private final static String DEFAULT_KEY = "www.itboyhub.com";
    private String key = DEFAULT_KEY;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
