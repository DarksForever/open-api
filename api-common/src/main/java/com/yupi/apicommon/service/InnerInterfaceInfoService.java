package com.yupi.apicommon.service;

import com.yupi.apicommon.model.entity.InterfaceInfo;

/**
 * @BelongsProject: api-common
 * @BelongsPackage: com.yupi.apicommon.service
 * @Author: liaocy
 * @CreateTime: 2023-08-14  16:53
 * @Description: 内部接口信息服务
 * @Version: 1.0
 */
public interface InnerInterfaceInfoService {
    /**
     * @description: 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
     * @author: liaocy
     * @date: 2023/8/14 16:54
     * @param: [path, method]
     * @return: com.yupi.apicommon.model.entity.InterfaceInfo
     **/
    InterfaceInfo getInterfaceInfo(String path, String method);
}

