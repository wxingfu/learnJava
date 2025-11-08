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
public class PDMTable {

    @Setter
    @Getter
    public String Id;
    @Setter
    @Getter
    private String Name;
    @Setter
    @Getter
    private String Code;
    private final ArrayList<PDMColumn> Columns = new ArrayList<>(20);
    @Getter
    private int ColNum = 0;
    private final ArrayList<PDMKey> Keys = new ArrayList<>(3);
    @Getter
    private int KeyNum = 0;
    private String PrimaryKey_Ref; // Keys中的Key的Id
    @Setter
    @Getter
    private String User;

    public PDMTable() {
    }

    public String getPrimaryKey() {
        return PrimaryKey_Ref;
    }

    public void setPrimaryKey(String pk) {
        PrimaryKey_Ref = pk;
    }

    public void addColumn(PDMColumn col) {
        Columns.add(col);
        ColNum++;
    }

    public PDMColumn getColumn(int i) {
        return Columns.get(i);
    }

    public void addKey(PDMKey key) {
        Keys.add(key);
        KeyNum++;
    }

    public PDMKey getKey(int i) {
        return Keys.get(i);
    }

}
