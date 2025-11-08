package com.weixf.schema.maker.pdm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @since 2022-01-21
 */
@Slf4j
@Component
public class ParseTableThread implements Runnable {

    private static final String space4 = "    ";
    // NameSpace
    private final String a = "attribute";
    private final boolean AllowErrorInPDM = true; // 是否允许PDM上有错误信息
    private final boolean PrimaryKeyIsNull = true; // 是否允许PK为空
    private final boolean ParserForeignKey = true; // 是否允许解析FK
    public PDM tPDM;
    public NodeList oTable;
    public int startPosition;
    public int size;
    protected Parser tParser;

    public ParseTableThread() {
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

    private void ParserTables(PDM tPDM, NodeList oTable, int startPosition, int size) throws Exception {
        // NodeList oTable = eTables.getElementsByTagNameNS(o, "Table");
        for (int i = startPosition; i < oTable.getLength() && i < startPosition + size; i++) {
            Element eTable = (Element) oTable.item(i);
            PDMTable tPDMTable = new PDMTable();
            tPDMTable.setId(eTable.getAttribute("Id"));
            tPDMTable.setName(getNodeValue(eTable, "Name"));
            tPDMTable.setCode(getNodeValue(eTable, "Code"));
            log.debug("  开始解析第" + (i + 1) + "个表:" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")");
            String c = "collection";
            Element eColumns = get1stChildElement(eTable, c, "Columns");
            if (eColumns == null) {
                if (AllowErrorInPDM) {
                    log.error("  表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")中没有找到任何字段");
                    continue;
                } else {
                    throw new Exception("表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")中没有找到任何字段");
                }
            } else {
                boolean errorInColumn = false;
                String o = "object";
                NodeList oColumn = eColumns.getElementsByTagNameNS(o, "Column");
                for (int j = 0; j < oColumn.getLength(); j++) {
                    Element eColumn = (Element) oColumn.item(j);
                    PDMColumn tPDMCol = new PDMColumn();
                    tPDMCol.setId(eColumn.getAttribute("Id"));
                    tPDMCol.setName(getNodeValue(eColumn, "Name"));
                    tPDMCol.setCode(getNodeValue(eColumn, "Code"));
                    log.debug(space4 + "开始解析第" + (j + 1) + "个字段:" + tPDMCol.getCode() + "(" + tPDMCol.getName() + ")");
                    try {
                        tPDMCol.setDataType(getNodeValue(eColumn, "DataType"));
                    } catch (Exception x) {
                        if (AllowErrorInPDM) {
                            errorInColumn = true;
                            log.error("  表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")的" + "字段" + tPDMCol.getCode() + "(" + tPDMCol.getName() + ")没有指定数据类型");
                            break;
                        } else {
                            throw new Exception("表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")的" + "字段" + tPDMCol.getCode() + "(" + tPDMCol.getName() + ")没有指定数据类型");
                        }
                    }
                    tPDMCol.setLength(getNodeValueInt(eColumn, "Length"));
                    tPDMCol.setPrecision(getNodeValueInt(eColumn, "Precision"));
                    tPDMCol.setMandatory(getNodeValueInt(eColumn, "Mandatory"));
                    tPDMCol.setDefaultValue(getNodeValueStr(eColumn, "DefaultValue"));
                    tPDMCol.setLowValue(getNodeValueStr(eColumn, "LowValue"));
                    tPDMCol.setHighValue(getNodeValueStr(eColumn, "HighValue"));
                    tPDMTable.addColumn(tPDMCol);
                }
                if (errorInColumn) {
                    continue;
                }
                Element eKeys = get1stChildElement(eTable, c, "Keys");
                if (eKeys == null) {
                    if (AllowErrorInPDM) {
                        log.error("  表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")" + "没有任何键");
                    } else {
                        if (!PrimaryKeyIsNull) {
                            throw new Exception("表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")" + "没有任何键");
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
                        log.debug(space4 + "开始解析第" + (j + 1) + "个键:" + tPDMKey.getCode() + "(" + tPDMKey.getName() + ")");
                        tPDMKey.setConstraintName(getNodeValueStr(eKey, "ConstraintName"));
                        Element eKeyColumns = get1stChildElement(eKey, c, "Key.Columns");
                        if (eKeyColumns == null) {
                            if (AllowErrorInPDM) {
                                log.error("  表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")" + "的键" + tPDMKey.getCode() + "(" + tPDMKey.getName() + ")没有指定任何字段");
                                continue;
                            } else {
                                if (!PrimaryKeyIsNull) {
                                    throw new Exception("表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")" + "的键" + tPDMKey.getCode() + "(" + tPDMKey.getName() + ")没有指定任何字段");
                                }
                            }
                        } else {
                            NodeList oKeyColumn = eKeyColumns.getElementsByTagNameNS(o, "Column");
                            for (int k = 0; k < oKeyColumn.getLength(); k++) {
                                Element eKeyColumn = (Element) oKeyColumn.item(k);
                                tPDMKey.addColumn(eKeyColumn.getAttribute("Ref"));
                            }
                        }
                        tPDMTable.addKey(tPDMKey);
                    }
                    try {
                        log.debug(space4 + "开始解析主键");
                        Element ePrimaryKey = get1stChildElement(eTable, c, "PrimaryKey");
                        Element ePrimaryKeyK = get1stChildElement(ePrimaryKey, o, "Key");
                        tPDMTable.setPrimaryKey(ePrimaryKeyK.getAttribute("Ref"));
                    } catch (Exception ex) {
                        if (!AllowErrorInPDM) {
                            if (!PrimaryKeyIsNull) {
                                throw new Exception("表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")" + "没有主键");
                            }
                        } else {
                            log.error("  表" + tPDMTable.getCode() + "(" + tPDMTable.getName() + ")" + "没有主键");
                        }
                    }
                }
            }
            tPDM.addTable(tPDMTable);
        }
    }

    public void run() {
        try {
            ParserTables(tPDM, oTable, startPosition, size);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tParser.removeRunThread();
    }

}
