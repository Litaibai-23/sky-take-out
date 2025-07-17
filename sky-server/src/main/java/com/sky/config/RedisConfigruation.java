package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * ClassName: RedisConfigruation
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/8 16:39
 */
@Configuration
@Slf4j
public class RedisConfigruation {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建RedisTemplate对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        log.info("创建RedisTemplate对象成功！");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
