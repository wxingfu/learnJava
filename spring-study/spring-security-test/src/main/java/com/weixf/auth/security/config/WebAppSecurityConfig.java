package com.weixf.auth.security.config;

import com.weixf.auth.security.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import javax.sql.DataSource;

@EnableWebSecurity // 开启Web环境下权限控制
@Configuration
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    @Resource
    private MyUserDetailsService userDetailsService;


    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭默认的csrf认证
        // http.csrf().disable();

        http.formLogin()
                .loginPage("/index.jsp")
                .permitAll()
                .loginProcessingUrl("/do/login.html") // 指定提交登录表单的地址
                .usernameParameter("loginAcct")
                .passwordParameter("userPswd") // 定制登录账号和密码的请求参数名
                .defaultSuccessUrl("/main.html"); // 设置登录成功后默认前往的URL 地址

        http.logout()
                .logoutUrl("/do/logout.html")
                .logoutSuccessUrl("/index.jsp");

        http.authorizeRequests() // 对请求授权
                .antMatchers("/layui/**", "/index.jsp") // 使用ANT风格设置要授权的URL地址
                .permitAll() // 允许上面使用ANT风格设置的全部请求
                .antMatchers("/level1/**").hasRole("学徒")
                .antMatchers("/level2/**").hasAuthority("内门弟子")
                .anyRequest() // 其他未设置的全部请求
                .authenticated();

        // 访问被拒绝时访问的页面---403
        // 方式一
        // http.exceptionHandling()
        //         .accessDeniedPage("/to/no/auth/page.html");
        // 方式二
        http.exceptionHandling()
                .accessDeniedHandler((request, response, e) -> {
                    request.setAttribute("message", "抱歉！您没有访问这个功能的权限！");
                    request.getRequestDispatcher("/WEB-INF/views/no_auth.jsp")
                            .forward(request, response);
                });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // auth.inMemoryAuthentication()
        //         .passwordEncoder(new MyPasswordEncoder())
        //         .withUser("tom").password("123123") //设置账号密码
        //         .roles("ADMIN", "学徒") //设置角色
        //         .and()
        //         .passwordEncoder(new MyPasswordEncoder())
        //         .withUser("jerry").password("123123")//设置另一个账号密码
        //         .authorities("SAVE", "EDIT", "内门弟子"); //设置权限

        // 使用自定义的userDetailsService
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }
}
