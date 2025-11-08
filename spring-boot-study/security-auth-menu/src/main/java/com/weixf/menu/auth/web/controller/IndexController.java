package com.weixf.menu.auth.web.controller;

import com.weixf.menu.auth.entity.TbMenu;
import com.weixf.menu.auth.springboot.service.TbMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class IndexController {
    @Resource
    private TbMenuService menuService;

    @RequestMapping(value = {"/", "index", "/login"}, method = RequestMethod.GET)
    public String index() {
        return "main/index";
    }

    @RequestMapping(value = "/main")
    public String main(ModelMap map) {
        // 加载菜单
        List<TbMenu> menus = menuService.findAuditMenu();
        map.put("menus", menus);
        if (menus.isEmpty()) {
            return "main/main";
        }
        return "main/main1";
    }
}
