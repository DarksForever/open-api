package com.example.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.example.apiclientsdk.entity.User;
import com.example.apiclientsdk.utils.SignUtil;


import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: api-interface
 * @BelongsPackage: client
 * @Author: liaocy
 * @CreateTime: 2023-08-07  10:25
 * @Description: 通过Hutool调用接口的工具类
 * @Version: 1.0
 */
public class HuApiClient {

    private String accessKey;
    private String secretKey;

    private static final String GATEWAY_HOST="http://localhost:8090";

    public HuApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    //封装到请求头中
    private Map<String,String> getHeaders(String body){
        Map<String,String> headers=new HashMap<>();
        headers.put("accessKey",accessKey);
        headers.put("body",body);
        //模拟 四位随机数
        headers.put("nonce", RandomUtil.randomNumbers(4));
        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
        //生成签名
        headers.put("sign", SignUtil.getSign(body,secretKey));
        return headers;
    }

    public String getNameByGet(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result= HttpUtil.get(GATEWAY_HOST+"/api/name/", paramMap);
        return result;
    }

    public String getNameByPost(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result = HttpUtil.post(GATEWAY_HOST+"/api/name/", paramMap);
        return result;
    }

    public String getUsernameByPost(User user){
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+"/api/name/user")
                .addHeaders(getHeaders(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result= httpResponse.body();
        return result;
    }
}
