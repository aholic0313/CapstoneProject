package com.generalnetdisk.entity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig<V> {

    @Bean
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //设置Key的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置Value的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //设置Hash的Key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置Hash的Value的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;

    }
}
