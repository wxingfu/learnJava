package com.weixf.freemarker;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.weixf.freemarker.vo.RiskDutyInfoVO;
import com.weixf.freemarker.vo.RiskInfoVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @since 2022-10-10
 */
public class FreemarkerUtils {

    public static final String BAR_CODE_FILE_TEMP_NAME = "tempBarCodeFile";
    private static final String basePath = "D:/WorkSpaces/ideaProjects/maven-test/code-test-module/src/main/resources/";
    private static final String fontPath = "font/simsun.ttf";

    public static void main(String[] args) throws Exception {
        Map<String, Object> data = new HashMap<>();

        String param = "BarHeight=20&amp;BarRation=2&amp;BarWidth=1&amp;BgColor=FFFFFF&amp;ForeColor=000000&amp;XMargin=10&amp;YMargin=10";
        String barCodePath = FreemarkerUtils.generateBarCode("785744859254", param, basePath);

        data.put("barCodeUrl", basePath + barCodePath);
        data.put("appntName", "肖肥虾");
        data.put("appntPhone", "18897456321");
        data.put("gender", "0");
        data.put("polApplyDate", "2022年03月20日");
        data.put("insuredName", "什么时");
        data.put("prtNo", "109011686902121");
        data.put("sendDate", "2022年03月20日");
        data.put("UWIdea", "被保人：1、客户存在健康异常的原因");

        List<RiskInfoVO> riskInfoVOList = getRiskInfoVOList();
        data.put("riskInfo", riskInfoVOList);

        data.put("replyDate", "2020年03月27日");
        data.put("sysDate", "2022年09月20日");
        data.put("agentName", "网销虚拟代理人");
        data.put("agentCode", "520099E000");
        data.put("agentPhone", "15512348765");
        data.put("manageCom", "总公司营业总部");
        data.put("comAddress", "贵州省贵阳市高新区长岭南路178号茅台国际商务中心A栋13层");
        data.put("serviceHotline", "400-684-1888");

        data.put("isZJ", false);
        data.put("ZJAgentName", "中介人姓名");
        data.put("ZJAgentCode", "中介人代码");
        data.put("ZJAgentPhone", "中介人电话");
        data.put("ZJAgentComName", "中介机构");

        // String inFileName = "UWOpinionAdvice.ftl";
        String inFileName = "UWOpinionAdviceDynamic.ftl";
        String outFileName = "核保意见通知函Dynamic.pdf";
        String outFilePath = basePath + outFileName;
        System.out.println(outFilePath);
        FreemarkerUtils.generatePdf(basePath, inFileName, data, outFilePath);

    }

    private static List<RiskInfoVO> getRiskInfoVOList() {
        List<RiskDutyInfoVO> riskDutyInfoVOList = new ArrayList<>();

        RiskDutyInfoVO riskDutyInfoVO1 = new RiskDutyInfoVO();
        riskDutyInfoVO1.setRiskDutyName("身故或身体全残保险金");
        riskDutyInfoVO1.setInsuranceAmount("3000000");
        riskDutyInfoVO1.setStandPrem("1214.34");
        riskDutyInfoVO1.setAddPrem("0.00");
        riskDutyInfoVO1.setQuotaAmount("2000000");
        riskDutyInfoVO1.setFinalPrem("1014.34");

        RiskDutyInfoVO riskDutyInfoVO2 = new RiskDutyInfoVO();
        riskDutyInfoVO2.setRiskDutyName("航空意外身故或身体全残保险金");
        riskDutyInfoVO2.setInsuranceAmount("10000000");
        riskDutyInfoVO2.setStandPrem("23.34");
        riskDutyInfoVO2.setAddPrem("0.00");
        riskDutyInfoVO2.setQuotaAmount("3000000");
        riskDutyInfoVO2.setFinalPrem("13.34");

        RiskDutyInfoVO riskDutyInfoVO3 = new RiskDutyInfoVO();
        riskDutyInfoVO3.setRiskDutyName("水陆公共交通意外身故或身体全残保险金");
        riskDutyInfoVO3.setInsuranceAmount("6000000");
        riskDutyInfoVO3.setStandPrem("13.25");
        riskDutyInfoVO3.setAddPrem("0.00");
        riskDutyInfoVO3.setQuotaAmount("2000000");
        riskDutyInfoVO3.setFinalPrem("11.25");

        riskDutyInfoVOList.add(riskDutyInfoVO1);
        riskDutyInfoVOList.add(riskDutyInfoVO2);
        riskDutyInfoVOList.add(riskDutyInfoVO3);

        RiskInfoVO riskInfoVO = new RiskInfoVO();
        riskInfoVO.setRiskName("华贵大麦旗舰版定期寿险( 互联网专属）");
        riskInfoVO.setUwIdea("限额承保");
        riskInfoVO.setPayEndYear("30年");
        riskInfoVO.setRiskDutyInfoVOList(riskDutyInfoVOList);

        RiskInfoVO riskInfoVO2 = new RiskInfoVO();
        riskInfoVO2.setRiskName("华贵附加麦芽糖失能收入损失保险（互联网专属）");
        riskInfoVO2.setUwIdea("拒保");
        riskInfoVO2.setPayEndYear("30年");

        List<RiskDutyInfoVO> riskDutyInfoVOList2 = new ArrayList<>();
        RiskDutyInfoVO riskDutyInfoVO4 = new RiskDutyInfoVO();
        riskDutyInfoVO4.setRiskDutyName("附加失能保险");
        riskDutyInfoVO4.setInsuranceAmount("10000");
        riskDutyInfoVO4.setStandPrem("1214.34");
        riskDutyInfoVO4.setAddPrem("0.00");
        riskDutyInfoVO4.setQuotaAmount("7500");
        riskDutyInfoVO4.setFinalPrem("1014.34");

        riskDutyInfoVOList2.add(riskDutyInfoVO4);

        riskInfoVO2.setRiskDutyInfoVOList(riskDutyInfoVOList2);

        List<RiskInfoVO> riskInfoVOList = new ArrayList<>();
        riskInfoVOList.add(riskInfoVO);
        // riskInfoVOList.add(riskInfoVO2);
        return riskInfoVOList;
    }


    private static void generatePdf(String baseDir, String inFileName,
                                    Map<String, Object> data, String outFilePath) throws Exception {
        if (baseDir == null || data == null
                || inFileName == null || "".equals(inFileName)) {
            throw new IllegalArgumentException("请输入模板文件路径和模板全名称以及模板数据");
        }
        File fileBaseDir = new File(baseDir);
        String htmlContent = loadFtlHtml(fileBaseDir, inFileName, data);
        System.out.println(htmlContent);
        savePdf(htmlContent, outFilePath, "UTF-8");
    }

    public static String loadFtlHtml(File baseDir, String fileName, Map<String, Object> data) throws Exception {
        if (!baseDir.isDirectory()) {
            throw new IllegalArgumentException("请输入正确模板文件路径");
        }
        Configuration cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(baseDir);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setClassicCompatible(true);
            Template temp = cfg.getTemplate(fileName);
            StringWriter stringWriter = new StringWriter();
            temp.process(data, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new Exception("加载模板文件出错");
        }
    }


    public static void savePdf(String htmlContent, String filepath, String charset) throws Exception {
        Document document = new Document(PageSize.A4);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
            document.open();
            XMLWorkerFontProvider fontProvider =
                    new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
            fontProvider.register(fontPath);
            XMLWorkerHelper instance = XMLWorkerHelper.getInstance();
            instance.parseXHtml(writer,
                    document,
                    new ByteArrayInputStream(htmlContent.getBytes(charset)),
                    null,
                    Charset.forName(charset),
                    fontProvider);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("生成PDF出错");
        } finally {
            document.close();
        }
    }


    public static String generateBarCode(String sCode, String sParam, String barCodeOutPath) {
        FileOutputStream fileOutputStream = null;
        String t_Str = "";
        BarCode bcode = new BarCode(sCode);
        String fileName = sCode + BAR_CODE_FILE_TEMP_NAME + "." + BarCode.FORMAT_GIF;
        String barCodePath = barCodeOutPath + fileName;
        try {
            fileOutputStream = new FileOutputStream(barCodePath);
            // 获得条形码参数
            if (sParam != null && !sParam.equalsIgnoreCase("")) {
                String[] params = sParam.split("&");
                for (int j = 0; j < params.length; j++) {
                    // 获得条形码宽度
                    if (params[j].toLowerCase().startsWith("barheight")) {
                        t_Str = params[j].split("=")[1];
                        if (IsNumeric(t_Str)) {
                            bcode.setBarHeight(Integer.parseInt(t_Str));
                        }
                    }
                    // 获得条形码宽度
                    if (params[j].toLowerCase().startsWith("barwidth")) {
                        t_Str = params[j].split("=")[1];
                        if (IsNumeric(t_Str)) {
                            bcode.setBarWidth(Integer.parseInt(t_Str));
                        }
                    }
                    // 获得条形码粗细线条比例
                    if (params[j].toLowerCase().startsWith("barratio")) {
                        t_Str = params[j].split("=")[1];
                        if (IsNumeric(t_Str)) {
                            bcode.setBarRatio(Integer.parseInt(t_Str));
                        }
                    }
                    // 获得条形码图片背景色
                    if (params[j].toLowerCase().startsWith("bgcolor")) {
                        t_Str = params[j].split("=")[1];
                        bcode.setBgColor(t_Str);
                    }
                    // 获得条形码颜色
                    if (params[j].toLowerCase().startsWith("forecolor")) {
                        t_Str = params[j].split("=")[1];
                        bcode.setForeColor(t_Str);
                    }
                    // 获得条形码图片横向空白区长度
                    if (params[j].toLowerCase().startsWith("xmargin")) {
                        t_Str = params[j].split("=")[1];
                        if (IsNumeric(t_Str)) {
                            bcode.setXMargin(Integer.parseInt(t_Str));
                        }
                    }
                    // 获得条形码图片竖向空白区长度
                    if (params[j].toLowerCase().startsWith("ymargin")) {
                        t_Str = params[j].split("=")[1];
                        if (IsNumeric(t_Str)) {
                            bcode.setYMargin(Integer.parseInt(t_Str));
                        }
                    }
                }
            }
            bcode.setFormatType(BarCode.FORMAT_GIF);
            byte[] bytes = bcode.getBytes();
            fileOutputStream.write(bytes);
            System.out.println("条形码生成成功，临时路径为：" + barCodePath);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("生成条形码异常！");
            fileName = "";
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("关闭条形码输出流异常！");
                }
            }
        }
        return fileName;
    }


    public static boolean IsNumeric(String str) {
        boolean b = true;
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            b = false;
        }
        return b;
    }


}
