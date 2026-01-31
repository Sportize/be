package com.be.sportizebe.global.s3.service;

import com.be.sportizebe.global.config.S3Config;
import com.be.sportizebe.global.exception.CustomException;
import com.be.sportizebe.global.s3.enums.PathName;
import com.be.sportizebe.global.s3.exception.S3ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
  private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

  private final S3Client s3Client;
  private final S3Config s3Config;

  @Override
  public String uploadFile(PathName pathName, MultipartFile file) {
    validateFile(file);

    String fileName = createFileName(pathName, file.getOriginalFilename());

    try {
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(s3Config.getBucket())
          .key(fileName)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      return getFileUrl(fileName);
    } catch (IOException e) {
      log.error("S3 파일 업로드 실패: {}", e.getMessage());
      throw new CustomException(S3ErrorCode.FILE_SERVER_ERROR);
    }
  }

  @Override
  public void deleteFile(String fileUrl) {
    if (fileUrl == null || !fileUrl.contains(".com/")) {
      throw new CustomException(S3ErrorCode.FILE_URL_INVALID);
    }

    String fileName = extractFileName(fileUrl);

    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(s3Config.getBucket())
        .key(fileName)
        .build();

    s3Client.deleteObject(deleteObjectRequest);
    log.info("S3 파일 삭제 완료: {}", fileName);
  }

  // 파일 유효성 검증
  private void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new CustomException(S3ErrorCode.FILE_NOT_FOUND);
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new CustomException(S3ErrorCode.FILE_SIZE_INVALID);
    }

    String extension = extractExtension(file.getOriginalFilename()).toLowerCase();
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new CustomException(S3ErrorCode.FILE_TYPE_INVALID);
    }
  }

  // key (경로 + 파일명) 생성
  private String createFileName(PathName pathName, String originalFileName) {
    String extension = extractExtension(originalFileName);

    return switch (pathName) {
      case PROFILE -> s3Config.getProfile();
      case CLUB -> s3Config.getClub();
    }
    + "/" + UUID.randomUUID() + extension;
  }

  // 확장자 추출
  private String extractExtension(String originalFileName) {
    if (originalFileName == null || !originalFileName.contains(".")) {
      return "";
    }
    return originalFileName.substring(originalFileName.lastIndexOf("."));
  }

  // 파일명 추출
  private String extractFileName(String fileUrl) {
    // https://bucket.s3.region.amazonaws.com/folder/filename.ext -> folder/filename.ext
    return fileUrl.substring(fileUrl.indexOf(".com/") + 5);
  }

  // s3 url : https://{버킷명}.s3.ap-northeast-2.amazonaws.com/{파일명}
  private String getFileUrl(String fileName) {
    return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s",
        s3Config.getBucket(), fileName);
  }
}
