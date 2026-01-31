package com.be.sportizebe.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Getter
@Configuration
public class S3Config {

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket; // 버킷 명

  @Value("${spring.cloud.aws.s3.path.profile}")
  private String profile; // 프로필 사진

  @Value("${spring.cloud.aws.s3.path.club}")
  private String club;

  @Bean
  S3Client s3Client() {
    return S3Client.builder()
      .region(Region.AP_NORTHEAST_2)
      .credentialsProvider(DefaultCredentialsProvider.create())
      .build();
  }
}
