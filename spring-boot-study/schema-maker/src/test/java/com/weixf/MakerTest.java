package com.weixf;

import com.weixf.schema.maker.maker.Maker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 *
 * @since 2022-01-21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MakerTest {

    @Resource
    private Maker maker;

    @Test
    public void test() throws Exception {
        maker.createJavaFile();
    }
}
