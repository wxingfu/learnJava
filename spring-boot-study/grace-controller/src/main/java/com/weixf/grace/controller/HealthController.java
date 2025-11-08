package com.weixf.grace.controller;

import com.weixf.grace.annotation.NotControllerResponseAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @since 2022-06-07
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    @NotControllerResponseAdvice
    public String health() {
        return "success";
    }

    // @PostMapping("/findByVo")
    // public ProductInfo findByVo(@Validated ProductInfoVo vo) {
    //     ProductInfo productInfo = new ProductInfo();
    //     BeanUtils.copyProperties(vo, productInfo);
    //     return productInfoService.getOne(new QueryWrapper(productInfo));
    // }
}
