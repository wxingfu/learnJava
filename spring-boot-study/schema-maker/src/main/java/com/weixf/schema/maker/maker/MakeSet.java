package com.weixf.schema.maker.maker;


import com.weixf.schema.maker.table.Column;
import com.weixf.schema.maker.table.Table;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @since 2022-01-21
 */
@Component
public class MakeSet {

    private static final String space4_1 = "    ";
    private static final String space4_2 = space4_1 + space4_1;
    private static final String space4_3 = space4_2 + space4_1;
    private static final String space4_4 = space4_3 + space4_1;
    private static final String space4_5 = space4_3 + space4_2;
    private String packageName;
    private String schemaOutputPATH;
    private String outputPackagePATH;
    private String DBName;
    @Setter
    private boolean UserInfo = false;

    public MakeSet() {
    }

    public MakeSet(String packageName, String schemaOutputPATH, String outputPackagePATH, String DBName) {
        this.packageName = packageName;
        this.schemaOutputPATH = schemaOutputPATH;
        this.outputPackagePATH = outputPackagePATH;
        this.DBName = DBName;
    }

    private String getTimestamp() {
        String pattern = "yyyy-MM-dd HH:mm:ss SSS";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        return df.format(today);
    }

    public void create(Table tTable) {
        String TableName = tTable.getCode();
        PrintWriter out = null;
        String filePath = schemaOutputPATH + outputPackagePATH + "vschema/";
        String ClassName = TableName + "Set";
        String FileName = ClassName + ".java";
        String SchemaName = TableName + "Schema";
        try {
            // 创建目录
            File dir = new File(filePath);
            if (!dir.exists() || !dir.isDirectory()) {
                boolean b = dir.mkdirs();
            }
            Path path = new File(filePath + FileName).toPath();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(path), "GBK");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            out = new PrintWriter(bufferedWriter, true);
            // 文件头信息
            out.println("/**");
            out.println(" * Copyright (c) " + getTimestamp().substring(0, 4) + " Sinosoft Co.,LTD.");
            out.println(" * All right reserved.");
            out.println(" */");
            out.println();
            // @Package
            out.println("package " + packageName + ".vschema;");
            out.println();
            // @Import
            out.println("import " + packageName + ".schema." + SchemaName + ";");
            out.println("import com.sinosoft.utility.SysConst;");
            out.println("import com.sinosoft.utility.CError;");
            out.println("import com.sinosoft.utility.CErrors;");
            out.println("import com.sinosoft.utility.SchemaSet;");
            out.println();
            // 类信息
            out.println("/**");
            out.println(" * <p>自动生成的文件，不可手工修改！</p>");
            out.println(" * <p>ClassName: " + ClassName + " </p>");
            out.println(" * <p>Description: DB层 Set 类文件 </p>");
            out.println(" * <p>Company: Sinosoft Co.,LTD </p>");
            out.println(" * @Database: " + DBName);
            out.println(" * @author: Makerx");
            out.println(" * @CreateDatetime: " + getTimestamp());
            if (UserInfo) {
                Properties props = System.getProperties();
                out.println(" * @vm: " + props.getProperty("java.vm.name") + "(build " + props.getProperty("java.vm.version") + ", " + props.getProperty("java.vm.vendor") + ")");
                out.println(" * @os: " + props.getProperty("os.name") + "(" + props.getProperty("os.arch") + ")");
                out.println(" * @creator: " + props.getProperty("user.name") + "(" + props.getProperty("user.country") + ")");
            }
            out.println(" */");
            // @Begin
            out.println("public class " + ClassName + " extends SchemaSet{");
            // 生成方法
            out.println(space4_1 + "// @Method");
            // 生成 add 方法
            add(out, ClassName, SchemaName);
            out.println();
            // 生成 remove 方法
            remove(out, SchemaName);
            out.println();
            // 生成 get 方法
            get(out, SchemaName);
            out.println();
            // 生成 set 方法
            set(out, ClassName, SchemaName);
            out.println();
            // xjh Add 2006-08-28 生成 copy 方法
            // 说明:此copy方法不是实现Clone，而是复制数据；为了避免未来实现真正的clone,方法名定为copy
            copy(out, ClassName);
            out.println();
            // 生成 encode 方法
            encode(TableName, out, SchemaName);
            out.println();
            // 生成 decode 方法
            decode(out, SchemaName);
            // 生成结尾
            out.println("}");
        } // end of try
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void orderby(PrintWriter out, Table tTable) {
        for (int i = 0; i < tTable.getColumnNum(); i++) {
            Column f = tTable.getColumn(i);
            String FieldCode = f.getCode();
            String FieldType = f.getDataType();
            boolean direction = true;
            for (int j = 0; j < 2; j++) {
                if (j == 1) {
                    direction = false;
                }
                out.println(space4_1 + "/**");
                if (direction) {
                    out.println(space4_1 + " * 按照字段" + FieldCode + "(" + f.getName() + ")进行ASE排序");
                } else {
                    out.println(space4_1 + " * 按照字段" + FieldCode + "(" + f.getName() + ")进行排序，");
                    out.println(space4_1 + " * 如果direction为true,则进行ASE排序，否则进行DESC排序");
                    out.println(space4_1 + " *");
                    out.println(space4_1 + " * @param direction boolean");
                }
                out.println(space4_1 + " */");
                out.println(space4_1 + "public void orderby" + FieldCode + "(" + (direction ? "" : "boolean direction") + "){");
                if (!direction) {
                    out.println(space4_2 + "if(direction){");
                    out.println(space4_3 + "orderby" + FieldCode + "();");
                    out.println(space4_3 + "return;");
                    out.println(space4_2 + "}");
                }
                out.println(space4_2 + "int iPos = 0;");
                switch (FieldType) {
                    case "int":
                        out.println(space4_2 + "int iTemp = 0;");
                        break;
                    case "double":
                        out.println(space4_2 + "double iTemp = 0d;");
                        break;
                    case "float":
                        out.println(space4_2 + "float iTemp = 0f;");
                        break;
                    case "String":
                        out.println(space4_2 + "String iTemp = null;");
                        break;
                    case "Date":
                        out.println(space4_2 + "Date iTemp = null;");
                        break;
                }
                out.println(space4_2 + tTable.getCode() + "Schema tSchema = null;");
                out.println(space4_2 + "for(int i = 1; i < this.size(); i++){");
                out.println(space4_3 + "iPos = i;");
                out.println(space4_3 + "iTemp = this.get(i).get" + FieldCode + "();");
                out.println(space4_3 + "for(int j = i + 1; j <= this.size(); j++){");
                if (FieldType.equals("int") || FieldType.equals("double") || FieldType.equals("float")) {
                    out.println(space4_4 + "if(this.get(j).get" + FieldCode + "() " + (direction ? "<" : ">") + " iTemp){");
                    out.println(space4_5 + "iPos = j;");
                    out.println(space4_5 + "iTemp = this.get(j).get" + FieldCode + "();");
                    out.println(space4_4 + "}");
                }
                if (FieldType.equals("String") || FieldType.equals("Date")) {
                    if (direction) {
                        out.println(space4_4 + "if(iTemp == null){");
                        out.println(space4_5 + "continue;");
                        out.println(space4_4 + "}");
                        out.println(space4_4 + "if(this.get(j).get" + FieldCode + "() == null || this.get(j).get" + FieldCode + "().compareTo(iTemp) < 0){");
                        out.println(space4_5 + "iPos = j;");
                        out.println(space4_5 + "iTemp = this.get(j).get" + FieldCode + "();");
                        out.println(space4_4 + "}");
                    } else {
                        out.println(space4_4 + "if(this.get(j).get" + FieldCode + "() == null){");
                        out.println(space4_5 + "continue;");
                        out.println(space4_4 + "}");
                        out.println(space4_4 + "if(iTemp == null || iTemp.compareTo(this.get(j).get" + FieldCode + "()) < 0){");
                        out.println(space4_5 + "iPos = j;");
                        out.println(space4_5 + "iTemp = this.get(j).get" + FieldCode + "();");
                        out.println(space4_4 + "}");
                    }
                }
                out.println(space4_3 + "}");
                out.println(space4_3 + "if(i != iPos){");
                out.println(space4_4 + "tSchema = this.get(i);");
                out.println(space4_4 + "this.set(i, this.get(iPos));");
                out.println(space4_4 + "this.set(iPos, tSchema);");
                out.println(space4_3 + "}");
                out.println(space4_2 + "}");
                out.println(space4_1 + "}");
                out.println();
            }
        }
    }

    private void copy(PrintWriter out, String ClassName) {
        out.println(space4_1 + "public boolean copy(" + ClassName + " set){");
        out.println(space4_2 + "if(set == null){");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "this.clear();");
        out.println(space4_2 + "for(int i = 1; i <= set.size(); i++){");
        out.println(space4_3 + "this.add(set.get(i).getSchema());");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return true;");
        out.println(space4_1 + "}");
        out.println();
        out.println(space4_1 + "public " + ClassName + " copy(){");
        out.println(space4_2 + ClassName + " set = new " + ClassName + "();");
        out.println(space4_2 + "for(int i = 1; i <= this.size(); i++){");
        out.println(space4_3 + "set.add(this.get(i).getSchema());");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return set;");
        out.println(space4_1 + "}");
    }

    private void add(PrintWriter out, String ClassName, String SchemaName) {
        out.println(space4_1 + "public boolean add(" + SchemaName + " schema){");
        out.println(space4_2 + "return super.add(schema);");
        out.println(space4_1 + "}");
        out.println();
        out.println(space4_1 + "public boolean add(" + ClassName + " set){");
        out.println(space4_2 + "return super.add(set);");
        out.println(space4_1 + "}");
    }

    private void remove(PrintWriter out, String SchemaName) {
        out.println(space4_1 + "public boolean remove(" + SchemaName + " schema){");
        out.println(space4_2 + "return super.remove(schema);");
        out.println(space4_1 + "}");
    }

    private void get(PrintWriter out, String SchemaName) {
        out.println(space4_1 + "public " + SchemaName + " get(int index){");
        out.println(space4_2 + SchemaName + " schema = (" + SchemaName + ")super.getObj(index);");
        out.println(space4_2 + "return schema;");
        out.println(space4_1 + "}");
    }

    private void set(PrintWriter out, String ClassName, String SchemaName) {
        out.println(space4_1 + "public boolean set(int index, " + SchemaName + " schema){");
        out.println(space4_2 + "return super.set(index, schema);");
        out.println(space4_1 + "}");
        out.println();
        out.println(space4_1 + "public boolean set(" + ClassName + " set){");
        out.println(space4_2 + "return super.set(set);");
        out.println(space4_1 + "}");
    }

    private void encode(String TableName, PrintWriter out, String SchemaName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 数据打包，按 XML 格式打包，顺序参见<A href ={@docRoot}/dataStructure/tb.html#Prp" + TableName + "描述/A>表字段");
        // out.println(" * @param: 无");
        out.println(space4_1 + " * @return: String 返回打包后字符串");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public String encode(){");
        out.println(space4_2 + "StringBuffer strReturn = new StringBuffer(\"\");");
        out.println(space4_2 + "int n = this.size();");
        out.println(space4_2 + "for(int i = 1; i <= n; i++){");
        out.println(space4_3 + SchemaName + " schema = this.get(i);");
        out.println(space4_3 + "strReturn.append(schema.encode());");
        out.println(space4_3 + "if(i != n)");
        out.println(space4_4 + "strReturn.append(SysConst.RECORDSPLITER);");
        out.println(space4_2 + "}");
        out.println(space4_2 + "return strReturn.toString();");
        out.println(space4_1 + "}");
    }

    private void decode(PrintWriter out, String SchemaName) {
        out.println(space4_1 + "/**");
        out.println(space4_1 + " * 数据解包");
        out.println(space4_1 + " * @param: str String 打包后字符串");
        out.println(space4_1 + " * @return: boolean");
        out.println(space4_1 + " */");
        out.println(space4_1 + "public boolean decode(String str){");
        out.println(space4_2 + "int nBeginPos = 0;");
        out.println(space4_2 + "int nEndPos = str.indexOf('^');");
        out.println(space4_2 + "this.clear();");
        out.println(space4_2 + "while(nEndPos != -1){");
        out.println(space4_3 + SchemaName + " aSchema = new " + SchemaName + "();");
        out.println(space4_3 + "if(aSchema.decode(str.substring(nBeginPos, nEndPos))){");
        out.println(space4_4 + "this.add(aSchema);");
        out.println(space4_4 + "nBeginPos = nEndPos + 1;");
        out.println(space4_4 + "nEndPos = str.indexOf('^', nEndPos + 1);");
        out.println(space4_3 + "}");
        out.println(space4_3 + "else{");
        out.println(space4_4 + "// @@错误处理");
        out.println(space4_4 + "this.mErrors.copyAllErrors(aSchema.mErrors);");
        out.println(space4_4 + "return false;");
        out.println(space4_3 + "}");
        out.println(space4_2 + "}");
        out.println(space4_2 + SchemaName + " tSchema = new " + SchemaName + "();");
        out.println(space4_2 + "if(tSchema.decode(str.substring(nBeginPos))){");
        out.println(space4_3 + "this.add(tSchema);");
        out.println(space4_3 + "return true;");
        out.println(space4_2 + "}");
        out.println(space4_2 + "else{");
        out.println(space4_3 + "// @@错误处理");
        out.println(space4_3 + "this.mErrors.copyAllErrors(tSchema.mErrors);");
        out.println(space4_3 + "return false;");
        out.println(space4_2 + "}");
        out.println(space4_1 + "}");
    }
}
