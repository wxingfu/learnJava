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
public class PDMReference {

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
    private String ParentTable_Ref; // Object1
    private String ParentKey_Ref;
    private String ChildTable_Ref; // Object2
    @Setter
    @Getter
    private int UpdateConstraint = 1;
    @Setter
    @Getter
    private int DeleteConstraint = 1;
    @Setter
    @Getter
    private String ImplementationType;
    private final ArrayList<PDMReferenceJoin> Joins = new ArrayList<>(3);
    private int Join_Num = 0;

    public PDMReference() {
    }

    public String getParentTable() {
        return ParentTable_Ref;
    }

    public void setParentTable(String code) {
        ParentTable_Ref = code;
    }

    public String getParentKey() {
        return ParentKey_Ref;
    }

    public void setParentKey(String code) {
        ParentKey_Ref = code;
    }

    public String getChildTable() {
        return ChildTable_Ref;
    }

    public void setChildTable(String code) {
        ChildTable_Ref = code;
    }

    public void addJoin(PDMReferenceJoin col) {
        Joins.add(col);
        Join_Num++;
    }

    public int getJoinNum() {
        return Join_Num;
    }

    public PDMReferenceJoin getJoin(int i) {
        return Joins.get(i);
    }
}
