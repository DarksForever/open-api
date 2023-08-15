package com.yupi.apicommon.service;

import com.yupi.apicommon.model.entity.User;

/**
 * @BelongsProject: api-common
 * @BelongsPackage: com.yupi.apicommon.service
 * @Author: liaocy
 * @CreateTime: 2023-08-14  16:52
 * @Description: 内部用户服务
 * @Version: 1.0
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}

