package com.hang.ssd.service;

import java.util.Map;

/**
 * Redis服务
 * @author yinhang
 */
public interface CacheService {

    /**
     * 判断一个key是否存在
     * @param key
     * @return
     */
    boolean hasKey(String key);

    /**
     * 获取该key
     * @param key
     * @return
     */
    Map<Object, Object> hmget(String key);

    /**
     * 创建token并返回
     * @param openId
     * @param sessionKey
     * @return
     */
    String addTokenCache(String openId, String sessionKey);

    /**
     * 直接获取用户openId
     * @param key
     * @return
     */
    Object getUserOpenId(String key);
}
