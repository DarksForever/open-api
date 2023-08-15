package com.yupi.project.provider;

/**
 * @BelongsProject: api-gateway
 * @BelongsPackage: com.example.apigateway.provider
 * @Author: liaocy
 * @CreateTime: 2023-08-14  15:26
 * @Description: TODO
 * @Version: 1.0
 */

import java.util.concurrent.CompletableFuture;

public interface DemoService {

    String sayHello(String name);

    String sayHello2(String name);

    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }

}

