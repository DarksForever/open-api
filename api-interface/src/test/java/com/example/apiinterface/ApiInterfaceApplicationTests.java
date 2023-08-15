package com.example.apiinterface;

import com.example.apiclientsdk.LcyClientConfig;
import com.example.apiclientsdk.client.HuApiClient;
import com.example.apiclientsdk.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    @Resource
    private HuApiClient huApiClient;

    @Test
    void contextLoads() {
    }

    //模拟客户端
    @Test
    void apiClientTest() {
        String result1 = huApiClient.getNameByGet("aaa");
        String result2 = huApiClient.getNameByPost("aaa");
        User user = new User();
        user.setUsername("aaa");
        String result3 = huApiClient.getUsernameByPost(user);
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
    }
}
