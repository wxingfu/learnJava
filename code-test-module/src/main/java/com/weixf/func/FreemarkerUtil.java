package com.weixf.func;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @since 2022-10-09
 */
public class FreemarkerUtil {

    private final String basePath = "D:/WorkSpaces/ideaProjects/maven-test/code-test-module/src/main/resources/";
    private final String fontPath = "font/simsun.ttf";


    public static void main(String[] args) throws Exception {
        FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
        Map<String, Object> data = new HashMap<>();
        String pdf = freemarkerUtil.createPdf(data);
        System.out.println(pdf);
    }

    private String createPdf(Map<String, Object> data) throws Exception {
        // 获取本类路径
        String rootPath = "";
        String pdfFilePath = "";
        try {
            rootPath = getTemplatePath();
            // 获取本类路径下的HTML模板文件
            String templatePath = rootPath + "UWOpinionAdvice.html";
            File htmlTemplate = new File(templatePath);
            // 根据模板生成html文件
            String content = createHtml(htmlTemplate, data);
            // 生成pdf
            pdfFilePath = rootPath + "核保意见通知函bak1.pdf";
            html2pdf(content, pdfFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回pdf文件路径
        return pdfFilePath;
    }

    private void html2pdf(String content, String outPath) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
        document.open();
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(fontPath);
        XMLWorkerHelper instance = XMLWorkerHelper.getInstance();
        instance.parseXHtml(writer,
                document,
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                null,
                Charset.defaultCharset(),
                fontImp);
        document.close();
    }

    private String createHtml(File file, Map<String, Object> data) throws Exception {
        // 获取模板文件路径
        String path = file.getParentFile().getPath();
        // FreeMarker配置
        Configuration configuration = new Configuration();
        Writer writer = new StringWriter();
        try {
            // 加载模板文件路径
            configuration.setDirectoryForTemplateLoading(new File(path));
            // 获取模板文件
            Template template = configuration.getTemplate(file.getName());
            template.setEncoding("UTF-8");
            // 填充数据，生成HTML文件
            template.process(data, writer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("模板内容填充出错！");
        }
        return writer.toString();
    }

    private String getTemplatePath() throws Exception {
        return basePath;
    }
}
