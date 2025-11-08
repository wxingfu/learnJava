package com.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源的抽象和访问接口
 *
 * @since 2022-06-23
 */
public interface Resource {

    InputStream getInputStream() throws IOException;

}
