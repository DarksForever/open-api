package com.example.apiclientsdk.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: api-interface
 * @BelongsPackage: com.example.apiinterface.entity
 * @Author: liaocy
 * @CreateTime: 2023-08-07  10:14
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class User implements Serializable {
    private String username;
}
