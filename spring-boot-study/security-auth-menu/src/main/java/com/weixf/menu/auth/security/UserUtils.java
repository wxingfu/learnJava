package com.weixf.menu.auth.security;

import com.weixf.menu.auth.entity.SysUser;
import com.weixf.menu.auth.springboot.service.SysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建会话，获取当前登录对象
 */
@Component
public class UserUtils {

    @Resource
    private SysUserService userService;

    /**
     * 获取当前登录者的信息
     *
     * @return 当前者信息
     */
    public SysUser getUser() {
        // 获取当前用户的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }

    /**
     * 判断此用户中是否包含roleName菜单权限
     */
    public Boolean hasRole(String roleName) {
        // 获取UserDetails类，
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roleCodes = new ArrayList<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            // getAuthority()返回用户对应的菜单权限
            roleCodes.add(authority.getAuthority());
        }
        return roleCodes.contains(roleName);
    }
}
