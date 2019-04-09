package com.management.service;

import com.management.model.ICacheManager;
import com.management.model.RedisCacheConfig;
import com.management.utility.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;



@ConditionalOnProperty(name = "cache.manager", havingValue = "redisCacheManager")
@Service
public class RedisCacheManager implements ICacheManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisScript<Boolean> script;

    @Override
    public Object createCache(String appName, String cacheName, long ttl) {

        RedisCacheConfig config = new RedisCacheConfig();
        config.setCacheName(appName + ":" + cacheName + ":");
        config.setTtl(ttl);
        config.setTtlEnabled((ttl > 0) ? true : false);

        return config;
    }

    @Override
    public void destroyCache(Object cache) {

        RedisCacheConfig config = (RedisCacheConfig) cache;
        this.redisTemplate.execute(this.script, null, config.getCacheName() + "*");
    }

    @Override
    public void put(Object cache, String key, Object data) {

        RedisCacheConfig config = (RedisCacheConfig) cache;
        String cacheKey = config.getCacheName() + key;
        this.redisTemplate.opsForValue().set(cacheKey, data);
        if (config.isTtlEnabled()) {
            this.redisTemplate.expire(cacheKey, config.getTtl(), TimeUnit.SECONDS);
        }
    }

    @Override
    public Object get(Object cache, String key) {

        try {
            RedisCacheConfig config = (RedisCacheConfig) cache;
            String cacheKey = config.getCacheName() + key;
            if (config.isTtlEnabled()) {
                this.redisTemplate.expire(cacheKey, config.getTtl(), TimeUnit.SECONDS);
            }
            return this.redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception ex) {
            LoggerUtil.logError(logger, ex);
        }

        return null;
    }

    @Override
    public boolean isPresent(Object cache, String key) {

        if (this.get(cache, key) != null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean remove(Object cache, String key) {
        RedisCacheConfig config = (RedisCacheConfig) cache;
        String cacheKey = config.getCacheName() + key;
        this.redisTemplate.delete(cacheKey);

        return true;
    }

    @Override
    public void purgeExpiredElements(Object cache) {

    }
}
