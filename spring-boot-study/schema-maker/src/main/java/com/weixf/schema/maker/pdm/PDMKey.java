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
public class PDMKey {

    @Setter
    @Getter
    private String Id;
    @Setter
    @Getter
    private String Name;
    @Setter
    @Getter
    private String Code;
    @Setter
    @Getter
    private String ConstraintName;
    private final ArrayList<String> Columns_Ref = new ArrayList<>(5); // 使用Column中的ID
    @Getter
    private int ColNum = 0;

    public PDMKey() {
    }

    public void addColumn(String col) {
        Columns_Ref.add(col);
        ColNum++;
    }

    public String getColumn(int i) {
        return (String) Columns_Ref.get(i);
    }

}
