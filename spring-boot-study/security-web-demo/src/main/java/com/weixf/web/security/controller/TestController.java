package com.weixf.web.security.controller;

import com.weixf.web.security.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @since 2020-12-09
 *
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "hello security";
    }

    @GetMapping("index")
    public String index() {
        return "hello index";
    }

    @GetMapping("update")
    @Secured({"ROLE_sale", "ROLE_manager"})  // 做角色验证
    // @PreAuthorize("hasAnyAuthority('admin')")  // 方法执行前做权限验证
    // @PostAuthorize("hasAnyAuthority('admin')")  // 方法执行后做权限验证
    public String update() {
        System.out.println("update......");
        return "hello update";
    }

    @GetMapping("getAll")
    @PostAuthorize("hasAnyAuthority('admin')")
    @PostFilter("filterObject.username == 'admin1'")  // 方法返回数据做过滤
    public List<Users> getAllUser() {
        ArrayList<Users> list = new ArrayList<>();
        list.add(new Users(11, "admin1", "6666"));
        list.add(new Users(21, "admin2", "888"));
        System.out.println(list);
        return list;
    }
}
