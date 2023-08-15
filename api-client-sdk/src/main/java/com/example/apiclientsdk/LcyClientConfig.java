package com.example.apiclientsdk;

import com.example.apiclientsdk.client.HuApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @BelongsProject: api-client-sdk
 * @BelongsPackage: com.example.apiclientsdk
 * @Author: liaocy
 * @CreateTime: 2023-08-08  11:38
 * @Description: TODO
 * @Version: 1.0
 */
@Configuration
@ConfigurationProperties("lcy.client")
@Data
@ComponentScan
public class LcyClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public HuApiClient mofengApiClient(){
        return new HuApiClient(accessKey, secretKey);
    }
}
