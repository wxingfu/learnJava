package com.weixf.schema.maker.table;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 *
 * @since 2022-01-21
 */
@Component
public class Schema {

    @Setter
    @Getter
    private String Name;
    @Setter
    @Getter
    private String Code;
    @Setter
    @Getter
    private String DBMSCode;
    @Setter
    @Getter
    private String DBMSName;
    private final ArrayList<Table> Tables = new ArrayList<>();
    @Getter
    private int TabNum = 0;

    public Schema() {
    }

    public void addTable(Table table) {
        Tables.add(table);
        TabNum++;
    }

    public Table getTable(int i) {
        return Tables.get(i);
    }

}
