package com.weixf.schema.maker.pdm;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @since 2022-01-21
 */
@Slf4j
@Component
public class Parser {

    private static final String space4 = "    ";
    // NameSpace
    private final String a = "attribute";
    private final String c = "collection";
    private final String o = "object";

    @Setter
    private boolean AllowErrorInPDM = false; // 是否允许PDM上有错误信息

    @Setter
    private boolean PrimaryKeyIsNull = false; // 是否允许PK为空

    @Setter
    private boolean ParserForeignKey = true; // 是否允许解析FK

    private int runThreadCount = 0; //

    public Parser() {
    }

    public PDM readPDMFile(String inFile) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        // dbf.setValidating(true);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            log.error("Parser异常", pce);
            return null;
        }
        Document doc = null;
        try {
            File f = new File(inFile);
            doc = db.parse(f);
        } catch (DOMException dom) {
            log.error("DOM异常", dom);
            return null;
        } catch (IOException ioe) {
            log.error("IO异常", ioe);
            return null;
        }
        Element rootModel = doc.getDocumentElement();
        Element eRootObject = get1stChildElement(rootModel, o, "RootObject");
        Element eChildren = get1stChildElement(eRootObject, c, "Children");
        Element eModel = get1stChildElement(eChildren, o, "Model");
        PDM tPDM = new PDM();
        tPDM.setId(eModel.getAttribute("Id"));
        tPDM.setName(getNodeValue(eModel, "Name"));
        tPDM.setCode(getNodeValue(eModel, "Code"));
        log.info("  解析PDM为:" + tPDM.getCode() + "(" + tPDM.getName() + ")");
        Element cDBMS = get1stChildElement(eModel, c, "DBMS");
        Element oShortcut = get1stChildElement(cDBMS, o, "Shortcut");
        tPDM.setDBMSCode(getNodeValue(oShortcut, "Code"));
        tPDM.setDBMSName(getNodeValue(oShortcut, "Name"));
        log.info("  解析DBMS为:" + tPDM.getDBMSCode() + "(" + tPDM.getDBMSName() + ")");
        Element eTables = get1stChildElement(eModel, c, "Tables");
        if (eTables == null) {
            if (AllowErrorInPDM) {
                log.error("  PDM中没有找到任何表");
                return tPDM;
            } else {
                throw new Exception("PDM中没有找到任何表");
            }
        } else {
            log.info("  开始解析Tables");
            // 多线程，每个线程取size个表
            int size = 90;
            int threadCount = 1;
            int position = 0;
            NodeList oTable = eTables.getElementsByTagNameNS(o, "Table");
            if (oTable.getLength() > size) {
                threadCount = oTable.getLength() / size + 1;
            }
            log.info("table num:" + oTable.getLength());
            log.info("thread num:" + threadCount);
            for (int j = 0; j < threadCount; j++) {
                ParseTableThread pt = new ParseTableThread();
                pt.tPDM = tPDM;
                pt.oTable = eTables.getElementsByTagNameNS(o, "Table");
                pt.startPosition = position;
                position = position + size;
                pt.size = size;
                pt.tParser = this;
                Thread td = new Thread(pt);
                td.start();
                addRunThread();
                log.info(String.valueOf(j));
            }
            while (this.runThreadCount > 0) {
                log.info("runThreadCount--" + runThreadCount);
            }
            // ParserTables(tPDM, eTables);
            if (ParserForeignKey) {
                log.info("  开始解析References");
                ParserReferences(tPDM, eModel);
            }
        }
        return tPDM;
    }

    private void ParserUsers(PDM tPDM, Element eUsers) {
        NodeList oUser = eUsers.getElementsByTagNameNS(o, "User");
        for (int i = 0; i < oUser.getLength(); i++) {
            Element eUser = (Element) oUser.item(i);
            PDMUser tPDMUser = new PDMUser();
            tPDMUser.setId(eUser.getAttribute("Id"));
            tPDMUser.setName(getNodeValue(eUser, "Name"));
            tPDMUser.setCode(getNodeValue(eUser, "Code"));
            tPDM.addUser(tPDMUser);
        }
    }

    private void ParserPhysicalDiagrams(PDM tPDM, Element ePhysicalDiagrams) {
        NodeList oPhysicalDiagram = ePhysicalDiagrams.getElementsByTagNameNS(o,
                "PhysicalDiagram");
        for (int i = 0; i < oPhysicalDiagram.getLength(); i++) {
            Element ePhysicalDiagram = (Element) oPhysicalDiagram.item(i);
            PDMPhysicalDiagram tPDMPhyDiag = new PDMPhysicalDiagram();
            tPDMPhyDiag.setId(ePhysicalDiagram.getAttribute("Id"));
            tPDMPhyDiag.setName(getNodeValue(ePhysicalDiagram, "Name"));
            tPDMPhyDiag.setCode(getNodeValue(ePhysicalDiagram, "Code"));
            Element eSymbols = get1stChildElement(ePhysicalDiagram, c,
                    "Symbols");
            NodeList cTableSymbol = eSymbols.getElementsByTagNameNS(o,
                    "TableSymbol");
            for (int j = 0; j < cTableSymbol.getLength(); j++) {
                Element eTableSymbol = (Element) cTableSymbol.item(j);
                Element eObject = get1stChildElement(eTableSymbol, c, "Object");
                Element eTable = get1stChildElement(eObject, o, "Table");
                tPDMPhyDiag.addTable(eTable.getAttribute("Ref"));
            }
            tPDM.addPhysicalDiagram(tPDMPhyDiag);
        }
    }

    private void ParserTables(PDM tPDM, Element eTables) throws Exception {
        NodeList oTable = eTables.getElementsByTagNameNS(o, "Table");
        for (int i = 0; i < oTable.getLength(); i++) {

            Element eTable = (Element) oTable.item(i);
            PDMTable tPDMTable = new PDMTable();
            tPDMTable.setId(eTable.getAttribute("Id"));
            tPDMTable.setName(getNodeValue(eTable, "Name"));
            tPDMTable.setCode(getNodeValue(eTable, "Code"));
            log.debug("  开始解析第" + (i + 1) + "个表:" + tPDMTable.getCode() +
                    "(" + tPDMTable.getName() + ")");
            Element eColumns = get1stChildElement(eTable, c, "Columns");
            if (eColumns == null) {
                if (AllowErrorInPDM) {
                    log.error("  表" + tPDMTable.getCode() + "(" +
                            tPDMTable.getName() + ")中没有找到任何字段");
                    continue;
                } else {
                    throw new Exception("表" + tPDMTable.getCode() + "(" +
                            tPDMTable.getName() + ")中没有找到任何字段");
                }
            } else {
                boolean errorInColumn = false;
                NodeList oColumn = eColumns.getElementsByTagNameNS(o, "Column");
                for (int j = 0; j < oColumn.getLength(); j++) {
                    Element eColumn = (Element) oColumn.item(j);
                    PDMColumn tPDMCol = new PDMColumn();
                    tPDMCol.setId(eColumn.getAttribute("Id"));
                    tPDMCol.setName(getNodeValue(eColumn, "Name"));
                    tPDMCol.setCode(getNodeValue(eColumn, "Code"));
                    log.debug(space4 + "开始解析第" + (j + 1) + "个字段:" +
                            tPDMCol.getCode() + "(" + tPDMCol.getName() +
                            ")");
                    try {
                        tPDMCol.setDataType(getNodeValue(eColumn, "DataType"));
                    } catch (Exception x) {
                        if (AllowErrorInPDM) {
                            errorInColumn = true;
                            log.error("  表" + tPDMTable.getCode() + "(" +
                                    tPDMTable.getName() + ")的" +
                                    "字段" + tPDMCol.getCode() + "(" +
                                    tPDMCol.getName() + ")没有指定数据类型");
                            break;
                        } else {
                            throw new Exception("表" + tPDMTable.getCode() + "(" +
                                    tPDMTable.getName() + ")的" +
                                    "字段" + tPDMCol.getCode() + "(" +
                                    tPDMCol.getName() + ")没有指定数据类型");
                        }
                    }
                    tPDMCol.setLength(getNodeValueInt(eColumn, "Length"));
                    tPDMCol.setPrecision(getNodeValueInt(eColumn, "Precision"));
                    tPDMCol.setMandatory(getNodeValueInt(eColumn, "Mandatory"));
                    tPDMCol.setDefaultValue(getNodeValueStr(eColumn,
                            "DefaultValue"));
                    tPDMCol.setLowValue(getNodeValueStr(eColumn, "LowValue"));
                    tPDMCol.setHighValue(getNodeValueStr(eColumn, "HighValue"));
                    tPDMTable.addColumn(tPDMCol);
                }
                if (errorInColumn) {
                    continue;
                }
                // Element eOwner = get1stChildElement(eTable, c, "Owner");
//                if(eOwner != null)
//                {
//                    Element eUser = get1stChildElement(eOwner, o, "User");
//                    tPDMTable.setUser(eUser.getAttribute("Ref"));
//                }
                Element eKeys = get1stChildElement(eTable, c, "Keys");
                if (eKeys == null) {
                    if (AllowErrorInPDM) {
                        log.error("  表" + tPDMTable.getCode() + "(" +
                                tPDMTable.getName() + ")" +
                                "没有任何键");
                    } else {
                        if (!PrimaryKeyIsNull) {
                            throw new Exception("表" + tPDMTable.getCode() + "(" +
                                    tPDMTable.getName() + ")" +
                                    "没有任何键");
                        }
                    }
                } else {
                    NodeList oKey = eKeys.getElementsByTagNameNS(o, "Key");
                    for (int j = 0; j < oKey.getLength(); j++) {
                        Element eKey = (Element) oKey.item(j);
                        PDMKey tPDMKey = new PDMKey();
                        tPDMKey.setId(eKey.getAttribute("Id"));
                        tPDMKey.setName(getNodeValue(eKey, "Name"));
                        tPDMKey.setCode(getNodeValue(eKey, "Code"));
                        log.debug(space4 + "开始解析第" + (j + 1) + "个键:" +
                                tPDMKey.getCode() + "(" + tPDMKey.getName() +
                                ")");
                        tPDMKey.setConstraintName(getNodeValueStr(eKey,
                                "ConstraintName"));
                        Element eKeyColumns = get1stChildElement(eKey, c,
                                "Key.Columns");
                        if (eKeyColumns == null) {
                            if (AllowErrorInPDM) {
                                log.error("  表" + tPDMTable.getCode() +
                                        "(" + tPDMTable.getName() + ")" +
                                        "的键" + tPDMKey.getCode() + "(" +
                                        tPDMKey.getName() + ")没有指定任何字段");
                                continue;
                            } else {
                                if (!PrimaryKeyIsNull) {
                                    throw new Exception("表" + tPDMTable.getCode() +
                                            "(" + tPDMTable.getName() + ")" +
                                            "的键" + tPDMKey.getCode() + "(" +
                                            tPDMKey.getName() + ")没有指定任何字段");
                                }
                            }
                        } else {
                            NodeList oKeyColumn = eKeyColumns.
                                    getElementsByTagNameNS(o,
                                            "Column");
                            for (int k = 0; k < oKeyColumn.getLength(); k++) {
                                Element eKeyColumn = (Element) oKeyColumn.item(k);
                                tPDMKey.addColumn(eKeyColumn.getAttribute("Ref"));
                            }
                        }
                        tPDMTable.addKey(tPDMKey);
                    }
                    try {
                        log.debug(space4 + "开始解析主键");
                        Element ePrimaryKey = get1stChildElement(eTable, c,
                                "PrimaryKey");
                        Element ePrimaryKeyK = get1stChildElement(ePrimaryKey,
                                o, "Key");
                        tPDMTable.setPrimaryKey(ePrimaryKeyK.getAttribute("Ref"));
                    } catch (Exception ex) {
                        if (!AllowErrorInPDM) {
                            if (!PrimaryKeyIsNull) {

                                throw new Exception("表" + tPDMTable.getCode() +
                                        "(" + tPDMTable.getName() + ")" +
                                        "没有主键");
                            }
                        } else {
                            log.error("  表" + tPDMTable.getCode() +
                                    "(" + tPDMTable.getName() + ")" +
                                    "没有主键");
                        }
                    }
                }
            }
            tPDM.addTable(tPDMTable);
        }
    }

    private void ParserReferences(PDM tPDM, Element eModel) throws Exception {
        Element eReferences = get1stChildElement(eModel, c, "References");
        if (eReferences != null) {
            NodeList oReference = eReferences.getElementsByTagNameNS(o, "Reference");
            for (int i = 0; i < oReference.getLength(); i++) {
                Element eReference = (Element) oReference.item(i);
                PDMReference tPDMRef = new PDMReference();
                tPDMRef.setId(eReference.getAttribute("Id"));
                tPDMRef.setName(getNodeValue(eReference, "Name"));
                tPDMRef.setCode(getNodeValue(eReference, "Code"));
                log.debug("  开始解析第" + (i + 1) + "个外键:" + tPDMRef.getCode() + "(" + tPDMRef.getName() + ")");
                tPDMRef.setConstraintName(getNodeValueStr(eReference, "ForeignKeyConstraintName"));
                tPDMRef.setUpdateConstraint(getNodeValueInt(eReference, "UpdateConstraint"));
                tPDMRef.setDeleteConstraint(getNodeValueInt(eReference, "DeleteConstraint"));
                tPDMRef.setImplementationType(getNodeValueStr(eReference, "ImplementationType"));
                Element eObject1 = get1stChildElement(eReference, c, "Object1");
                Element eTable1 = get1stChildElement(eObject1, o, "Table");
                tPDMRef.setParentTable(eTable1.getAttribute("Ref"));
                Element eObject2 = get1stChildElement(eReference, c, "Object2");
                Element eTable2 = get1stChildElement(eObject2, o, "Table");
                tPDMRef.setChildTable(eTable2.getAttribute("Ref"));
                Element eParentKey = get1stChildElement(eReference, c, "ParentKey");
                if (eParentKey == null) {
                    if (AllowErrorInPDM) {
                        log.error("  外键" + tPDMRef.getCode() + "(" + tPDMRef.getName() + ")" + "没有找到ParentKey");
                        continue;
                    } else {
                        throw new Exception("外键" + tPDMRef.getCode() + "(" + tPDMRef.getName() + ")" + "没有找到ParentKey");
                    }
                }
                Element eKey = get1stChildElement(eParentKey, o, "Key");
                tPDMRef.setParentKey(eKey.getAttribute("Ref"));
                Element eJoins = get1stChildElement(eReference, c, "Joins");
                NodeList oReferenceJoin = eJoins.getElementsByTagNameNS(o, "ReferenceJoin");
                boolean errorInJoin = false;
                for (int j = 0; j < oReferenceJoin.getLength(); j++) {
                    Element eReferenceJoin = (Element) oReferenceJoin.item(j);
                    PDMReferenceJoin tPDMRefJoin = new PDMReferenceJoin();
                    tPDMRefJoin.setId(eReferenceJoin.getAttribute("Id"));
                    Element eObject1_Ref = get1stChildElement(eReferenceJoin, c, "Object1");
                    Element eColumn1 = get1stChildElement(eObject1_Ref, o, "Column");
                    tPDMRefJoin.setParentTableCol(eColumn1.getAttribute("Ref"));
                    Element eObject2_Ref = get1stChildElement(eReferenceJoin, c, "Object2");
                    if (eObject2_Ref == null) {
                        if (AllowErrorInPDM) {
                            errorInJoin = true;
                            log.error("  外键" + tPDMRef.getCode() + "(" + tPDMRef.getName() + ")" + "没有找到相关的关联字段");
                            break;
                        } else {
                            throw new Exception("外键" + tPDMRef.getCode() + "(" + tPDMRef.getName() + ")" + "没有找到相关的关联字段");
                        }
                    }
                    Element eColumn2 = get1stChildElement(eObject2_Ref, o, "Column");
                    tPDMRefJoin.setChildTableCol(eColumn2.getAttribute("Ref"));
                    tPDMRef.addJoin(tPDMRefJoin);
                }
                if (errorInJoin) {
                    continue;
                }
                tPDM.addReference(tPDMRef);
            }
        }
    }

    private Element get1stChildElement(Element element, String ns, String localName) {
        NodeList nodeList = element.getElementsByTagNameNS(ns, localName);
        return (Element) nodeList.item(0);
    }

    // 用来取得标签中间的值(String)
    // 和getNodeValueStr不同的地方在于使用这个方法必须有返回值，如果没有则表示PDM文件错误
    private String getNodeValue(Element element, String TagName) throws DOMException {
        NodeList aName = element.getElementsByTagNameNS(a, TagName);
        Element eName = (Element) aName.item(0);
        Text tName = (Text) eName.getFirstChild();
        return tName.getNodeValue();
    }

    // 用来取得标签中间的值(String)，值可有可无
    private String getNodeValueStr(Element element, String TagName) throws DOMException {
        NodeList aName = element.getElementsByTagNameNS(a, TagName);
        if (aName.getLength() > 0) {
            Element eName = (Element) aName.item(0);
            Text tName = (Text) eName.getFirstChild();
            return tName.getNodeValue();
        } else {
            return null;
        }
    }

    // 用来取得标签中间的值(int)，值可有可无
    private int getNodeValueInt(Element element, String TagName) throws DOMException {
        String NodeValuestr = getNodeValueStr(element, TagName);
        if (NodeValuestr == null) {
            return 0;
        } else {
            return Integer.parseInt(NodeValuestr);
        }
    }

    /**
     * 增加正在运行的线程数
     */
    protected synchronized void addRunThread() {
        this.runThreadCount++;
        log.info("add:" + this.runThreadCount);
    }

    /**
     * 减少正在运行的线程数
     */
    protected synchronized void removeRunThread() {
        this.runThreadCount--;
        log.info("remove:" + this.runThreadCount);
    }
}
