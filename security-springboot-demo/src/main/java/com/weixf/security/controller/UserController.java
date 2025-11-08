package com.weixf.security.controller;


import com.weixf.security.common.ResultUtil;
import com.weixf.security.entity.SysMenu;
import com.weixf.security.security.entity.SecurityUser;
import com.weixf.security.service.SysMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通用户
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 用户端信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Map<String, Object> userLogin() {
        Map<String, Object> result = new HashMap<>();
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        result.put("title", "用户端信息");
        result.put("data", userDetails);
        return ResultUtil.resultSuccess(result);
    }

    /**
     * 拥有USER角色和sys:user:info权限可以访问
     */
    @PreAuthorize("hasRole('USER') and hasPermission('/user/menuList','sys:user:info')")
    @RequestMapping(value = "/menuList", method = RequestMethod.GET)
    public Map<String, Object> sysMenuEntity() {
        Map<String, Object> result = new HashMap<>();
        List<SysMenu> sysMenuEntityList = sysMenuService.list();
        result.put("title", "拥有USER角色和sys:user:info权限可以访问");
        result.put("data", sysMenuEntityList);
        return ResultUtil.resultSuccess(result);
    }

}
