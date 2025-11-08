package com.weixf.security.controller;


import com.weixf.security.common.ResultUtil;
import com.weixf.security.common.SecurityUtil;
import com.weixf.security.entity.SysMenu;
import com.weixf.security.entity.SysRole;
import com.weixf.security.entity.SysUser;
import com.weixf.security.security.entity.SecurityUser;
import com.weixf.security.service.SysMenuService;
import com.weixf.security.service.SysRoleService;
import com.weixf.security.service.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Map<String, Object> userLogin() {
        Map<String, Object> result = new HashMap<>();
        SecurityUser userDetails = SecurityUtil.getUserInfo();
        result.put("title", "管理端信息");
        result.put("data", userDetails);
        return ResultUtil.resultSuccess(result);
    }

    /**
     * 拥有ADMIN或者USER角色可以访问
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<SysUser> sysUserEntityList = sysUserService.list();
        result.put("title", "拥有用户或者管理员角色都可以查看");
        result.put("data", sysUserEntityList);
        return ResultUtil.resultSuccess(result);
    }

    /**
     * 拥有ADMIN和USER角色可以访问
     */
    @PreAuthorize("hasRole('ADMIN') and hasRole('USER')")
    @RequestMapping(value = "/menuList", method = RequestMethod.GET)
    public Map<String, Object> menuList() {
        Map<String, Object> result = new HashMap<>();
        List<SysMenu> sysMenuEntityList = sysMenuService.list();
        result.put("title", "拥有用户和管理员角色都可以查看");
        result.put("data", sysMenuEntityList);
        return ResultUtil.resultSuccess(result);
    }


    /**
     * 拥有sys:user:info权限可以访问
     * hasPermission 第一个参数是请求路径 第二个参数是权限表达式
     */
    @PreAuthorize("hasPermission('/admin/userList','sys:user:info')")
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public Map<String, Object> userList() {
        Map<String, Object> result = new HashMap<>();
        List<SysUser> sysUserEntityList = sysUserService.list();
        result.put("title", "拥有sys:user:info权限都可以查看");
        result.put("data", sysUserEntityList);
        return ResultUtil.resultSuccess(result);
    }


    /**
     * 拥有ADMIN角色和sys:role:info权限可以访问
     */
    @PreAuthorize("hasRole('ADMIN') and hasPermission('/admin/adminRoleList','sys:role:info')")
    @RequestMapping(value = "/adminRoleList", method = RequestMethod.GET)
    public Map<String, Object> adminRoleList() {
        Map<String, Object> result = new HashMap<>();
        List<SysRole> sysRoleEntityList = sysRoleService.list();
        result.put("title", "拥有ADMIN角色和sys:role:info权限可以访问");
        result.put("data", sysRoleEntityList);
        return ResultUtil.resultSuccess(result);
    }
}
