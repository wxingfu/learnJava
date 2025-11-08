package com.weixf.web.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weixf.web.security.entity.Users;
import com.weixf.web.security.mapper.UsersMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @since 2020-12-09
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private UsersMapper usersMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 调用usersMapper方法，根据用户名查询数据库
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        // where username=?
        wrapper.eq("username", username);
        Users users = usersMapper.selectOne(wrapper);
        // 判断
        if (users == null) {// 数据库没有用户名，认证失败
            throw new UsernameNotFoundException("用户名不存在！");
        }

        List<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_sale,ROLE_buy");

        return new User(users.getUsername(),
                new BCryptPasswordEncoder().encode(users.getPassword()), authorities);
    }
}
