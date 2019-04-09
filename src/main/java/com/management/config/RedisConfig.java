package com.management.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import redis.clients.jedis.JedisPoolConfig;



@ConditionalOnProperty(name = "cache.manager", havingValue = "redisCacheManager")
@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${app.redisHost}")
    private String creditCardPortalRedisHost;
    @Value("${app.redisPort}")
    private int creditCardRedisPort;
    @Value("${redis.path.resource}")
    private String rediPathResource;

    public static boolean validPath(String path) {
        String path_REGEX = "^[ A-Za-z0-9_@./#&+-]*$";

        if (path.matches(path_REGEX))
            return true;
        else
            return false;
    }

    @Bean
    RedisTemplate redisTemplate(){

        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxWaitMillis(10000);

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(this.creditCardPortalRedisHost);
        connectionFactory.setPort(this.creditCardRedisPort);
        connectionFactory.afterPropertiesSet();

        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setEnableDefaultSerializer(true);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisScript<Boolean> script() {


        String path = "META-INF/scripts/purgeCache.lua";

        if (!validPath(path)) {

            logger.error("RedisScript(): Invalid Path");
            throw new IllegalArgumentException();
        }
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();

        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(path)));
        redisScript.setResultType(Boolean.class);

        return redisScript;
    }


}
