package com.weixf.demo.web.aop;

import com.alibaba.fastjson.JSON;
import com.weixf.demo.web.common.BusinessException;
import com.weixf.demo.web.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 防刷切面实现类
 */
@Aspect
@Component
public class PreventAop {

    private static final Logger log = LoggerFactory.getLogger(PreventAop.class);

    @Resource
    private RedisUtil redisUtil;


    /**
     * 切入点
     */
    @Pointcut("@annotation(com.weixf.demo.web.aop.Prevent)")
    public void pointcut() {
    }


    /**
     * 处理前
     *
     * @return
     */
    @Before("pointcut()")
    public void joinPoint(JoinPoint joinPoint) throws Exception {
        String requestStr = JSON.toJSONString(joinPoint.getArgs()[0]);
        if (ObjectUtils.isEmpty(requestStr) || requestStr.equalsIgnoreCase("{}")) {
            throw new BusinessException("[防刷]入参不允许为空");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(),
                methodSignature.getParameterTypes());

        Prevent preventAnnotation = method.getAnnotation(Prevent.class);
        String methodFullName = method.getDeclaringClass().getName() + method.getName();

        entrance(preventAnnotation, requestStr, methodFullName);
    }


    /**
     * 入口
     */
    private void entrance(Prevent prevent, String requestStr, String methodFullName) throws Exception {
        PreventStrategy strategy = prevent.strategy();
        if (strategy == PreventStrategy.DEFAULT) {
            defaultHandle(requestStr, prevent, methodFullName);
        } else {
            throw new BusinessException("无效的策略");
        }
    }


    /**
     * 默认处理方式
     */
    private void defaultHandle(String requestStr, Prevent prevent, String methodFullName) throws Exception {
        String base64Str = toBase64String(requestStr);
        long expire = Long.parseLong(prevent.value());

        String resp = redisUtil.get(methodFullName + base64Str);
        if (ObjectUtils.isEmpty(resp)) {
            redisUtil.set(methodFullName + base64Str, requestStr, expire);
        } else {
            String message = !ObjectUtils.isEmpty(prevent.message()) ?
                    prevent.message() : expire + "秒内不允许重复请求";
            throw new BusinessException(message);
        }
    }


    /**
     * 对象转换为base64字符串
     */
    private String toBase64String(String obj) throws Exception {
        if (ObjectUtils.isEmpty(obj)) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = obj.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(bytes);
    }


}
