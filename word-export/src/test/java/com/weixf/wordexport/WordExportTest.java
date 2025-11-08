package com.weixf.wordexport;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.Cells;
import com.deepoove.poi.data.ChartMultiSeriesRenderData;
import com.deepoove.poi.data.ChartSingleSeriesRenderData;
import com.deepoove.poi.data.Charts;
import com.deepoove.poi.data.DocumentRenderData;
import com.deepoove.poi.data.Documents;
import com.deepoove.poi.data.Includes;
import com.deepoove.poi.data.MergeCellRule;
import com.deepoove.poi.data.NumberingFormat;
import com.deepoove.poi.data.NumberingRenderData;
import com.deepoove.poi.data.Numberings;
import com.deepoove.poi.data.Paragraphs;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.Tables;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.data.style.BorderStyle;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.plugin.comment.CommentRenderData;
import com.deepoove.poi.plugin.comment.CommentRenderPolicy;
import com.deepoove.poi.plugin.comment.Comments;
import com.deepoove.poi.plugin.highlight.HighlightRenderData;
import com.deepoove.poi.plugin.highlight.HighlightRenderPolicy;
import com.deepoove.poi.plugin.highlight.HighlightStyle;
import com.deepoove.poi.plugin.table.LoopColumnTableRenderPolicy;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.policy.DocumentRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import com.deepoove.poi.render.WhereDelegate;
import com.weixf.wordexport.entity.AddrModel;
import com.weixf.wordexport.entity.Author;
import com.weixf.wordexport.entity.Data;
import com.weixf.wordexport.entity.DetailData;
import com.weixf.wordexport.entity.DetailDataTable;
import com.weixf.wordexport.entity.Dog;
import com.weixf.wordexport.entity.Goods;
import com.weixf.wordexport.entity.Labor;
import com.weixf.wordexport.entity.LoopData;
import com.weixf.wordexport.entity.okr.KeyResult;
import com.weixf.wordexport.entity.okr.OKRData;
import com.weixf.wordexport.entity.okr.OKRItem;
import com.weixf.wordexport.entity.okr.Objective;
import com.weixf.wordexport.entity.okr.User;
import com.weixf.wordexport.entity.payment.PaymentData;
import com.weixf.wordexport.entity.resume.ExperienceData;
import com.weixf.wordexport.entity.resume.ResumeData;
import com.weixf.wordexport.entity.resume.ResumeDataIterable;
import com.weixf.wordexport.word.plugin.DetailTablePolicy;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class WordExportTest {


    public static String base;
    public static String templatePath;
    public static String imgPath;
    public static String markdownPath;
    public static String outPath;

    @Before
    public void init() {
        Properties properties = System.getProperties();
        String projectPath = (String) properties.get("user.dir");
        base = projectPath + "/src/main/resources/wordExport/";
        templatePath = base + "templates/";
        imgPath = base + "images/";
        markdownPath = base + "markdown/";
        outPath = base;
    }

    /**
     * 入门
     */
    @Test
    public void test1() {
        System.out.println(base);
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Hello Word!  POI-TL");
        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test1.docx");
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output1.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * map和Object
     */
    @Test
    public void test2() {

        Author author = new Author();
        author.setAge(1000);
        author.setName("哈利波特");

        Data data = new Data();
        data.setName("指环王");
        data.setStart("2021-08-05 12:12:12");
        data.setAuthor(author);


        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test2.docx");
        XWPFTemplate template = compile.render(data);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output2.docx");
            template.writeAndClose(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文本 {{var}}
     */
    @Test
    public void test3() {

        Map<String, Object> map = new HashMap<>();
        // map.put("name", "Sayi");
        // map.put("author", new TextRenderData("000000", "Sayi"));
        // map.put("link", new HyperlinkTextRenderData("website", "http://deepoove.com"));
        // map.put("anchor", new HyperlinkTextRenderData("anchortxt", "anchor:appendix1"));

        map.put("name", "Sayi");
        map.put("author", Texts.of("Sayi").color("FF0000").fontSize(50).fontFamily("华文楷体").italic().create());
        map.put("link", Texts.of("website").link("http://deepoove.com").create());
        map.put("anchor", Texts.of("anchortxt").anchor("appendix1").create());

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test3.docx");
        XWPFTemplate template = compile.render(map);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output3.docx");
            template.writeAndClose(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片 {{@var}}
     */
    @Test
    public void test4() throws FileNotFoundException {

        Map<String, Object> map = new HashMap<>();

        // 指定图片路径
        map.put("image", imgPath + "logo.png");

        // 设置图片宽高
        map.put("image1", Pictures.ofLocal(imgPath + "logo.png").size(120, 120).create());

        // 图片流
        map.put("streamImg", Pictures.ofStream(new FileInputStream(imgPath + "logo.jpeg"), PictureType.JPEG)
                .size(100, 120).create());

        // 网络图片(注意网络耗时对系统可能的性能影响)
        map.put("urlImg", Pictures.ofUrl("http://deepoove.com/images/icecream.png").size(100, 100).create());

        // svg图片
        map.put("svg", "https://img.shields.io/badge/jdk-1.6%2B-orange.svg");

        // java图片
        // map.put("buffered", Pictures.ofBufferedImage(bufferImage, PictureType.PNG)
        //         .size(100, 100).create());

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test4.docx");
        XWPFTemplate template = compile.render(map);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output4.docx");
            template.writeAndClose(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 表格 {{#var}}
     */
    @Test
    public void test5() {

        Map<String, Object> map = new HashMap<>();
        // 一个2行2列的表格
        map.put("table0", Tables.of(new String[][]{
                new String[]{"00", "01"},
                new String[]{"10", "11"}
        }).border(BorderStyle.DEFAULT).create());

        // 第0行居中且背景为蓝色的表格
        RowRenderData row00 = Rows.of("姓名", "学历").textColor("FFFFFF")
                .bgColor("4472C4").center().create();
        RowRenderData row11 = Rows.create("李四", "博士");
        map.put("table1", Tables.create(row00, row11));

        // 合并第1行所有单元格的表格
        RowRenderData row0 = Rows.of("列0", "列1", "列2").center().bgColor("4472C4").create();
        RowRenderData row1 = Rows.create("没有数据", null, null);
        // 合并坐标： 起始单元格，截止单元格
        MergeCellRule rule = MergeCellRule.builder().map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(1, 2)).build();
        map.put("table2", Tables.of(row0, row1).mergeRule(rule).create());


        // 测试插入文本和图片
        RowRenderData row22 = Rows.of("姓名", "学历").textColor("FFFFFF").bgColor("4472C4").center().create();

        TextRenderData textRenderData1 = Texts.of("Sayi").color("FF0000").fontSize(10).fontFamily("华文楷体").italic().create();
        TextRenderData textRenderData2 = Texts.of("AAAA").color("FFFF00").fontSize(10).fontFamily("华文楷体").italic().create();
        RowRenderData row33 = Rows.of(textRenderData1, textRenderData2).create();

        PictureRenderData pictureRenderData1 = Pictures.ofLocal(imgPath + "logo.png").size(80, 80).create();
        PictureRenderData pictureRenderData2 = Pictures.ofLocal(imgPath + "logo.png").size(100, 100).create();
        CellRenderData cellRenderData1 = Cells.of(pictureRenderData1).center().create();
        CellRenderData cellRenderData2 = Cells.of(pictureRenderData2).center().create();
        RowRenderData row44 = Rows.of(cellRenderData1, cellRenderData2).create();

        map.put("table3", Tables.create(row22, row33, row44));

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test5.docx");
        XWPFTemplate template = compile.render(map);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output5.docx");
            template.writeAndClose(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 列表 {{*var}}
     */
    @Test
    public void test6() {

        Map<String, Object> map = new HashMap<>();

        NumberingRenderData data =
                Numberings.of(NumberingFormat.DECIMAL)
                        .addItem("Plug-in grammar")
                        .addItem("Supports word text, pictures, table...")
                        .addItem("Not just templates")
                        .create();

        map.put("list", data);

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test6.docx");
        XWPFTemplate template = compile.render(map);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output6.docx");
            template.writeAndClose(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 区块 {{?var}} {{content}} {{/var}}
     */
    @Test
    public void test7() {

        Map<String, Object> map = new HashMap<>();

        map.put("announce", true);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "Sayi");
        map.put("person", map1);

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "Memories");
        list.add(map2);
        map2 = new HashMap<>();
        map2.put("name", "Sugar");
        list.add(map2);
        map2 = new HashMap<>();
        map2.put("name", "Last Dance");
        list.add(map2);
        map.put("songs", list);

        List<String> list2 = new ArrayList<>();
        list2.add("application/json");
        list2.add("application/xml");
        map.put("produces", list2);

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test7.docx");
        XWPFTemplate template = compile.render(map);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output7.docx");
            template.writeAndClose(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 嵌套 {{+var}}
     */
    @Test
    public void test8() {

        Map<String, Object> map = new HashMap<>();


        List<AddrModel> subData = new ArrayList<>();
        subData.add(new AddrModel("Hangzhou,China"));
        subData.add(new AddrModel("Shanghai,China"));
        map.put("nested", Includes.ofLocal(templatePath + "sub.docx").setRenderModel(subData).create());


        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test8.docx");
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output8.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 引用标签
     */
    @Test
    public void test9() {

        Map<String, Object> map = new HashMap<>();

        map.put("img", Pictures.ofLocal(imgPath + "logo.png").create());

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test9.docx");
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output9.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多系列图表
     * 指的是条形图（3D条形图）、柱形图（3D柱形图）、面积图（3D面积图）、折线图（3D折线图）、雷达图等
     */
    @Test
    public void test10() {

        Map<String, Object> map = new HashMap<>();


        ChartMultiSeriesRenderData chart = Charts
                .ofMultiSeries("中英文使用比较", new String[]{"中文", "English"})
                .addSeries("国家数量", new Double[]{15.0, 6.0})
                .addSeries("使用者数量", new Double[]{223.0, 119.0})
                .create();

        map.put("barChart", chart);

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test10.docx");
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output10.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单系列图表
     * 指的是饼图（3D饼图）、圆环图等
     */
    @Test
    public void test11() {

        Map<String, Object> map = new HashMap<>();


        ChartSingleSeriesRenderData pie = Charts
                .ofSingleSeries("ChartTitle", new String[]{"美国", "中国"})
                .series("countries", new Integer[]{9826675, 9596961})
                .create();

        map.put("pieChart", pie);

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test11.docx");
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output11.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 组合图表
     * 指的是由多系列图表（柱形图、折线图、面积图）组合而成的图表。
     */
    @Test
    public void test12() {

        Map<String, Object> map = new HashMap<>();

        ChartMultiSeriesRenderData comb = Charts
                .ofComboSeries("MyChart", new String[]{"中文", "English"})
                .addBarSeries("countries", new Double[]{15.0, 6.0})
                .addBarSeries("speakers", new Double[]{223.0, 119.0})
                .addBarSeries("NewBar", new Double[]{223.0, 119.0})
                .addLineSeries("youngs", new Double[]{323.0, 89.0})
                .addLineSeries("NewLine", new Double[]{123.0, 59.0}).create();

        map.put("combChart", comb);

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test12.docx");
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output12.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * SpringEL表达式
     */
    @Test
    public void test13() {

        Map<String, Object> map = new HashMap<>();

        map.put("name", "poi-tl");

        map.put("empty", "");

        map.put("sex", true);

        Date date = new Date();
        map.put("time", date);

        map.put("price", 100000);

        Dog[] dogArray = new Dog[10];
        for (int i = 0; i < 10; i++) {
            Dog dog = new Dog();
            dog.setName("二哈" + i);
            dog.setAge(i + 100);
            dogArray[i] = dog;
        }
        map.put("dogs", dogArray);

        LocalDate now = LocalDate.now();
        map.put("localDate", now);


        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL();
        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test13.docx", builder.build());
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output13.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spring表达式与区块对结合
     * 使用SpringEL时区块对的结束标签可以是：{{/}}
     */
    @Test
    public void test14() {

        Map<String, Object> map = new HashMap<>();

        map.put("desc", "");
        map.put("summary", "Find A Pet");

        List<String> list = new ArrayList<>();
        list.add("application/xml");
        map.put("produces", list);


        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL();
        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test14.docx", builder.build());
        XWPFTemplate template = compile.render(map);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output14.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模板生成模板
     */
    @Test
    public void test15() {

        Configure config = Configure.builder().bind("title", new DocumentRenderPolicy()).build();

        Map<String, Object> data = new HashMap<>();

        DocumentRenderData document = Documents.of()
                .addParagraph(Paragraphs.of("{{title}}").create())
                .addParagraph(Paragraphs.of("{{#table}}").create())
                .create();
        data.put("title", document);

        XWPFTemplate compile = XWPFTemplate.compile(templatePath + "test15.docx", config);
        XWPFTemplate template = compile.render(data);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outPath + "output15.docx");
            template.writeAndClose(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插件开发 与 使用
     */
    @Test
    public void test16() throws IOException {

        // where绑定policy
        Configure config = Configure.builder().bind("sea", new AbstractRenderPolicy<String>() {
            @Override
            public void doRender(RenderContext<String> context) {
                // anywhere
                XWPFRun where = context.getWhere();
                // anything
                String thing = context.getThing();
                // do 文本
                where.setText(thing, 0);
            }
        }).bind("sea_img", new AbstractRenderPolicy<String>() {
            @Override
            public void doRender(RenderContext<String> context) throws Exception {
                // anywhere delegate
                WhereDelegate where = context.getWhereDelegate();
                // any thing
                String thing = context.getThing();
                // do 图片
                FileInputStream stream = null;
                try {
                    stream = new FileInputStream(thing);
                    where.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, 80, 80);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
                // clear
                clearPlaceholder(context, false);
            }
        }).bind("sea_feature", new AbstractRenderPolicy<List<String>>() {
            @Override
            public void doRender(RenderContext<List<String>> context) throws Exception {
                // anywhere delegate
                WhereDelegate where = context.getWhereDelegate();
                // anything
                List<String> thing = context.getThing();

                // do 列表
                // where.renderNumbering(Numberings.of(thing.toArray(new String[] {})).create());

                String[] strings = thing.toArray(new String[]{});

                // do 列表
                where.renderNumbering(Numberings.ofDecimal(strings).create());


                // clear
                clearPlaceholder(context, true);
            }
        }).build();

        // 初始化where的数据
        HashMap<String, Object> args = new HashMap<>();
        args.put("sea", "Hello, world!");
        args.put("sea_img", imgPath + "aaa.png");
        args.put("sea_feature", Arrays.asList("面朝大海春暖花开", "今朝有酒今朝醉"));
        args.put("sea_location", Arrays.asList("日落：日落山花红四海", "花海：你想要的都在这里"));

        XWPFTemplate.compile(templatePath + "test16.docx", config).render(args).writeToFile(outPath + "output16.docx");

    }


    /**
     * LoopRowTableRenderPolicy 是一个特定场景的插件，根据集合数据循环表格行。
     * <p>
     * 货物明细和人工费在同一个表格中，货物明细需要展示所有货物，人工费需要展示所有费用。{{goods}} 是个标准的标签，将 {{goods}} 置于循环行的上一行，循环行设置要循环的标签和内容，注意此时的标签应该使用 [] ，以此来区别poi-tl的默认标签语法。同理，{{labors}} 也置于循环行的上一行。
     */
    @Test
    public void test17() {

        LoopData data = new LoopData();

        List<Goods> goods = new ArrayList<>();
        Goods good = new Goods();
        good.setCount(4);
        good.setName("墙纸");
        good.setDesc("书房卧室");
        good.setDiscount(1500);
        good.setPrice(400);
        good.setTax(new Random().nextInt(10) + 20);
        good.setTotalPrice(1600);
        good.setPicture(
                Pictures.ofLocal(imgPath + "earth.png")
                        .size(24, 24).create());
        goods.add(good);
        goods.add(good);
        goods.add(good);
        data.setGoods(goods);

        List<Labor> labors = new ArrayList<>();
        Labor labor = new Labor();
        labor.setCategory("油漆工");
        labor.setPeople(2);
        labor.setPrice(400);
        labor.setTotalPrice(1600);
        labors.add(labor);
        labors.add(labor);
        labors.add(labor);
        data.setLabors(labors);

        data.setTotal("1024");

        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("goods", policy)
                .bind("labors", policy)
                .build();

        XWPFTemplate template = XWPFTemplate.compile(templatePath + "test17.docx", config).render(data);
        try {
            FileOutputStream fos = new FileOutputStream(outPath + "output17.docx");
            template.writeAndClose(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * LoopColumnTableRenderPolicy 是一个特定场景的插件，根据集合数据循环表格列。要注意的是，由于文档宽度有限，因此模板列必须设置宽度，所有循环列将平分模板列的宽度。
     */
    @Test
    public void test18() {

        LoopData data = new LoopData();
        data.setTotal("总共：7200");

        List<Goods> goods = new ArrayList<>();
        Goods good = new Goods();
        good.setCount(4);
        good.setName("墙纸");
        good.setDesc("书房卧室");
        good.setDiscount(1500);
        good.setPrice(400);
        good.setTax(new Random().nextInt(10) + 20);
        good.setTotalPrice(1600);
        good.setPicture(Pictures.ofLocal(imgPath + "earth.png").size(24, 24).create());
        goods.add(good);
        goods.add(good);
        goods.add(good);
        goods.add(good);
        data.setGoods(goods);

        LoopColumnTableRenderPolicy policy = new LoopColumnTableRenderPolicy();
        Configure config = Configure.builder().bind("goods", policy).build();

        XWPFTemplate template = XWPFTemplate.compile(templatePath + "test18.docx", config).render(data);
        try {
            FileOutputStream fos = new FileOutputStream(outPath + "output18.docx");
            template.writeAndClose(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当需求中的表格更加复杂的时候，我们完全可以设计好那些固定的部分，
     * 将需要动态渲染的部分单元格交给自定义模板渲染策略。
     * poi-tl提供了抽象表格策略 DynamicTableRenderPolicy 来实现这样的功能。
     */
    @Test
    public void test19() {
        DetailDataTable datas = new DetailDataTable();

        DetailData detailTable = new DetailData();

        RowRenderData good = Rows.of("4", "墙纸", "书房卧室", "1500", "/", "400", "1600").center().create();
        List<RowRenderData> goods = Arrays.asList(good, good, good);

        RowRenderData labor = Rows.of("油漆工", "2", "200", "400").center().create();
        List<RowRenderData> labors = Arrays.asList(labor, labor, labor, labor);

        detailTable.setGoods(goods);
        detailTable.setLabors(labors);

        datas.setDetailData(detailTable);
        datas.setTotal(200);

        DetailTablePolicy policy = new DetailTablePolicy();
        Configure config = Configure.builder()
                .bind("detail_table", policy)
                .build();

        XWPFTemplate template = XWPFTemplate.compile(templatePath + "test19.docx", config).render(datas);
        try {
            FileOutputStream fos = new FileOutputStream(outPath + "output19.docx");
            template.writeAndClose(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * CommentRenderPolicy 是内置插件，提供了对批注完整功能的支持。
     */
    @Test
    public void test20() {

        Map<String, Object> data = new HashMap<>();
        data.put("title", "Sayi");
        data.put("pic", Pictures.ofLocal(imgPath + "sayi.png")
                .size(60, 60).create());
        data.put("author", "骆宾王作为“初唐四杰”之一，对荡涤六朝文学颓波，革新初唐浮靡诗风。他一生著作颇丰，是一个才华横溢的诗人。");
        data.put("interpretation1", "弯着脖子");
        data.put("interpretation2", "划动");

        CommentRenderPolicy policy = new CommentRenderPolicy();
        Configure config = Configure.builder()
                .bind("", policy)
                .build();
        try {
            XWPFTemplate.compile(templatePath + "test20.docx", config)
                    .render(data)
                    .writeToFile(outPath + "output20.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * CommentRenderPolicy 是内置插件，提供了对批注完整功能的支持。
     */
    @Test
    public void test20_1() {
        // comment
        CommentRenderData comment0 = Comments.of().signature("Sayi", "s", LocaleUtil.getLocaleCalendar()).addText(Texts.of("咏鹅").fontSize(20).bold().create())
                .comment(Documents.of()
                        .addParagraph(Paragraphs.of(Pictures.ofLocal(imgPath + "/logo.png").create()).create())
                        .create())
                .create();
        CommentRenderData comment1 = Comments.of().signature("Sayi", "s", LocaleUtil.getLocaleCalendar()).addText("骆宾王")
                .comment("骆宾王作为“初唐四杰”之一，对荡涤六朝文学颓波，革新初唐浮靡诗风。他一生著作颇丰，是一个才华横溢的诗人。")
                .create();
        CommentRenderData comment2 = Comments.of().signature("Sayi", "s", LocaleUtil.getLocaleCalendar()).addText("曲项").comment("弯着脖子").create();
        CommentRenderData comment3 = Comments.of().signature("Sayi", "s", LocaleUtil.getLocaleCalendar()).addText("拨").comment("划动").create();

        // document
        Documents.DocumentBuilder documentBuilder = Documents.of()
                .addParagraph(Paragraphs.of().addComment(comment0).center().create());
        documentBuilder.addParagraph(Paragraphs.of().addComment(comment1).center().create());
        documentBuilder.addParagraph(Paragraphs.of("鹅，鹅，鹅，").addComment(comment2).addText("向天歌。").center().create());
        documentBuilder.addParagraph(Paragraphs.of("白毛浮绿水，红掌").addComment(comment3).addText("清波。").center().create());

        // render
        try {
            XWPFTemplate.create(
                    documentBuilder.create(),
                    Style.builder()
                            .buildFontFamily("微软雅黑")
                            .buildFontSize(14f)
                            .build()
            ).writeToFile(outPath + "output20-1.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * HighlightRenderPolicy 插件对Word代码块进行高亮展示
     */
    @Test
    public void test21() {
        HighlightRenderData code = new HighlightRenderData();
        code.setCode("/**\n"
                + " * John Smith <john.smith@example.com>\n"
                + "*/\n"
                + "package l2f.gameserver.model;\n"
                + "\n"
                + "public abstract strictfp class L2Char extends L2Object {\n"
                + "  public static final Short ERROR = 0x0001;\n"
                + "\n"
                + "  public void moveTo(int x, int y, int z) {\n"
                + "    _ai = null;\n"
                + "    log(\"Should not be called\");\n"
                + "    if (1 > 5) { // wtf!?\n"
                + "      return;\n"
                + "    }\n"
                + "  }\n"
                + "}");
        code.setLanguage("java");
        code.setStyle(HighlightStyle.builder().withShowLine(true).withTheme("zenburn").build());
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);

        HighlightRenderPolicy policy = new HighlightRenderPolicy();
        Configure config = Configure.builder()
                .bind("code", policy)
                .build();

        try {
            XWPFTemplate.compile(templatePath + "test21.docx", config)
                    .render(data)
                    .writeToFile(outPath + "output21.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 有错误
     */
    @Test
    public void test22() throws IOException {
        // MarkdownRenderData code = new MarkdownRenderData();
        // Path path = Paths.get(markdownPath + "README.md");
        // byte[] bytes = Files.readAllBytes(path);
        // String s = new String(bytes);
        // code.setMarkdown(s);
        // // code.setStyle(MarkdownStyle.newStyle());
        //
        // Map<String, Object> data = new HashMap<>();
        // data.put("md", code);
        //
        // MarkdownRenderPolicy policy = new MarkdownRenderPolicy();
        // Configure config = Configure.builder()
        //         .bind("md", policy)
        //         .build();
        // XWPFTemplate.compile(templatePath + "test22.docx", config)
        //         .render(data)
        //         .writeToFile(outPath + "output22.docx");
    }


    /**
     * 需要生成这样的一份软件说明书：拥有封面和页眉，正文含有不同样式的文本，还有表格，列表和图片。
     */
    @Test
    public void testRenderTemplateByMap() throws Exception {
        // create table
        RowRenderData header = Rows.of("Word处理方案", "是否跨平台", "易用性").textColor("FFFFFF").bgColor("ff9800").center()
                .rowHeight(2.5f).create();
        RowRenderData row0 = Rows.create("Poi-tl", "纯Java组件，跨平台", "简单：模板引擎功能，并对POI进行了一些封装");
        RowRenderData row1 = Rows.create("Apache Poi", "纯Java组件，跨平台", "简单，缺少一些功能的封装");
        RowRenderData row2 = Rows.create("Freemarker", "XML操作，跨平台", "复杂，需要理解XML结构");
        RowRenderData row3 = Rows.create("OpenOffice", "需要安装OpenOffice软件", "复杂，需要了解OpenOffice的API");
        RowRenderData row4 = Rows.create("Jacob、winlib", "Windows平台", "复杂，不推荐使用");
        TableRenderData table = Tables.create(header, row0, row1, row2, row3, row4);

        Map<String, Object> datas = new HashMap<>();
        // text
        datas.put("header", "Deeply love what you love.");
        datas.put("name", "Poi-tl");
        datas.put("word", "模板引擎");
        datas.put("time", "2020-08-31");
        datas.put("what",
                "Java Word模板引擎： Minimal Microsoft word(docx) templating with {{template}} in Java. It works by expanding tags in a template using values provided in a JavaMap or JavaObject.");
        datas.put("author", Texts.of("Sayi卅一").color("000000").create());

        // hyperlink
        datas.put("introduce", Texts.of("http://www.deepoove.com")
                .link("http://www.deepoove.com").create());
        // picture
        datas.put("portrait", Pictures.ofLocal(imgPath + "sayi.png")
                .size(60, 60).create());
        // table
        datas.put("solution_compare", table);
        // numbering
        datas.put("feature",
                Numberings.ofDecimal()
                        .addItem("Plug-in grammar, add new grammar by yourself")
                        .addItem("Supports word text, local pictures, web pictures, table, list, header, footer...")
                        .addItem("Templates, not just templates, but also style templates")
                        .create());

        XWPFTemplate.compile(templatePath + "soft_description.docx").render(datas)
                .writeToFile(outPath + "out_soft_description.docx");
    }


    /**
     * 需要生成这样的一份流行的通知书：大部分数据是由表格构成的，
     * 需要创建一个订单的表格(图中第一个表格)，
     * 还需要在一个已有表格中，填充货物明细和人工费数据(图中第二个表格)。
     * <p>
     * 使用{{#order}}生成poi-tl提供的默认样式的表格，
     * 设置{{detail_table}}为自定义模板渲染策略(继承抽象表格策略DynamicTableRenderPolicy)，
     * 自定义已有表格中部分单元格的渲染。
     */
    @Test
    public void testPaymentExample() throws Exception {

        PaymentData datas = new PaymentData();

        datas.setNO("KB.6890451");
        datas.setID("ZHANG_SAN_091");
        datas.setTaitou("深圳XX家装有限公司");
        datas.setConsignee("丙丁");
        datas.setSubtotal("8000");
        datas.setTax("600");
        datas.setTransform("120");
        datas.setOther("250");
        datas.setUnpay("6600");
        datas.setTotal("总共：7200");

        RowRenderData header = Rows.of("日期", "订单编号", "销售代表", "离岸价", "发货方式", "条款", "税号").bgColor("F2F2F2").center()
                .textColor("7F7f7F").textFontFamily("Hei").textFontSize(9).create();
        RowRenderData row = Rows.of("2018-06-12", "SN18090", "李四", "5000元", "快递", "附录A", "T11090").center().create();
        BorderStyle borderStyle = new BorderStyle();
        borderStyle.setColor("A6A6A6");
        borderStyle.setSize(4);
        borderStyle.setType(XWPFTable.XWPFBorderType.SINGLE);
        TableRenderData table = Tables.ofA4MediumWidth().addRow(header).addRow(row).border(borderStyle).center()
                .create();
        datas.setOrder(table);

        DetailData detailTable = new DetailData();

        RowRenderData good = Rows.of("4", "墙纸", "书房+卧室", "1500", "/", "400", "1600").center().create();
        List<RowRenderData> goods = Arrays.asList(good, good, good);

        RowRenderData labor = Rows.of("油漆工", "2", "200", "400").center().create();
        List<RowRenderData> labors = Arrays.asList(labor, labor, labor, labor);

        detailTable.setGoods(goods);
        detailTable.setLabors(labors);

        datas.setDetailTable(detailTable);

        DetailTablePolicy policy = new DetailTablePolicy();
        Configure config = Configure.builder()
                .bind("detail_table", policy)
                .build();

        XWPFTemplate.compile(templatePath + "payment.docx", config)
                .render(datas)
                .writeToFile(outPath + "out_payment.docx");
    }

    /**
     * 需要制定一份OKR目标计划，业务目标和管理目标使用表格呈现，数量不等。
     * <p>
     * 将表格放到区块对中，当区块对取值为空集合或者null则不会展示目标表格，当区块对是一个非空集合则循环展示表格。
     */
    @Test
    public void testOKRExample() {
        OKRData data = new OKRData();
        User user = new User();
        user.setName("李彦宏");
        user.setDepart("百度CEO");
        data.setUser(user);

        List<OKRItem> objectives = new ArrayList<>();
        OKRItem item = new OKRItem();
        item.setIndex(1);
        item.setObject(new Objective("打造一个空前繁荣、强大的百度移动生态", "4%"));
        item.setKr1(new KeyResult("恪守安全可控、引人向上、忠诚服务、降低门槛的产品价值观，持续优化用户体验，提升百度系产品的总时长份额", "5%"));
        item.setKr2(new KeyResult("恪守良币驱逐劣币的商业价值观，实现在爱惜品牌口碑、优化用户体验基础上的收入增长", "1%"));
        item.setKr3(new KeyResult("产品要有创新，不能总是me too，me later", "1%"));
        objectives.add(item);

        item = new OKRItem();
        item.setIndex(2);
        item.setObject(new Objective("主流AI赛道模式跑通，实现可持续增长", "10%"));
        item.setKr1(new KeyResult("小度小度进入千家万户", "15%"));
        item.setKr2(new KeyResult("智能驾驶、智能交通找到规模化发展路径", "0%"));
        item.setKr3(new KeyResult("云及AI2B业务至少在个万亿级行业成为第一", "1%"));
        objectives.add(item);
        data.setObjectives(objectives);

        List<OKRItem> manageObjectives = new ArrayList<>();
        item = new OKRItem();
        item.setIndex(1);
        item.setObject(new Objective("提升百度的组织能力，有效支撑住业务规模的高速增长，不拖战略的后腿", "1%"));
        item.setKr1(new KeyResult("全公司成功推行OKR制度，有效降低沟通协调成本，激励大家为更高目标奋斗取得比KPI管理更好的业绩", "1%"));
        item.setKr2(new KeyResult("激发从ESTAFF到一线员工的主人翁意识，使之比2018年更有意愿有能力自我驱动管理好各自负责的领域", "0%"));
        item.setKr3(new KeyResult("建立合理的管理人员新陈代谢机制，打造出不少于2名业界公认的优秀领军人物", "1%"));
        manageObjectives.add(item);
        data.setManageObjectives(manageObjectives);

        data.setDate("2020-01-31");

        try {
            XWPFTemplate.compile(templatePath + "okr.docx")
                    .render(data)
                    .writeToFile(outPath + "out_okr.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 针对野生动物出具一份现状的调查报告，野生动物种类不确定，调查报告包含图片、文字和图表。
     * <p>
     * 不确定动物种类使用区块对{{?animals}}的循环功能实现，图片和图表如模板所示，使用引用标签，在可选文字标题位置输入标签。
     */
    @Test
    public void testAnimal() throws IOException {
        Map<String, Object> elephant = new HashMap<String, Object>() {
            {
                put("name", "大象");
                put("chart",
                        Charts.ofMultiSeries("大象生存现状", new String[]{"2018年", "2019年", "2020年"})
                                .addSeries("成年象", new Integer[]{500, 600, 700})
                                .addSeries("幼象", new Integer[]{200, 300, 400})
                                .addSeries("全部", new Integer[]{700, 900, 1100})
                                .create());

            }
        };
        Map<String, Object> giraffe = new HashMap<String, Object>() {
            {
                put("name", "长颈鹿");
                put("picture", Pictures.ofLocal(imgPath + "lu.png").size(100, 120).create());
                put("chart",
                        Charts.ofMultiSeries("长颈鹿生存现状", new String[]{"2018年", "2019年", "2020年"})
                                .addSeries("成年鹿", new Integer[]{500, 600, 700})
                                .addSeries("幼鹿", new Integer[]{200, 300, 400})
                                .create());

            }
        };
        List<Map<String, Object>> animals = Arrays.asList(elephant, giraffe);
        XWPFTemplate.compile(templatePath + "animal.docx").render(new HashMap<String, Object>() {
            {
                put("animals", animals);
            }
        }).writeToFile(outPath + "out_animal.docx");
    }


    /**
     * 颁发一张由特殊样式图片、姓名、日期构成的证书奖状。
     * <p>
     * 图片格式和布局由模板指定，图片使用引用标签替换即可。
     */
    @Test
    public void testRenderTextBox() throws Exception {
        Map<String, Object> datas = new HashMap<String, Object>() {
            {
                put("name", "Poi-tl");
                put("department", "DEEPOOVE.COM");
                put("y", "2020");
                put("m", "8");
                put("d", "19");
                put("img", Pictures.ofLocal(imgPath + "lu.png")
                        .size(120, 120).create());
            }
        };

        XWPFTemplate.compile(templatePath + "certificate.docx").render(datas)
                .writeToFile(outPath + "out_certificate.docx");

    }

    /**
     * 嵌套
     * 工作经历可以使用嵌套标签，我们制作两个模板，一套主模板简历resume.docx，一套为文档模板segment.docx。
     */
    @Test
    public void testResumeExample() {
        ResumeData datas = new ResumeData();
        datas.setPortrait(Pictures.ofLocal(imgPath + "sayi.png")
                .size(100, 100).create());
        datas.setName("卅一");
        datas.setJob("BUG工程师");
        datas.setPhone("18080809090");
        datas.setSex("男");
        datas.setProvince("杭州");
        datas.setBirthday("2000.08.19");
        datas.setEmail("adasai90@gmail.com");
        datas.setAddress("浙江省杭州市西湖区古荡1号");
        datas.setEnglish("CET6 620");
        datas.setUniversity("美国斯坦福大学");
        datas.setProfession("生命工程");
        datas.setEducation("学士");
        datas.setRank("班级排名 1/36");
        datas.setHobbies("音乐、画画、乒乓球、旅游、读书\nhttps://github.com/Sayi");

        // 技术栈部分
        TextRenderData textRenderData = new TextRenderData("SpringBoot、SprigCloud、Mybatis");
        Style style = Style.builder().buildFontSize(10).buildColor("7F7F7F").buildFontFamily("微软雅黑").build();
        textRenderData.setStyle(style);
        datas.setStack(Numberings.of(textRenderData, textRenderData, textRenderData).create());

        // 模板文档循环合并
        List<ExperienceData> experiences = new ArrayList<>();
        ExperienceData data0 = new ExperienceData();
        data0.setCompany("香港微软");
        data0.setDepartment("Office 事业部");
        data0.setTime("2001.07-2020.06");
        data0.setPosition("BUG工程师");
        textRenderData = new TextRenderData("负责生产BUG，然后修复BUG，同时有效实施招聘行为");
        textRenderData.setStyle(style);
        data0.setResponsibility(new NumberingRenderData(NumberingFormat.LOWER_ROMAN, textRenderData, textRenderData));
        ExperienceData data1 = new ExperienceData();
        data1.setCompany("自由职业");
        data1.setDepartment("OpenSource 项目组");
        data1.setTime("2015.07-2020.06");
        data1.setPosition("研发工程师");
        textRenderData = new TextRenderData("开源项目的迭代和维护工作");
        textRenderData.setStyle(style);
        TextRenderData textRenderData1 = new TextRenderData("持续集成、Swagger文档等工具调研");
        textRenderData1.setStyle(style);
        data1.setResponsibility(Numberings.of(NumberingFormat.LOWER_ROMAN).addItem(textRenderData)
                .addItem(textRenderData1).addItem(textRenderData).create());
        experiences.add(data0);
        experiences.add(data1);
        experiences.add(data0);
        experiences.add(data1);
        datas.setExperience(
                Includes.ofLocal(templatePath + "segment.docx")
                        .setRenderModel(experiences).create());

        try {
            XWPFTemplate.compile(templatePath + "resume.docx")
                    .render(datas)
                    .writeToFile(outPath + "out_resume.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResumeExampleIterable() {

        ResumeDataIterable datas = new ResumeDataIterable();

        datas.setPortrait(Pictures.ofLocal(imgPath + "sayi.png")
                .size(100, 100).create());
        datas.setName("卅一");
        datas.setJob("BUG工程师");
        datas.setPhone("18080809090");
        datas.setSex("男");
        datas.setProvince("杭州");
        datas.setBirthday("2000.08.19");
        datas.setEmail("adasai90@gmail.com");
        datas.setAddress("浙江省杭州市西湖区古荡1号");
        datas.setEnglish("CET6 620");
        datas.setUniversity("美国斯坦福大学");
        datas.setProfession("生命工程");
        datas.setEducation("学士");
        datas.setRank("班级排名 1/36");
        datas.setHobbies("音乐、画画、乒乓球、旅游、读书\nhttps://github.com/Sayi");

        // 技术栈部分
        TextRenderData textRenderData = new TextRenderData("SpringBoot、SprigCloud、Mybatis");
        Style style = Style.builder()
                .buildFontSize(10)
                .buildColor("7F7F7F")
                .buildFontFamily("微软雅黑")
                .build();
        textRenderData.setStyle(style);
        datas.setStack(Numberings.of(textRenderData, textRenderData, textRenderData).create());

        /*
         * {{?experiences}} {{company}} {{department}} {{time}} {{position}}
         * {{*responsibility}} {{/experiences}}
         */
        List<ExperienceData> experiences = new ArrayList<>();

        ExperienceData data0 = new ExperienceData();
        data0.setCompany("香港微软");
        data0.setDepartment("Office 事业部");
        data0.setTime("2001.07-2020.06");
        data0.setPosition("BUG工程师");
        textRenderData = new TextRenderData("负责生产BUG，然后修复BUG，同时有效实施招聘行为");
        textRenderData.setStyle(style);
        data0.setResponsibility(Numberings.of(textRenderData, textRenderData).create());

        ExperienceData data1 = new ExperienceData();
        data1.setCompany("自由职业");
        data1.setDepartment("OpenSource 项目组");
        data1.setTime("2015.07-2020.06");
        data1.setPosition("研发工程师");
        textRenderData = new TextRenderData("开源项目的迭代和维护工作");
        textRenderData.setStyle(style);
        TextRenderData textRenderData1 = new TextRenderData("持续集成、Swagger文档等工具调研");
        textRenderData1.setStyle(style);
        data1.setResponsibility(Numberings.of(textRenderData, textRenderData1, textRenderData).create());

        experiences.add(data0);
        experiences.add(data1);
        experiences.add(data0);
        experiences.add(data1);

        datas.setExperiences(experiences);

        XWPFTemplate template =
                XWPFTemplate.compile(templatePath + "iterable_resume.docx").render(datas);
        try {
            FileOutputStream out = new FileOutputStream(outPath + "out_resume_iterable.docx");
            template.writeAndClose(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
