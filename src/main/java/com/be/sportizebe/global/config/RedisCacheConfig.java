package com.be.sportizebe.global.config;

import com.be.sportizebe.domain.post.dto.response.PostPageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 캐시 역직렬화를 위해 타입 정보(@class)를 포함하도록 설정
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // ❗ 타입 정보 절대 쓰지 않음
                .build();
        // 캐시에 들어가는 값의 직렬화 방식 결정

        // 기본 캐시(대부분)는 Object로 직렬화/역직렬화
        Jackson2JsonRedisSerializer<Object> defaultValueSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        defaultValueSerializer.setObjectMapper(objectMapper);

        // postList 캐시는 PostPageResponse 타입으로 역직렬화되어야 ClassCastException이 나지 않음
        Jackson2JsonRedisSerializer<PostPageResponse> postListValueSerializer =
                new Jackson2JsonRedisSerializer<>(PostPageResponse.class);
        postListValueSerializer.setObjectMapper(objectMapper);
        // TTL(캐시 수명) 정책 정의
        // 아무 설정 없는 캐시(Default) = 5분
        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(defaultValueSerializer)
                        )
                        .entryTtl(Duration.ofMinutes(5));

        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();


        // 캐시별로 TTL override 가능 ( post, commet 등 우리가 원하는 값으로 TTL 설정 가능 )
        cacheConfigs.put(
                "facilityNear",
                defaultConfig.entryTtl(Duration.ofSeconds(60))
        );
        cacheConfigs.put(
                "facilityMarkers",
                defaultConfig.entryTtl(Duration.ofSeconds(60))
        );
        cacheConfigs.put(
                "postList",
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(postListValueSerializer)
                        )
                        .entryTtl(Duration.ofSeconds(30))
        );
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}