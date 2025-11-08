package com.weixf;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception {

        SAXBuilder saxBuilder = new SAXBuilder();
        FileInputStream fileInputStream = new FileInputStream("D:\\MyWork\\files\\test2.xml");
        Document inDocument = saxBuilder.build(fileInputStream);
        Element rootElement = inDocument.getRootElement();
        Element body = rootElement.getChild("Body");
        List<Element> risks = body.getChild("Risks").getChildren();

        List<Element> DelRisk = new ArrayList<>();
        DelRisk.add(new Element("aaaa").setText("228289"));
        risks.removeAll(DelRisk);
    }
}
