package com.be.sportizebe.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class S3Config {

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket; // 버킷 명

  @Value("${spring.cloud.aws.s3.path.profile}")
  private String profile; // 프로필 사진

  @Value("${spring.cloud.aws.s3.path.club}")
  private String club;
}
