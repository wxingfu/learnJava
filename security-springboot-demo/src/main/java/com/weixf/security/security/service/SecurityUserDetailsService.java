package com.weixf.security.security.service;


import com.weixf.security.entity.SysUser;
import com.weixf.security.security.entity.SecurityUser;
import com.weixf.security.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * SpringSecurity用户的业务实现
 */
@Component
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    /**
     * 查询用户信息
     */
    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        SysUser sysUserEntity = sysUserService.selectUserByName(username);
        if (sysUserEntity != null) {
            // 组装参数
            SecurityUser securityUser = new SecurityUser();
            BeanUtils.copyProperties(sysUserEntity, securityUser);
            return securityUser;
        }
        return null;
    }
}
