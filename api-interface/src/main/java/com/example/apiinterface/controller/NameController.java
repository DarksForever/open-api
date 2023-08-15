package com.example.apiinterface.controller;

import com.example.apiclientsdk.entity.User;
import com.example.apiclientsdk.utils.SignUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @BelongsProject: api-interface
 * @BelongsPackage: com.example.apiinterface.controller
 * @Author: liaocy
 * @CreateTime: 2023-08-07  10:13
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/")
    public String getNameByGet(String name, HttpServletRequest httpServletRequest) {
        /*if (!isValid(httpServletRequest)) {
            throw new RuntimeException("无权限");
        }*/
        return "Get 你的名字是" + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name, HttpServletRequest httpServletRequest) {
        /*if (!isValid(httpServletRequest)) {
            throw new RuntimeException("无权限");
        }*/
        return "Post 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest httpServletRequest) {
        /*if (!isValid(httpServletRequest)) {
            throw new RuntimeException("无权限");
        }*/
        return "Post 用户名字是" + user.getUsername();
    }

    private boolean isValid(HttpServletRequest httpServletRequest) {
        //从请求头获取api签名
        String accessKey = httpServletRequest.getHeader("accessKey");
        String sign = httpServletRequest.getHeader("sign");
        String body = httpServletRequest.getHeader("body");
        String nonce = httpServletRequest.getHeader("nonce");
        String timestamp = httpServletRequest.getHeader("timestamp");

        //校验accessKeyc TODO 实际应与数据库中的数据对比
        if (!"jinze".equals(accessKey)) {
            return false;
        }
        //校验随机数，给定的随机数规则是5位以下
        if(Long.parseLong(nonce)>10000){
            return false;
        }
        //校验时间戳，5min内有效

        //校验签名 TODO 实际secretKey是从数据库获取的
        String realSign = SignUtil.getSign(body, "abcdefg");
        if(!realSign.equals(sign)){
            return false;
        }
        return true;
    }
}
