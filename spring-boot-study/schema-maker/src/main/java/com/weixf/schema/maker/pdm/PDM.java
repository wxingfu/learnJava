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
public class PDM {
    @Setter
    @Getter
    private String Id;
    @Getter
    @Setter
    private String Name;
    @Getter
    @Setter
    private String Code;
    @Getter
    @Setter
    private String DBMSCode;
    @Getter
    @Setter
    private String DBMSName;
    private final ArrayList<PDMPhysicalDiagram> PhysicalDiagrams = new ArrayList<>();
    @Getter
    private int PhysicalDiagramNum = 0;
    private final ArrayList<PDMUser> Users = new ArrayList<>(3);
    @Getter
    private int UserNum = 0;
    private final ArrayList<PDMTable> Tables = new ArrayList<>(20);
    @Getter
    private int TabNum = 0;
    private final ArrayList<PDMReference> References = new ArrayList<>();
    @Getter
    private int RefNum = 0;

    public PDM() {
    }

    public void addPhysicalDiagram(PDMPhysicalDiagram PhysicalDiagram) {
        PhysicalDiagrams.add(PhysicalDiagram);
        PhysicalDiagramNum++;
    }

    public PDMPhysicalDiagram getPhysicalDiagram(int i) {
        return PhysicalDiagrams.get(i);
    }

    public void addUser(PDMUser User) {
        Users.add(User);
        UserNum++;
    }

    public PDMUser getUser(int i) {
        return Users.get(i);
    }

    public synchronized void addTable(PDMTable table) {
        Tables.add(table);
        TabNum++;
    }

    public PDMTable getTable(int i) {
        return Tables.get(i);
    }

    public void addReference(PDMReference table) {
        References.add(table);
        RefNum++;
    }

    public PDMReference getReference(int i) {
        return References.get(i);
    }
}
