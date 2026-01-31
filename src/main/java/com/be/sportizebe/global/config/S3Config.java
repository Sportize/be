package com.be.sportizebe.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
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

  @Value("${spring.cloud.aws.s3.path.post}")
  private String post;

  @Value("${spring.cloud.aws.credentials.access-key}")
  private String accessKey;

  @Value("${spring.cloud.aws.credentials.secret-key}")
  private String secretKey;

  @Bean
  S3Client s3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
      .region(Region.AP_NORTHEAST_2)
      // DefaultCredentialsProvider: 환경 변수나 AWS 프로필 파일에서 자격 증명을 찾음
      // StaticCredentialsProvider: 개발환경에서는 application.yml에서 찾도록 설정
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .build();
  }
}
