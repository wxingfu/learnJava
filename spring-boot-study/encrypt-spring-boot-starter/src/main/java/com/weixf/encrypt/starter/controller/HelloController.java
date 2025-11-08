package com.weixf.encrypt.starter.controller;

import com.weixf.encrypt.starter.anno.Decrypt;
import com.weixf.encrypt.starter.anno.Encrypt;
import com.weixf.encrypt.starter.entity.User;
import com.weixf.encrypt.starter.entity.vo.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @since 2022-06-17
 */
@RestController
public class HelloController {
    @GetMapping("/user")
    @Encrypt
    public RespBean getUser() {
        User user = new User();
        user.setId((long) 99);
        user.setUsername("javaboy");
        return RespBean.ok("ok", user);
    }

    @PostMapping("/user")
    public RespBean addUser(@RequestBody @Decrypt User user) {
        System.out.println("user = " + user);
        return RespBean.ok("ok", user);
    }
}
