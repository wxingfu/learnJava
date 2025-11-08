package com.weixf.demo.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @since 2022-09-21
 */
@RestController
@RequestMapping("/claim")
public class TestController {

    @PostMapping(value = "/report")
    public String report(@RequestBody String param) {
        System.out.println("=========================================");
        System.out.println(param);
        System.out.println("=========================================");
        return param;
    }

    @PostMapping(value = "/sync")
    public String sync(@RequestBody String param) {
        System.out.println("=========================================");
        System.out.println(param);
        System.out.println("=========================================");
        return param;
    }
}
