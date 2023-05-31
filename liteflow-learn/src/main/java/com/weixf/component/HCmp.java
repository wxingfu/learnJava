package com.weixf.component;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;

@LiteflowComponent(id = "h", name = "H")
public class HCmp extends NodeComponent {

    @Override
    public void process() {
        System.out.println("执行了H组件...");
    }
}
