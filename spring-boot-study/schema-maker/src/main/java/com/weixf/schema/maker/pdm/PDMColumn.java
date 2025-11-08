package com.weixf.schema.maker.pdm;

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
public class PDMColumn {

    private String Id;
    private String Name;
    private String Code;
    private String DataType; // 数据类型
    private int Length; // 数据长度
    private int Precision; // 数据精度
    private int Mandatory = 0; // 字段是否为空，默认可空
    private String DefaultValue; // 字段默认值
    private String LowValue;
    private String HighValue;

    public PDMColumn() {
    }

}
