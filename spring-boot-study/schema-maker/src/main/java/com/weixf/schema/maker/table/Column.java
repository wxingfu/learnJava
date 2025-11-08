package com.weixf.schema.maker.table;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 *
 * @since 2022-01-21
 */
@Setter
@Getter
@Component
public class Column {
    private String Name;
    private String Code;
    private String DataType; // Java数据类型
    private int Length; // 数据长度
    private int Precision; // 数据精度
    private int Nullable = 1; // 默认可空
    private String DefaultValue; // 字段默认值

    private int DBSqlType; // 字段的SQL类型(数据库)，主要是针对ps的null值处理
    private int DBLength; // 字段的长度(数据库)，主要是针对char类型的ps处理

    private String PDMDataType; // 字段类型(PDM)

    public Column() {
    }

}
