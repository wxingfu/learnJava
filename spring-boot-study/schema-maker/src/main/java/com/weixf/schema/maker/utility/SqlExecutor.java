package com.weixf.schema.maker.utility;

import com.weixf.schema.maker.repository.MyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * @since 2022-01-21
 */
@Slf4j
@Component
public class SqlExecutor {

    @Resource
    private MyRepository myRepository;

    public String getOneValue(String sql) {
        return myRepository.getOneValue(sql);
    }

    public DataCollection execSQL(String sql) {
        return myRepository.execSQL(sql);
    }
}
