package com.be.sportizebe.global.config;

import com.be.sportizebe.domain.comment.dto.response.CommentListResponse;
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

        // CommentList 캐시는 CommentListResponse 타입으로 역직렬화
        Jackson2JsonRedisSerializer<CommentListResponse> commentListSerializer =
                new Jackson2JsonRedisSerializer<>(CommentListResponse.class);
        commentListSerializer.setObjectMapper(objectMapper);

        // commentCount 캐시는 Long 타입으로 역직렬화
        Jackson2JsonRedisSerializer<Long> commentCountSerializer =
                new Jackson2JsonRedisSerializer<>(Long.class);
        commentCountSerializer.setObjectMapper(objectMapper);

        // 기본 캐시 설정
        // TTL: 5분, Serializer: Object 기준
        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(defaultValueSerializer)
                        )
                        .entryTtl(Duration.ofMinutes(5));

        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();


        // 개별 캐시 설정
        // Post, Comment 등 역직렬화 문제 생기는 것들은 우리가 임의로 설정해주기
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
        cacheConfigs.put(
                "commentList",
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(commentListSerializer)
                        )
                        .entryTtl(Duration.ofSeconds(30))
        );
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}