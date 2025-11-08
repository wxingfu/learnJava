package com.weixf.wordexport.util;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class PdfUtil {

    public static void main(String[] args) throws Exception {
        Properties properties = System.getProperties();
        String projectPath = (String) properties.get("user.dir");
        String base = projectPath + "/word-export/src/main/resources/wordExport/";

        File wordFile = new File(base + "/out_resume.docx");
        File target = new File(base + "/out_resume.pdf");
        IConverter converter = LocalConverter.builder()
                .baseFolder(new File(base))
                .workerPool(5, 8, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();
        boolean execute = converter.convert(wordFile).as(DocumentType.DOCX)
                .to(target).as(DocumentType.PDF).execute();
        if (execute) {
            converter.shutDown();
        } else {
            converter.kill();
        }
    }
}
