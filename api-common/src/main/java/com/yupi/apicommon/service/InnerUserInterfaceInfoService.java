package com.yupi.apicommon.service;

/**
 * @BelongsProject: api-common
 * @BelongsPackage: com.yupi.apicommon.service
 * @Author: liaocy
 * @CreateTime: 2023-08-14  16:53
 * @Description: 内部用户接口信息服务
 * @Version: 1.0
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}

