package com.weixf.compress;

import cn.hutool.core.util.ZipUtil;

import java.io.File;

public class Compress {

    public static void main(String[] args) {

        File unzip = ZipUtil.unzip("D:\\WorkSpaces\\ideaProjects\\learnJava\\code-test-module\\src\\main\\java\\com\\weixf\\compress\\mmm", "e:\\aaa");
    }
}
