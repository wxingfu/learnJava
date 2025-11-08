package com.weixf.web.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @since 2020-12-09
 *
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    // 注入数据源
    @Resource
    private DataSource dataSource;

    // 配置对象
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // String password = passwordEncoder.encode("123");
        // auth.inMemoryAuthentication().withUser("wxf").password(password).roles("admin");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 退出
        http.logout().logoutUrl("/logout").
                logoutSuccessUrl("/test/hello").permitAll();

        // 配置没有权限访问跳转自定义页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");

        // 自定义自己编写的登录页面
        http.formLogin()
                .loginPage("/login.html")  // 登录页面设置
                .loginProcessingUrl("/user/login")   // 登录访问路径
                // .defaultSuccessUrl("/test/index").permitAll()  //登录成功之后，跳转路径
                .defaultSuccessUrl("/success.html").permitAll()  // 登录成功之后，跳转路径
                .failureUrl("/unauth.html"); // 登录失败跳转路径

        // 设置哪些路径可以直接访问，不需要认证
        http.authorizeRequests().antMatchers("/", "/test/hello", "/uesr/login").permitAll()
                // 基于权限访问控制，有权限admin才可以访问
                // .antMatchers("/test/index").hasAuthority("admin") // hasAuthority方法 单个权限
                // .antMatchers("/test/index").hasAnyAuthority("admin,manager") //hasAnyAuthority方法  多个权限
                // 基于角色访问控制，属于sale角色才可以访问
                // .antMatchers("/test/index").hasRole("sale") // hasRole方法 单一角色
                .antMatchers("/test/index").hasAnyRole("sale,buy") // hasAnyRole方法 多角色
                .anyRequest().authenticated();

        // 配置自动登录
        http.rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60) // 设置有效时长，单位为秒
                .userDetailsService(userDetailsService);

        // 关闭csrf防护,跨站请求伪造
        http.csrf().disable();
    }

}
