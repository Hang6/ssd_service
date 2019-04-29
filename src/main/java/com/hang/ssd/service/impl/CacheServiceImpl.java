package com.hang.ssd.service.impl;

import com.hang.ssd.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis服务
 * @author yinhang
 */
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private String TokenDB = "TOKEN_ID";
    private String OpenIdDB = "ID_TOKEN";

    private String OPENID = "openId";

    /**
     * 判断openId是否已存储
     * @param openId
     * @return
     */
    @Override
    public boolean hasId(String openId) {
        try {
            return redisTemplate.opsForHash().hasKey(OpenIdDB, openId);
        } catch (Exception e) {
            log.error("get key from redis error!", e);
            return false;
        }
    }

    /**
     * 获取当前openId的token
     * @param openId
     * @return
     */
    @Override
    public Object getToken(String openId) {
        return redisTemplate.opsForHash().get(OpenIdDB, openId);
    }

    /**
     * 创建token并返回
     *
     * @param openId
     * @param sessionKey
     * @return
     */
    @Override
    public String addTokenCache(String openId, String sessionKey) {
        String token = RandomStringUtils.randomAlphanumeric(16);
        try {
            redisTemplate.opsForHash().put(TokenDB, token, openId);
            redisTemplate.opsForHash().put(OpenIdDB, openId, token);
        } catch (Exception e){
            log.error("set token error!", e);
            return null;
        }
        return token;
    }

    /**
     * 直接获取用户openId
     *
     * @param key
     * @return
     */
    @Override
    public Object getUserOpenId(String key) { return redisTemplate.opsForHash().get(TokenDB, key); }
}
