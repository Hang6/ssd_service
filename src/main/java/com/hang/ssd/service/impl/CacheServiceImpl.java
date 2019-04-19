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

    private String OPENID = "openId";
    private String SESSION = "sessionKey";

    /**
     * 判断一个key是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("get key from redis error!", e);
            return false;
        }
    }

    /**
     * 获取该key
     *
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
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
        Map<String, String> map = new HashMap<>(16);
        map.put(OPENID, openId);
        map.put(SESSION, sessionKey);
        try {
            redisTemplate.opsForHash().putAll(token, map);
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
    public Object getUserOpenId(String key) {
        return redisTemplate.opsForHash().get(key, OPENID);
    }
}
