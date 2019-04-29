package com.hang.ssd.service;


/**
 * Redis服务
 * @author yinhang
 */
public interface CacheService {

    /**
     * 判断该openId是否存在
     * @param openId
     * @return
     */
    boolean hasId(String openId);

    /**
     * 获取openId对用token
     * @param openId
     * @return
     */
    Object getToken(String openId);

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
