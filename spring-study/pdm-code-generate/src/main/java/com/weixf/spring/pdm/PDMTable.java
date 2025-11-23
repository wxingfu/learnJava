package com.weixf.spring.pdm;

import java.util.ArrayList;


public class PDMTable {

    public String id;
    private String name;
    private String code;
    private PDMUser user;
    private ArrayList<PDMColumn> columns = new ArrayList<PDMColumn>();
    private ArrayList<PDMKey> keys = new ArrayList<PDMKey>();
    private PDMKey primaryKey;
    private ArrayList<PDMIndex> indexs = new ArrayList<PDMIndex>();

    public PDMTable() {
    }

    public PDMTable(String id, String name, String code, PDMUser user, ArrayList<PDMColumn> columns, ArrayList<PDMKey> keys, PDMKey primaryKey, ArrayList<PDMIndex> indexs) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.user = user;
        this.columns = columns;
        this.keys = keys;
        this.primaryKey = primaryKey;
        this.indexs = indexs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PDMUser getUser() {
        return user;
    }

    public void setUser(PDMUser user) {
        this.user = user;
    }

    public ArrayList<PDMColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<PDMColumn> columns) {
        this.columns = columns;
    }

    public ArrayList<PDMKey> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<PDMKey> keys) {
        this.keys = keys;
    }

    public PDMKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PDMKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public ArrayList<PDMIndex> getIndexs() {
        return indexs;
    }

    public void setIndexs(ArrayList<PDMIndex> indexs) {
        this.indexs = indexs;
    }

    @Override
    public String toString() {
        return "PDMTable{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", user=" + user +
                ", columns=" + columns +
                ", keys=" + keys +
                ", primaryKey=" + primaryKey +
                ", indexs=" + indexs +
                '}';
    }

    public void addColumn(PDMColumn column) {
        columns.add(column);
        column.setTable(this);
    }

    public void addKey(PDMKey key) {
        keys.add(key);
    }

    public void addIndex(PDMIndex index) {
        indexs.add(index);
    }

    public PDMColumn getPDMColumn(String id) throws Exception {
        for (PDMColumn column : columns) {
            if (id.equals(column.getId())) {
                return column;
            }
        }
        throw new Exception("Id为：" + id + "对应的列不存在！");
    }

    public PDMKey getPDMKey(String id) throws Exception {
        for (PDMKey key : keys) {
            if (id.equals(key.getId())) {
                return key;
            }
        }
        throw new Exception("Id为：" + id + "对应的Key不存在！");
    }
}
