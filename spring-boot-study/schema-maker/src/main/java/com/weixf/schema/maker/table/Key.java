package com.weixf.schema.maker.table;

import com.weixf.schema.maker.utility.DBTypes;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 *
 * @since 2022-01-21
 */
@Component
public class Key {

    @Setter
    @Getter
    private String ConstraintName;
    @Setter
    @Getter
    private int KeyType = 0;
    private final ArrayList<String> Columns = new ArrayList<>();
    @Getter
    private int ColumnNum = 0;
    @Setter
    private String RefTable;
    @Setter
    private String RefKey;
    private final ArrayList<String> RefColumns = new ArrayList<>();
    @Setter
    private int DeleteType = -1;
    @Setter
    private int UpdateType = -1;

    public Key() {
    }

    public String getRefTable() {
        if (KeyType == DBTypes.ForeignKey) {
            return RefTable;
        } else {
            return null;
        }
    }

    public String getRefKey() {
        if (KeyType == DBTypes.ForeignKey) {
            return RefKey;
        } else {
            return null;
        }
    }

    public int getDeleteType() {
        if (KeyType == DBTypes.ForeignKey) {
            return DeleteType;
        } else {
            return 0;
        }
    }

    public int getUpdateType() {
        if (KeyType == DBTypes.ForeignKey) {
            return UpdateType;
        } else {
            return 0;
        }
    }

    public void addColumn(String col, String refcol) {
        Columns.add(col);
        if (refcol != null && !"".equals(refcol)) {
            RefColumns.add(refcol);
        }
        ColumnNum++;
    }

    public String getColumn(int i) {
        return Columns.get(i);
    }

    public String getRefColumn(int i) {
        if (KeyType == DBTypes.ForeignKey) {
            return RefColumns.get(i);
        } else {
            return null;
        }
    }

    public String[] getRefJoin(int i) {
        if (KeyType == DBTypes.ForeignKey) {
            String[] a = new String[2];
            a[0] = Columns.get(i);
            a[1] = RefColumns.get(i);
            return a;
        } else {
            return null;
        }
    }

}
