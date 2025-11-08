package com.weixf.schema.maker.pdm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 *
 * @since 2022-01-21
 */
@Component
public class PDMPhysicalDiagram {

    @Setter
    @Getter
    private String Id;
    @Setter
    @Getter
    private String Name;
    @Setter
    @Getter
    private String Code;
    private final ArrayList<String> Tables = new ArrayList<>(20);
    @Getter
    private int TabNum = 0;

    public PDMPhysicalDiagram() {
    }

    public void addTable(String table) {
        Tables.add(table);
        TabNum++;
    }

    public String getTable(int i) {
        return Tables.get(i);
    }

}
