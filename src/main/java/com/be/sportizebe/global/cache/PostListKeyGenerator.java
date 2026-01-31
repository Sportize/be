package com.be.sportizebe.global.cache;

import com.be.sportizebe.domain.post.entity.PostProperty;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("postListKeyGenerator")
public class PostListKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        PostProperty property = (PostProperty) params[0];
        Pageable pageable = (Pageable) params[1];

        return String.format(
                "%s:%d:%d:%s",
                property.name(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().toString()
        );
    }
}