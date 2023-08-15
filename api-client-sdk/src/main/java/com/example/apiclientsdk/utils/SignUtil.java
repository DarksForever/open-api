package com.example.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @BelongsProject: api-interface
 * @BelongsPackage: com.example.apiinterface.utils
 * @Author: liaocy
 * @CreateTime: 2023-08-08  10:16
 * @Description: 签名工具
 * @Version: 1.0
 */
public class SignUtil {
    public static String getSign(String body, String secretKey) {
        String text = body + "." + secretKey;
        //使用Hutool提供的签名算法
        Digester md5 = new Digester(DigestAlgorithm.SHA256);

        // 5393554e94bf0eb6436f240a4fd71282
        return md5.digestHex(text);
    }
}
