package com.weixf;

import com.weixf.context.MyContext;
import com.weixf.context.MyContext2;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 *
 *
 * @since 2023-05-31
 */
@SpringBootTest
public class LiteFlowAppTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void test() {

    }

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testConfig() {
        // LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
        LiteflowResponse response = flowExecutor.execute2Resp(
                "test2",
                "1111",
                MyContext.class,
                MyContext2.class);
        MyContext contextBean = response.getContextBean(MyContext.class);
        System.out.println(contextBean.getAge());

        MyContext2 contextBean2 = response.getContextBean(MyContext2.class);
        System.out.println(contextBean2.getUsername());
    }
}
